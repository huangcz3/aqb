package com.aqb.cn.action;

import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.MyAssets_redpacket;
import com.aqb.cn.service.JifenService;
import com.aqb.cn.service.RedpacketService;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
@Controller
public class RedpacketAction {

    private final Log logger = LogFactory.getLog(RedpacketAction.class);

    @Autowired
    private RedpacketService redpacketService;
    @Autowired
    private UserService userService;
    @Autowired
    private JifenService jifenService;


    /**
     * 新增红包
     * @param req
     * @return state : 0 -- 成功, 6--红包已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/redpacket/addredpacket", method = RequestMethod.POST)
    public Object addredpacket(HttpServletRequest request,@RequestBody Request req) {
        int status;
        String message = "";
//        if(redpacket.getredpacketCode() == null || redpacket.getredpacketCode() == ""){
//            return new Response(Status.ERROR, "不能为空");
//        }
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

        Redpacket redpacket = req.getRedpacket();
        redpacket.setId(UUIDCreator.getUUID());
        redpacket.setUserId(obj.getId());
        redpacket.setRedFoundDate(new Date());

        status = redpacketService.add(redpacket);
        if (status == Status.SUCCESS) {
            message = "添加红包成功";
            return new Response(status, message, redpacket.getId());
        }if(status == Status.EXISTS){
            message = "红包已经存在";
            return new Response(status, message);
        }
        message = "添加红包失败";
        return new Response(status, message);
    }




    /**
     * 删除红包
     * @param request
     * @param redpacket
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/redpacket/deleteredpacket", method = RequestMethod.POST)
    public Object deleteredpacket(HttpServletRequest request, @RequestBody Redpacket redpacket) {
        int status;
        String message = "";
        if(redpacket.getId() == null || redpacket.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = redpacketService.delete(redpacket);
        if(status == Status.NOT_EXISTS){
            message = "红包不存在";
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
     * 修改红包
     * @param request
     * @param redpacket redpacket.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 红包不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/redpacket/updateredpacket", method = RequestMethod.POST)
    public Object updateredpacket(HttpServletRequest request, @RequestBody Redpacket redpacket) {
        int status = Status.ERROR;
        String message = "";
        if(redpacket.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = redpacketService.update(redpacket);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "红包不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 我的红包资产查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/redpacket/queryredpacket", method = RequestMethod.GET)
    public Object querymyAssets(HttpServletRequest request, @RequestParam("sessionId")String sessionId) {

        //判断用户的session是否正常
        //获取session
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

        float count_redpacket = 0;
        MyAssets_redpacket myAssets = new MyAssets_redpacket();
        List<Redpacket> redpacketList = new ArrayList<>();
        List<Redpacket> redpackets = redpacketService.queryRedpacketByUserId(obj.getId());//查出该userId的红包表

        for (Redpacket redpacket:redpackets){
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p =decimalFormat.format(redpacket.getRedSubtotal());//format 返回的是字符串
            redpacket.setRedSubtotal(Float.valueOf(p));
            redpacketList.add(redpacket);
        }
        //传入myAssets

        myAssets.setRedpackets(redpacketList);
        
        //红包
        for (Redpacket redpacket:redpacketList) {
            //true为收入，false为支出
            if (!redpacket.getRedIncomeOut()){
                Float f = redpacket.getRedSubtotal();
                redpacket.setRedSubtotal(-f);//支出，取负数
            }
            float f1 = redpacket.getRedSubtotal();
            count_redpacket = count_redpacket + f1;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p =decimalFormat.format(count_redpacket);//format 返回的是字符串
        myAssets.setRedpacket_Assets(Float.valueOf(p));
        
        return new Response(Status.SUCCESS,myAssets);
    }

    /**
     * 红包提现
     * @param request
     * @param deposit_money deposit_money
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 红包不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/redpacket/redpacket", method = RequestMethod.POST)
    public Object redpacket(HttpServletRequest request,
                                   @RequestParam("sessionId")String sessionId,
                                   @RequestParam("deposit_money") float deposit_money) {
        //判断用户的session是否正常
        //获取session
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
        //红包总金额
        float myAssets_repacket = 0;
        List<Redpacket> redpackets = redpacketService.queryRedpacketByUserId(obj.getId());
        for (Redpacket red:redpackets){
            if (!red.getRedIncomeOut()){
                Float f = red.getRedSubtotal();
                red.setRedSubtotal(-f);
            }
            myAssets_repacket = myAssets_repacket + red.getRedSubtotal();
        }
        if (deposit_money > myAssets_repacket){
            return new Response(Status.ERROR,"红包金额不足，无法提现");
        }

        Redpacket redpacket = new Redpacket();
        redpacket.setId(UUIDCreator.getUUID());
        redpacket.setUserId(obj.getId());
        redpacket.setRedIncomeOut(false);//提现，支出
        redpacket.setRedCategory(1);//1-提现
        redpacket.setRedSubtotal(myAssets_repacket);
        redpacket.setRedStatus(0);
        redpacket.setRedFoundDate(new Date());
        return new Response(Status.SUCCESS,"提现成功");
    }


    /**
     * 红包转余额
     * @param request
     * @param
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 红包不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/redpacket/redpacketToYue", method = RequestMethod.GET)
    public Object redpacketToYue(HttpServletRequest request,
                            @RequestParam("sessionId")String sessionId,
                            @RequestParam("deposit_money") String deposit_money1) {
        Float deposit_money = Float.valueOf(deposit_money1);
        //判断用户的session是否正常
        //获取session
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
        //红包总金额
        Float myAssets_repacket = redpacketService.countAsessts(obj.getId());
        if (deposit_money > myAssets_repacket){
            return new Response(Status.ERROR,"红包金额不足");
        }
        redpacketService.updateRedpactToYue(obj.getId(),deposit_money,deposit_money);
        return new Response(Status.SUCCESS,"成功");
    }
}
