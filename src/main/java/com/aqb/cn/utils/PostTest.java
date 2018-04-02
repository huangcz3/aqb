package com.aqb.cn.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.*;

/**
 * Created by 黄成钊 on 2017/9/18.
 */
public class PostTest {


    public static String sendPost(String url,String xmlString){
        //创建httpclient工具对象
        HttpClient client = new HttpClient();
        //创建post请求方法
        PostMethod myPost = new PostMethod(url);
        //设置请求超时时间
        client.setConnectionTimeout(3000*1000);
        String responseString = null;
        try{
            //设置请求头部类型
            myPost.setRequestHeader("Content-Type","text/xml");
            myPost.setRequestHeader("charset","utf-8");
            //设置请求体，即xml文本内容，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
            myPost.setRequestBody(xmlString);
            int statusCode = client.executeMethod(myPost);
            //只有请求成功200了，才做处理
            if(statusCode == HttpStatus.SC_OK){
                InputStream inputStream = myPost.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                responseString = stringBuffer.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            myPost.releaseConnection();
        }
        return responseString;
    }


    public static void main(String[] args) throws Exception {
        //String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><PACKET type=\"REQUEST\" version=\"1.0\"><HEAD><TRANSTYPE>SNY</TRANSTYPE><SYSCODE></SYSCODE><TRANSCODE ></TRANSCODE><CONTENTTYPE></CONTENTTYPE><VERIFYTYPE></VERIFYTYPE><USER>zhongchuang</USER><PASSWORD>zhongchuang</PASSWORD><SVCSEQNO></SVCSEQNO></HEAD><THIRD><EXTENTERPCODE>CMBC0601037074</EXTENTERPCODE><PRODNO>0000</PRODNO><PLANNO>0000</PLANNO><TRANSCODE>100001</TRANSCODE><TRANSDATE>2015-09-24</TRANSDATE><TRANSTIME>08:30:26</TRANSTIME></THIRD><BODY><VhlTypeQuery><VEHICLE_NO>湘E1MM16</VEHICLE_NO><RACK_NO>LFV2A21K473076632</RACK_NO><VEHICLE_CODE></VEHICLE_CODE><VEHICLE_NAME>STD1009YQD</VEHICLE_NAME><DEPT_NO>430100</DEPT_NO></VhlTypeQuery></BODY></PACKET>\n";
        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<PACKET type=\"REQUEST\" version=\"1.0\">\n" +
                "\t<!-- 我方属性 -->\n" +
                "\t<HEAD>\n" +
                "         <!--同步异步请求标志-->\n" +
                "<TRANSTYPE>SNY</TRANSTYPE>\n" +
                "<!--请求系统编码-->\n" +
                "         <SYSCODE></SYSCODE>\n" +
                "<!--请求类型-->\n" +
                "         <TRANSCODE ></TRANSCODE>\n" +
                "<!--业务属性格式-->\n" +
                "         <CONTENTTYPE></CONTENTTYPE>\n" +
                "<!--验证类型-->\n" +
                "         <VERIFYTYPE></VERIFYTYPE>\n" +
                "<!--验证用户名-->\n" +
                "         <USER>zhongchuang</USER>\n" +
                "<!--验证用户密码-->\n" +
                "<PASSWORD>zhongchuang</PASSWORD>\n" +
                "<!--交易标识号-->\n" +
                "<SVCSEQNO></SVCSEQNO>\n" +
                "\t</HEAD>\n" +
                "\t<THIRD>\n" +
                " <!--渠道类型-->\n" +
                "<EXTENTERPCODE>CMBC0601037074</EXTENTERPCODE>\n" +
                " <!--产品代码-->\n" +
                "<PRODNO>0000</PRODNO>\n" +
                " <!--方案代码-->\n" +
                "<PLANNO>0000</PLANNO>\n" +
                " <!--交易类型-->\n" +
                "<TRANSCODE>100001</TRANSCODE>\n" +
                " <!--交易日期-->\n" +
                "<TRANSDATE>2015-09-24</TRANSDATE>\n" +
                "<!--交易时间-->\n" +
                "\t\t<TRANSTIME>08:30:26</TRANSTIME>\n" +
                "\t</THIRD>\n" +
                "\t<!-- 业务属性 -->\n" +
                "\t<BODY>\n" +
                "\t\t<VhlTypeQuery>\n" +
                "           <VEHICLE_NO>湘E1MM16</VEHICLE_NO>\n" +
                "           <RACK_NO>LFV2A21K473076632</RACK_NO>\n" +
                "          <VEHICLE_CODE></VEHICLE_CODE>\n" +
                "          <VEHICLE_NAME>STD1009YQD</VEHICLE_NAME>\n" +
                "          <DEPT_NO>430100</DEPT_NO>\n" +
                "          </VhlTypeQuery>\n" +
                "\t</BODY>\n" +
                "</PACKET>\n";
        String url = "http://agenttest.sinosafe.com.cn/carservice";
        String str = sendPost(url,xmlString);
        String xml = formatXml(str);
        System.out.println(xml);

        jdomParseXml(xml);


        Map<String, Object> map = xml2mapWithAttr(xml, false);
        System.out.println(map);
//遍历map
        for (Object v : map.values()) {
            System.out.println("value= " + v);
        }

    }

    public static String formatXml(String str) throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(str);
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        // 将document写入到输出流
        xmlWriter.write(document);
        xmlWriter.close();
        return writer.toString();



    }


