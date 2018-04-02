package com.aqb.cn.mapper;

import com.aqb.cn.bean.Task;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    List<Task> queryTask(QueryBase queryBase);

    long countTask(QueryBase queryBase);

    List<Task> queryByUserId(String userId);

    //Task querytodaySign(String userId,Date date);
}