package com.aqb.cn.utils.zxing;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;


/**
 * 在图片上画字符串（或在图片上加图片）
 */
public class TestImage {

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        //exportImg2("万魔声学","d:/logo.jpg");
        exportImg1("弹幕猎人1.0","智能LED屏幕","1176101101");
    }


    /**
     * 根据 字符串 绘制图片
     */
    public static void exportImg1(String s,String s1,String s2){
        int width = 200;
        int height = 80;
//        String s = "弹幕猎人1.0";
//        String s1 = "智能LED屏幕";
//        String s2 = "Device ID:1176101101";

        File file = new File("E:/test/QRCode2/"+s2+".jpg");

        Font font = new Font("TimesRoman", Font.PLAIN, 5);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);

//        FontRenderContext context = g2.getFontRenderContext();
//        Rectangle2D bounds = font.getStringBounds(s, context);
//        double x = (width - bounds.getWidth()) / 2;
//        double y = (height - bounds.getHeight()) / 2;
//        double ascent = -bounds.getY();
//        double baseY = y + ascent;

        g2.drawString(s,  14, 20);
        g2.drawString(s1, 14, 40);
        g2.drawString("Device ID:", 14, 65);
        g2.setFont(new Font("宋体", Font.BOLD, 20));    //改变字体大小
        g2.drawString(s2, 75, 65);
        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 加图片
     */
    public static void exportImg2(String username,String headImg){
        try {
            //1.jpg是你的 主图片的路径
            InputStream is = new FileInputStream("E:/test/QRCodeUtil/59241799(1).jpg");


            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            Graphics g = buffImg.getGraphics();

            //创建你要附加的图象。
            //小图片的路径
            ImageIcon imgIcon = new ImageIcon(headImg);

            //得到Image对象。
            Image img = imgIcon.getImage();

            //将小图片绘到大图片上。
            //5,300 .表示你的小图片在大图片上的位置。
            g.drawImage(img,400,15,null);

            //设置颜色。
            g.setColor(Color.BLACK);


            //最后一个参数用来设置字体的大小
            Font f = new Font("宋体",Font.PLAIN,25);
            Color mycolor = Color.red;//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);

            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            g.drawString(username,100,135);

            g.dispose();


            OutputStream os;

            //os = new FileOutputStream("d:/union.jpg");
            String shareFileName = "\\upload\\" + System.currentTimeMillis() + ".jpg";
            os = new FileOutputStream(shareFileName);
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);

            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ImageFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}