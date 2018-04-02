package com.aqb.cn.service.impl;

import com.aqb.cn.bean.BindingNumber;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BindingNumberMapper;
import com.aqb.cn.service.BindingNumberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
@Service
public class BindingNumberServiceImpl implements BindingNumberService {

    private Log logger = LogFactory.getLog(BindingNumberServiceImpl.class);

    @Autowired
    private BindingNumberMapper bindingNumberMapper;


    @Override
    public int add(BindingNumber bindingNumber) {
        BindingNumber bindingNumber_db = bindingNumberMapper.selectByPrimaryKey(bindingNumber.getId());
        if(bindingNumber_db == null) {
            if (bindingNumberMapper.insertSelective(bindingNumber) > 0) {
                logger.debug("添加设备" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(BindingNumber bindingNumber) {
        BindingNumber bindingNumber2 =bindingNumberMapper.selectByPrimaryKey(bindingNumber.getId());
        if(bindingNumber2 == null){
            logger.warn("尝试更新设备"  + " ,但是设备不存在");
            return Status.NOT_EXISTS;
        }
        if (bindingNumberMapper.updateByPrimaryKeySelective(bindingNumber) > 0) {
            logger.debug("更新设备成功" + bindingNumber2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(BindingNumber bindingNumber) {
        BindingNumber bindingNumber2 = bindingNumberMapper.selectByPrimaryKey(bindingNumber.getId());
        if (bindingNumber2 == null) {
            logger.warn("尝试删除设备，但是设备不存在");
            return Status.NOT_EXISTS;
        }
        if (bindingNumberMapper.deleteByPrimaryKey(bindingNumber.getId()) > 0 ) {
            logger.debug("删除设备成功" + bindingNumber.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public BindingNumber get(String id) {
        return bindingNumberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    /**
     * 导入
     * @param bindingNumbers
     * @return
     */
    @Override
    public int addList(List<BindingNumber> bindingNumbers) {
        //设备号验证
        for(int i = 0;i<bindingNumbers.size();i++){
            BindingNumber bindingNumber = bindingNumbers.get(i);
            BindingNumber bindingNumber_db = bindingNumberMapper.selectByDeviceNumber(bindingNumber.getDeviceNumber());
            if(bindingNumber_db != null ||  //说明该设备号已存在
                    bindingNumber.getDeviceNumber() == null ||
                    bindingNumber.getDeviceNumber().equals("")){
                bindingNumbers.remove(bindingNumber);
                i--;
            }
        }
        return bindingNumberMapper.insertBindingNumberList(bindingNumbers);
    }

    @Override
    public BindingNumber selectByDeviceNumber(String number) {
        return bindingNumberMapper.selectByDeviceNumber(number);
    }

    @Override
    public List<BindingNumber> queryWhole() {
        return bindingNumberMapper.queryWhole();
    }
}
