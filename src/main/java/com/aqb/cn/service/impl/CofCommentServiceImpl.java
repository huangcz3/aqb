package com.aqb.cn.service.impl;


import com.aqb.cn.bean.CofComment;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.CofCommentMapper;
import com.aqb.cn.service.CofCommentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*
 * Created by Administrator on 2017/6/27.
*/


@Service
public class CofCommentServiceImpl implements CofCommentService{
    private Log logger = LogFactory.getLog(CofCommentServiceImpl.class);

    @Autowired
    private CofCommentMapper cofCommentMapper;


    @Override
    public int add(CofComment CofComment) {
        CofComment CofComment_db = cofCommentMapper.selectByPrimaryKey(CofComment.getId());
        if(CofComment_db == null) {
            if (cofCommentMapper.insertSelective(CofComment) > 0) {
                logger.debug("添加评论" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(CofComment CofComment) {
        CofComment CofComment2 =cofCommentMapper.selectByPrimaryKey(CofComment.getId());
        if(CofComment2 == null){
            logger.warn("尝试更新评论"  + " ,但是评论不存在");
            return Status.NOT_EXISTS;
        }
        if (cofCommentMapper.updateByPrimaryKeySelective(CofComment) > 0) {
            logger.debug("更新评论成功" + CofComment2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(CofComment CofComment) {
        CofComment CofComment2 = cofCommentMapper.selectByPrimaryKey(CofComment.getId());
        if (CofComment2 == null) {
            logger.warn("尝试删除评论，但是评论不存在");
            return Status.NOT_EXISTS;
        }
        if (cofCommentMapper.deleteByPrimaryKey(CofComment.getId()) > 0 ) {
            logger.debug("删除评论成功" + CofComment.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public CofComment get(String id) {
        return cofCommentMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(cofCommentMapper.queryCofComment(queryBase));
        queryBase.setTotalRow(cofCommentMapper.countCofComment(queryBase));
    }
}
