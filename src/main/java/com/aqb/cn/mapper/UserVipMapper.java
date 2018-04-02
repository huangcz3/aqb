package com.aqb.cn.mapper;

import com.aqb.cn.bean.UserVip;

import java.util.List;

public interface UserVipMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    List<UserVip> queryUserVip();

    long countUserVip(String userId);

    List<UserVip> selectByUserId(String userId);

    UserVip selectByUserIdVipGrade(String userId,Integer vipGrade);

    UserVip selectUserVipUse(String userId);//查询用户正在使用的会员

    List<UserVip> selectUserVipUnuse(String userId);//查询用户未使用的会员

    List<UserVip> UsingUserVip();

    List<UserVip> selectMyVip(String userId);
}