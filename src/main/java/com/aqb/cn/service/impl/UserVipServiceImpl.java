package com.aqb.cn.service.impl;


import com.aqb.cn.bean.UserVip;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserVipMapper;
import com.aqb.cn.pojo.MyVip;
import com.aqb.cn.service.UserVipService;
import com.aqb.cn.utils.TimeToString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24/0024.
 */
@Service
public class UserVipServiceImpl implements UserVipService {
    private Log logger = LogFactory.getLog(UserVipServiceImpl.class);

    @Autowired
    private UserVipMapper userVipMapper;


    @Override
    public int add(UserVip userVip) {
        UserVip userVip_db = userVipMapper.selectByPrimaryKey(userVip.getId());
        if(userVip_db == null) {
            if (userVipMapper.insertSelective(userVip) > 0) {
                logger.debug("添加其他设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(UserVip userVip) {
        UserVip userVip2 =userVipMapper.selectByPrimaryKey(userVip.getId());
        if(userVip2 == null){
            logger.warn("尝试更新其他设置"  + " ,但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (userVipMapper.updateByPrimaryKeySelective(userVip) > 0) {
            logger.debug("更新其他设置成功" + userVip2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(UserVip userVip) {
        UserVip userVip2 = userVipMapper.selectByPrimaryKey(userVip.getId());
        if (userVip2 == null) {
            logger.warn("尝试删除其他设置，但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (userVipMapper.deleteByPrimaryKey(userVip.getId()) > 0 ) {
            logger.debug("删除其他设置成功" + userVip.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public UserVip get(String id) {
        return userVipMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        //queryBase.setTotalRow(userVipMapper.countUserVip(queryBase));
    }

    @Override
    public List<UserVip> queryuserVip() {
        return userVipMapper.queryUserVip();
    }

    @Override
    public long countUserVip(String userId) {
        return userVipMapper.countUserVip(userId);
    }

    @Override
    public List<UserVip> selectByUserId(String userId) {
        return userVipMapper.selectByUserId(userId);
    }

    @Override
    public UserVip selectByUserIdVipGrade(String userId, Integer vipGrade) {
        return userVipMapper.selectByUserIdVipGrade(userId,vipGrade);
    }

    @Override
    public Integer selectUserVipGrade(String userId) {
        UserVip userVip = userVipMapper.selectUserVipUse(userId);
        return userVip.getVipGrade();
    }

    @Override
    public List<UserVip> selectUserVipUnuse(String userId) {
        return userVipMapper.selectUserVipUnuse(userId);
    }

    @Override
    public UserVip selectUserVipUse(String userId) {
        return userVipMapper.selectUserVipUse(userId);
    }

    @Override
    public List<UserVip> selectMyVip(String userId) {
        return userVipMapper.selectMyVip(userId);
    }

    @Override
    public List<MyVip> myVip(String userId) throws ParseException {
        List<UserVip> userVips = userVipMapper.selectMyVip(userId);
        List<MyVip> myVips = new ArrayList<>();
        MyVip myVip1 = new MyVip();
        MyVip myVip2 = new MyVip();
        MyVip myVip3 = new MyVip();
        Integer surplusDays3 = 0;
        Integer surplusDays2 = 0;
        Integer surplusDays1 = 0;
        for (UserVip userVip:userVips){
            if (userVip.getVipGrade()==3){
                surplusDays3 = userVip.getSurplusDays();
                String expirationDateStr3 = TimeToString.plusDay2(TimeToString.DateToStr(new Date()), userVip.getSurplusDays());
                myVip3.setVipGrade(3);
                myVip3.setSurplusDays(surplusDays3);
                myVip3.setExpirationDate(TimeToString.StrToDate(expirationDateStr3));
                myVip3.setStatus(userVip.getStatus());
            }
            if (userVip.getVipGrade()==2){
                surplusDays2 = userVip.getSurplusDays();
                String expirationDateStr3 = TimeToString.plusDay(surplusDays3);
                String expirationDateStr2 = TimeToString.plusDay2(expirationDateStr3, surplusDays2);
                myVip2.setVipGrade(2);
                myVip2.setSurplusDays(surplusDays2);
                myVip2.setExpirationDate(TimeToString.StrToDate(expirationDateStr2));
                myVip2.setStatus(userVip.getStatus());
            }
            if (userVip.getVipGrade()==1){
                surplusDays1 = userVip.getSurplusDays();
                String expirationDateStr3 = TimeToString.plusDay(surplusDays3);
                String expirationDateStr2 = TimeToString.plusDay2(expirationDateStr3, surplusDays2);
                String expirationDateStr1 = TimeToString.plusDay2(expirationDateStr2, surplusDays1);
                myVip1.setVipGrade(1);
                myVip1.setSurplusDays(surplusDays1);
                myVip1.setExpirationDate(TimeToString.StrToDate(expirationDateStr1));
                myVip1.setStatus(userVip.getStatus());
            }
        }

        myVips.add(myVip1);myVips.add(myVip2);myVips.add(myVip3);
        for (int i=0;i<myVips.size();i++){
            if (myVips.get(i).getVipGrade()==null||myVips.get(i).getSurplusDays()==null|| myVips.get(i).getSurplusDays()==0){
                myVips.remove(i);
                i--;
            }
        }
        return myVips;
    }
}
