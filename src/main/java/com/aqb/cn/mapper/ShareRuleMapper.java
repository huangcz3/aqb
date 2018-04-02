package com.aqb.cn.mapper;

import com.aqb.cn.bean.a.ShareRule;

public interface ShareRuleMapper {
    int deleteByPrimaryKey(Integer shareSetId);

    int insert(ShareRule record);

    int insertSelective(ShareRule record);

    ShareRule selectByPrimaryKey(Integer shareSetId);

    int updateByPrimaryKeySelective(ShareRule record);

    int updateByPrimaryKey(ShareRule record);

    ShareRule selectByState(Integer state);
}