package com.aqb.cn.mapper;

import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.pojo.RcAndUser;

import java.util.List;

public interface RepairCommentMapper {
    int deleteByPrimaryKey(String id);

    int insert(RepairComment record);

    int insertSelective(RepairComment record);

    RepairComment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RepairComment record);

    int updateByPrimaryKey(RepairComment record);

    List<RepairComment> queryRepairComment(QueryBase queryBase);

    long countRepairComment(QueryBase queryBase);

    List<RepairComment> queryByUserId(String userId);

    List<RepairComment> queryByRepairId(String repairId);

    List<RcAndUser> queryRC(QueryBase queryBase);

    Long countRC(QueryBase queryBase);

    List<RcAndUser> searchRCByComment(QueryBase queryBase);//搜索指定评论内容的评论

    Long countRCByComment(QueryBase queryBase);

    List<RcAndUser> searchRCByRepairName(QueryBase queryBase);//搜索商家的修车圈评论

    Long countRCByRepairName(QueryBase queryBase);

    List<RcAndUser> searchRCByUserName(QueryBase queryBase);//搜索用户评论

    Long countRCByUserName(QueryBase queryBase);
}