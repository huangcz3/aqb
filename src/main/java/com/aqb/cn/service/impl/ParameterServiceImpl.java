package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Parameter;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ParameterMapper;
import com.aqb.cn.service.ParameterService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
@Service
public class ParameterServiceImpl implements ParameterService {
    private Log logger = LogFactory.getLog(ParameterServiceImpl.class);

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public int add(Parameter parameter) {
        Parameter parameter_db = parameterMapper.selectByPrimaryKey(parameter.getId());
        if(parameter_db == null) {
            if (parameterMapper.insertSelective(parameter) > 0) {
                logger.debug("添加其他设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Parameter parameter) {
        Parameter parameter2 =parameterMapper.selectByPrimaryKey(parameter.getId());
        if(parameter2 == null){
            logger.warn("尝试更新其他设置"  + " ,但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (parameterMapper.updateByPrimaryKeySelective(parameter) > 0) {
            logger.debug("更新其他设置成功" + parameter2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Parameter parameter) {
        Parameter parameter2 = parameterMapper.selectByPrimaryKey(parameter.getId());
        if (parameter2 == null) {
            logger.warn("尝试删除其他设置，但是其他设置不存在");
            return Status.NOT_EXISTS;
        }
        if (parameterMapper.deleteByPrimaryKey(parameter.getId()) > 0 ) {
            logger.debug("删除其他设置成功" + parameter.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Parameter get(String id) {
        return parameterMapper.selectByPrimaryKey(Integer.valueOf(id));
    }

    @Override
    public void query(QueryBase queryBase) {
    }


    @Override
    public Parameter queryPopPara() {
        return parameterMapper.queryPopPara();
    }
}
