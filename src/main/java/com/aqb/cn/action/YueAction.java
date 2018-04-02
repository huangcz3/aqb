package com.aqb.cn.action;

import com.aqb.cn.bean.Deposit;
import com.aqb.cn.bean.Proportion;
import com.aqb.cn.bean.User;
import com.aqb.cn.bean.Yue;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DepositMapper;
import com.aqb.cn.mapper.DepositMoneyMapper;
import com.aqb.cn.pojo.MyAssets_yue;
import com.aqb.cn.service.JifenService;
import com.aqb.cn.service.ProportionService;
import com.aqb.cn.service.UserService;
import com.aqb.cn.service.YueService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Controller
public class YueAction {

    private final Log logger = LogFactory.getLog(YueAction.class);

    @Autowired
    private YueService yueService;
    @Autowired
    private UserService userService;
    @Autowired
    private JifenService jifenService;
    @Autowired
    private ProportionService proportionService;
    @Autowired
    private DepositMapper depositMapper;
    @Autowired
    private DepositMoneyMapper depositMoneyMapper;


    /**
     * 新增余额
     * @param req
     * @return state : 0 -- 成功, 6--余额已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/yue/addyue", method = RequestMethod.POST)
    public Object addyue(HttpServletRequest request,@RequestBody Request req) {
        int status;
        String message = "";
//        if(yue.getyueCode() == null || yue.getyueCode() == ""){
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
            return new Response(14, "登录失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "登录失效");
        }

        Yue yue = req.getYue();
        yue.setId(UUIDCreator.getUUID());
        yue.setUserId(obj.getId());
        yue.setYueFoudDate(new Date());

        status = yueService.add(yue);
        if (status == Status.SUCCESS) {
            message = "添加余额成功";
            return new Response(status, message, yue.getId());
        }if(status == Status.EXISTS){
            message = "余额已经存在";
            return new Response(status, message);
        }
        message = "添加余额失败";
        return new Response(status, message);
    }




    /**
     * 删除余额
     * @param request
     * @param yue
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/yue/deleteyue", method = RequestMethod.POST)
    public Object deleteyue(HttpServletRequest request, @RequestBody Yue yue) {
        int status;
        String message = "";
        if(yue.getId() == null || yue.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = yueService.delete(yue);
        if(status == Status.NOT_EXISTS){
            message = "余额不存在";
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
     * 修改余额
     * @param request
     * @param yue yue.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 余额不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/yue/updateyue", method = RequestMethod.POST)
    public Object updateyue(HttpServletRequest request, @RequestBody Yue yue) {
        int status = Status.ERROR;
        String message = "";
        if(yue.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = yueService.update(yue);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "余额不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 我的资产查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/yue/queryyue", method = RequestMethod.GET)
    public Object querymyAssets(HttpServletRequest request,
                           @RequestParam("sessionId")String sessionId) {
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

        Float count_yue = new Float(0);
        MyAssets_yue myAssets = new MyAssets_yue();
        List<Yue> yues = yueService.queryYueByUserId(obj.getId());//查出该userId的余额表
        if (yues == null){
            myAssets.setYue_Assets("0.00");
        }
        // 传入myAssets
        myAssets.setYues(yues);
        //余额
        for (Yue yue:yues) {
            //true为收入，false为支出
            if (yue.getYueIncomeOut() == false){
                Float f = yue.getYueSubtotal();
                yue.setYueSubtotal(-f);//支出，取负数
            }
            Float f1 = yue.getYueSubtotal();
            count_yue = count_yue + f1;
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p =decimalFormat.format(count_yue);//format 返回的是字符串
        myAssets.setYue_Assets(p);

        //查询用户是否缴纳押金
        List<Deposit> deposits = depositMapper.selectByUserId(obj.getId());
        if(deposits != null && deposits.size() > 0){
            myAssets.setaBoolean(true);
            myAssets.setaFloat(depositMoneyMapper.selectByPrimaryKey(deposits.get(0).getMoneyId()).getMoney());
        }
        return new Response(Status.SUCCESS,myAssets);
    }


    /**
     * 金额兑换积分
     *
     * @param sessionId
     * @param money    输入的金额
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 积分不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/yue/yueTojifen", method = RequestMethod.GET)
    public Object yueTojifen(HttpServletRequest request,
                             @RequestParam("money")Float money,
                             @RequestParam("sessionId")String sessionId) {
        int status = Status.ERROR;
        String message = "";
        Float cost_money = money;
        //获取session
        if (sessionId == null || sessionId.equals("")){
            return new Response(status,"sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if (httpSession == null){
            return new Response(14,"session失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null){
            return new Response(14, "sessionId失效");
        }

        if(!(money>0)){
            return new Response(status,"金额必须大于0");
        }
        //查出用户总余额
        List<Yue> yues = yueService.queryYueByUserId(obj.getId());//
        float count_yue = 0;
        for (Yue yue:yues){
            //true为收入，false为支出
            if (!yue.getYueIncomeOut()){
                float f = yue.getYueSubtotal();
                yue.setYueSubtotal(-f);
            }
            count_yue = count_yue + yue.getYueSubtotal();
        }
        //判断用户输入的金额是否 小于用户的总余额
        if (money > count_yue){
            return new Response(status,"不能大于总余额");
        }
        List<Proportion> proportions = proportionService.selectByStatus(2);//余额兑换积分
        //余额需要转换成积分
        Float ye = new Float(0);//转换后的积分
        for (Proportion proportion:proportions){
            ye = ye + money/proportion.getProportionFront()*proportion.getProportionAfter();
            money = money%proportion.getProportionFront();
            if (money==0){
                break;
            }
        }
        //执行数据库的操作
        if (!(money>0)){
            return new Response(status, "数额太小，无法兑换");
        }
        status = yueService.updateYueToJifen(obj.getId(),cost_money,ye);

        if (status == Status.SUCCESS) {
            message = "成功";
        } else {
            message = "失败";
        }
        return new Response(status, message);

    }



}
