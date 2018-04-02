package com.aqb.cn.service;

import com.aqb.cn.bean.Task;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface TaskService extends BaseService<Task>{
    List<Task> queryByUserId(String userId);
    //Task querytodaySign(String userId);
    int insertNewbieTask(String userId);
}
