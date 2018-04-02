package com.aqb.cn.mapper;

import com.aqb.cn.bean.UserFriends;

import java.util.List;

public interface UserFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserFriends record);

    int insertSelective(UserFriends record);

    UserFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserFriends record);

    int updateByPrimaryKey(UserFriends record);

    UserFriends selectByUserId(String userId,String userId2);

    UserFriends selectById1AndId2(String userId, String userId2);
}