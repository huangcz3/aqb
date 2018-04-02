package com.aqb.cn.mapper;

import com.aqb.cn.bean.NewNotice;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface NewNoticeMapper {
    int deleteByPrimaryKey(String id);

    int insert(NewNotice record);

    int insertSelective(NewNotice record);

    NewNotice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NewNotice record);

    int updateByPrimaryKey(NewNotice record);

    List<NewNotice> querynewNotice(QueryBase queryBase);
    long countnewNotice(QueryBase queryBase);

    List<NewNotice> queryNewNoticeStatus(String userId);//查询用户是否有未读消息
    int updateByStatus(String userId);//查询到用户的未读消息列表后，把所有的未读消息改为已读消息
}