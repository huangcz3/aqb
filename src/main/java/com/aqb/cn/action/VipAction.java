package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.EncryptionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.MyVip;
import com.aqb.cn.service.*;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
public class VipAction {

    private final Log logger = LogFactory.getLog(VipAction.class);

    @Autowired
    private VipService vipService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private UserService userService;
    @Autowired
    private YueService yueService;
    @Autowired
    private JifenService jifenService;
    @Autowired
    private NewPayService newPayService;

    /**
     * 新增vip权限
     * （增加vip权限类别）
     * 后台
     * @param vip         vip权限信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--vip权限已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/vip/addVip", method = RequestMethod.POST)
    public Object addvip(HttpServletRequest request, @RequestBody Vip vip, HttpSession httpSession) {
        int status;
        String message = "";
        if (vip.getVipName() == null || vip.getVipName().equals("")) {
            return new Response(Status.ERROR, "名称不能为空");
        }
        //如果未设置，默认为未拥有 0--未拥有，1--已拥有
        //vip.setId(UUIDCreator.getUUID());
        vip.setFoundDate(new Date());
        vip.setStatus(1);

        status = vipService.add(vip);
        if (status == Status.SUCCESS) {
            message = "添加vip权限成功";
            return new Response(status, message, vip.getId());
        }
        if (status == Status.EXISTS) {
            message = "vip权限已经存在";
            return new Response(status, message);
        }
        message = "添加vip权限失败";
        return new Response(status, message);
    }


    /**
     * 删除vip权限
     *
     * @param request
     * @param vip
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/vip/deleteVip", method = RequestMethod.POST)
    public Object deleteVip(HttpServletRequest request, @RequestBody Vip vip) {
        int status;
        String message = "";
        if (vip.getId() == null || vip.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = vipService.delete(vip);
        if (status == Status.NOT_EXISTS) {
            message = "vip权限不存在";
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
     * 修改vip权限
     * 后台
     * @param request
     * @param vip     vip.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- vip权限不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/vip/updateVip", method = RequestMethod.POST)
    public Object updateVip(HttpServletRequest request, @RequestBody Vip vip) {
        int status = Status.ERROR;
        String message = "";
        if (vip.getId() == null || vip.getId().equals("")) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        vip.setVipName(null);
        status = vipService.update(vip);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "vip权限不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 修改vip价格
     * 后台
     *
     * @param request
     * @param vip     vip.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- vip权限不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/vip/updateVipPrice", method = RequestMethod.POST)
    public Object updateVipPrice(HttpServletRequest request, @RequestBody Vip vip) {
        int status = Status.ERROR;
        String message = "";
        if (vip.getId() == null || vip.getId().equals("")) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        Vip vip_db = new Vip();
        vip_db.setId(vip.getId());
        if (vip.getVipMoney() != null) {
            vip_db.setVipMoney(vip.getVipMoney());
        }
        if (vip.getVipJifen() != null) {
            vip_db.setVipJifen(vip.getVipJifen());
        }

        //vip.setVipName(null);
        status = vipService.update(vip_db);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "vip权限不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * vip权限列表查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/vip/queryVip", method = RequestMethod.GET)
    public Object queryVip() {
//        //判断用户的session是否正常
//        //获取session
//        if (req.getSessionId() == null || req.getSessionId().equals("")) {
//            return new Response(Status.ERROR, "sessionId不能为空");
//        }
//        MySessionContext myc = MySessionContext.getInstance();
//        HttpSession httpSession = myc.getSession(req.getSessionId());
//        if (httpSession == null) {
//            return new Response(14, "sessionId失效");
//        }
//        //登录时，用户信息已经保存在session中，在session中取出用户信息
//        User obj = ActionUtil.getCurrentUser(httpSession);
//        if (obj == null) {
//            return new Response(14, "sessionId失效");
//        }
//        User user = userService.get(obj.getId());

        List<Vip> vips = vipService.queryvip();
        return new Response(Status.SUCCESS, vips);
    }

    /**
     * 购买会员
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/yue/buyVip", method = RequestMethod.POST)
    public Object buyVip(HttpServletRequest request, @RequestBody Request req) {
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
        UserVip userVip = req.getUserVip();
        User user = userService.get(obj.getId());
        ////前端传参数进来，通过会员等级vipGrade在vip表查到vip
        Vip vip = vipService.selectByGrade(userVip.getVipGrade());
        float myAssets_yue = 0;
        float myAssets_jifen = 0;
        List<Yue> yues = yueService.queryYueByUserId(obj.getId());//查出该userId的余额表
        List<Jifen> jifens = jifenService.queryJifenByUserId(obj.getId());//查出该userId的积分表
        //统计余额
        for (Yue yue : yues) {
            //true为收入，false为支出
            if (!yue.getYueIncomeOut()) {
                Float f = yue.getYueSubtotal();
                yue.setYueSubtotal(-f);//支出，取负数
            }
            float f1 = yue.getYueSubtotal();
            myAssets_yue = myAssets_yue + f1;
        }
        //统计积分
        for (Jifen jifen : jifens) {
            //true为收入，false为支出
            if (!jifen.getJifenIncomeOut()) {
                float i = jifen.getJifenSubtotal();
                jifen.setJifenSubtotal(-i);
            }
            float j = jifen.getJifenSubtotal();
            myAssets_jifen = myAssets_jifen + j;
        }
        //判断支付密码
        if (!user.getUserPay().equals(EncryptionUtil.encrypt(req.getUserPay()))) {
            return new Response(Status.ERROR, "支付密码错误");
        }
        userVip.setId(UUIDCreator.getUUID());
        userVip.setUserId(user.getId());
        userVip.setVipId(vip.getId());
        userVip.setFoundDate(new Date());
        //查询user_vip表是否有该用户的信息，有几条
        //0-用户未购买会员
        List<UserVip> userVips = userVipService.selectByUserId(user.getId());
        //判断余额、积分够不够vipMoney vipJifen:输入金额 先判断支付方式
        //costStatus购买会员支付方式(1--金额，2--积分)
        if (req.getCostStatus() == 1) {
            Float cost = vip.getVipMoney() * userVip.getLengthTime();//花费
            if (cost > myAssets_yue) {
                return new Response(Status.ERROR, "余额不足，请充值");
            }
        }
        if (req.getCostStatus() == 2) {
            Integer cost = vip.getVipJifen() * userVip.getLengthTime();
            if (cost > myAssets_jifen) {
                return new Response(Status.ERROR, "积分不足，请充值");
            }
        }


        if (userVips.size() == 0) {
            Date openTime = new Date();
            //String stopTime = TimeToString.plusDay(userVip.getLengthTime() * 30);
            userVip.setOpenTime(openTime);
            userVip.setStartTime(openTime);
            userVip.setSurplusDays(30 * userVip.getLengthTime());
            //userVip.setStopTime(TimeToString.StrToDate(stopTime));
            userVip.setStatus(1);//状态（0--未启用，1--已启用）
            status = userVipService.add(userVip);
        }
        if (userVips.size() == 1) {
            UserVip userVip1 = userVips.get(0);
            Integer i = req.getUserVip().getVipGrade();
            if (userVip1.getVipGrade().equals(i)) {
                //充值
                Integer days = userVip1.getSurplusDays();
                userVip1.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip1);
            } else if (i > userVip1.getVipGrade()) {
                Date openTime = new Date();
                //String stopTime = TimeToString.plusDay(userVip.getLengthTime() * 30);
                userVip.setOpenTime(openTime);
                userVip.setStartTime(openTime);
                userVip.setSurplusDays(30 * userVip.getLengthTime());
                //userVip.setStopTime(TimeToString.StrToDate(stopTime));
                userVip.setStatus(1);//状态（0--未启用，1--已启用）
                status = userVipService.add(userVip);
                //停用userVip1
                userVip1.setStatus(0);
                userVip1.setStopTime(new Date());
                userVipService.update(userVip1);
            } else {
                Date openTime = new Date();
                //String stopTime = TimeToString.plusDay(userVip.getLengthTime() * 30);
                userVip.setOpenTime(openTime);
                userVip.setStartTime(openTime);
                userVip.setSurplusDays(30 * userVip.getLengthTime());
                //userVip.setStopTime(TimeToString.StrToDate(stopTime));
                userVip.setStatus(0);//状态（0--未启用，1--已启用）
                status = userVipService.add(userVip);
            }

        }
        if (userVips.size() == 2) {
            UserVip userVip1 = userVips.get(0);
            UserVip userVip2 = userVips.get(1);
            Integer i = req.getUserVip().getVipGrade();
            if (userVip1.getVipGrade().equals(i)) {
                //充值
                Integer days = userVip1.getSurplusDays();
                userVip1.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip1);
            } else if (userVip2.getVipGrade().equals(i)) {
                //充值
                Integer days = userVip2.getSurplusDays();
                userVip2.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip2);
            }
            //重新添加数据，判断i同时大于userVip1 userVip2
            else if (i > userVip1.getVipGrade() && i > userVip2.getVipGrade()) {
                Date openTime = new Date();
                userVip.setOpenTime(openTime);
                userVip.setStartTime(openTime);
                userVip.setSurplusDays(30 * userVip.getLengthTime());
                userVip.setStatus(1);//状态（0--未启用，1--已启用）
                status = userVipService.add(userVip);
                //停用userVip2
                userVip2.setStatus(0);
                userVip2.setStopTime(new Date());
                userVipService.update(userVip2);
            } else {
                //充值
                Integer days = userVip2.getSurplusDays();
                userVip2.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip2);
            }

        }
        if (userVips.size() == 3) {
            UserVip userVip1 = userVips.get(0);
            UserVip userVip2 = userVips.get(1);
            UserVip userVip3 = userVips.get(2);
            Integer i = req.getUserVip().getVipGrade();
            if (userVip1.getVipGrade().equals(i)) {
                //充值
                Integer days = userVip1.getSurplusDays();
                userVip1.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip1);
            } else if (userVip2.getVipGrade().equals(i)) {
                //充值
                Integer days = userVip2.getSurplusDays();
                userVip2.setSurplusDays(30 * userVip.getLengthTime() + days);
                status = userVipService.update(userVip2);
            } else {
                //充值
                Integer days = userVip3.getSurplusDays();
                userVip3.setSurplusDays(30 * userVip.getLengthTime() + days);
                userVip3.setStatus(1);
                status = userVipService.update(userVip3);
                if (userVip2.getSurplusDays() != 0) {
                    //停用userVip2
                    userVip2.setStatus(0);
                    userVip2.setStopTime(new Date());
                    userVipService.update(userVip2);
                }
                if (userVip1.getSurplusDays() != 0) {
                    //停用userVip1
                    userVip1.setStatus(0);
                    userVip1.setStopTime(new Date());
                    userVipService.update(userVip1);
                }
            }
        }
        if (status == Status.SUCCESS) {
            //扣除购买费用，添加记录至yue jifen数据库
            if (req.getCostStatus() == 1) {
                Float cost = vip.getVipMoney() * userVip.getLengthTime();//花费 单价*购买月数
                Yue yue_db = new Yue();
                yue_db.setId(UUIDCreator.getUUID());
                yue_db.setUserId(obj.getId());
                yue_db.setYueCategory(8);//8-充值会员
                yue_db.setYueSubtotal(cost);//花费
                yue_db.setYueStatus(0);
                yue_db.setYueIncomeOut(false);
                yue_db.setYueFoudDate(new Date());
                yueService.add(yue_db);
                //添加支付通知记录
                NewPay newPay = new NewPay();
                newPay.setId(UUIDCreator.getUUID());
                newPay.setUserId(obj.getId());
                newPay.setPayType(1);//支付方式(1--余额支付，2--积分支付，3--微信支付，4--支付宝支付)
                newPay.setPayMoney(cost);
                newPay.setPayName("会员购买支付成功");
                newPay.setPayNumber(yue_db.getId());
                newPay.setStatus(1);//状态(1--未读，2--已读）
                newPay.setFoundDate(new Date());
                newPayService.add(newPay);
            }
            if (req.getCostStatus() == 2) {
                float cost = vip.getVipJifen() * userVip.getLengthTime();
                Jifen jifen_db = new Jifen();
                jifen_db.setId(UUIDCreator.getUUID());
                jifen_db.setUserId(obj.getId());
                jifen_db.setJifenCategory(11);//11-充值会员
                jifen_db.setJifenIncomeOut(false);//收入
                jifen_db.setJifenSubtotal(cost);
                jifen_db.setJifenStatus(0);
                jifen_db.setJifenFoudDate(new Date());
                jifenService.add(jifen_db);
                //添加支付通知记录
                NewPay newPay = new NewPay();
                newPay.setId(UUIDCreator.getUUID());
                newPay.setUserId(obj.getId());
                newPay.setPayType(2);//支付方式(1--余额支付，2--积分支付，3--微信支付，4--支付宝支付)
                newPay.setPayMoney(cost);
                newPay.setPayName("会员购买支付成功");
                newPay.setPayNumber(jifen_db.getId());
                newPay.setStatus(1);//状态(1--未读，2--已读）
                newPay.setFoundDate(new Date());
                newPayService.add(newPay);
            }
            message = "购买会员成功";
            return new Response(status, message);
        }
        if (status == Status.EXISTS) {
            message = "会员已经存在";
            return new Response(status, message);
        }
        message = "购买会员失败";
        return new Response(status, message);
    }

    /**
     * 我的会员查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/vip/myVip", method = RequestMethod.GET)
    public Object myVip(@RequestParam("sessionId") String sessionId) throws ParseException {
//        //判断用户的session是否正常
//        //获取session
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
        List<MyVip> myVips = userVipService.myVip(obj.getId());
        return new Response(Status.SUCCESS, myVips);
    }


}

