package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Parameter;
import com.aqb.cn.bean.PopAdv;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ParameterMapper;
import com.aqb.cn.mapper.PopAdvMapper;
import com.aqb.cn.pojo.PopAndPara;
import com.aqb.cn.service.PopAdvService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
@Service
public class PopAdvServiceImpl implements PopAdvService {
    private Log logger = LogFactory.getLog(PopAdvServiceImpl.class);

    @Autowired
    private PopAdvMapper popAdvMapper;
    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public int add(PopAdv popAdv) {
        PopAdv popAdv_db = popAdvMapper.selectByPrimaryKey(popAdv.getId());
        if (popAdv_db == null){
            if (popAdvMapper.insertSelective(popAdv) > 0){
                logger.debug("添加弹窗广告成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(PopAdv popAdv) {
        PopAdv popAdv_db = popAdvMapper.selectByPrimaryKey(popAdv.getId());
        if (popAdv_db == null){
            logger.warn("尝试更新弹窗广告，但是弹窗广告不存在");
            return Status.NOT_EXISTS;
        }
        if (popAdvMapper.updateByPrimaryKeySelective(popAdv) > 0){
            logger.debug("更新弹窗广告成功" + popAdv_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(PopAdv popAdv) {
        PopAdv popAdv_db = popAdvMapper.selectByPrimaryKey(popAdv.getId());
        if (popAdv_db == null) {
            logger.warn("尝试删除弹窗广告，但是弹窗广告不在");
            return Status.NOT_EXISTS;
        }
        if (popAdvMapper.deleteByPrimaryKey(popAdv.getId()) > 0) {
            logger.debug("尝试删除弹窗广告成功" + popAdv_db.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public PopAdv get(String id) {
        return popAdvMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        queryBase.setResults(popAdvMapper.queryPopAdv());
        queryBase.setTotalRow(popAdvMapper.countPopAdv());
    }

    @Override
    public PopAndPara queryPopAdv() {
        //从参数表取出弹窗广告开关设置的参数
        PopAndPara popAndPara = new PopAndPara();
        Parameter para = parameterMapper.queryPopPara();
        Integer status = para.getStatus();//0-关闭弹窗，1-开启弹窗
        popAndPara.setParaId(String.valueOf(para.getId()));
        if (status == 0){
            popAndPara.setOnOff_para(false);
        }
        if (status == 1){
            popAndPara.setOnOff_para(true);
        }
        popAndPara.setPopAdv(popAdvMapper.queryPopAdv());
        return popAndPara;
    }

    @Override
    public PopAndPara queryappPopAdv() {
        //从参数表取出弹窗广告开关设置的参数
        PopAndPara popAndPara = new PopAndPara();
        Parameter para = parameterMapper.queryPopPara();
        Integer status = para.getStatus();//0-关闭弹窗，1-开启弹窗
        popAndPara.setParaId(String.valueOf(para.getId()));
        if (status == 0){
            popAndPara.setOnOff_para(false);
        }
        if (status == 1){
            popAndPara.setOnOff_para(true);
        }
        List<PopAdv> popAdvList = popAdvMapper.queryPopAdv();
        for (int i = 0;i < popAdvList.size();i++){
            if (popAdvList.get(i).getStatus()==0){
                popAdvList.remove(i);
                i--;
            }
        }
        popAndPara.setPopAdv(popAdvList);
        return popAndPara;
    }
}
