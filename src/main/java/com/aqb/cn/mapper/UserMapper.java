package com.aqb.cn.mapper;

import com.aqb.cn.bean.User;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.utils.Request;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User queryByName(String userName);

    //修改默认地址为空
    int updateAddressToNull(String id);

    List<User> queryUser(QueryBase queryBase);//查询用户

    long countUser(QueryBase queryBase);

    User queryByCode1(String userCode1);//查询微信登录用户的code1

    User queryByCode2(String userCode2);//查询qq登录用户的code2

    User queryByCode3(String userCode3);//查询微博登录用户的code3

    List<User> queryUser1(Request req);//模糊查询user

    long countUser1(Request req);//模糊查询user总条数

    List<User> queryUsermohu(QueryBase queryBase);//模糊查询用户

    List<User> queryUser2(Map map);//模糊查询user

    List<Map> queryUser3(Map map);

    Map findC();

    List<User> queryUserFriends(String userId);
}