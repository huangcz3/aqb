package com.aqb.cn.utils.weixin.pay;

import com.aqb.cn.utils.weixin.Utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PayCommonUtil {
    private static Logger log = LoggerFactory.getLogger(PayCommonUtil.class);


    public static String CreateNoncestr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    public static String CreateNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }
    /**
     * @author 鏉庢妗?
     * @date 2014-12-5涓嬪崍2:29:34
     * @Description: sign签名
     * @param characterEncoding 编码格式
     * @param parameters 请求参数
     * @return
     */
    public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
//		StringBuffer sb = new StringBuffer();
//		Set es = parameters.entrySet();
//		Iterator it = es.iterator();
//		while(it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
//			String k = (String)entry.getKey();
//			Object v = entry.getValue();
//			if(null != v && !"".equals(v)
//					&& !"sign".equals(k) && !"key".equals(k)) {
//				sb.append(k + "=" + v + "&");
//			}
//		}
////		sb.append("key=" + ConfigUtil.API_KEY);
//
//		//appScret
//		sb.append("key=9678e65b9542226da5c679bc15326a51");
//
//
//		System.out.println("string:"+sb.toString());
//		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
//		return sign;


        String[] keys = parameters.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuffer reqStr = new StringBuffer();
        for(String key : keys){
            String v = (String) parameters.get(key);
            if(v != null && !v.equals("")){
                reqStr.append(key).append("=").append(v).append("&");
            }
        }
        StringBuffer result=reqStr.append("key").append("=").append("danmulierencarscube0987654321568");
        System.out.println("签名:"+result.toString());
        //MD5鍔犲瘑
//		      return RMd5.encode(reqStr.toString()).toUpperCase();
        return MD5Util.MD5Encode(reqStr.toString(), characterEncoding).toUpperCase();

    }
    /**
     * @author 鏉庢妗?
     * @date 2014-12-5涓嬪崍2:32:05
     * @Description锛氬皢璇锋眰鍙傛暟杞崲涓簒ml鏍煎紡鐨剆tring
     * @param parameters  璇锋眰鍙傛暟
     * @return
     */
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            //鎵?鏈夊瓧娈靛姞涓奀DATA銆?
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
    /**
     * @author 鏉庢妗?
     * @date 2014-12-3涓婂崍10:17:43
     * @Description: 回调后将结果返回给微信
     * @param return_code 杩斿洖缂栫爜
     * @param return_msg  杩斿洖淇℃伅
     * @return
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }
}

