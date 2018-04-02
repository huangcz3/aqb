package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Jifen;
import com.aqb.cn.bean.Yue;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.JifenMapper;
import com.aqb.cn.mapper.YueMapper;
import com.aqb.cn.service.YueService;
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
public class YueServiceImpl implements YueService {

    private Log logger = LogFactory.getLog(YueServiceImpl.class);

    @Autowired
    private YueMapper yueMapper;
    @Autowired
    private JifenMapper jifenMapper;

    @Override
    public int add(Yue yue) {
        Yue yue_db = yueMapper.selectByPrimaryKey(yue.getId());
        if (yue_db == null) {
            if (yueMapper.insertSelective(yue) > 0) {
                logger.debug("添加余额成功");
                return Status.SUCCESS;
            }
            return Status.EXISTS;
        }
        return Status.ERROR;
    }

    @Override
    public int update(Yue yue) {
        Yue yue_db = yueMapper.selectByPrimaryKey(yue.getId());
        if (yue_db == null) {
            logger.warn("尝试更新钱包，但是钱包不存在");
            return Status.NOT_EXISTS;
        }
        if (yueMapper.updateByPrimaryKeySelective(yue) > 0) {
            logger.debug("更新钱包成功" + yue_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Yue yue) {
        Yue yue_db = yueMapper.selectByPrimaryKey(yue.getId());
        if (yue_db == null) {
            logger.warn("尝试删除钱包，但是钱包不在");
            return Status.NOT_EXISTS;
        }
        if (yueMapper.deleteByPrimaryKey(yue.getId()) > 0) {
            logger.debug("尝试删除钱包成功" + yue_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Yue get(String id) {
        return yueMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }

    @Override
    public List<Yue> queryYue() {
        return yueMapper.queryYue();

    }

    @Override
    public List<Yue> queryYueByUserId(String userId) {
        return yueMapper.queryYueByUserId(userId);
    }

    @Override
    public int updateYueToJifen(String userId, Float cost_money, Float ye) {//ye转换后的积分
        Date date = new Date();

        Yue yue_db = new Yue();
        yue_db.setId(UUIDCreator.getUUID());
        yue_db.setUserId(userId);
        yue_db.setYueCategory(6);//6-余额转积分
        yue_db.setYueIncomeOut(false);//支出
        yue_db.setYueSubtotal(cost_money);
        yue_db.setYueStatus(0);
        yue_db.setYueFoudDate(date);
        yueMapper.insertSelective(yue_db);

        Jifen jifen_db = new Jifen();
        jifen_db.setId(UUIDCreator.getUUID());
        jifen_db.setUserId(userId);
        jifen_db.setJifenCategory(9);//9-余额转积分
        jifen_db.setJifenIncomeOut(true);//收入
        jifen_db.setJifenSubtotal(ye);
        jifen_db.setJifenStatus(0);
        jifen_db.setJifenFoudDate(date);
        jifenMapper.insertSelective(jifen_db);

        return Status.SUCCESS;
    }


}
