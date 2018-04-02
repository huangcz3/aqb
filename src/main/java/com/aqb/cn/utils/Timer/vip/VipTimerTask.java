package com.aqb.cn.utils.Timer.vip;

import com.aqb.cn.bean.UserVip;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.mapper.UserVipMapper;
import com.aqb.cn.utils.ApplicationContext.MyApplicationContextUtil;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/2/0002.
 */
public class VipTimerTask extends TimerTask{
//    private String userId;
//
//    public VipTimerTask(String userId) {
//        this.userId = userId;
//    }

    @Override
    public void run() {
        System.out.println("执行定时任务时的时间...");
        System.out.println(new Date());

        UserVipMapper userVipMapper = (UserVipMapper) MyApplicationContextUtil.getContext().getBean("userVipMapper");
        UserMapper userMapper = (UserMapper) MyApplicationContextUtil.getContext().getBean("userMapper");
        List<UserVip> userVips_use = userVipMapper.UsingUserVip();//查出所有用户正使用的会员
        for (UserVip userVip:userVips_use){
            Integer surplusDays = userVip.getSurplusDays();
            String userId = userVip.getUserId();
            List<UserVip> userVip_unuse = userVipMapper.selectUserVipUnuse(userId);//查出指定用户未使用的会员
            UserVip userVip1 = new UserVip();
            if (userVip_unuse.size() >0){
                for (UserVip unuse:userVip_unuse){
                    if (unuse.getSurplusDays() > 0){
                        userVip1 = unuse;
                        break;
                    }
                }
            }
             if (surplusDays > 0){
                userVip.setSurplusDays(surplusDays-1);//每执行一次剩余天数减 1
                userVipMapper.updateByPrimaryKeySelective(userVip);
                surplusDays = surplusDays-1;
            }
            if (surplusDays == 0) {
                if (userVip_unuse.size() > 0) {
                    userVip1.setStatus(1);//设置状态为启用状态
                    userVip1.setStartTime(new Date());
                    userVipMapper.updateByPrimaryKeySelective(userVip1);
                    userVip.setStatus(0);//设置状态为未启用状态
                    userVipMapper.updateByPrimaryKeySelective(userVip);
                }
            }
        }
    }
}
