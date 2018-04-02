package com.aqb.cn.utils.weixin;

import com.aqb.cn.utils.weixin.Utils.*;
import com.aqb.cn.utils.weixin.entity.Unifiedorder;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付测试
 * @author xiebin
 * @date 2015年11月26日上午9:58:19
 */
public class WeixinPayTest {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		WeixinConfigUtils config = new WeixinConfigUtils();
		//参数组
		String appid = config.appid; //appid
		System.out.println("appid是："+appid);
		String mch_id = config.mch_id;//mch_id
		System.out.println("mch_id是："+mch_id);
		String nonce_str = RandCharsUtils.getRandomString(16);//nonce_str
		System.out.println("随机字符串是："+nonce_str);
		String body = "谢彬测试微信支付0.01_2";//body
		String detail = "谢彬0.01_元测试开始";
		String attach = "备用参数，先留着，后面会有用的";
		String out_trade_no = "2015112500001000811017342394";//out_trade_no
		int total_fee = 1;//单位是分，即是0.01元				//total_fee
		String spbill_create_ip = "127.0.0.1";				//spbill_create_ip
		String time_start = RandCharsUtils.timeStart();
		System.out.println(time_start);
		String time_expire = RandCharsUtils.timeExpire();
		System.out.println(time_expire);
		String notify_url = config.notify_url;				//notify_url
		System.out.println("notify_url是："+notify_url);
		String trade_type = "APP";							//trade_type


		//参数：开始生成签名
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appid", appid);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", nonce_str);
		parameters.put("body", body);
		parameters.put("nonce_str", nonce_str);
		parameters.put("detail", detail);
		parameters.put("attach", attach);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);
		parameters.put("time_start", time_start);
		parameters.put("time_expire", time_expire);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("spbill_create_ip", spbill_create_ip);

		String sign = WXSignUtils.createSign("UTF-8", parameters);
		System.out.println("签名是："+sign);


		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(appid);
		unifiedorder.setMch_id(mch_id);
		unifiedorder.setNonce_str(nonce_str);
		unifiedorder.setSign(sign);
		unifiedorder.setBody(body);
		unifiedorder.setDetail(detail);
		unifiedorder.setAttach(attach);
		unifiedorder.setOut_trade_no(out_trade_no);
		unifiedorder.setTotal_fee(total_fee);
		unifiedorder.setSpbill_create_ip(spbill_create_ip);
		unifiedorder.setTime_start(time_start);
		unifiedorder.setTime_expire(time_expire);
		unifiedorder.setNotify_url(notify_url);
		unifiedorder.setTrade_type(trade_type);


		//构造xml参数
		String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);

		String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		String method = "POST";

		String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();

		System.out.println(weixinPost);

		ParseXMLUtils.jdomParseXml(weixinPost);



	}

}
