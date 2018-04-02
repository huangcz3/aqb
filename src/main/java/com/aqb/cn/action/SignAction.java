package com.aqb.cn.action;

import com.aqb.cn.bean.Jifen;
import com.aqb.cn.bean.Sign;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.JifenService;
import com.aqb.cn.service.SignService;
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
 * Created by Administrator on 2017/7/11.
 */
@Controller
public class SignAction {

    private final Log logger = LogFactory.getLog(SignAction.class);

    @Autowired
    private SignService signService;
    @Autowired
    private JifenService jifenService;

    /**
     * 新增用户签到
     * APP
     * @param req
     * @return state : 0 -- 成功, 6--用户签到已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/sign/addsign", method = RequestMethod.POST)
    public Object addsign(HttpServletRequest request, @RequestBody Request req) {
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
        //今日是否已签到
        if (signService.querytodaySign(obj.getId()) != null){
            return new Response(Status.EXISTS,"用户签到已存在");
        }

        Sign sign = new Sign();
        sign.setId(UUIDCreator.getUUID());
        sign.setUserId(obj.getId());
        sign.setFoundDate(new Date());
        status = signService.add(sign);
        //完成签到即可获得积分奖励
        Jifen jifen = new Jifen();
        jifen.setId(UUIDCreator.getUUID());
        jifen.setUserId(sign.getUserId());
        jifen.setJifenCategory(1);//类别：签到
        jifen.setJifenIncomeOut(true);//收入
        jifen.setJifenSubtotal(new Float(2));//小计：2积分
        jifen.setJifenFoudDate(new Date());
        jifenService.add(jifen);
        if (status == Status.SUCCESS) {
            message = "添加用户签到成功";
            return new Response(status, message, sign.getId());
        }if(status == Status.EXISTS){
            message = "用户签到已经存在";
            return new Response(status, message);
        }
        message = "添加用户签到失败";
        return new Response(status, message);
    }


    /**
     * 删除用户签到
     * @param request
     * @param sign
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/sign/deletesign", method = RequestMethod.POST)
    public Object deletesign(HttpServletRequest request, @RequestBody Sign sign) {
        int status;
        String message = "";
        if(sign.getId() == null || sign.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = signService.delete(sign);
        if(status == Status.NOT_EXISTS){
            message = "用户签到不存在";
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
     * 修改用户签到
     * @param request
     * @param sign sign.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 用户签到不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/sign/updatesign", method = RequestMethod.POST)
    public Object updatesign(HttpServletRequest request, @RequestBody Sign sign) {
        int status = Status.ERROR;
        String message = "";
        if(sign.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = signService.update(sign);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "用户签到不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 用户签到列表查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/sign/querysign", method = RequestMethod.POST)
    public Object querysign(HttpServletRequest request,@RequestBody Request req) {

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

        List<Sign> signs = signService.querySignByUserId(obj.getId());
        return new Response(Status.SUCCESS,signs);
    }


    /**
     * 用户最近七天的签到查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/sign/querySevenSign", method = RequestMethod.POST)
    public Object querySevenSign(HttpServletRequest request,@RequestBody Request req) {

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


        //List<Sign> signs = signService.querysign();
        List<Sign> signs = signService.querySevenSign(obj.getId());
        return new Response(Status.SUCCESS,signs);
    }

}
