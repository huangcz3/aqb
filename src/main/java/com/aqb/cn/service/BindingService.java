package com.aqb.cn.service;

import com.aqb.cn.bean.Binding;
import com.aqb.cn.common.QueryBase;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface BindingService extends BaseService<Binding>{
    Binding queryByUserId(String userId);

    Binding queryByUserId1(String userId);//查询成功绑定的设备 status = 1

    List<Binding> queryByUserId2(String userId);

    Binding queryByUserId3(String userId);//查询用户审核中的绑定的设备 status = 2

    List<Binding> queryByUserId4(String userId);//查询未通过审核的设备 status = 0

    Binding queryByBindingNumber(String bindingNumber);

    List<Binding> adminBinding();

    void queryBindingAdmin(QueryBase queryBase);

    Binding selectByBN(String bindingNumber);//通过设备号 查询 成功绑定且缴纳押金的设备

    List<Binding> queryRelieveByUserId(String userId);//查询解绑审核中的绑定设备 status = 4

    List<Binding> queryUnRelieveByUserId(String userId);//查询解绑审核不通过的绑定设备 status = 5

    List<Binding> unboundAuditList();//解除绑定审核列表
}
