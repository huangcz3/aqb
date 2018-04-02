package com.aqb.cn.utils;

import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/6/21.
 */
public class RedisTest {

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
        //查看服务是否运行
        System.out.println("Server is running: "+jedis.ping());

        //设置 redis 字符串数据
        jedis.set("admin", "adminUser");
        // 获取存储的数据并输出
        System.out.println("取出值："+ jedis.get("admin"));


    }

}
