package com.aqb.cn.action;

import com.aqb.cn.bean.Admin;
import com.aqb.cn.bean.User;
import com.aqb.cn.bean.NewNotice;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.NoticeAndPay;
import com.aqb.cn.service.NewNoticeService;
import com.aqb.cn.service.NewPayService;
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
 * Created by Administrator on 2017/8/2.
 */
@Controller
public class NewNoticeAction {

    private final Log logger = LogFactory.getLog(NewNoticeAction.class);

    @Autowired
    private NewNoticeService newNoticeService;
    @Autowired
    private NewPayService newPayService;

    /**
     * 新增消息
     * APP申请消息
     *
     * @param newNotice 消息信息
     * @return state : 0 -- 成功, 6--消息已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/newNotice/addNewNotice", method = RequestMethod.POST)
    public Object addNewNotice(HttpServletRequest request,@RequestBody NewNotice newNotice) {
        int status;
        String message = "";

//        if(newNotice.getAccountNumber() == null || newNotice.getAccountNumber().equals("")){
//            return new Response(Status.ERROR, "消息账号不能为空");
//        }
//        if(newNotice.getnewNoticeMoney() == null || newNotice.getnewNoticeMoney() <= 0){
//            return new Response(Status.ERROR, "消息账号不能为空");
//        }

        newNotice.setId(UUIDCreator.getUUID());
        newNotice.setStatus(1);//状态(1--未读，2--已读）
        newNotice.setFoundDate(new Date());
        newNotice.setNoticeTitle("支付通知");//标题
        newNotice.setNoticeContent("内容");//内容
        newNotice.setNoticeType(1);//类型(1--支付消息，2--通知消息)

        status = newNoticeService.add(newNotice);
        if (status == Status.SUCCESS) {
            message = "成功";
            return new Response(status, message, newNotice.getId());
        }
        message = "失败";
        return new Response(status, message);
    }


    /**
     * 删除消息
     * @param request
     * @param newNotice
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/newNotice/deletenewNotice", method = RequestMethod.POST)
    public Object deletenewNotice(HttpServletRequest request, @RequestBody NewNotice newNotice) {
        int status;
        String message = "";
        if(newNotice.getId() == null || newNotice.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = newNoticeService.delete(newNotice);
        if(status == Status.NOT_EXISTS){
            message = "消息不存在";
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
     * 修改消息
     *
     * status 状态(1--未读，2--已读）
     * @param request
     * @param newNotice newNotice.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 消息不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/newNotice/updatenewNotice", method = RequestMethod.POST)
    public Object updatenewNotice(HttpServletRequest request, @RequestBody NewNotice newNotice) {
        int status = Status.ERROR;
        String message = "";
        if(newNotice.getId() == null || newNotice.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(newNotice.getStatus() == 2){
            //拒绝，将钱返回给客户。并添加通知消息

        }else if(newNotice.getStatus() == 3){
            //同意，将钱转给客户的账号。根据转账是否成功，来通知客户

        }else {
            return new Response(Status.ERROR, "status错误");
        }

        Admin obj = ActionUtil.getCurrentAdmin(request);

        status = newNoticeService.update(newNotice);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "消息不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 用户的通知消息列表查询
     * APP
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/newNotice/queryNewNotice", method = RequestMethod.POST)
    public Object querynewNoticeAdmin(HttpServletRequest request, @RequestBody Request req) {
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
        newNoticeService.query(query);
        newNoticeService.updateByStatus(obj.getId());//查询到用户的未读消息列表后，把所有的未读消息改为已读消息
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 用户查询自己是否有未读消息
     * APP
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/newNotice/queryNewNoticeStatus", method = RequestMethod.POST)
    public Object queryNewNoticeStatus(HttpServletRequest request,@RequestBody Request req) {
        //判断用户的session是否正常
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
        NoticeAndPay noticeAndPay = new NoticeAndPay();
        boolean noticeStatus = newNoticeService.queryNewNoticeStatus(obj.getId());
        boolean payStatus = newPayService.queryNewPayStatus(obj.getId());
        noticeAndPay.setNoticeStatus(noticeStatus);
        noticeAndPay.setPayStatus(payStatus);

        return new Response(Status.SUCCESS,"",noticeAndPay);
    }

//    /**
//     * 用户读取消息
//     *
//     * status 状态(1--未读，2--已读）
//     * @param request
//     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 消息不存在, 1 -- 修改失败
//     */
//    @ResponseBody
//    @RequestMapping(value = "/api/newNotice/updateNewNoticeRead", method = RequestMethod.POST)
//    public Object updateNewNoticeRead(HttpServletRequest request, @RequestBody Request req) {
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
//        NewNotice newNotice = req.getNewNotice();
//        if(newNotice == null || newNotice.getId() == null){
//            return new Response(Status.ERROR, "Id不能为空");
//        }
//        newNotice.setStatus(2);//2--已读
//
//        int status = newNoticeService.update(newNotice);
//        String message = "";
//        if(status == Status.SUCCESS){
//            message = "成功";
//        }else if(status == Status.NOT_EXISTS){
//            message = "消息不存在，修改失败";
//        }else {
//            message = "失败";
//        }
//        return new Response(status, message);
//    }

}
