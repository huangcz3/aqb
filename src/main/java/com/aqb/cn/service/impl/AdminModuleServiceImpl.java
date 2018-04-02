package com.aqb.cn.service.impl;

import com.aqb.cn.bean.AdminModule;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.AdminModuleMapper;
import com.aqb.cn.service.AdminModuleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@Service
public class AdminModuleServiceImpl implements AdminModuleService {

    private Log logger = LogFactory.getLog(AdminModuleServiceImpl.class);

    @Autowired
    private AdminModuleMapper adminModuleMapper;

    @Override
    public List<AdminModule> queryByAdminId(String adminId) {
        return adminModuleMapper.selectByAdminId(adminId);
    }

    @Override
    public int updateAppointAdmin(List<AdminModule> adminModules) {
        //循环更改
        for(AdminModule adminModule : adminModules){
            if(adminModule.getId() != null && !adminModule.getId().equals("") &&
            adminModule.getStatus() != null && !adminModule.getStatus().equals("")){
                if(adminModuleMapper.updateByPrimaryKeySelective(adminModule) > 0){
                    logger.debug("修改成功");
                }
            }
        }
        return Status.SUCCESS;
    }

    @Override
    public int add(AdminModule adminModule) {
        AdminModule adminModule1 = adminModuleMapper.selectByPrimaryKey(adminModule.getId());
        if (adminModule1==null){
            if (adminModuleMapper.insertSelective(adminModule)>0){
                logger.debug("添加后台管理模块成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(AdminModule obj) {
        return 0;
    }

    @Override
    public int delete(AdminModule adminModule) {
        AdminModule adminModule1 = adminModuleMapper.selectByPrimaryKey(adminModule.getId());
        if (adminModule == null){
            logger.debug("尝试删除后台管理模块，但是后台管理模块不存在");
            return Status.NOT_EXISTS;
        }
        if (adminModuleMapper.deleteByPrimaryKey(adminModule.getId()) > 0 ){
            logger.debug("删除后台管理模块成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public AdminModule get(String id) {
        return adminModuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

    }
}
