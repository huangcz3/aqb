package com.aqb.cn.utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XMLUtil {
	/**
	 * 鐟欙絾鐎絰ml,鏉╂柨娲栫粭顑跨缁狙冨帗缁辩娀鏁崐鐓庮嚠閵嗗倸顩ч弸婊咁儑娑擄拷缁狙冨帗缁辩姵婀佺�涙劘濡悙鐧哥礉閸掓瑦顒濋懞鍌滃仯閻ㄥ嫬锟藉吋妲哥�涙劘濡悙鍦畱xml閺佺増宓侀妴锟�
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws JDOMException, IOException {
		//strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		strxml = strxml.replaceFirst("encoding=\".*\"","encoding=\"UTF-8\"");
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		
		Map m = new HashMap();
		
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = XMLUtil.getChildrenText(children);
			}
			
			m.put(k, v);
		}
		
		//閸忔娊妫村ù锟�
		in.close();
		
		return m;
	}
	
	/**
	 * 閼惧嘲褰囩�涙劗绮ㄩ悙鍦畱xml
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(XMLUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		
		return sb.toString();
	}
	
}
