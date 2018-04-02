package com.aqb.cn.mapper;

import java.util.List;

import com.aqb.cn.bean.Shield;
import com.aqb.cn.common.QueryBase;

public interface ShieldMapper {
    int deleteByPrimaryKey(String id);

    int insert(Shield record);

    int insertSelective(Shield record);

    Shield selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Shield record);

    int updateByPrimaryKey(Shield record);

    Shield selectByGuanjianci(String shieldGuanjianci);

    List<Shield> queryShield(QueryBase queryBase);
    long countShield(QueryBase queryBase);

    List<Shield> selectShieldList();//查询所有已经启用的屏蔽关键词
}