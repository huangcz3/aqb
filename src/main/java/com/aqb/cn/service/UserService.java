package com.aqb.cn.service;

import com.aqb.cn.bean.User;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.utils.Request;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
public interface UserService extends BaseService<User> {

    int login(User user);
    User queryByName(String name);

    User queryByCode1(String userCode1);

    User queryByCode2(String userCode2);

    User queryByCode3(String userCode3);

    List<User> queryUser(Request req);

    long countUser(Request req);//模糊查询user总条数

    List<User> queryUsermohu(QueryBase queryBase);//模糊查询用户

    List<User> queryUser2(Map map);//模糊查询user

    Map findC();

    List<Map> queryUser3(Map map);
}
