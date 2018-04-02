package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.ShareReward;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.ShareRewardService;
import com.aqb.cn.service.UserService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/9.
 */
@Controller
public class ShareRewardAction {

    private final Log logger = LogFactory.getLog(ShareRewardAction.class);

    @Autowired
    private ShareRewardService shareRewardService;
    @Autowired
    private UserService userService;

    /**
     * 新增推荐人奖励设置
     * 选择一个用户单独设置
     * @param shareReward 推荐人奖励设置信息
     * @return state : 0 -- 成功, 6--推荐人奖励设置已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/shareReward/addShareReward", method = RequestMethod.POST)
    public Object addShareReward(HttpServletRequest request,@RequestBody ShareReward shareReward) {
        int status;
        String message = "";
        if(shareReward.getUserId() == null || shareReward.getUserId().equals("") ||
                shareReward.getRewardPercent() == null || shareReward.getRewardPercent2() == null){
            return new Response(Status.ERROR, "参数不正确");
        }
        User user = userService.queryByName(shareReward.getUserId());
        if(user== null){
            return new Response(Status.ERROR, "手机号不存在");
        }
        shareReward.setUserId(user.getId());
        shareReward.setId(UUIDCreator.getUUID());
        shareReward.setStatus(2);
        shareReward.setFoundDate(new Date());
        shareReward.setRewardContent("被推荐人作为车主进圈发一条广告后，推荐人可获得的奖励。");
        shareReward.setRewardContent2("被推荐人作为投放者一次性投放超过1000元广告后，推荐人可获得的奖励。");

        status = shareRewardService.add(shareReward);
        if (status == Status.SUCCESS) {
            message = "添加推荐人奖励设置成功";
            return new Response(status, message, shareReward.getId());
        }if(status == Status.EXISTS){
            message = "推荐人奖励设置已经存在";
            return new Response(status, message);
        }
        message = "添加推荐人奖励设置失败";
        return new Response(status, message);
    }


    /**
     * 删除推荐人奖励设置
     * @param request
     * @param shareReward
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/shareReward/deleteshareReward", method = RequestMethod.POST)
    public Object deleteshareReward(HttpServletRequest request, @RequestBody ShareReward shareReward) {
        int status;
        String message = "";
        if(shareReward.getId() == null || shareReward.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = shareRewardService.delete(shareReward);
        if(status == Status.NOT_EXISTS){
            message = "推荐人奖励设置不存在";
        }
        if(status == Status.ERROR){
            message = "删除失败";
        }
        if(status == Status.SUCCESS){
            message = "删除成功";
        }
        return new Response(status,message);
    }

    /**
     * 修改推荐人奖励设置
     * @param request
     * @param shareReward shareReward.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 推荐人奖励设置不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/shareReward/updateShareReward", method = RequestMethod.POST)
    public Object updateShareReward(HttpServletRequest request, @RequestBody ShareReward shareReward) {
        int status = Status.ERROR;
        String message = "";
        if(shareReward.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = shareRewardService.update(shareReward);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "推荐人奖励设置不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 推荐人奖励设置列表查询
     * 默认设置查询
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/shareReward/queryShareReward", method = RequestMethod.GET)
    public Object queryShareReward(HttpServletRequest request) {
        List<ShareReward> shareRewards = shareRewardService.queryShareRewardStatus(1);//默认设置
        return new Response(Status.SUCCESS,shareRewards);
    }

    /**
     * 推荐人奖励设置列表查询
     * 单独设置查询
     * userName 手机号
     * currentPage  页数
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/shareReward/queryShareRewardStatus", method = RequestMethod.GET)
    public Object queryShareRewardStatus(HttpServletRequest request,
                                         @RequestParam(value = "userName", required = false) String userName,
                                         @RequestParam(value = "currentPage", required = false) Long currentPage
                                         ) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        Map map = query.getParameters();
        System.out.println(userName);
        map.put("userName",userName);
        shareRewardService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 推荐人奖励设置查询
     * 按手机号搜索
     * userName 手机号
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/shareReward/queryShareRewardUserName", method = RequestMethod.GET)
    public Object queryShareRewardUserName(HttpServletRequest request, @RequestParam("userName") String userName) {
        User user = userService.queryByName(userName);
        if(user == null){
            return new Response(Status.ERROR,"没有该手机号");
        }
        ShareReward shareReward = shareRewardService.queryByUserId(user.getId());
        if(shareReward == null){
            return new Response(2,"未单独设置推荐人奖励",userName);
        }
        Map map = new HashMap();
        map.put("userName",userName);
        map.put("shareReward",shareReward);
        return new Response(Status.SUCCESS,"已单独设置了推荐人奖励",map);
    }

}
