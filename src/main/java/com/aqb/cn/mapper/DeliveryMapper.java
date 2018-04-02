package com.aqb.cn.mapper;

import com.aqb.cn.bean.Delivery;

import java.util.List;
import java.util.Map;

public interface DeliveryMapper {
    int deleteByPrimaryKey(String id);

    int insert(Delivery record);

    int insertSelective(Delivery record);

    Delivery selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Delivery record);

    int updateByPrimaryKey(Delivery record);

    List<Delivery> selectByDevNum(String devNum,int limit);
    Float sumByDeliveryMoney(String advertisementId,Integer status);//统计已经投放的金额
    int sumByDeliveryNumber(String advertisementId,Integer status);//统计已经投放的条数
    List<Delivery> queryRed(String userId);
    //一键领取红包
//    int updateRed(List<Delivery> deliverys);
    int updateRed(Map map);
}