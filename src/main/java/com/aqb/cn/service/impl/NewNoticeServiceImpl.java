package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.bean.NewNotice;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.RedpacketMapper;
import com.aqb.cn.mapper.NewNoticeMapper;
import com.aqb.cn.service.NewNoticeService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */
@Service
public class NewNoticeServiceImpl implements NewNoticeService {

    private Log logger = LogFactory.getLog(NewNoticeServiceImpl.class);

    @Autowired
    private NewNoticeMapper newNoticeMapper;


    @Override
    public int add(NewNotice newNotice) {
        NewNotice newNotice_db = newNoticeMapper.selectByPrimaryKey(newNotice.getId());
        if(newNotice_db == null) {
            if (newNoticeMapper.insertSelective(newNotice) > 0) {
                logger.debug("添加消息" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(NewNotice newNotice) {
        NewNotice newNotice2 =newNoticeMapper.selectByPrimaryKey(newNotice.getId());
        if(newNotice2 == null){
            logger.warn("尝试更新消息"  + " ,但是消息不存在");
            return Status.NOT_EXISTS;
        }
        if (newNoticeMapper.updateByPrimaryKeySelective(newNotice) > 0) {
            logger.debug("更新消息成功" + newNotice2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(NewNotice newNotice) {
        NewNotice newNotice2 = newNoticeMapper.selectByPrimaryKey(newNotice.getId());
        if (newNotice2 == null) {
            logger.warn("尝试删除消息，但是消息不存在");
            return Status.NOT_EXISTS;
        }
        if (newNoticeMapper.deleteByPrimaryKey(newNotice.getId()) > 0 ) {
            logger.debug("删除消息成功" + newNotice.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public NewNotice get(String id) {
        return newNoticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(newNoticeMapper.querynewNotice(queryBase));
        queryBase.setTotalRow(newNoticeMapper.countnewNotice(queryBase));
    }

    @Override
    public boolean queryNewNoticeStatus(String userId) {
        List<NewNotice> newNotices = newNoticeMapper.queryNewNoticeStatus(userId);
        if(newNotices == null || newNotices.size() == 0){
            return false;//没有查到未读消息，返回false
        }
        return true;//否则返回true
    }

    //查询到用户的未读消息列表后，把所有的未读消息改为已读消息
    @Override
    public int updateByStatus(String userId) {
        return newNoticeMapper.updateByStatus(userId);
    }
}
