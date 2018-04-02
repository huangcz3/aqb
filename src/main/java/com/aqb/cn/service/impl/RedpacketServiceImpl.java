package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Proportion;
import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.bean.Yue;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ProportionMapper;
import com.aqb.cn.mapper.RedpacketMapper;
import com.aqb.cn.mapper.YueMapper;
import com.aqb.cn.service.RedpacketService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
@Controller
public class RedpacketServiceImpl implements RedpacketService {
    private Log logger = LogFactory.getLog(RedpacketServiceImpl.class);

    @Autowired
    private RedpacketMapper redpacketMapper;
    @Autowired
    private YueMapper yueMapper;
    @Autowired
    private ProportionMapper proportionMapper;

    @Override
    public int add(Redpacket redpacket) {
        Redpacket redpacket_db = redpacketMapper.selectByPrimaryKey(redpacket.getId());
        if (redpacket_db == null) {
            if (redpacketMapper.insertSelective(redpacket) > 0) {
                logger.debug("添加余额成功");
                return Status.SUCCESS;
            }
            return Status.EXISTS;
        }
        return Status.ERROR;
    }

    @Override
    public int update(Redpacket redpacket) {
        Redpacket redpacket_db = redpacketMapper.selectByPrimaryKey(redpacket.getId());
        if (redpacket_db == null) {
            logger.warn("尝试更新钱包，但是钱包不存在");
            return Status.NOT_EXISTS;
        }
        if (redpacketMapper.updateByPrimaryKeySelective(redpacket) > 0) {
            logger.debug("更新钱包成功" + redpacket_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Redpacket redpacket) {
        Redpacket redpacket_db = redpacketMapper.selectByPrimaryKey(redpacket.getId());
        if (redpacket_db == null) {
            logger.warn("尝试删除钱包，但是钱包不在");
            return Status.NOT_EXISTS;
        }
        if (redpacketMapper.deleteByPrimaryKey(redpacket.getId()) > 0) {
            logger.debug("尝试删除钱包成功" + redpacket_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Redpacket get(String id) {
        return redpacketMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }

    @Override
    public List<Redpacket> queryRedpacketByUserId(String userId) {
        return redpacketMapper.queryRedpacketByUserId(userId);
    }

    @Override
    public Float countAsessts(String userId) {
        Float myAssets_repacket = new Float(0);
        List<Redpacket> redpackets = redpacketMapper.queryRedpacketByUserId(userId);
        for (Redpacket red:redpackets){
            if (!red.getRedIncomeOut()){
                Float f = red.getRedSubtotal();
                red.setRedSubtotal(-f);
            }
            myAssets_repacket = myAssets_repacket + red.getRedSubtotal();
        }
        return myAssets_repacket;
    }

    @Override
    public Float redTo(Float score,Integer status) {
        Float result = new Float(0);//转换后的结果
        List<Proportion> proportions = proportionMapper.selectByStatus(status);
        for(Proportion proportion : proportions){
            result = result + ((int)(score / proportion.getProportionFront()) * proportion.getProportionAfter());
            score = score % proportion.getProportionFront();
            if(score == 0){
                break;
            }
        }
        return result;
    }

    @Override
    public int updateRedpactToYue(String userId, Float redpacket, Float yue) {
        Date date = new Date();
        Redpacket redpacket_db = new Redpacket();
        redpacket_db.setId(UUIDCreator.getUUID());
        redpacket_db.setUserId(userId);
        redpacket_db.setRedIncomeOut(false);
        redpacket_db.setRedSubtotal(redpacket);
        redpacket_db.setRedFoundDate(date);
        redpacket_db.setRedStatus(0);
        redpacket_db.setRedCategory(4);//4-红包转余额
        redpacketMapper.insertSelective(redpacket_db);

        Yue yue_db = new Yue();
        yue_db.setId(UUIDCreator.getUUID());
        yue_db.setUserId(userId);
        yue_db.setYueCategory(11);//11--红包转金额
        yue_db.setYueIncomeOut(true);
        yue_db.setYueSubtotal(yue);
        yue_db.setYueStatus(0);
        yue_db.setYueFoudDate(date);
        yueMapper.insertSelective(yue_db);
        return Status.SUCCESS;
    }

}
