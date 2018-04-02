package com.aqb.cn.service;

import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.common.QueryBase;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public interface RepairCommentService extends BaseService<RepairComment>{

    List<RepairComment> queryByRepairId(String repairId);

    List<RepairComment> queryByUserId(String userId);

    void queryRC(QueryBase queryBase);

    void searchRCByComment(QueryBase queryBase);

    void searchRCByRepairName(QueryBase queryBase);

    void searchRCByUserName(QueryBase queryBase);




}
