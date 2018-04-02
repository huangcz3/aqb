package com.aqb.cn.mapper;

import com.aqb.cn.bean.BindingNumber;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface BindingNumberMapper {
    int deleteByPrimaryKey(String id);

    int insert(BindingNumber record);

    int insertSelective(BindingNumber record);

    BindingNumber selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BindingNumber record);

    int updateByPrimaryKey(BindingNumber record);

    BindingNumber selectByDeviceNumber(String deviceNumber);

    //分页查询
    List<BindingNumber> queryBindingAdmin(QueryBase queryBase);
    long countBindingAdmin(QueryBase queryBase);

    //批量添加
    int insertBindingNumberList(List<BindingNumber> bindingNumberList);
    List<BindingNumber> queryWhole();

}