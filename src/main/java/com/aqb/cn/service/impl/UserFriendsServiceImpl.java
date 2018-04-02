package com.aqb.cn.service.impl;

import com.aqb.cn.bean.User;
import com.aqb.cn.bean.UserFriends;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserFriendsMapper;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.service.UserFriendsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Service
public class UserFriendsServiceImpl implements UserFriendsService {

    private Log logger = LogFactory.getLog(UserFriendsServiceImpl.class);

    @Autowired
    private UserFriendsMapper userFriendsMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public int add(UserFriends userFriends) {
        UserFriends userFriends_db = userFriendsMapper.selectById1AndId2(userFriends.getUserId(), userFriends.getUserId2());
        if(userFriends_db == null) {
            if (userFriendsMapper.insertSelective(userFriends) > 0) {
                logger.debug("添加联系人" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(UserFriends userFriends) {
        UserFriends userFriends2 =userFriendsMapper.selectByPrimaryKey(userFriends.getId());
        if(userFriends2 == null){
            logger.warn("尝试更新联系人"  + " ,但是联系人不存在");
            return Status.NOT_EXISTS;
        }
        if (userFriendsMapper.updateByPrimaryKeySelective(userFriends) > 0) {
            logger.debug("更新联系人成功" + userFriends2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(UserFriends userFriends) {
        UserFriends userFriends2 = userFriendsMapper.selectByPrimaryKey(userFriends.getId());
        if (userFriends2 == null) {
            logger.warn("尝试删除联系人，但是联系人不存在");
            return Status.NOT_EXISTS;
        }
        if (userFriendsMapper.deleteByPrimaryKey(userFriends.getId()) > 0 ) {
            logger.debug("删除联系人成功" + userFriends.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public UserFriends get(String id) {
        return userFriendsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<User> queryUserFriends(String userId) {
        List<User> strings = userMapper.queryUserFriends(userId);
        return strings;
    }
    
}
