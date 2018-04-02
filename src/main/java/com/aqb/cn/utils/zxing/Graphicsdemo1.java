package com.aqb.cn.utils.zxing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2017/7/5.
 */
/*
 * 演示Graphics2D 画字符串的例子;
 * @author hjx
 *
 */

public class Graphicsdemo1 extends JFrame {

    public static void main(String args[]){

        new Graphicsdemo1();


    }


    public Graphicsdemo1(){

        super();
        setTitle("绘制字符串");
        setBounds(100,100,400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


    }

    //重写组件 的paint(Graphics g)方法
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D graphics=(Graphics2D)g;  //转化graphics 为Graphics2D对象
        String str="好好学习，天天向上";

        Font font=new Font("隶书",Font.BOLD,30); // 定义字体对象
        graphics.setFont(font);  //设置graphics的字体对象S

        BasicStroke bk=new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL); //自定义笔画对象
        graphics.setStroke(bk);  //设置graphics的笔画对象;

        graphics.setColor(Color.blue); //设置颜色

        graphics.drawString(str, 50, 80);  //绘制字符串;

        graphics.setColor(Color.red);  //设置颜色

        //循环输出字符串
        for(int i=0;i<str.length();i++){

            graphics.drawString(str.charAt(i)+"", 50+i*font.getSize() , 80+i*font.getSize());

        }

        graphics.setColor(Color.green); //设置颜色
        //循环输出字符串
        for(int i=0;i<str.length();i++){

            graphics.drawString(str.charAt(i)+"", 40+i*font.getSize() , 70+i*font.getSize());

        }


        //画矩形
        graphics.drawRect(50, 300, 150, 100);



    }
}
