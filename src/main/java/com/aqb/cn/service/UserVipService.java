package com.aqb.cn.service;

import com.aqb.cn.bean.UserVip;
import com.aqb.cn.pojo.MyVip;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24/0024.
 */
public interface UserVipService extends BaseService<UserVip>{
    List<UserVip> queryuserVip();

    long countUserVip(String userId);

    List<UserVip> selectByUserId(String userId);

    UserVip selectByUserIdVipGrade(String userId,Integer vipGrade);

    Integer selectUserVipGrade(String userId);

    List<UserVip> selectUserVipUnuse(String userId);

    UserVip selectUserVipUse(String userId);

    List<UserVip> selectMyVip(String userId);

    List<MyVip> myVip(String userId) throws ParseException;
}
