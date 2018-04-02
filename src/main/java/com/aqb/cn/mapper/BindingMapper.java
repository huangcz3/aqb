package com.aqb.cn.mapper;

import com.aqb.cn.bean.Binding;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface BindingMapper {
    int deleteByPrimaryKey(String id);

    int insert(Binding record);

    int insertSelective(Binding record);

    Binding selectByPrimaryKey(String id);
    Binding selectByStatus(String bindingNumber);

    int updateByPrimaryKeySelective(Binding record);

    int updateByPrimaryKey(Binding record);

    List<Binding> selectByBindingNumber(String bindingNumber);

    List<Binding> queryBinding(QueryBase queryBase);
    long countBinding(QueryBase queryBase);

    Binding queryByUserId(String userId);

    Binding queryByUserId1(String userId);//查询用户成功绑定的设备

    Binding queryByBindingNumber(String bindingNumber);

    List<Binding> adminBinding();

    List<Binding> queryByUserId2(String userId);//查询曾经绑定的设备（有失败记录）

    Binding queryByUserId3(String userId);//查询用户成功绑定的设备

    List<Binding> queryByUserId4(String userId);//查询未通过审核的设备

    int updateByUserId(String userId);//根据userId修改押金状态

    Binding selectByBN(String bindingNumber);//通过设备号 查询 成功绑定且缴纳押金的设备

    List<Binding> queryRelieveByUserId(String userId);//查询解绑审核中的绑定设备

    List<Binding> queryUnRelieveByUserId(String userId);

    List<Binding> unboundAudit();//解除绑定审核列表
}