package com.aqb.cn.mapper;

import com.aqb.cn.bean.Redpacket;

import java.util.List;

public interface RedpacketMapper {
    int deleteByPrimaryKey(String id);

    int insert(Redpacket record);

    int insertSelective(Redpacket record);

    Redpacket selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Redpacket record);

    int updateByPrimaryKey(Redpacket record);

    List<Redpacket> queryRedpacket();

    List<Redpacket> queryRedpacketByUserId(String userId);

    Float sumSubtotal(String userId, Boolean redIncomeOut);//统计用户的总收入和总支出
}