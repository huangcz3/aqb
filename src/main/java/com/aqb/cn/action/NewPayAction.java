package com.aqb.cn.action;

import com.aqb.cn.bean.Admin;
import com.aqb.cn.bean.NewPay;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.NewPayService;
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

/**
 * Created by Administrator on 2017/8/3.
 */
@Controller
public class NewPayAction {

    private final Log logger = LogFactory.getLog(NewPayAction.class);

    @Autowired
    private NewPayService newPayService;

    /**
     * 新增
     * APP
     *
     * @param newPay 支付消息信息
     * @return state : 0 -- 成功, 6--支付消息已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/newPay/addnewPay", method = RequestMethod.POST)
    public Object addnewPay(HttpServletRequest request,@RequestBody NewPay newPay) {
        int status;
        String message = "";

//        if(newPay.getAccountNumber() == null || newPay.getAccountNumber().equals("")){
//            return new Response(Status.ERROR, "支付消息账号不能为空");
//        }
//        if(newPay.getnewPayMoney() == null || newPay.getnewPayMoney() <= 0){
//            return new Response(Status.ERROR, "支付消息账号不能为空");
//        }

        newPay.setId(UUIDCreator.getUUID());
        newPay.setStatus(1);//状态(1--未读，2--已读）
        newPay.setFoundDate(new Date());


        status = newPayService.add(newPay);
        if (status == Status.SUCCESS) {
            message = "成功";
            return new Response(status, message, newPay.getId());
        }
        message = "失败";
        return new Response(status, message);
    }


    /**
     * 删除支付消息
     * @param request
     * @param newPay
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/newPay/deletenewPay", method = RequestMethod.POST)
    public Object deletenewPay(HttpServletRequest request, @RequestBody NewPay newPay) {
        int status;
        String message = "";
        if(newPay.getId() == null || newPay.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = newPayService.delete(newPay);
        if(status == Status.NOT_EXISTS){
            message = "支付消息不存在";
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
     * 修改支付消息
     *
     * status 状态(1--未读，2--已读）
     * @param request
     * @param newPay newPay.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 支付消息不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/newPay/updatenewPay", method = RequestMethod.POST)
    public Object updatenewPay(HttpServletRequest request, @RequestBody NewPay newPay) {
        int status = Status.ERROR;
        String message = "";
        if(newPay.getId() == null || newPay.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(newPay.getStatus() == 2){
            //拒绝，将钱返回给客户。并添加通知支付消息

        }else if(newPay.getStatus() == 3){
            //同意，将钱转给客户的账号。根据转账是否成功，来通知客户

        }else {
            return new Response(Status.ERROR, "status错误");
        }

        Admin obj = ActionUtil.getCurrentAdmin(request);

        status = newPayService.update(newPay);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "支付消息不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 用户的支付消息列表查询
     * APP
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/newPay/queryNewPay", method = RequestMethod.POST)
    public Object queryNewPay(HttpServletRequest request, @RequestBody Request req) {
        //获取session
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "登录失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "登录失效");
        }
        QueryBase query = new QueryBase();
        if (req.getCurrentPage() != null) {
            query.setCurrentPage(req.getCurrentPage());
        }
//        query.getParameters().put("status", 1);//状态(1--未读，2--已读）
        query.getParameters().put("userId", obj.getId());//用户id
        newPayService.query(query);
        newPayService.updateByStatus(obj.getId());//查询到用户的未读消息列表后，把所有的未读消息改为已读消息
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }


//    /**
//     * 用户读取支付消息
//     *
//     * status 状态(1--未读，2--已读）
//     * @param request
//     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 支付消息不存在, 1 -- 修改失败
//     */
//    @ResponseBody
//    @RequestMapping(value = "/api/newPay/updatenewPayRead", method = RequestMethod.POST)
//    public Object updatenewPayRead(HttpServletRequest request, @RequestBody Request req) {
//        //获取session
//        if (req.getSessionId() == null || req.getSessionId().equals("")) {
//            return new Response(Status.ERROR, "sessionId不能为空");
//        }
//        MySessionContext myc = MySessionContext.getInstance();
//        HttpSession httpSession = myc.getSession(req.getSessionId());
//        if (httpSession == null) {
//            return new Response(14, "登录失效");
//        }
//        //登录时，用户信息已经保存在session中，在session中取出用户信息
//        User obj = ActionUtil.getCurrentUser(httpSession);
//        if (obj == null) {
//            return new Response(14, "登录失效");
//        }
//        NewPay newPay = req.getNewPay();
//        if(newPay == null || newPay.getId() == null){
//            return new Response(Status.ERROR, "Id不能为空");
//        }
//        newPay.setStatus(2);//2--已读
//
//        int status = newPayService.update(newPay);
//        String message = "";
//        if(status == Status.SUCCESS){
//            message = "成功";
//        }else if(status == Status.NOT_EXISTS){
//            message = "支付消息不存在，修改失败";
//        }else {
//            message = "失败";
//        }
//        return new Response(status, message);
//    }
    
}
