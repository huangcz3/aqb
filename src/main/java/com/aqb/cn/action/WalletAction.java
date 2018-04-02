package com.aqb.cn.action;

import com.aqb.cn.bean.User;
import com.aqb.cn.bean.Wallet;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.UserService;
import com.aqb.cn.service.WalletService;
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
 * Created by Administrator on 2017/7/13.
 */
@Controller
public class WalletAction {

    private final Log logger = LogFactory.getLog(WalletAction.class);

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;

    /**
     * 新增钱包
     * @param req
     * @return state : 0 -- 成功, 6--钱包已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/wallet/addwallet", method = RequestMethod.POST)
    public Object addwallet(HttpServletRequest request,@RequestBody Request req) {
        int status;
        String message = "";
//        if(wallet.getwalletCode() == null || wallet.getwalletCode() == ""){
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

        Wallet wallet = req.getWallet();
        wallet.setId(UUIDCreator.getUUID());
        wallet.setUserId(obj.getId());
        wallet.setFoundDate(new Date());

        status = walletService.add(wallet);
        if (status == Status.SUCCESS) {
            message = "添加钱包成功";
            return new Response(status, message, wallet.getId());
        }if(status == Status.EXISTS){
            message = "钱包已经存在";
            return new Response(status, message);
        }
        message = "添加钱包失败";
        return new Response(status, message);
    }


    /**
     * 删除钱包
     * @param request
     * @param wallet
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/wallet/deletewallet", method = RequestMethod.POST)
    public Object deletewallet(HttpServletRequest request, @RequestBody Wallet wallet) {
        int status;
        String message = "";
        if(wallet.getId() == null || wallet.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = walletService.delete(wallet);
        if(status == Status.NOT_EXISTS){
            message = "钱包不存在";
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
     * 修改钱包
     * @param request
     * @param wallet wallet.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 钱包不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/wallet/updatewallet", method = RequestMethod.POST)
    public Object updatewallet(HttpServletRequest request, @RequestBody Wallet wallet) {
        int status = Status.ERROR;
        String message = "";
        if(wallet.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = walletService.update(wallet);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "钱包不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 钱包列表查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/wallet/querywallet", method = RequestMethod.GET)
    public Object querywallet(HttpServletRequest request,@RequestBody Request req) {

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

        Wallet wallet = walletService.queryByUserID(obj.getId());

//        List<Wallet> wallets = walletService.queryWallet();
//
        return new Response(Status.SUCCESS,wallet);
    }

//    @ResponseBody
//    @RequestMapping(value = "/weixinpay.do",  produces = "text/html;charset=UTF-8",method={RequestMethod.POST})
//    public static String weixinpay(String body, //商品描述
//                                   String detail,  //商品详情
//                                   String attach,  //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
//                                   String out_trade_no, //商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
//                                   String total_price, //订单总金额，单位为分，详见支付金额
//                                   String spbill_create_ip //用户端实际ip
//
//    ) throws Exception {
//
//        WeixinConfigUtils config = new WeixinConfigUtils();
//        //参数组
//        String appid = config.appid;//微信开放平台审核通过的应用APPID
//        System.out.println("appid是："+appid);
//        String mch_id = config.mch_id;
//        System.out.println("mch_id是："+mch_id);
//        String nonce_str = RandCharsUtils.getRandomString(16);
//        System.out.println("随机字符串是："+nonce_str);
//
//
//        body = body;//"测试微信支付0.01_2";
//        detail = detail;//"0.01_元测试开始";
//        //attach = attach;//"备用参数，先留着，后面会有用的";
//        //String out_trade_no = OrderUtil.getOrderNo();//"2015112500001000811017342394";
//
//        double totalfee =0;
//        try{
//            totalfee=Double.parseDouble(total_price);////单位是分，即是0.01元
//        }catch (Exception e) {
//            totalfee=0;
//        }
//        int total_fee=(int) (totalfee*100);
//        spbill_create_ip = spbill_create_ip;//"127.0.0.1";
//
//        String time_start = RandCharsUtils.timeStart();
//        System.out.println(time_start);
//        String time_expire = RandCharsUtils.timeExpire();
//        System.out.println(time_expire);
//        String notify_url = config.notify_url;
//        System.out.println("notify_url是："+notify_url);
//        String trade_type = "APP";
//
//        //参数：开始生成签名
//        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
//        parameters.put("appid", appid);
//        parameters.put("mch_id", mch_id);
//        parameters.put("nonce_str", nonce_str);
//        parameters.put("body", body);
//        parameters.put("nonce_str", nonce_str);
//        parameters.put("detail", detail);
//        parameters.put("attach", attach);
//        parameters.put("out_trade_no", out_trade_no);
//        parameters.put("total_fee", total_fee);
//        parameters.put("time_start", time_start);
//        parameters.put("time_expire", time_expire);
//        parameters.put("notify_url", notify_url);
//        parameters.put("trade_type", trade_type);
//        parameters.put("spbill_create_ip", spbill_create_ip);
//
//        String sign = WXSignUtils.createSign("UTF-8", parameters);
//        System.out.println("签名是：" + sign);
//
//
//        Unifiedorder unifiedorder = new Unifiedorder();
//        unifiedorder.setAppid(appid);
//        unifiedorder.setMch_id(mch_id);
//        unifiedorder.setNonce_str(nonce_str);
//        unifiedorder.setSign(sign);
//        unifiedorder.setBody(body);
//        unifiedorder.setDetail(detail);
//        unifiedorder.setAttach(attach);
//        unifiedorder.setOut_trade_no(out_trade_no);
//        unifiedorder.setTotal_fee(total_fee);
//        unifiedorder.setSpbill_create_ip(spbill_create_ip);
//        unifiedorder.setTime_start(time_start);
//        unifiedorder.setTime_expire(time_expire);
//        unifiedorder.setNotify_url(notify_url);
//        unifiedorder.setTrade_type(trade_type);
//
//        //System.out.println(MD5Util.md5("fenxiangzhuyi") + "========================");
//
//        //构造xml参数
//        String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
//
//        String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//
//        String method = "POST";
//
//        String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();
//
//        System.out.println(weixinPost);
//
//        ParseXMLUtils.jdomParseXml(weixinPost);
//
//
//
//        //String json = JsonUtil.xml2jsonString(weixinPost);
//        String json = JsonUtils.xml2json(weixinPost);
//        System.out.println("=========================================================");
//
//        //Bean b = JsonUtil.getSingleBean(json, Bean.class);
//        Bean b = JsonUtils.jsonToBean(json, Bean.class);
//        if(null!=b){
//            //WeixinOrder weixin = b.getXml();
//            Unifiedorder weixin;
//            //参数：开始生成签名
//            SortedMap<Object,Object> par = new TreeMap<Object,Object>();
//            par.put("appid", weixin.getAppid());
//            par.put("mch_id", weixin.getMch_id());
//            //par.put("prepayid", weixin.getPrepay_id());
//            par.put("sign", "Sign=WXPay");
//            par.put("noncestr", weixin.getNonce_str());
//
//            //时间戳
//            Date date = new Date();
//            long time = date.getTime();
//            //mysq 时间戳只有10位 要做处理
//            String dateline = time + "";
//            dateline = dateline.substring(0, 10);
//
//            par.put("timestamp", dateline);
//
//            String signnew = WXSignUtils.createSign("UTF-8", par);
//            System.out.println("再次签名是：" + signnew);
//
//
//            //SetPay setPay = new SetPay();
//            UnifiedorderResult setPay = new UnifiedorderResult();
//
//            setPay.setAppid(weixin.getAppid());
//            //setPay.setPartnerid(weixin.getMch_id());
//            setPay.setMch_id(weixin.getMch_id());
//            setPay.setPrepay_id(weixin.getPrepay_id());
//            setPay.setNonce_str(weixin.getNonce_str());
//
//            setPay.setTime_expire(dateline);
//            setPay.setSign(signnew);
//            //setPay.setPack("Sign=WXPay");
//
//            JSONObject js = JSONObject.fromObject(setPay);
//            StringBuilder msg = new StringBuilder();
//            msg.append("{\"code\":\"1\",");
//            msg.append("\"msg\":\"查询成功！\",");
//            msg.append("\"datas\":");
//            msg.append(js.toString());
//            msg.append("}");
//
//            System.out.println(js);
//
//            return msg.toString();
//        }
//        StringBuilder msg = new StringBuilder();
//        msg.append("{\"code\":\"1\",");
//        msg.append("\"msg\":\"查询成功！\",");
//        msg.append("\"datas\":");
//        msg.append("支付失败！");
//        msg.append("}");
//
//
//        return msg.toString();
//
//    }


}

