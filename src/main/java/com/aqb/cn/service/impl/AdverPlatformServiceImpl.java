package com.aqb.cn.service.impl;

import com.aqb.cn.bean.AdverPlatform;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.AdverPlatformMapper;
import com.aqb.cn.service.AdverPlatformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/28.
 */
@Service
public class AdverPlatformServiceImpl implements AdverPlatformService {

    private Log logger = LogFactory.getLog(AdverPlatformServiceImpl.class);

    @Autowired
    private AdverPlatformMapper adverPlatformMapper;


    @Override
    public int add(AdverPlatform adverPlatform) {
        AdverPlatform adverPlatform_db = adverPlatformMapper.selectByPrimaryKey(adverPlatform.getId());
        if(adverPlatform_db == null) {
            if (adverPlatformMapper.insertSelective(adverPlatform) > 0) {
                logger.debug("添加公益广告" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(AdverPlatform adverPlatform) {
        AdverPlatform adverPlatform2 =adverPlatformMapper.selectByPrimaryKey(adverPlatform.getId());
        if(adverPlatform2 == null){
            logger.warn("尝试更新公益广告"  + " ,但是公益广告不存在");
            return Status.NOT_EXISTS;
        }
        if (adverPlatformMapper.updateByPrimaryKeySelective(adverPlatform) > 0) {
            logger.debug("更新公益广告成功" + adverPlatform2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(AdverPlatform adverPlatform) {
        AdverPlatform adverPlatform2 = adverPlatformMapper.selectByPrimaryKey(adverPlatform.getId());
        if (adverPlatform2 == null) {
            logger.warn("尝试删除公益广告，但是公益广告不存在");
            return Status.NOT_EXISTS;
        }
        if (adverPlatformMapper.deleteByPrimaryKey(adverPlatform.getId()) > 0 ) {
            logger.debug("删除公益广告成功" + adverPlatform.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public AdverPlatform get(String id) {
        return adverPlatformMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }


}
