package com.aqb.cn.service.impl;

import com.aqb.cn.bean.AdverDate;
import com.aqb.cn.bean.Advertisement;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.AdverDateMapper;
import com.aqb.cn.mapper.AdvertisementMapper;
import com.aqb.cn.service.AdvertisementService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private Log logger = LogFactory.getLog(AdvertisementServiceImpl.class);

    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private AdverDateMapper adverDateMapper;


    @Override
    public int add(Advertisement advertisement) {
        Advertisement advertisement_db = advertisementMapper.selectByPrimaryKey(advertisement.getId());
        if(advertisement_db == null) {
            if (advertisementMapper.insertSelective(advertisement) > 0) {
                logger.debug("添加广告" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Advertisement advertisement) {
        Advertisement advertisement2 =advertisementMapper.selectByPrimaryKey(advertisement.getId());
        if(advertisement2 == null){
            logger.warn("尝试更新广告"  + " ,但是广告不存在");
            return Status.NOT_EXISTS;
        }
        if (advertisementMapper.updateByPrimaryKeySelective(advertisement) > 0) {
            logger.debug("更新广告成功" + advertisement2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Advertisement advertisement) {
        Advertisement advertisement2 = advertisementMapper.selectByPrimaryKey(advertisement.getId());
        if (advertisement2 == null) {
            logger.warn("尝试删除广告，但是广告不存在");
            return Status.NOT_EXISTS;
        }
        if (advertisementMapper.deleteByPrimaryKey(advertisement.getId()) > 0 ) {
            logger.debug("删除广告成功" + advertisement.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Advertisement get(String id) {
        return advertisementMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(advertisementMapper.queryAdvertisement(queryBase));
        queryBase.setTotalRow(advertisementMapper.countAdvertisement(queryBase));

    }

    /**
     * 投放广告
     * @param advertisement
     * @param adverDates
     * @return
     */
    @Override
    public int addAdvertisement(Advertisement advertisement, List<AdverDate> adverDates) {
        Advertisement advertisement_db = advertisementMapper.selectByPrimaryKey(advertisement.getId());
        if (advertisement_db == null) {
            if (advertisementMapper.insertSelective(advertisement) > 0) {
                logger.debug("添加广告" + "成功");
                //添加广告的投放时间段
                Date date = new Date();
                for(AdverDate adverDate : adverDates){
                    adverDate.setId(UUIDCreator.getUUID());
                    adverDate.setFoundDate(date);
                    adverDate.setStatus(0);
                    adverDate.setAdvertisementId(advertisement.getId());
                    adverDateMapper.insertSelective(adverDate);
                }
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public List<Advertisement> queryAdvertisement() {
        return advertisementMapper.queryAdvertisementList();
    }

    @Override
    public List<Advertisement> queryByUserId(String userId) {
        return advertisementMapper.queryByUserId(userId);
    }

    @Override
    public List<Advertisement> adminAdvertisement(Integer shstatus) {
        return advertisementMapper.adminAdvertisement(shstatus);
    }

    @Override
    public List<Advertisement> adminAdvertisementstatus() {
        return advertisementMapper.adminAdvertisementstatus();
    }

    @Override
    public List<Advertisement> adminAdvertisementrfStatus() {
        return advertisementMapper.adminAdvertisementrfStatus();
    }

    @Override
    public List<Advertisement> adminAdvertisementstatus3() {
        return advertisementMapper.adminAdvertisementstatus3();
    }
}
