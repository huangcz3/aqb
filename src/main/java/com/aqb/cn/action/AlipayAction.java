package com.aqb.cn.action;


/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import com.alipay.api.internal.util.AlipaySignature;
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
import com.aqb.cn.utils.TimeToString;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class AlipayAction {


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




    public static final String URL = "https://openapi.alipay.com/gateway.do"; //支付宝网关（固定）
    public static final String APP_ID = "2017070507650195";//APPID即创建应用后生成
    public static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDXR2/M1Zx6BaXV954I0a6J8lbkSOg8Yk/XCSFfF7GSQhFF7d1vGcoF9M4wDbhASNYdb0Nfe0SgI28Tq1vZZeHObjdJdlAdHfRU4k3HGJNcR5CAKL6LiUy7PkkMShxESqDhWQ8M5QnVGuUfrhmOcz50nZlsQeOrZkrsJrMlSqcbCWrsIWdDqRZi15gn+nFugBMlydwg5rMkR4rpvAvCfpaCQ0CUKkb4ijIYnVUMWeYmwrktXQioeVtA4yXRnhpdCvmqzhRD7IQTFDK4gQrhS/UMEA7n2OQe4V7xVpbfnhwTiPbLfty7gj/5TlXaf/qfpYqP8L5WhAAhdLMf74cze9l7AgMBAAECggEBALUt1hXaIVtArZUqVMKvVembHy35fhpz0yXy9TcGxdeF6uujiR0b4z6oDW+73DmeEzWqklUldVgg+jbfnOmWKTiG4djdzgbzUtKtsy/AqySaxi5WHCdrYjyj6u6A4NFIzaVaQqRjjqbff73t1Gxi6UlT6e+GdImoCZ4947+a2JCTDCrjGzo0OfcsTCBoQM18dd/cSDcZvNjC2EwP0ibCq/JhNTJR/zYlTOy18TSGXlErHp9wFZpvnbtv7iLYm5ChcD55MplwPKXMNM3MV9Hbj/QuytnwT4bQm8baZ5Mmtwy550BqN/d0P9WQOFRrwE2WRqHkrRyJ1NunRFeNoFLEykECgYEA/Yx0x6/cvnYyE81Fh9VXaMn8lBT0kjPVv6Mad3RNc1VEp0gQGYBqoIbNfVBM7uhCCjm+12yPg/Q9M09uGAIsnanUbZe8YfA7dvsqykX5akOTLGIixbEu0X6APeZ4yABuk/IX/gWL5IYEpT3sD63c9B3n7rHGRoMc61KRwuWkjukCgYEA2VxC+cf4SYn11a/Fq+pgjPfHhpjGWfX/PWc7T2WUp5SHcn9di3c/lHTa3JsZTHZJD+OQlDa1nYvkrEMlFNvpsujQlEMOjqRkk0tp3l3H1KgUCACxfT8nMAxFbrAK78SHg65N70gzs3YJCTQNtEgG6T80iZvVpkjw3y6CsbfYTsMCgYEAq5KsQ6RhwBDDelrhvjVD3Qkx/hLgHT8uWWvJsSPmdTEyL/C31Ent3Yra6v230cMhTUIePjCcPTK9Z0dMvLpb7qTJtw+CztN9qdn07sPDL4FiyhnuZdCsWd88cKbQ/KYuLbcZPTALpix6YEcywrloV1kVZZSvE3AN1cAeGsDDRmECgYEAz5fOX8Ec4R9iKzQ1FnxCul/rsCE5T7IilAKEItBhqauxsSjzrr+1eZQhc5CuGNjf7szoyKV3z4BMZjm6sLFYAgdZLkDdJl7br9HWywnGUNQ2TW5cFcK7KqiNp6l6yIRq6NkQlfffaQbSmnecnxIrh5lWFBdBdg8z/fPt3s43SQECgYAlzh8S0SgWfMq0q/x34jhKuUPkV6W1jQtYeFegFrLxhBBX1ecaNrtZcFM8rtf5UpYwra7G4iyOZ4k+fU7dMGA7ruHeXjHWPb8I57JELXtudbC8cNKnW4U8x6bXyifjqUcv8+AyqPUjQQwsBmXWYF75yYrv0FALvtJKc+rXSox2EA==";//开发者应用私钥，由开发者自己生成
    public static final String FORMAT = "json";//参数返回格式，只支持json
    public static final String CHARSET = "UTF-8";//请求和签名使用的字符编码格式，支持GBK和UTF-8
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhValhZh5PAUGcmcRT4lugM4ztZjzaeV3YITipR4MBC/LAqbsI9nG06L1edkkf9TyWW94+DcotpXBVQVl0xD9dnFocOpHgu1cymQ2JitHjW7ZHQm4IGwcFojKWoG1X4QNHhAxebaokD1ANTaKumALxio47yQqm9+op9HwwVxh/ynoohvbEwODmrlmi3doqs/5jjYMM2kgB8/4+QyJot0bWIz5X0g7q6J4WK7tXDMSokApbRIJMKywkHczOyeI399+MyEzspMf7lykSeOYLYURm7IRQRpvH1uMQ9QI8xm11P86bnUNirKt1w0iyrZ0znEDcSDnzX0zGee6OYdjW8zN2QIDAQAB";//支付宝公钥，由支付宝生成
    public static final String SIGN_TYPE = "RSA2";//商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
    public static final String HD_URL = "http://47.94.45.159:8080/aqb/api/alipayHD";//回调地址
    public static final String METHOD = "alipay.trade.app.pay";//APP支付
    public static final String VERSION = "1.0";//version


    @ResponseBody
    @RequestMapping(value = "/api/alipay", method = RequestMethod.POST)
    public Object alipay(@RequestBody Request req) throws Exception{
        System.out.println("成功调用alipay");

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
        //判断是充值还是缴纳押金
        if(req.getAlipay().getStatus() == 1){//充值
            //        String total_amount = "0.01";//获取传过来的金额
            total_amount = req.getAlipay().getTotalAmount();//获取传过来的金额
        }else if(req.getAlipay().getStatus() == 2){//缴纳押金
            //判断是否缴纳押金
            Deposit deposit_db = depositService.queryDepositByUserId(obj.getId());//成功充值押金的记录
            if (deposit_db != null){
                return new Response(Status.ERROR,"该用户已成功充值押金");
            }
            //数据库读取押金金额
            DepositMoney depositMoney = depositMoneyMapper.queryDepositMoney();
            total_amount = String.valueOf(depositMoney.getMoney());
            rechargeOrder.setMoneyId(depositMoney.getId());
        }

        //获取金额，商品名称
        String out_trade_no = UUIDCreator.getUUID();//系统自动生成订单号
        String subject = req.getAlipay().getSubject();//获取传过来的商品名称
//        String subject = "充值";//获取传过来的商品名称



        //根据用户输入的金额，在数据库中生成订单

        rechargeOrder.setId(out_trade_no);//设置订单号
        rechargeOrder.setFoundDate(new Date());
        rechargeOrder.setOrderMoney(Float.valueOf(total_amount));
        rechargeOrder.setOrderName(subject);
        rechargeOrder.setUserId(obj.getId());
        rechargeOrder.setStatus(0);//状态（0--未支付，1--已支付）
        rechargeOrder.setPayType(req.getAlipay().getStatus());

        if(!(rechargeOrderService.add(rechargeOrder) == Status.SUCCESS)){
            return new Response(Status.ERROR,"服务器异常");
        }

        //业务参数
        String biz_content = "{\"timeout_express\":\"30m\"," +
                            "\"product_code\":\"QUICK_MSECURITY_PAY\"," +
                            "\"total_amount\":\""+total_amount+"\"," +
                            "\"subject\":\""+subject+"\"," +
                            "\"out_trade_no\":\""+out_trade_no+"\"}";
//        System.out.println("biz_content:" + biz_content);
        String biz_content_code = URLEncoder.encode(biz_content, "UTF-8");
//        System.out.println("biz_content_code:"+biz_content_code);
        String HD_URL_code = URLEncoder.encode(HD_URL, "UTF-8");
//        System.out.println("HD_URL_code:" + HD_URL_code);
        String timestamp = TimeToString.DateToStr(new Date());
//        System.out.println("timestamp:"+timestamp);
        String timestamp_code = URLEncoder.encode(timestamp, "UTF-8");
//        System.out.println("timestamp_code:"+timestamp_code);

        String signString = "app_id=" + APP_ID +
                            "&biz_content=" + biz_content +
                            "&charset=" + CHARSET +
                            "&format=" + FORMAT +
                            "&method=" + METHOD +
                            "&notify_url=" + HD_URL +
                            "&sign_type=" + SIGN_TYPE +
                            "&timestamp=" + timestamp +
                            "&version=" + VERSION;
//        System.out.println("signString:"+signString);
        //调用支付宝的签名方法
        String sign = AlipaySignature.rsaSign(signString, APP_PRIVATE_KEY, CHARSET, SIGN_TYPE);//RSA签名
//        System.out.println("sign:"+sign);
        String sign_code = URLEncoder.encode(sign, "UTF-8");
//        System.out.println("sign_code:"+sign_code);
                //拼接签名到 signString 后面
        String signPingjie = "app_id=" + APP_ID +
                             "&biz_content=" + biz_content_code +
                             "&charset=" + CHARSET +
                             "&format=" + FORMAT +
                             "&method=" + METHOD +
                             "&notify_url=" + HD_URL_code +
                             "&sign=" + sign_code +
                             "&sign_type=" + SIGN_TYPE +
                             "&timestamp=" + timestamp_code +
                             "&version=" + VERSION;
//        signString = signString.replaceAll("\\+","%20");
        System.out.println("signPingjie:"+signPingjie);
        return new Response(Status.SUCCESS,"",signPingjie);
    }

    /**
     * 支付宝APP支付回调函数
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/api/alipayHD", method = RequestMethod.POST)
    public String alipayHD(HttpServletRequest request) throws Exception{
        System.out.println("成功调用alipayHD");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
//        System.out.println(params);
        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2");
        System.out.println(flag);//打印验签结果
        //判断异步通知是否成功，调整数据库

        if (flag){
            System.out.println("***支付成功***");
            //修改数据库
            String out_trade_no = params.get("out_trade_no");//获取商户订单号
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
                rechargeOrder.setPayStatus(1);//支付方式（1--支付宝，2--微信,3--银联，4--其他）
                rechargeOrder.setYueId(yueId);
                int key2 = rechargeOrderService.update(rechargeOrder);
                if(key1 == 0 && key2 == 0){
                    return "SUCCESS";//给支付宝返回一个确认成功的字符串
                }
            }else if(rechargeOrder != null && rechargeOrder.getPayType() == 2){//缴纳押金
                //添加押金表的数据
                Deposit deposit = new Deposit();
                deposit.setId(UUIDCreator.getUUID());
                deposit.setUserId(rechargeOrder.getUserId());
                deposit.setFoundDate(new Date());
                deposit.setMoneyId(rechargeOrder.getMoneyId());
                deposit.setPayStatus(1);
                deposit.setStatus(0);
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
                rechargeOrder.setPayStatus(1);//支付方式（1--支付宝，2--微信,3--银联，4--其他）
                rechargeOrder.setDepositId(deposit.getId());
                int key2 = rechargeOrderService.update(rechargeOrder);

                //修改binding表中的数据，把缴纳了押金的该用户的binding数据，押金状态改为已缴纳
                bindingMapper.updateByUserId(rechargeOrder.getUserId());

                if(key1 == 0 && key2 == 0){
                    return "SUCCESS";//给支付宝返回一个确认成功的字符串
                }
            }
        }
        return "failure";
    }


    public static void main(String[] args) {
//        AlipayAction alipayAction = new AlipayAction();
//        try {
//            Request req = new Request();
//            Alipay alipay = new Alipay();
//            alipay.setSubject("123");
//            alipay.setTotalAmount("0.01");
//            req.setAlipay(alipay);
//            alipayAction.alipay();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }


}

