package com.aqb.cn.action;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.bean.CofComment;
import com.aqb.cn.bean.User;
import com.aqb.cn.bean.UserFriends;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserFriendsMapper;
import com.aqb.cn.service.CofCommentService;
import com.aqb.cn.service.CofService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/*
 * Created by Administrator on 2017/6/27.
*/


@Controller
public class CofCommentAction {

    private final Log logger = LogFactory.getLog(CofCommentAction.class);

    @Autowired(required = true)
    private CofCommentService cofCommentService;
    @Autowired(required = true)
    private CofService cofService;
    @Autowired(required = true)
    private UserFriendsMapper userFriendsMapper;

/*
     * 新增评论
     *
     * @param req 评论信息
     * @return state : 0 -- 成功, 6--评论已存在
 */

    @ResponseBody
    @RequestMapping(value = "/api/cofComment/addcofComment", method = RequestMethod.POST)
    public Object addcofComment(HttpServletRequest request, @RequestBody Request req) {
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

        CofComment cofComment = req.getCofComment();

        //判断是否为好友
        Cof cof = cofService.get(cofComment.getCofId());
        if(!cof.getUserId().equals(obj.getId())){
            if(cof != null){
                UserFriends userFriends = userFriendsMapper.selectByUserId(cof.getUserId(),obj.getId());
                if (userFriends == null){
                    return new Response(Status.ERROR,"");
                }
            }
        }

        //从Request中获取评论参数
        cofComment.setUserId(obj.getId());
        cofComment.setId(UUIDCreator.getUUID());
        cofComment.setFoundDate(new Date());

        if (cofComment.getCofId() == null || cofComment.getCofId().equals("")){
            return new Response(Status.ERROR,"CofId不能为空");
        }
        if (cofComment.getStatus() == null ||
            (cofComment.getStatus() != 1 && cofComment.getStatus() != 2)){
            return new Response(Status.ERROR,"Status不能为空");
        }
        if (cofComment.getStatus() == 1){
            Cof cof1 = cofService.get(cofComment.getCofId());
            if (cof1 == null){
                return new Response(Status.ERROR,"CofId不存在。");
            }
        }else {
            CofComment cofComment1 = cofCommentService.get(cofComment.getCofId());
            if (cofComment1 == null){
                return new Response(Status.ERROR,"CofId不存在");
            }
        }

        status = cofCommentService.add(cofComment);
        if (status == Status.SUCCESS) {
            message = "添加评论成功";
            return new Response(status, message, cofComment.getId());
        }
        if (status == Status.EXISTS) {
            message = "评论已经存在";
            return new Response(status, message);
        }
        message = "添加评论失败";
        return new Response(status, message);
    }


/*
     * 删除评论
     *
     * @param request
     * @param cofComment
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
*/

    @ResponseBody
    @RequestMapping(value = "/api/cofComment/deletecofComment", method = RequestMethod.POST)
    public Object deletecofComment(HttpServletRequest request, @RequestBody Request req) {
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

        CofComment cofComment = req.getCofComment();
        if (cofComment.getId() == null || cofComment.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = cofCommentService.delete(cofComment);
        if (status == Status.NOT_EXISTS) {
            message = "评论不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

/*
     * 修改评论
     *
     * @param request
     * @param cofComment     cofComment.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 评论不存在, 1 -- 修改失败
*/

//    @ResponseBody
//    @RequestMapping(value = "/api/cofComment/updatecofComment", method = RequestMethod.POST)
    public Object updatecofComment(HttpServletRequest request, @RequestBody CofComment cofComment) {
        int status = Status.ERROR;
        String message = "";
        if (cofComment.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = cofCommentService.update(cofComment);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "评论不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

/*
     * 评论列表查询
     *
     * @param request
     * @return
*/

    @ResponseBody
    @RequestMapping(value = "/api/cofComment/querycofComment", method = RequestMethod.GET)
    public Object querycofComment(HttpServletRequest request,
                           @RequestParam(value = "currentPage", required = false) Long currentPage,
                           @RequestParam(value = "maxRow", required = false) Long maxRow,
                           @RequestParam("sessionId")String sessionId) {
        //判断用户是否登录
        if (sessionId == null || sessionId.equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        cofCommentService.query(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
}

