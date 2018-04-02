package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Delivery;
import com.aqb.cn.bean.Jifen;
import com.aqb.cn.bean.Redpacket;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DeliveryMapper;
import com.aqb.cn.mapper.JifenMapper;
import com.aqb.cn.mapper.RedpacketMapper;
import com.aqb.cn.service.DeliveryService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/24.
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private Log logger = LogFactory.getLog(DeliveryServiceImpl.class);

    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private RedpacketMapper redpacketMapper;
    @Autowired
    private JifenMapper jifenMapper;




    @Override
    public int add(Delivery delivery) {
        Delivery delivery_db = deliveryMapper.selectByPrimaryKey(delivery.getId());
        if(delivery_db == null) {
            if (deliveryMapper.insertSelective(delivery) > 0) {
                logger.debug("添加广告投放记录" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Delivery delivery) {
        Delivery delivery2 =deliveryMapper.selectByPrimaryKey(delivery.getId());
        if(delivery2 == null){
            logger.warn("尝试更新广告投放记录"  + " ,但是广告投放记录不存在");
            return Status.NOT_EXISTS;
        }
        if (deliveryMapper.updateByPrimaryKeySelective(delivery) > 0) {
            logger.debug("更新广告投放记录成功" + delivery2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Delivery delivery) {
        Delivery delivery2 = deliveryMapper.selectByPrimaryKey(delivery.getId());
        if (delivery2 == null) {
            logger.warn("尝试删除广告投放记录，但是广告投放记录不存在");
            return Status.NOT_EXISTS;
        }
        if (deliveryMapper.deleteByPrimaryKey(delivery.getId()) > 0 ) {
            logger.debug("删除广告投放记录成功" + delivery.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Delivery get(String id) {
        return deliveryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Delivery> queryRed(String userId) {
        return deliveryMapper.queryRed(userId);
    }

    /**
     * 一键领取红包
     * @param deliverys
     * @return
     */
    @Override
    public int updateRed(List<Delivery> deliverys,String userId) {
        Float money = new Float(0.0);//领取的总金额
        Float scoer = new Float(0.0);//领取的总积分
        Date date = new Date();
        //操作Delivery表中的数据
        for(Delivery delivery : deliverys){
            //计算领取的总金额和总积分
            if(delivery.getRedPacketStatus() == 1 && delivery.getRedPacketType() == 1){
                money = money + delivery.getRedPacketMoney();
            }
            if(delivery.getRedPacketStatus() == 1 && delivery.getRedPacketType() == 2){
                scoer = scoer + delivery.getRedPacketMoney();
            }
            delivery.setRedPacketStatus(0);
        }
        //执行批量更新操作
//        int number = deliveryMapper.updateRed(deliverys);
        Map map = new HashMap();
        map.put("deliverys",deliverys);
        map.put("redPacketStatus",0);
        int number = deliveryMapper.updateRed(map);

        if(number > 0){
            //更新钱包和积分
            if(money > 0.0){
                Redpacket redpacket = new Redpacket();
                redpacket.setId(UUIDCreator.getUUID());
                redpacket.setUserId(userId);
                redpacket.setRedCategory(2);
                redpacket.setRedIncomeOut(true);
                redpacket.setRedSubtotal(money);
                redpacket.setRedStatus(0);
                redpacket.setRedFoundDate(date);
                redpacketMapper.insertSelective(redpacket);
            }
            if(scoer > 0.0){
                Jifen jifen = new Jifen();
                jifen.setId(UUIDCreator.getUUID());
                jifen.setUserId(userId);
                jifen.setJifenCategory(7);
                jifen.setJifenIncomeOut(true);
                jifen.setJifenSubtotal(scoer);
                jifen.setJifenStatus(0);
                jifen.setJifenFoudDate(date);
                jifenMapper.insertSelective(jifen);
            }

            return Status.SUCCESS;
        }
        return Status.ERROR;
    }
}
