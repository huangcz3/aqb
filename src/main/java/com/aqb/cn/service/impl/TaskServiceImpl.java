package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Task;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.TaskMapper;
import com.aqb.cn.service.TaskService;
import com.aqb.cn.utils.TimeToString;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
@Service
public class TaskServiceImpl implements TaskService{

    private Log logger = LogFactory.getLog(TaskServiceImpl.class);

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public int add(Task task) {
        Task task_db = taskMapper.selectByPrimaryKey(task.getId());
        if(task_db == null) {
            if (taskMapper.insertSelective(task) > 0) {
                logger.debug("添加绑定设备设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Task task) {
        Task task2 =taskMapper.selectByPrimaryKey(task.getId());
        if(task2 == null){
            logger.warn("尝试更新绑定设备设置"  + " ,但是绑定设备设置不存在");
            return Status.NOT_EXISTS;
        }
        if (taskMapper.updateByPrimaryKeySelective(task) > 0) {
            logger.debug("更新绑定设备设置成功" + task2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Task task) {
        Task task2 = taskMapper.selectByPrimaryKey(task.getId());
        if (task2 == null) {
            logger.warn("尝试删除绑定设备设置，但是绑定设备设置不存在");
            return Status.NOT_EXISTS;
        }
        if (taskMapper.deleteByPrimaryKey(task.getId()) > 0 ) {
            logger.debug("删除绑定设备设置成功" + task.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Task get(String id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(taskMapper.queryTask(queryBase));
        queryBase.setTotalRow(taskMapper.countTask(queryBase));
    }

    @Override
    public List<Task> queryByUserId(String userId) {

//        List<Task> tasks = taskMapper.queryByUserId(userId);
//        for (Task task:tasks) {
//            task.getTaskName();
//        }

        return taskMapper.queryByUserId(userId);
    }

    @Override
    public int insertNewbieTask(String userId) {
        List<Task> tasks = new ArrayList<>();
        long date = new Date().getTime();
        long date2 = date + 1000;
        long date3 = date2 + 1000;
        long date4 = date3 + 1000;
        Task task1 = new Task();
        task1.setId(UUIDCreator.getUUID());
        task1.setUserId(userId);
        task1.setStatus(0);//设置为0未完成
        task1.setTaskName("每日签到");
        task1.setTaskDetails("完成每日签到");
        task1.setTaskReward(2);
        task1.setFoundDate(TimeToString.longToDate(date));
        tasks.add(task1);

        Task task2 = new Task();
        task2.setId(UUIDCreator.getUUID());
        task2.setUserId(userId);
        task2.setStatus(0);
        task2.setTaskName("绑定设备");
        task2.setTaskDetails("绑定设备");
        task2.setTaskReward(50);
        task2.setFoundDate(TimeToString.longToDate(date2));
        tasks.add(task2);

        Task task3 = new Task();
        task3.setId(UUIDCreator.getUUID());
        task3.setUserId(userId);
        task3.setStatus(0);
        task3.setTaskName("完善个人信息");
        task3.setTaskDetails("完善个人信息");
        task3.setTaskReward(10);
        task3.setFoundDate(TimeToString.longToDate(date3));
        tasks.add(task3);

        Task task4 = new Task();
        task4.setId(UUIDCreator.getUUID());
        task4.setUserId(userId);
        task4.setStatus(0);
        task4.setTaskName("产品精选");
        task4.setTaskDetails("产品精选");
        task4.setTaskReward(666);
        task4.setFoundDate(TimeToString.longToDate(date4));
        tasks.add(task4);
        for (Task task:tasks){
            taskMapper.insertSelective(task);
        }
        return 0;
    }

//    @Override
//    public Task querytodaySign(String userId) {
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String str = sdf.format(date);
//
//        return taskMapper.querytodaySign(userId, TimeToString.StrToDate2(str));
//    }
}
