package com.aqb.cn.service;

import com.aqb.cn.bean.AdverDate;
import com.aqb.cn.bean.Advertisement;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface AdvertisementService extends BaseService<Advertisement> {
    int addAdvertisement(Advertisement advertisement, List<AdverDate> adverDates);
    List<Advertisement> queryAdvertisement();
    List<Advertisement> queryByUserId(String userId);
    List<Advertisement> adminAdvertisement(Integer shstatus);
    List<Advertisement> adminAdvertisementstatus();
    List<Advertisement> adminAdvertisementrfStatus();
    List<Advertisement> adminAdvertisementstatus3();
}
