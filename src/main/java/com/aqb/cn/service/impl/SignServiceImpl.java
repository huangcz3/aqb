package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Sign;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.SignMapper;
import com.aqb.cn.service.SignService;
import com.aqb.cn.utils.DateSeven;
import com.aqb.cn.utils.TimeToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */
@Service
public class SignServiceImpl implements SignService{
    private Log logger = LogFactory.getLog(SignServiceImpl.class);

    @Autowired
    private SignMapper signMapper;


    @Override
    public int add(Sign sign) {
        Sign sign_db = signMapper.selectByPrimaryKey(sign.getId());
        if(sign_db == null) {
            if (signMapper.insertSelective(sign) > 0) {
                logger.debug("添加其他设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Sign sign) {
        Sign sign2 =signMapper.selectByPrimaryKey(sign.getId());
        if(sign2 == null){
            logger.warn("尝试更新其他设置"  + " ,但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (signMapper.updateByPrimaryKeySelective(sign) > 0) {
            logger.debug("更新其他设置成功" + sign2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Sign sign) {
        Sign sign2 = signMapper.selectByPrimaryKey(sign.getId());
        if (sign2 == null) {
            logger.warn("尝试删除其他设置，但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (signMapper.deleteByPrimaryKey(sign.getId()) > 0 ) {
            logger.debug("删除其他设置成功" + sign.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Sign get(String id) {
        return signMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Sign> querysign() {
        return signMapper.querySign();
    }

    @Override
    public List<Sign> querySignByUserId(String userId) {
        return signMapper.querySignByUserId(userId);
    }

    @Override
    public List<Sign> querySevenSign(String userId) {
        List<Sign> signs = DateSeven.getSeven();
        for(Sign sign : signs){
            List<Sign> signs1 = signMapper.querySevenSign(userId, TimeToString.StrToDate2(sign.getDateSeven()));
            if(signs1.size() == 0){//没签到
                sign.setSin(false);
            }else {//已签到
                sign.setSin(true);
            }
            //转星期
            sign.setWeek(TimeToString.getWeek(sign.getDateSeven()));
        }

        return signs;
    }

    @Override
    public Sign querytodaySign(String userId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now_sdate = sdf.format(date);
        return signMapper.querytodaySign(userId,TimeToString.StrToDate2(now_sdate));
    }


}
