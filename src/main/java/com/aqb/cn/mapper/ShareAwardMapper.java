package com.aqb.cn.mapper;

import com.aqb.cn.bean.a.ShareAward;

import java.util.List;

public interface ShareAwardMapper {
    int deleteByPrimaryKey(Integer shareAwardId);

    int insert(ShareAward record);

    int insertSelective(ShareAward record);

    ShareAward selectByPrimaryKey(Integer shareAwardId);

    int updateByPrimaryKeySelective(ShareAward record);

    int updateByPrimaryKey(ShareAward record);

    List<ShareAward> selectByShareTel(String shareTel);
    List<ShareAward> selectByShareTelStatus(String shareTel);

}