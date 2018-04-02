package com.aqb.cn.mapper;

import com.aqb.cn.bean.Module;

public interface ModuleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Module record);

    int insertSelective(Module record);

    Module selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Module record);

    int updateByPrimaryKey(Module record);

    Module selectByModuleCode(String moduleCode);
}