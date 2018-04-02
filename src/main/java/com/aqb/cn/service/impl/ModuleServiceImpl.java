package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Module;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ModuleMapper;
import com.aqb.cn.service.ModuleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/11.
 */
@Service
public class ModuleServiceImpl implements ModuleService {
    
    private Log logger = LogFactory.getLog(ModuleServiceImpl.class);

    @Autowired
    private ModuleMapper moduleMapper;


    @Override
    public int add(Module module) {
        Module module_db = moduleMapper.selectByPrimaryKey(module.getId());
        if(module_db == null) {
            if (moduleMapper.insertSelective(module) > 0) {
                logger.debug("添加模块" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Module module) {
        Module module2 =moduleMapper.selectByPrimaryKey(module.getId());
        if(module2 == null){
            logger.warn("尝试更新模块"  + " ,但是模块不存在");
            return Status.NOT_EXISTS;
        }
        if (moduleMapper.updateByPrimaryKeySelective(module) > 0) {
            logger.debug("更新模块成功" + module2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Module module) {
        Module module2 = moduleMapper.selectByPrimaryKey(module.getId());
        if (module2 == null) {
            logger.warn("尝试删除模块，但是模块不存在");
            return Status.NOT_EXISTS;
        }
        if (moduleMapper.deleteByPrimaryKey(module.getId()) > 0 ) {
            logger.debug("删除模块成功" + module.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Module get(String id) {
        return moduleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }


}
