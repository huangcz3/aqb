package com.aqb.cn.mapper;

import com.aqb.cn.bean.Advertisement;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.pojo.Position;
import com.aqb.cn.utils.GetPositionList;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(String id);

    int insert(Advertisement record);

    int insertSelective(Advertisement record);

    Advertisement selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Advertisement record);

    int updateByPrimaryKey(Advertisement record);

    List<Advertisement> queryAdvertisement(QueryBase queryBase);
    long countAdvertisement(QueryBase queryBase);

    //查询在当前时间所有能发广告的圈
    List<Advertisement> queryAdvertisementList();
    List<Advertisement> queryByUserId(String userId);
    List<Advertisement> adminAdvertisement(Integer shstatus);
    List<Advertisement> adminAdvertisementstatus();
    List<Advertisement> adminAdvertisementrfStatus();
    List<Advertisement> adminAdvertisementstatus3();
}