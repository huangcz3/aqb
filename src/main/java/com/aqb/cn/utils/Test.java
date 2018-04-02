package com.aqb.cn.utils;

import com.alipay.api.internal.util.AlipaySignature;
import com.aqb.cn.action.AlipayAction;
import com.aqb.cn.bean.Sign;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Test {

    public static void main(String[] args) {

        String month = "2017-02";
        int year = Integer.valueOf(month.substring(0,4));
        int mon = 0;
        if(Integer.valueOf(month.substring(5,6)) == 0){
            mon = Integer.valueOf(month.substring(6,7));
        }
        if(Integer.valueOf(month.substring(5,6)) == 1){
            mon = Integer.valueOf(month.substring(5,7));
        }

        Calendar c = Calendar.getInstance();
        c.set(year, mon, 0); //输入类型为int类型

        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        System.out.println(year + "年" + month + "月有" + dayOfMonth + "天");

//        Socket socket =null;
//        InputStream inputStream =null;
//        OutputStream outputStream =null;
//        try{
//            //创建Socket实例
//            socket = new Socket("120.55.196.129",9590);
//            //获取输出流,向服务器发生数据
//            outputStream = socket.getOutputStream();
//            //{ "login":{"user":"admin","pwd":"password"} }
////            String s = "{\"login\":{\"get_key\":1}}";
//            String s = "{\"cmd\":{\"get\":\"power\"},\"ids_dev\":\"1172050927\",\"sno\":8}";
//            outputStream.write(s.getBytes());
//
//            ///获取输入流,获取服务器的响应
//            inputStream = socket.getInputStream();
//            byte[] buff = new byte[1024];
//            int len = 0;
//            len =  inputStream.read(buff);
//            //打印服务端的相应
//            System.out.println(new String(buff,0,len));
//
//
//
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally {
//            //释放资源
//            try {
//                inputStream.close();
//                outputStream.close();
//                socket.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }


    }

}
