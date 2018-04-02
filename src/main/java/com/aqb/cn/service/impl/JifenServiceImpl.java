package com.aqb.cn.service.impl;


import com.aqb.cn.bean.Jifen;
import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.bean.Yue;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.JifenMapper;
import com.aqb.cn.mapper.RedpacketMapper;
import com.aqb.cn.mapper.YueMapper;
import com.aqb.cn.service.JifenService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Service
public class JifenServiceImpl implements JifenService {
    private Log logger = LogFactory.getLog(JifenServiceImpl.class);

    @Autowired
    private JifenMapper jifenMapper;
    @Autowired
    private YueMapper yueMapper;
    @Autowired
    private RedpacketMapper redpacketMapper;


    @Override
    public int add(Jifen jifen) {
        Jifen jifen_db = jifenMapper.selectByPrimaryKey(jifen.getId());
        if (jifen_db == null) {
            if (jifenMapper.insertSelective(jifen) > 0) {
                logger.debug("添加余额成功");
                return Status.SUCCESS;
            }
            return Status.EXISTS;
        }
        return Status.ERROR;
    }

    @Override
    public int update(Jifen jifen) {
        Jifen jifen_db = jifenMapper.selectByPrimaryKey(jifen.getId());
        if (jifen_db == null) {
            logger.warn("尝试更新钱包，但是钱包不存在");
            return Status.NOT_EXISTS;
        }
        if (jifenMapper.updateByPrimaryKeySelective(jifen) > 0) {
            logger.debug("更新钱包成功" + jifen_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Jifen jifen) {
        Jifen jifen_db = jifenMapper.selectByPrimaryKey(jifen.getId());
        if (jifen_db == null) {
            logger.warn("尝试删除钱包，但是钱包不在");
            return Status.NOT_EXISTS;
        }
        if (jifenMapper.deleteByPrimaryKey(jifen.getId()) > 0) {
            logger.debug("尝试删除钱包成功" + jifen_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Jifen get(String id) {
        return jifenMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }

    @Override
    public List<Jifen> queryJifen() {
        return jifenMapper.queryJifen();

    }

    @Override
    public List<Jifen> queryJifenByUserId(String userId) {
        return jifenMapper.queryJifenByUserId(userId);
    }

    @Override
    public int updateJifenToMoney(String userId,Float score, Float money) {
        Date date = new Date();
        Jifen jifen_db = new Jifen();
        jifen_db.setId(UUIDCreator.getUUID());
        jifen_db.setUserId(userId);
        jifen_db.setJifenCategory(8);//积分转金额
        jifen_db.setJifenIncomeOut(false);
        jifen_db.setJifenSubtotal(score);
        jifen_db.setJifenStatus(0);
        jifen_db.setJifenFoudDate(date);
        jifenMapper.insertSelective(jifen_db);

        Yue yue_db = new Yue();
        yue_db.setId(UUIDCreator.getUUID());
        yue_db.setUserId(userId);
        yue_db.setYueCategory(5);//5-积分转金额
        yue_db.setYueIncomeOut(true);
        yue_db.setYueSubtotal(money);
        yue_db.setYueStatus(0);
        yue_db.setYueFoudDate(date);
        yueMapper.insertSelective(yue_db);
        return Status.SUCCESS;
    }

    @Override
    public int updateJifenToRedpacket(String userId, Float jifen, Float money) {
        Date date = new Date();
        Jifen jifen_db = new Jifen();
        jifen_db.setId(UUIDCreator.getUUID());
        jifen_db.setUserId(userId);
        jifen_db.setJifenCategory(13);//积分兑换红包
        jifen_db.setJifenIncomeOut(false);
        jifen_db.setJifenSubtotal(jifen);
        jifen_db.setJifenStatus(0);
        jifen_db.setJifenFoudDate(date);
        jifenMapper.insertSelective(jifen_db);

        Redpacket redpacket_db = new Redpacket();
        redpacket_db.setId(UUIDCreator.getUUID());
        redpacket_db.setUserId(userId);
        redpacket_db.setRedCategory(3);//3-积分兑红包
        redpacket_db.setRedIncomeOut(true);
        redpacket_db.setRedSubtotal(money);
        redpacket_db.setRedStatus(0);
        redpacket_db.setRedFoundDate(date);
        redpacketMapper.insertSelective(redpacket_db);
        return Status.SUCCESS;
    }
}
