package com.aqb.cn.action;


import com.aqb.cn.bean.*;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BindingMapper;
import com.aqb.cn.mapper.DepositMoneyMapper;
import com.aqb.cn.service.BindingService;
import com.aqb.cn.service.DepositService;
import com.aqb.cn.service.RechargeOrderService;
import com.aqb.cn.service.YueService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.XMLUtil;
import com.aqb.cn.utils.getSession.MySessionContext;
import com.aqb.cn.utils.weixin.pay.CommonUtil;
import com.aqb.cn.utils.weixin.pay.PayCommonUtil;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class WeiXinAction {

    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private YueService yueService;
    @Autowired
    private DepositMoneyMapper depositMoneyMapper;
    @Autowired
    private DepositService depositService;
    @Autowired
    private BindingMapper bindingMapper;
    @Autowired
    private BindingService bindingService;


    public static String URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//微信统一下单地址
    public static String appid = "wx0d32c8d92795eec6";//应用ID
    public static String AppSecret = "82c1e2452f086f394ef4e9799a8faea4";//AppSecret
    public static String mch_id = "1486258482";//商户号，未给
    public static String notify_url = "http://47.94.45.159:8080/aqb/api/weiXinHD";//回调地址
    public static String trade_type = "APP";//交易类型


    @ResponseBody
    @RequestMapping(value = "/aip/weiXinPay", method = RequestMethod.POST)
    public Object weiXinPay(@RequestBody Request req) throws Exception {
        System.out.println("成功调用weiXinPay");
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

        RechargeOrder rechargeOrder = new RechargeOrder();
        String total_amount = "0";
        float total_fee1 = 0;
        //判断是充值还是缴纳押金
        if(req.getAlipay().getStatus() == 1){//充值
            //        String total_amount = "0.01";//获取传过来的金额
            String total_fee = req.getAlipay().getTotalAmount();//获取传过来的金额
            total_fee1 = Float.valueOf(total_fee);
            int total_fee2 = (int)(total_fee1 * 100);
            total_amount = String.valueOf(total_fee2);
        }else if(req.getAlipay().getStatus() == 2){//缴纳押金
            //判断是否缴纳押金
            Deposit deposit_db = depositService.queryDepositByUserId(obj.getId());//成功充值押金的记录
            if (deposit_db != null){
                return new Response(Status.ERROR,"该用户已成功充值押金");
            }
            //数据库读取押金金额
            DepositMoney depositMoney = depositMoneyMapper.queryDepositMoney();
            total_amount = String.valueOf((int)(depositMoney.getMoney() * 100));
            rechargeOrder.setMoneyId(depositMoney.getId());
            total_fee1 = (int)(depositMoney.getMoney() * 100);
        }


        //统一下单的参数整理
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", appid);//应用ID
        parameters.put("mch_id",mch_id);//商户号
        parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());//随机字符串
        parameters.put("body", req.getAlipay().getSubject());//商品描述
        String out_trade_no = UUIDCreator.getUUID();
        parameters.put("out_trade_no", out_trade_no);//商户订单号

        parameters.put("total_fee", total_amount);//订单总金额，单位为分
        parameters.put("spbill_create_ip", "127.0.0.1");//终端IP
        parameters.put("notify_url", notify_url);//通知地址
        parameters.put("trade_type", trade_type);//交易类型

        System.out.println("开始签名");
        //整理参数，签名
        String sign = PayCommonUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);//签名

        System.out.println("开始下单");
        //调用微信统一下单接口，进行下单，得到预支付订单信息
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        String result = CommonUtil.httpsRequest(URL, "POST", requestXML);
        System.out.println("统一下单的结果："+result);
        Map<String, String> map = new HashMap();
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        };
        for(Map.Entry<String, String> entry: map.entrySet()){
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }

        //用于签名时的Map
        SortedMap<Object,Object> params = new TreeMap<Object,Object>();
        //调起支付接口的参数整理
        params.put("appid", appid);//应用ID
        params.put("partnerid", mch_id);//商户号
        params.put("prepayid", map.get("prepay_id"));//预支付交易会话ID
        params.put("package", "Sign=WXPay");//扩展字段
        String nonceStr=PayCommonUtil.CreateNoncestr();
        params.put("noncestr",nonceStr);//随机字符串
        String timeStamp = Long.toString(new Date().getTime());
        timeStamp = timeStamp.substring(0,10);
        params.put("timestamp", timeStamp);//时间戳

        //整理参数，签名
        String paySign =  PayCommonUtil.createSign("UTF-8", params);
        params.put("sign", paySign);//签名


        //根据用户输入的金额，在数据库中生成订单

        rechargeOrder.setId(out_trade_no);//设置订单号
        rechargeOrder.setFoundDate(new Date());
        rechargeOrder.setOrderMoney(total_fee1);
        rechargeOrder.setOrderName(req.getAlipay().getSubject());
        rechargeOrder.setUserId(obj.getId());
        rechargeOrder.setStatus(0);//状态（0--未支付，1--已支付）
        rechargeOrder.setPayType(req.getAlipay().getStatus());

        if(!(rechargeOrderService.add(rechargeOrder) == Status.SUCCESS)){
            return new Response(Status.ERROR,"服务器异常");
        }

        return new Response(Status.SUCCESS,"",params);

    }

    /**
     * 微信APP支付回调函数
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/api/weiXinHD", method = RequestMethod.POST)
    public void weiXinHD(HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("成功调用weiXinHD");
        //获取微信POST过来反馈信息
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result  = new String(outSteam.toByteArray(),"utf-8");
        Map<Object, Object> map = XMLUtil.doXMLParse(result);
        for(Object keyValue : map.keySet()){
            System.out.println(keyValue+"="+map.get(keyValue));
        }
        String result_code = (String)map.get("result_code");//获取支付结果
        String out_trade_no = (String)map.get("out_trade_no");//获取商户订单号

        System.out.println(result_code);//打印支付结果

        //判断异步通知是否成功，调整数据库
        if (result_code.equals("SUCCESS")){
            System.out.println("***支付成功***");
            //修改数据库
            System.out.println("out_trade_no：" + out_trade_no);

            //数据库中找到对应的订单
            RechargeOrder rechargeOrder = rechargeOrderService.get(out_trade_no);
            if(rechargeOrder != null && rechargeOrder.getPayType() == 1){//充值
                //添加余额表的收入数据
                Yue yue = new Yue();
                String yueId = UUIDCreator.getUUID();
                yue.setId(yueId);
                yue.setUserId(rechargeOrder.getUserId());
                yue.setYueCategory(7);//7--充值
                yue.setYueIncomeOut(true);
                yue.setYueSubtotal(rechargeOrder.getOrderMoney());
                yue.setYueStatus(0);
                yue.setYueFoudDate(new Date());
                int key1 = yueService.add(yue);

                rechargeOrder.setStatus(1);//状态（0--未支付，1--已支付）
                rechargeOrder.setPayStatus(2);//支付方式（1--支付宝，2--微信,3--银联，4--其他）
                rechargeOrder.setYueId(yueId);
                int key2 = rechargeOrderService.update(rechargeOrder);
                if(key1 == 0 && key2 == 0){
                    response.getWriter().write(PayCommonUtil.setXML("SUCCESS", "OK"));
                }
            }else if(rechargeOrder != null && rechargeOrder.getPayType() == 2){//缴纳押金
                //添加押金表的数据
                Deposit deposit = new Deposit();
                deposit.setId(UUIDCreator.getUUID());
                deposit.setUserId(rechargeOrder.getUserId());
                deposit.setFoundDate(new Date());
                deposit.setMoneyId(rechargeOrder.getMoneyId());
                deposit.setPayStatus(0);//支付方式（0-微信支付，1-支付宝支付，2-线下支付）
                deposit.setStatus(0);//状态（0-成功充值，1-已退款）
                int key1 = depositService.add(deposit);

                List<Binding> bindings = bindingService.queryByUserId2(rechargeOrder.getUserId());//查询曾经绑定的设备（有失败记录）
                for (Binding binding:bindings){
                    //审核中、成功绑定设备
                    if (binding.getStatus() == 1 || binding.getStatus() == 2){
                        binding.setDepositStatus(1);
                        bindingService.update(binding);
                    }
                }

                rechargeOrder.setStatus(1);//状态（0--未支付，1--已支付）
                rechargeOrder.setPayStatus(2);//支付方式（1--支付宝，2--微信,3--银联，4--其他）
                rechargeOrder.setDepositId(deposit.getId());
                int key2 = rechargeOrderService.update(rechargeOrder);

                //修改binding表中的数据，把缴纳了押金的该用户的binding数据，押金状态改为已缴纳
                bindingMapper.updateByUserId(rechargeOrder.getUserId());

                if(key1 == 0 && key2 == 0){
                    response.getWriter().write(PayCommonUtil.setXML("SUCCESS", "OK"));
                }
            }
        }
    }


}
