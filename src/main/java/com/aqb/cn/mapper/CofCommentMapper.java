package com.aqb.cn.mapper;

import com.aqb.cn.bean.CofComment;
import com.aqb.cn.bean.CofFabulous;
import com.aqb.cn.common.QueryBase;

import java.util.List;

public interface CofCommentMapper {
    int deleteByPrimaryKey(String id);

    int insert(CofComment record);

    int insertSelective(CofComment record);

    CofComment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CofComment record);

    int updateByPrimaryKey(CofComment record);

    List<CofComment> queryCofComment(QueryBase queryBase);

    long countCofComment(QueryBase queryBase);

    List<CofComment> selectByUserId(String userId);
    List<CofComment> selectByCofId(String cofId);


}