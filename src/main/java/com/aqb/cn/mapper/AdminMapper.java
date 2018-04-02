package com.aqb.cn.mapper;

import com.aqb.cn.bean.Admin;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface AdminMapper {
    int deleteByPrimaryKey(String id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin selectByAdminNumber(String adminNumber);
    Admin selectByAdminName(String adminName);

    Admin queryByName(String adminName);

    List<Admin> queryAdmin(QueryBase queryBase);
    long countAdmin(QueryBase queryBase);
}