    /**
     * 3、JDOM解析XML
     * 解析的时候自动去掉CDMA
     * @param xml
     */
    @SuppressWarnings("unchecked")
    public static void jdomParseXml(String xml){
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            org.jdom.Document doc;
            doc = (org.jdom.Document) sb.build(source);

            org.jdom.Element root = doc.getRootElement();// 指向根节点
            List<Element> list = root.getChildren();

            System.out.println(list);

            if(list!=null&&list.size()>0){
                for (org.jdom.Element element : list) {
                    System.out.println("key是："+element.getName()+"，值是："+element.getText());

					/*try{
						methodName =  element.getName();
						Method m = v.getClass().getMethod("set" + methodName, new Class[] { String.class });
						if(parseInt(methodName)){
							m.invoke(v, new Object[] { Integer.parseInt(element.getText()) });
						}else{
							m.invoke(v, new Object[] { element.getText() });
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}*/

                }
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * xml转map 带属性
     * @param xmlStr
     * @param needRootKey 是否需要在返回的map里加根节点键
     * @return
     * @throws DocumentException
     */
    public static Map xml2mapWithAttr(String xmlStr, boolean needRootKey) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        org.dom4j.Element root = doc.getRootElement();
        Map<String, Object> map = (Map<String, Object>) xml2mapWithAttr(root);
        if(root.elements().size()==0 && root.attributes().size()==0){
            return map; //根节点只有一个文本内容
        }
        if(needRootKey){
            //在返回的map里加根节点键（如果需要）
            Map<String, Object> rootMap = new HashMap<String, Object>();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }

    /**
     * xml转map 带属性
     * @param e
     * @return
     */
    private static Map xml2mapWithAttr(org.dom4j.Element element) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        List<Element> list = element.elements();
        List<Attribute> listAttr0 = element.attributes(); // 当前节点的所有属性的list
        for (Attribute attr : listAttr0) {
            map.put("@" + attr.getName(), attr.getValue());
        }
        if (list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                org.dom4j.Element iter = (org.dom4j.Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = xml2mapWithAttr(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {

                    List<Attribute> listAttr = iter.attributes(); // 当前节点的所有属性的list
                    Map<String, Object> attrMap = null;
                    boolean hasAttributes = false;
                    if (listAttr.size() > 0) {
                        hasAttributes = true;
                        attrMap = new LinkedHashMap<String, Object>();
                        for (Attribute attr : listAttr) {
                            attrMap.put("@" + attr.getName(), attr.getValue());
                        }
                    }

                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            // mapList.add(iter.getText());
                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            // mapList.add(iter.getText());
                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        // map.put(iter.getName(), iter.getText());
                        if (hasAttributes) {
                            attrMap.put("#text", iter.getText());
                            map.put(iter.getName(), attrMap);
                        } else {
                            map.put(iter.getName(), iter.getText());
                        }
                    }
                }
            }
        } else {
            // 根节点的
            if (listAttr0.size() > 0) {
                map.put("#text", element.getText());
            } else {
                map.put(element.getName(), element.getText());
            }
        }
        return map;
    }

}
