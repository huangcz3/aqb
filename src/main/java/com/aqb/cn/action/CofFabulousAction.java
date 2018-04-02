package com.aqb.cn.action;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.bean.CofFabulous;
import com.aqb.cn.bean.User;
import com.aqb.cn.bean.UserFriends;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserFriendsMapper;
import com.aqb.cn.service.CofFabulousService;
import com.aqb.cn.service.CofService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/27.
 */
@Controller
public class CofFabulousAction {

    private final Log logger = LogFactory.getLog(CofFabulousAction.class);

    @Autowired
    private CofFabulousService cofFabulousService;
    @Autowired
    private CofService cofService;
    @Autowired
    private UserFriendsMapper userFriendsMapper;


    /**
     * 新增点赞
     *
     * @param req 点赞信息
     * @return state : 0 -- 成功, 6--点赞已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/cofFabulous/addcofFabulous", method = RequestMethod.POST)
    public Object addcofFabulous(HttpServletRequest request, @RequestBody Request req) {
        int status;
        String message = "";

        //判断用户的session是否正常
        //获取session
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }
        CofFabulous cofFabulous = req.getCofFabulous();

        //判断是否为好友
        Cof cof = cofService.get(cofFabulous.getCofId());
        if(!cof.getUserId().equals(obj.getId())){
            if(cof != null){
                UserFriends userFriends = userFriendsMapper.selectByUserId(cof.getUserId(),obj.getId());
                if (userFriends == null){
                    return new Response(Status.ERROR,"");
                }
            }
        }

        //从Request中获取点赞参数



        cofFabulous.setUserId(obj.getId());

        // 自动生成主键ID和创建时间
        cofFabulous.setId(UUIDCreator.getUUID());

        cofFabulous.setFoundDate(new Date());
        if (cofFabulous.getCofId() == null || cofFabulous.getCofId().equals("")){
            return new Response(Status.ERROR,"CofId不能为空");
        }
        if (cofFabulous.getUserId() == null || cofFabulous.getUserId().equals("")){
            return new Response(Status.ERROR,"userId不能为空");
        }
        if (cofFabulousService.queryByUserIdCofId(cofFabulous.getUserId(),cofFabulous.getCofId()) != null){
            return new Response(Status.ERROR, "点赞已存在");
        }
        status = cofFabulousService.add(cofFabulous);
        if (status == Status.SUCCESS) {
            message = "添加点赞成功";
            return new Response(status, message, cofFabulous.getId());
        }
        if (status == Status.EXISTS) {
            message = "点赞已经存在";
            return new Response(status, message);
        }
        message = "添加点赞失败";
        return new Response(status, message);
    }


    /**
     * 删除点赞
     *
     * @param request
     * @param req
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @ResponseBody
        @RequestMapping(value = "/api/cofFabulous/deletecofFabulous", method = RequestMethod.POST)
    public Object deletecofFabulous(HttpServletRequest request, @RequestBody Request req) {
        int status;
        String message = "";
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }
        CofFabulous cofFabulous = req.getCofFabulous();

        if (cofFabulous.getId() == null || cofFabulous.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = cofFabulousService.delete(cofFabulous);
        if (status == Status.NOT_EXISTS) {
            message = "点赞不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

    /**
     * 修改点赞
     *
     * @param request
     * @param cofFabulous     cofFabulous.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 点赞不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/cofFabulous/updatecofFabulous", method = RequestMethod.POST)
    public Object updatecofFabulous(HttpServletRequest request, @RequestBody CofFabulous cofFabulous) {
        int status = Status.ERROR;
        String message = "";
        if (cofFabulous.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = cofFabulousService.update(cofFabulous);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "点赞不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 点赞列表查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/cofFabulous/querycofFabulous", method = RequestMethod.GET)
    public Object querycofFabulous(HttpServletRequest request,
                           @RequestParam(value = "currentPage", required = false) Long currentPage,
                           @RequestParam(value = "maxRow", required = false) Long maxRow,
                           @RequestParam("sessionId")String sessionId) {
        //判断用户是否登录
        if (sessionId == null || sessionId.equals("")){
            return new Response(Status.ERROR,"sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if (httpSession == null){
            return new Response(14,"sessionId无效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14,"sessionId无效");
        }

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        cofFabulousService.query(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
}
