package com.aqb.cn.utils;

import com.aqb.cn.pojo.Position;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetPositionList {
	static Jedis jedis;
	static{
		jedis = new Jedis("localhost");
	}
	
	public static void main(String[] args) throws IOException{
		String sendMsg="msg=测试数据&list=[\"1172050924\"]";
		sendPost("http://127.0.0.1:8888/aqb/tests/sendMsg",sendMsg);//发广告
		//String deleteMsg="list=[\"1172050923\"]";//删除广告
		//sendPost("http://127.0.0.1:8888/aqb/tests/deleteMsg",deleteMsg);
//		System.out.print(getPositionList());
//        System.out.print(sendPost("http://127.0.0.1:8888/aqb/tests/sendMsg",sendMsg));

	}
	
	public static String sendPost(String url,String Params)throws IOException{
		System.out.println(new Date().getTime());
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);//可以输出
            conn.setDoInput(true);//可以输入
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream(),"utf-8");
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            //解析接收到的json响应
            JsonParser parser=new JsonParser();
            JsonObject object=(JsonObject) parser.parse(response);
            System.out.println(response);
            System.out.println(object.get("status"));
            System.out.println(new Date().getTime());
            reader.close();
            // 断开连接
            conn.disconnect();
 
        } catch (Exception e) {
	        System.out.println("发送 POST 请求出现异常！"+e);
	        e.printStackTrace();
            return "e";
        }
        //使用finally块来关闭输出流、输入流
        finally{
        try{
            if(out!=null){
                out.close();
            }
            if(reader!=null){
                reader.close();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

        return response;
    }

    //获取在线设备列表
	public static List<Position> getPositionList(){
		List<Position> posList=new ArrayList<Position>();
		String devListStr=jedis.get("devList");
		String[] devList=devListStr.split(",");
		System.out.println(devList.length);
		for(String s:devList){
			System.out.println(s);
			String positionStr=jedis.get(s);
			System.out.println(positionStr);
			Position pos=new Position();
			if(positionStr.indexOf("GPS NO SINGAL")>=0){
				//System.out.println(positionStr.indexOf("GPS NO SINGAL"));
				pos.setHasPos(false);
                pos.setDevNum(s);
                posList.add(pos);
			}else{
				String[] params=positionStr.split(",");
				for(String str:params){
					System.out.println(str);//经度,纬度,高度,卫星数,速度(节/1.852km/h),时间
				}
				pos.setDevNum(s);
				pos.setHasPos(true);
				pos.setHeight(Double.parseDouble(params[2]));
				pos.setLatitude(Double.parseDouble(params[1]));
				pos.setLongitude(Double.parseDouble(params[0]));
				pos.setSpeed(Double.parseDouble(params[4]));
				pos.setTime(new Date(Long.parseLong(params[5])));
				posList.add(pos);
			}
		}
		
		return posList;
	}
}
//class Position{
//	private String devNum;
//	public String getDevNum() {
//		return devNum;
//	}
//
//	public void setDevNum(String devNum) {
//		this.devNum = devNum;
//	}
//
//	private boolean hasPos;
//	private double longitude; //经度
//	private double latitude; //纬度
//	private double height;
//	public double getHeight() {
//		return height;
//	}
//
//	public void setHeight(double height) {
//		this.height = height;
//	}
//
//	public double getSpeed() {
//		return speed;
//	}
//
//	public void setSpeed(double speed) {
//		this.speed = speed;
//	}
//
//	public Date getTime() {
//		return time;
//	}
//
//	public void setTime(Date time) {
//		this.time = time;
//	}
//
//	private double speed;
//	private Date time;
//	public double getLongitude() {
//		return longitude;
//	}
//
//	public void setLongitude(double longitude) {
//		this.longitude = longitude;
//	}
//
//	public double getLatitude() {
//		return latitude;
//	}
//
//	public void setLatitude(double latitude) {
//		this.latitude = latitude;
//	}
//
//	public boolean isHasPos() {
//		return hasPos;
//	}
//
//	public void setHasPos(boolean hasPos) {
//		this.hasPos = hasPos;
//	}
//	public String toString(){
//		return "devNo:"+this.devNum+"  longitude:"+this.longitude+"  latitude:"
//					+this.latitude+"  height:"+this.height+"  speed:"+this.speed+"  time:"+this.time+"";
//	}
//}