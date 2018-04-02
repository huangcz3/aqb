package com.aqb.cn.action;

import com.aqb.cn.bean.User;
import com.aqb.cn.bean.UserFriends;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.UserFriendsService;
import com.aqb.cn.service.UserService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Controller
public class UserFriendsAction {

    private final Log logger = LogFactory.getLog(UserFriendsAction.class);

    @Autowired
    private UserFriendsService userFriendsService;
    @Autowired
    private UserService userService;

    /**
     * 新增联系人
     * @param req 联系人信息
     * @return state : 0 -- 成功, 6--联系人已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/userFriends/addUserFriends", method = RequestMethod.POST)
    public Object addUserFriends(HttpServletRequest request,@RequestBody Request req) {
        int status;
        String message = "";
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"登录失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"登录失效");
        }
        User user_db = userService.get(obj.getId());
        if(user_db == null || user_db.getUserName() == null){
            return new Response(Status.ERROR,"未绑定手机号，不能添加联系人");
        }
        UserFriends userFriends = req.getUserFriends();
        if(userFriends.getUserId2() == null || userFriends.getUserId2().equals("")){//userFriends.getUserId2() 先装用户输入的手机号
            return new Response(Status.ERROR, "手机号不能为空");
        }
        User user2 = userService.queryByName(userFriends.getUserId2());
        if(user2 == null){
            return new Response(Status.ERROR, "添加的联系人不存在");
        }
        userFriends.setUserId2(user2.getId());
        userFriends.setId(UUIDCreator.getUUID());
        userFriends.setStatus(2);
        userFriends.setFoundDate(new Date());
        userFriends.setUserId(obj.getId());

        status = userFriendsService.add(userFriends);
        if (status == Status.SUCCESS) {
            message = "添加联系人成功";
            return new Response(status, message, userFriends.getId());
        }if(status == Status.EXISTS){
            message = "联系人已经存在";
            return new Response(status, message);
        }
        message = "添加联系人失败";
        return new Response(status, message);
    }


    /**
     * 删除联系人
     * @param request
     * @param userFriends
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/userFriends/deleteuserFriends", method = RequestMethod.POST)
    public Object deleteuserFriends(HttpServletRequest request, @RequestBody UserFriends userFriends) {
        int status;
        String message = "";
        if(userFriends.getId() == null || userFriends.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = userFriendsService.delete(userFriends);
        if(status == Status.NOT_EXISTS){
            message = "联系人不存在";
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
     * 修改联系人
     * @param request
     * @param userFriends userFriends.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 联系人不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/userFriends/updateuserFriends", method = RequestMethod.POST)
    public Object updateuserFriends(HttpServletRequest request, @RequestBody UserFriends userFriends) {
        int status = Status.ERROR;
        String message = "";
        if(userFriends.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = userFriendsService.update(userFriends);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "联系人不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 联系人列表查询
     *  APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/userFriends/queryUserFriends", method = RequestMethod.POST)
    public Object queryUserFriends(HttpServletRequest request,@RequestBody Request req) {
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"登录失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"登录失效");
        }
        List<User> users = userFriendsService.queryUserFriends(obj.getId());

        return new Response(Status.SUCCESS,users);
    }
    
}
