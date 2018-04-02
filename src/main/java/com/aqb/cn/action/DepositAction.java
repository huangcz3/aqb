package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DepositMoneyMapper;
import com.aqb.cn.pojo.DepositAndUser;
import com.aqb.cn.service.*;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/7/27/0027.
 */
@Controller
public class DepositAction {
    private final Log logger = LogFactory.getLog(DepositAction.class);

    @Autowired
    private BindingService bindingService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private DizhiService dizhiService;
    @Autowired
    private DepositMoneyService depositMoneyService;
    @Autowired
    private DepositMoneyMapper depositMoneyMapper;



    /**
     * 搜索用户
     * 后台
     *  传进手机号或用户名
     * @param req userName
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/searchUser", method = RequestMethod.POST)
    public Object searchUser(HttpServletRequest request,@RequestBody Request req) {
        Long reqLimit = req.getLimit();
        if (reqLimit.equals("") || reqLimit<=0){
            req.setLimit(0);
        }else {
            req.setLimit((reqLimit-1)*10);
        }

        List<User> users = userService.queryUser(req);
        List<DepositAndUser> depositAndUsers = new ArrayList<>();
        for (int i=0;i<users.size();i++){
            Deposit deposit = depositService.queryDepositByUserId(users.get(i).getId());
            Binding binding = bindingService.queryByUserId1(users.get(i).getId());//是否成功绑定
            DepositAndUser depositAndUser = new DepositAndUser();
            depositAndUser.setUserName(users.get(i).getUserName());
            depositAndUser.setUserFull(users.get(i).getUserFull());
            depositAndUser.setAddress(users.get(i).getUserAddress());
            Dizhi dizhi = dizhiService.get(users.get(i).getUserAddress());
            depositAndUser.setDizhi(dizhi);
            if (binding == null){
                depositAndUser.setBinding_status(0);
            }else {
                depositAndUser.setBinding_status(1);
            }
            if (deposit == null){
                depositAndUser.setDeposit_status(0);
            } else {
                depositAndUser.setDeposit_status(1);
            }
            depositAndUsers.add(i,depositAndUser);
        }
        long currentPage = reqLimit;//当前页
//        long totalRow = depositAndUsers.size();//总行数
        long totalRow = userService.countUser(req);
        long totalPage;//总页数
        if (totalRow <=10){
            totalPage =1;
        }else if (totalRow % 10 > 0){
            totalPage = totalRow/10+1;
        }else {
            totalPage = totalRow/10;
        }
        if (reqLimit <= 0){
            currentPage = 1;
        }

        return new Response(Status.SUCCESS,totalRow,totalPage,depositAndUsers,currentPage);
    }
    /**
     * 已交押金但未绑定用户
     * 后台
     * @param
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/searchUser1", method = RequestMethod.POST)
    public Object searchUser1(HttpServletRequest request,@RequestBody Request req) {
        req.setUserName("");
        List<User> users = userService.queryUser(req);
        List<DepositAndUser> depositAndUsers = new ArrayList<>(users.size());
        List<DepositAndUser> depositAndUsers1 = new ArrayList<>();
        for (int i=0;i<users.size();i++){
            Deposit deposit = depositService.queryDepositByUserId(users.get(i).getId());
            Binding binding = bindingService.queryByUserId1(users.get(i).getId());//已成功绑定设备
            DepositAndUser depositAndUser = new DepositAndUser();
            depositAndUser.setUserName(users.get(i).getUserName());
            depositAndUser.setUserFull(users.get(i).getUserFull());
            depositAndUser.setAddress(users.get(i).getUserAddress());
            Dizhi dizhi = dizhiService.get(users.get(i).getUserAddress());
            depositAndUser.setDizhi(dizhi);
            if (binding == null){
                depositAndUser.setBinding_status(0);
            }else {
                depositAndUser.setBinding_status(1);
            }
            if (deposit == null){
                depositAndUser.setDeposit_status(0);
            } else {
                depositAndUser.setDeposit_status(1);
            }
            depositAndUsers.add(i,depositAndUser);
        }
        //未绑定且已缴纳押金用户
        for (int i=0;i<depositAndUsers.size();i++){
            if (depositAndUsers.get(i).getBinding_status() == 0 && depositAndUsers.get(i).getDeposit_status() ==1){
                DepositAndUser depositAndUser = depositAndUsers.get(i);
                depositAndUsers1.add(depositAndUser);
            }
        }
        long currentPage = req.getLimit();//当前页
        long totalRow = depositAndUsers1.size();//总行数
        long totalPage;//总页数s
        if (totalRow <=10){
            totalPage =1;
        }else if (totalRow % 10 > 0){
            totalPage = totalRow/10+1;
        }else {
            totalPage = totalRow/10;
        }
        if (req.getLimit() <= 0){
            currentPage = 1;
        }
        return new Response(Status.SUCCESS,totalRow,totalPage,depositAndUsers1,currentPage);//数据库多少条，
    }

    /**
     * 充值押金
     * 后台
     *
     * @param req 传手机号
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/rechargeDeposit", method = RequestMethod.POST)
    public Object rechargeDeposit(HttpServletRequest request,@RequestBody Request req) {

        int status = Status.ERROR;
        String message="";
        //押金表添加一条记录，并查询该用户是否绑定设备，若绑定则更新押金状态
        String userName = req.getUserName();
        User user = userService.queryByName(userName);
        Deposit deposit_db = depositService.queryDepositByUserId(user.getId());//成功充值押金的记录
        //List<Binding> queryByUserId2(String userId);//查询曾经绑定的设备（有失败记录）
        List<Binding> bindings = bindingService.queryByUserId2(user.getId());//查询曾经绑定的设备（有失败记录）
        //Binding binding_db = bindingService.queryByUserId1(user.getId());//查询成功绑定的设备
        DepositMoney depositMoney = depositMoneyService.queryDepositMoney();
        if (deposit_db != null){
            return new Response(Status.ERROR,"该用户已成功充值押金");
        }
        Deposit deposit = new Deposit();
        deposit.setId(UUIDCreator.getUUID());
        deposit.setUserId(user.getId());
        deposit.setPayStatus(3);//支付方式（1-微信支付，2-支付宝支付，3-线下支付）
        deposit.setMoneyId(depositMoney.getId());
        status = depositService.add(deposit);
        for (Binding binding:bindings){
            //审核中、成功绑定设备
            if (binding.getStatus() == 1 || binding.getStatus() == 2){
                binding.setDepositStatus(1);
                bindingService.update(binding);
            }
        }
//        if (binding_db != null){
//            binding_db.setDepositStatus(1);
//            bindingService.update(binding_db);
//        }
        message = "充值成功";
        return new Response(status,message);
    }

    /**
     * 退还押金
     * 后台
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/return_deposit", method = RequestMethod.POST)
    public Object return_deposit(HttpServletRequest request) {
        //更新该用户的押金表里的数据status=1，删除该用户绑定设备表里的数据

        return null;
    }

    /**
     * 押金金额查询
     * 后台
     *
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/selectDepositNumber", method = RequestMethod.GET)
    public Object selectDepositNumber(HttpServletRequest request) {
        return new Response(Status.SUCCESS,depositMoneyMapper.queryDepositMoney());

    }

    /**
     * 修改押金金额（修改押金金额表）
     * 后台
     *
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/deposit/updateDepositNumber", method = RequestMethod.POST)
    public Object updateDepositNumber(HttpServletRequest request,@RequestParam("money")Float money) {
        if(money == null || money <= 0){
            return new Response(Status.ERROR,"金额不能为空");
        }
        //修改数据库原有的数据状态
        int key = depositMoneyMapper.updateAll();
        //添加一条新数据
        if(key > 0){
            DepositMoney depositMoney = new DepositMoney();
            depositMoney.setFoundDate(new Date());
            depositMoney.setStatus(1);
            depositMoney.setMoney(money);
            depositMoney.setAdminId("");//获取管理员id，set进去
            int num = depositMoneyMapper.insertSelective(depositMoney);
            if(num > 0){
                return new Response(Status.ERROR,"成功");
            }
        }
        return new Response(Status.ERROR,"失败");

    }
}
