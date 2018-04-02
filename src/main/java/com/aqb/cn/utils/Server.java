//package com.aqb.cn.utils;
//
//
//import org.jboss.netty.bootstrap.ServerBootstrap;
//import org.jboss.netty.channel.ChannelPipeline;
//import org.jboss.netty.channel.ChannelPipelineFactory;
//import org.jboss.netty.channel.Channels;
//import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
//
//import java.net.InetSocketAddress;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import javax.servlet.ServletContext;
//
//public class Server extends Thread {
//    //服务类
//    ServerBootstrap bootstrap = new ServerBootstrap();
//    //boss线程监听端口，worker线程负责数据读写
//    ExecutorService boss = Executors.newCachedThreadPool();
//    ExecutorService worker = Executors.newCachedThreadPool();
//    //监听端口
//    String   port ="3900";
//
//
//    public void run() {
//
//        System.out.println("服务器端已经启动，等待连接：");
//
//        //设置 NIO socket工厂
//        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
//        //设置管道的工厂
//        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//
//            @Override
//            public ChannelPipeline getPipeline() throws Exception {
//
//                ChannelPipeline pipeline = Channels.pipeline();
//                //				pipeline.addLast("decoder", new StringDecoder());
//                //				pipeline.addLast("encoder", new StringEncoder());
////                pipeline.addLast("ReceiveHandler", new ReceiveHandler());
//                return pipeline;
//
//            }
//        });
//
//        bootstrap.setOption("child.tcpNoDelay", true);
//        bootstrap.setOption("child.keepAlive", true);
//        bootstrap.bind(new InetSocketAddress(Integer.parseInt(port)));
//        System.out.println("-----------------------------server-------------------------start!!!");
//
//    }
//
//}
