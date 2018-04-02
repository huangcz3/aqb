package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Admin;
import com.aqb.cn.bean.User;
import com.aqb.cn.bean.Withdrawals;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.WithdrawalsService;
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
import java.util.Map;

/**
 * Created by Administrator on 2017/8/1.
 */
@Controller
public class WithdrawalsAction {

    private final Log logger = LogFactory.getLog(WithdrawalsAction.class);

    @Autowired
    private WithdrawalsService withdrawalsService;

    /**
     * 新增提现
     * APP申请提现
     *
     * @param req 提现信息
     * @return state : 0 -- 成功, 6--提现已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/withdrawals/addWithdrawals", method = RequestMethod.POST)
    public Object addWithdrawals(HttpServletRequest request,@RequestBody Request req) {
        int status;
        String message = "";
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

        Withdrawals withdrawals = req.getWithdrawals();
        if(withdrawals.getAccountNumber() == null || withdrawals.getAccountNumber().equals("")){
            return new Response(Status.ERROR, "提现账号不能为空");
        }
        if(withdrawals.getWithdrawalsMoney() == null || withdrawals.getWithdrawalsMoney() <= 0){
            return new Response(Status.ERROR, "提现账号不能为空");
        }

        withdrawals.setId(UUIDCreator.getUUID());
        withdrawals.setStatus(1);//状态（1--未审核，2--已拒绝，3--已通过）
        withdrawals.setFoundDate(new Date());
        withdrawals.setAccountNumberType(1);//提现账号类型(1--微信)
//        withdrawals.setUserId(obj.getId());

        status = withdrawalsService.add(withdrawals);
        if (status == Status.SUCCESS) {
            message = "提现成功,等待审核";
            return new Response(status, message, withdrawals.getId());
        }if(status == 20){
            message = "金额不足";
            return new Response(status, message);
        }
        message = "提现失败";
        return new Response(status, message);
    }


    /**
     * 删除提现
     * @param request
     * @param withdrawals
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/withdrawals/deletewithdrawals", method = RequestMethod.POST)
    public Object deletewithdrawals(HttpServletRequest request, @RequestBody Withdrawals withdrawals) {
        int status;
        String message = "";
        if(withdrawals.getId() == null || withdrawals.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = withdrawalsService.delete(withdrawals);
        if(status == Status.NOT_EXISTS){
            message = "提现不存在";
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
     * 提现审核
     * 后台
     * status 状态（1--未审核，2--已拒绝，3--已通过）
     * @param request
     * @param withdrawals withdrawals.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 提现不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/withdrawals/updateWithdrawals", method = RequestMethod.POST)
    public Object updateWithdrawals(HttpServletRequest request, @RequestBody Withdrawals withdrawals) {
        int status = Status.ERROR;
        String message = "";
        if(withdrawals.getId() == null || withdrawals.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(withdrawals.getStatus() == 2){
            //拒绝，将钱返回给客户。并添加通知消息

        }else if(withdrawals.getStatus() == 3){
            //同意，将钱转给客户的账号。根据转账是否成功，来通知客户

        }else {
            return new Response(Status.ERROR, "status错误");
        }

        Admin obj = ActionUtil.getCurrentAdmin(request);
//        withdrawals.setAdminId(obj.getId());
        withdrawals.setOperationDate(new Date());
        status = withdrawalsService.update(withdrawals);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "提现不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 提现列表查询
     * 后台
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/withdrawals/queryWithdrawalsAdmin", method = RequestMethod.GET)
    public Object queryWithdrawalsAdmin(HttpServletRequest request,
                                        @RequestParam(value = "currentPage", required = false) Long currentPage,
                                        @RequestParam(value = "maxRow", required = false) Long maxRow) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        query.getParameters().put("status", 1);//状态（1--未审核，2--已拒绝，3--已通过）
        withdrawalsService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }
    
}
