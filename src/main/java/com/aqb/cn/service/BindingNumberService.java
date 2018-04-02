package com.aqb.cn.service;

import com.aqb.cn.bean.BindingNumber;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface BindingNumberService extends BaseService<BindingNumber> {
    int addList(List<BindingNumber> bindingNumbers);
    BindingNumber selectByDeviceNumber(String number);
    List<BindingNumber> queryWhole();
}
