package com.aqb.cn.action;

import com.aqb.cn.bean.Dizhi;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.service.DizhiService;
import com.aqb.cn.service.UserService;
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
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@Controller
public class DizhiAction {

    private final Log logger = LogFactory.getLog(DizhiAction.class);

    @Autowired
    private DizhiService dizhiService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 新增地址
     * @param req
     * @return state : 0 -- 成功, 6--地址已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/dizhi/addDizhi", method = RequestMethod.POST)
    public Object addDizhi(HttpServletRequest request,
                           @RequestBody Request req) {
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
        //从Request中获取地址参数
        Dizhi dizhi = req.getDizhi();
        dizhi.setId(UUIDCreator.getUUID());
        dizhi.setUserId(obj.getId());
        dizhi.setFoundDate(new Date());

        User user = userService.get(dizhi.getUserId());
        user.setId(dizhi.getUserId());
        //新用户初次添加地址将此地址设为默认地址
        if (user.getUserAddress() == null || user.getUserAddress().equals("")){
            user.setUserAddress(dizhi.getId());
            //dizhi.setStatus(1);//默认地址status设置为1
            userService.update(user);
        }
//        else {
//            dizhi.setStatus(0);//其余地址status设置为0
//        }

        if(dizhi.getDizhiName() == null || dizhi.getDizhiName().equals("") ||
                dizhi.getDizhiPhone() == null || dizhi.getDizhiPhone().equals("") ||
                dizhi.getDizhiDiqu() == null || dizhi.getDizhiDiqu().equals("") ||
//                dizhi.getDizhiJiedao() == null || dizhi.getDizhiJiedao().equals("") ||
                dizhi.getDizhiXiangxi() == null || dizhi.getDizhiXiangxi().equals("")
                ){
            return new Response(Status.ERROR, "信息不完整");
        }
        status = dizhiService.add(dizhi);

        if (status == Status.SUCCESS) {
            message = "添加地址成功";
            return new Response(status, message, dizhi.getId());
        }if(status == Status.EXISTS){
            message = "地址已经存在";
            return new Response(status, message);
        }
        message = "添加地址失败";
        return new Response(status, message);
    }


    /**
     * 删除地址
     * @param req
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/dizhi/deleteDizhi", method = RequestMethod.POST)
    public Object deleteDizhi(HttpServletRequest request, @RequestBody Request req) {
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
        Dizhi dizhi = req.getDizhi();
        if(dizhi.getId() == null || dizhi.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }

        //需要先判断是否删除的是默认地址
        //User obj = ActionUtil.getCurrentUser(request);
        if(obj.getUserAddress().equals(dizhi.getId())){
            //修改默认地址为空
            userMapper.updateAddressToNull(obj.getId());
            //userService.updateAddressToNull(obj.getId());
        }
        status = dizhiService.delete(dizhi);
        if(status == Status.NOT_EXISTS){
            message = "地址不存在";
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
     * 修改地址
     * @param req
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 地址不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/dizhi/updateDizhi", method = RequestMethod.POST)
    public Object updateDizhi(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
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

        Dizhi dizhi = req.getDizhi();
        dizhi.setUserId(obj.getId());
        if(dizhi.getId() == null || dizhi.equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        if (dizhi.getUserId() == null || dizhi.getUserId().equals("")){
            return new Response(Status.ERROR,"userId不能为空");
        }

        status = dizhiService.update(dizhi);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "地址不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 修改默认地址
     * @param req
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 地址不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/dizhi/updateDefaultDizhi", method = RequestMethod.POST)
    public Object updateDefaultDizhi(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "";

//        User user = req.getUser();
//        if(user.getUserAddress() == null || user.getUserAddress().equals("")){
//            return new Response(Status.ERROR, "不存在，修改失败");
//        }

        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //获取session在登录是保存的用户
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }

        User user = userService.get(obj.getId());
        user.setId(obj.getId());
        if(user.getUserAddress() == null || user.getUserAddress().equals("")){
            return new Response(Status.NOT_EXISTS,"不存在，修改失败");
        }
        Dizhi dizhi = req.getDizhi();
        //Dizhi dizhi = dizhiService.queryByUserId(user.getId());
        //String dizhiId = req.getDizhi().getId();
        user.setUserAddress(dizhi.getId());
        //status = userService.update(user);
        //默认全部地址的status都是0，这里做个处理，把默认地址的status改为1，前端好做区分
//        Dizhi dizhi = dizhiService.get(user.getUserAddress());
//        dizhi.setStatus(1);
//        List<Dizhi> dizhis = dizhiService.queryDizhi(obj.getId());
//        //默认全部地址的status都是0，这里做个处理，把默认地址的status改为1，前端好做区分
//        for(Dizhi dizhi1 : dizhis){
//            if(user.getUserAddress().equals(dizhi1.getId())){
//                dizhi.setStatus(1);//默认地址status设置为1
//            }else {
//                dizhi.setStatus(0);
//            }
//            dizhiService.update(dizhi);
//        }
        status = userService.update(user);

        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 地址列表查询
     *  status--1:默认地址
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/dizhi/queryDizhi", method = RequestMethod.POST)
    public Object queryDizhi(HttpServletRequest request,@RequestBody Request req) {
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //获取session在登录是保存的用户
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }

        User user = userService.get(obj.getId());
        List<Dizhi> dizhis = dizhiService.queryDizhi(obj.getId());
        //默认全部地址的status都是0，这里做个处理，把默认地址的status改为1，前端好做区分
        for(Dizhi dizhi : dizhis){
            if(user.getUserAddress() != null && user.getUserAddress().equals(dizhi.getId())
                    //||user.getUserAddress() == dizhi.getId()
                    ){
                dizhi.setStatus(1);//默认地址status设置为1
            }else {
                dizhi.setStatus(0);//其余地址status设置为0
            }

        }
        return new Response(Status.SUCCESS,dizhis);
    }



}
