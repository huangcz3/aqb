package com.aqb.cn.service.impl;

import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.RepairCommentMapper;
import com.aqb.cn.service.RepairCommentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Service
public class RepairCommentServiceImpl implements RepairCommentService {
    private Log logger = LogFactory.getLog(RepairCommentServiceImpl.class);

    @Autowired
    private RepairCommentMapper repairCommentMapper;


    @Override
    public int add(RepairComment repairComment) {
        RepairComment repairComment_db = repairCommentMapper.selectByPrimaryKey(repairComment.getId());
        if(repairComment_db == null) {
            if (repairCommentMapper.insertSelective(repairComment) > 0) {
                logger.debug("添加朋友圈" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(RepairComment repairComment) {
        RepairComment repairComment2 =repairCommentMapper.selectByPrimaryKey(repairComment.getId());
        if(repairComment2 == null){
            logger.warn("尝试更新朋友圈"  + " ,但是朋友圈不存在");
            return Status.NOT_EXISTS;
        }
        if (repairCommentMapper.updateByPrimaryKeySelective(repairComment) > 0) {
            logger.debug("更新朋友圈成功" + repairComment2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(RepairComment repairComment) {
        RepairComment repairComment2 = repairCommentMapper.selectByPrimaryKey(repairComment.getId());
        if (repairComment2 == null) {
            logger.warn("尝试删除朋友圈，但是朋友圈不存在");
            return Status.NOT_EXISTS;
        }
        if (repairCommentMapper.deleteByPrimaryKey(repairComment.getId()) > 0 ) {
            logger.debug("删除朋友圈成功" + repairComment.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public RepairComment get(String id) {
        return repairCommentMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(repairCommentMapper.queryRepairComment(queryBase));
        queryBase.setTotalRow(repairCommentMapper.countRepairComment(queryBase));

    }

    @Override
    public List<RepairComment> queryByRepairId(String repairId) {
        return repairCommentMapper.queryByRepairId(repairId);
    }

    @Override
    public List<RepairComment> queryByUserId(String userId) {
        return repairCommentMapper.queryByUserId(userId);
    }

    @Override
    public void queryRC(QueryBase queryBase) {
        queryBase.setResults(repairCommentMapper.queryRC(queryBase));
        queryBase.setTotalRow(repairCommentMapper.countRC(queryBase));
    }

    @Override
    public void searchRCByComment(QueryBase queryBase) {
        queryBase.setResults(repairCommentMapper.searchRCByComment(queryBase));
        queryBase.setTotalRow(repairCommentMapper.countRCByComment(queryBase));
    }

    @Override
    public void searchRCByRepairName(QueryBase queryBase) {
        queryBase.setResults(repairCommentMapper.searchRCByRepairName(queryBase));
        queryBase.setTotalRow(repairCommentMapper.countRCByRepairName(queryBase));
    }

    @Override
    public void searchRCByUserName(QueryBase queryBase) {
        queryBase.setResults(repairCommentMapper.searchRCByUserName(queryBase));
        queryBase.setTotalRow(repairCommentMapper.countRCByUserName(queryBase));
    }



}
