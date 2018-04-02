package com.aqb.cn.service;

import com.aqb.cn.bean.User;
import com.aqb.cn.bean.UserFriends;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
public interface UserFriendsService extends BaseService<UserFriends> {
    List<User> queryUserFriends(String userId);
}
