package com.aqb.cn.utils;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/7/6.
 */
public class ImageUtil {
    /**
     * 将字符串转为图片
     * @param imgStr
     * @return
     */
    public static boolean generateImage(String imgStr,String imgFile)throws Exception {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = imgFile;// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * 图片是否符合 jpg gjf png格式
     * @param format
     * @return
     */
    public static boolean isRightFormat(String format){

        return (format.equals("jpg") || format.equals("gif") || format.equals("png"))?true:false;
    }
}
