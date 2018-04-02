package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Dizhi;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.DizhiMapper;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.service.DizhiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@Service
public class DizhiServiceImpl implements DizhiService {

    private Log logger = LogFactory.getLog(DizhiServiceImpl.class);

    @Autowired
    private DizhiMapper dizhiMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public int add(Dizhi dizhi) {

        if (dizhiMapper.insertSelective(dizhi) > 0) {
            logger.debug("添加地址" + "成功");
            //查看该用户是否已经存在默认地址，如果没有，则添加该地址为默认地址，如果有，则不改变默认地址
            User user = userMapper.selectByPrimaryKey(dizhi.getUserId());
            if(user.getUserAddress() == null){
                user.setUserAddress(dizhi.getId());
                //修改数据库
                if(userMapper.updateByPrimaryKeySelective(user) > 0){
                    logger.debug("添加默认地址" + "成功");
                }
            }
            return Status.SUCCESS;
        }
        return Status.ERROR;

    }

    @Override
    public int update(Dizhi dizhi) {
        Dizhi dizhi2 =dizhiMapper.selectByPrimaryKey(dizhi.getId());
        if(dizhi2 == null){
            logger.warn("尝试更新地址"  + " ,但是地址不存在");
            return Status.NOT_EXISTS;
        }
        if (dizhiMapper.updateByPrimaryKeySelective(dizhi) > 0) {
            logger.debug("更新地址成功" + dizhi2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Dizhi dizhi) {
        Dizhi dizhi2 = dizhiMapper.selectByPrimaryKey(dizhi.getId());
        if (dizhi2 == null) {
            logger.warn("尝试删除地址，但是地址不存在");
            return Status.NOT_EXISTS;
        }
        if (dizhiMapper.deleteByPrimaryKey(dizhi.getId()) > 0 ) {
            logger.debug("删除地址成功" + dizhi.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Dizhi get(String id) {
        return dizhiMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }


    @Override
    public List<Dizhi> queryDizhi(String userId) {
        return dizhiMapper.queryDizhi(userId);
    }

    @Override
    public Dizhi queryByUserId(String userId) {
        return dizhiMapper.queryByUserId(userId);
    }
}
