package com.aqb.cn.mapper;

import com.aqb.cn.bean.AdminModule;

import java.util.List;

public interface AdminModuleMapper {
    int deleteByPrimaryKey(String id);

    int insert(AdminModule record);

    int insertSelective(AdminModule record);

    AdminModule selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdminModule record);

    int updateByPrimaryKey(AdminModule record);

    //根据adminId查询员工的权限
    List<AdminModule> selectByAdminId(String adminId);
    //循环添加
    int addAdminModuleList(List<AdminModule> adminModules);
    int deleteByAdminId(String adminId);
}