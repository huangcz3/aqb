package com.aqb.cn.action;

import com.aqb.cn.bean.Jifen;
import com.aqb.cn.bean.Proportion;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.MyAssets_Jifen;
import com.aqb.cn.service.JifenService;
import com.aqb.cn.service.ProportionService;
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
 * Created by Administrator on 2017/7/13.
 */
@Controller
public class JifenAction {

    private final Log logger = LogFactory.getLog(JifenAction.class);


    @Autowired
    private JifenService jifenService;
    @Autowired
    private ProportionService proportionService;

    /**
     * 新增积分
     *
     * @param req
     * @return state : 0 -- 成功, 6--积分已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/jifen/addjifen", method = RequestMethod.POST)
    public Object addjifen(HttpServletRequest request, @RequestBody Request req) {
        int status;
        String message = "";
//        if(jifen.getjifenCode() == null || jifen.getjifenCode() == ""){
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

        Jifen jifen = req.getJifen();
        jifen.setId(UUIDCreator.getUUID());
        jifen.setUserId(obj.getId());
        jifen.setJifenFoudDate(new Date());

        status = jifenService.add(jifen);
        if (status == Status.SUCCESS) {
            message = "添加积分成功";
            return new Response(status, message, jifen.getId());
        }
        if (status == Status.EXISTS) {
            message = "积分已经存在";
            return new Response(status, message);
        }
        message = "添加积分失败";
        return new Response(status, message);
    }


    /**
     * 删除积分
     *
     * @param request
     * @param jifen
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/jifen/deletejifen", method = RequestMethod.POST)
    public Object deletejifen(HttpServletRequest request, @RequestBody Jifen jifen) {
        int status;
        String message = "";
        if (jifen.getId() == null || jifen.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = jifenService.delete(jifen);
        if (status == Status.NOT_EXISTS) {
            message = "积分不存在";
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
     * 修改积分
     *
     * @param request
     * @param jifen     jifen.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 积分不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/jifen/updatejifen", method = RequestMethod.POST)
    public Object updatejifen(HttpServletRequest request, @RequestBody Jifen jifen) {
        int status = Status.ERROR;
        String message = "";
        if (jifen.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = jifenService.update(jifen);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "积分不存在，修改失败";
        } else {
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
    @RequestMapping(value = "/api/jifen/queryjifen", method = RequestMethod.GET)
    public Object querymyAssets(HttpServletRequest request,
                                @RequestParam("sessionId") String sessionId) {
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

        float count_jifen = 0;
        MyAssets_Jifen myAssets = new MyAssets_Jifen();
        List<Jifen> jifenList = new ArrayList<>();
        List<Jifen> jifens = jifenService.queryJifenByUserId(obj.getId());//查出该userId的积分表
        //传入myAssets
        for (Jifen jifen:jifens){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(jifen.getJifenSubtotal());//format 返回的是字符串
            jifen.setJifenSubtotal(Float.valueOf(p));
            jifenList.add(jifen);
        }
        myAssets.setJifens(jifens);

        //积分
        for (Jifen jifen:jifenList) {
            //true为收入，false为支出
            if (jifen.getJifenIncomeOut() == false) {
                float i = jifen.getJifenSubtotal();
                jifen.setJifenSubtotal(-i);
            }
            float j = jifen.getJifenSubtotal();
            count_jifen = count_jifen + j;
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(count_jifen);//format 返回的是字符串
        myAssets.setJifen_Assets(Float.parseFloat(p));

        return new Response(Status.SUCCESS, myAssets);
    }

    /**
     * 积分兑换金额
     *
     * @param sessionId
     * @param score    输入的积分
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 积分不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/jifen/jifenToyue", method = RequestMethod.GET)
    public Object jifenToyue(HttpServletRequest request,
                              @RequestParam("score")Float score,
                              @RequestParam("sessionId")String sessionId) {
        int status = Status.ERROR;
        String message = "";
        Float he = score;//将传入的score值保存在一个变量中
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

        if(!(score > 0)){
            return new Response(Status.ERROR, "积分必须大于0");
        }

        List<Jifen> jifens = jifenService.queryJifenByUserId(obj.getId());//查出该userId的积分表
        //统计积分
        float count_jifen = 0;
        for (Jifen jifen:jifens) {
            //true为收入，false为支出
            if (jifen.getJifenIncomeOut() == false) {
                float i = jifen.getJifenSubtotal();
                jifen.setJifenSubtotal(-i);
            }
            float j = jifen.getJifenSubtotal();
            count_jifen = count_jifen + j;
        }
        //判断用户输入的积分是否 小于用户的总积分
        if(score > count_jifen){
            return new Response(Status.ERROR, "不能大于总积分");
        }

        //积分兑换成余额
        List<Proportion> proportions = proportionService.selectByStatus(1);//积分兑换现金
        //积分需要转换成金额
        Float je = new Float(0);//转换后的金额
        for(Proportion proportion : proportions){
            je = je + ((int)(score / proportion.getProportionFront()) * proportion.getProportionAfter());
            score = score % proportion.getProportionFront();
            if(score == 0){
                break;
            }
        }

        //执行数据库操作
        if(!(je>0)){
            return new Response(Status.ERROR, "数额太小，无法兑换");
        }

        status = jifenService.updateJifenToMoney(obj.getId(),he,je);
        if (status == Status.SUCCESS) {
            message = "成功";
        } else {
            message = "失败";
        }
        return new Response(status, message);
    }


    /**
     * 积分兑换红包
     *
     * @param sessionId
     * @param score1    输入的积分
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 积分不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/jifen/jifenTored", method = RequestMethod.GET)
    public Object jifenTored(HttpServletRequest request,
                             @RequestParam("score")String score1,
                             @RequestParam("sessionId")String sessionId) {
        int status = Status.ERROR;
        String message = "";
        int score = Integer.valueOf(score1);
        float he = score;//将传入的score值保存在一个变量中
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

        if(!(score > 0)){
            return new Response(Status.ERROR, "积分必须大于0");
        }

        List<Jifen> jifens = jifenService.queryJifenByUserId(obj.getId());//查出该userId的积分表
        //统计积分
        float count_jifen = 0;
        for (Jifen jifen:jifens) {
            //true为收入，false为支出
            if (!jifen.getJifenIncomeOut()) {
                float i = jifen.getJifenSubtotal();
                jifen.setJifenSubtotal(-i);
            }
            float j = jifen.getJifenSubtotal();
            count_jifen = count_jifen + j;
        }
        //判断用户输入的积分是否 小于用户的总积分
        if(score > count_jifen){
            return new Response(Status.ERROR, "不能大于总积分");
        }

        //积分兑换成余额
        List<Proportion> proportions = proportionService.selectByStatus(4);//积分兑换红包
        //积分需要转换成金额
        Float je = new Float(0);//转换后的金额
        for(Proportion proportion : proportions){
            je = je + ((int)(score / proportion.getProportionFront()) * proportion.getProportionAfter());
            float aFloat = score % proportion.getProportionFront();
            score = (int) aFloat;
            if(score == 0){
                break;
            }
        }

        //执行数据库操作
        if(!(je>0)){
            return new Response(Status.ERROR, "数额太小，无法兑换");
        }

        status = jifenService.updateJifenToRedpacket(obj.getId(),he,je);
        if (status == Status.SUCCESS) {
            message = "成功";
        } else {
            message = "失败";
        }
        return new Response(status, message);
    }


}