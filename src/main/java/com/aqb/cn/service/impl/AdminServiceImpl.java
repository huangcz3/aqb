package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Admin;
import com.aqb.cn.bean.AdminModule;
import com.aqb.cn.common.EncryptionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.AdminMapper;
import com.aqb.cn.mapper.AdminModuleMapper;
import com.aqb.cn.service.AdminService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private Log logger = LogFactory.getLog(AdminServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminModuleMapper adminModuleMapper;



    @Override
    public int add(Admin admin) {
        //员工用户名和工号唯一判定
        Admin admin_db = adminMapper.selectByAdminNumber(admin.getAdminNumber());
        Admin admin_db1 = adminMapper.selectByAdminName(admin.getAdminName());
        if(admin_db == null && admin_db1 == null) {
            String id = UUIDCreator.getUUID();
            admin.setId(id);
            admin.setStatus(1);//1--普通员工，0--超级管理员
            admin.setAdminPass(EncryptionUtil.encrypt(admin.getAdminPass()));//MD5加密
            Date date = new Date();
            admin.setFoundDate(date);
            admin.setAuthority1((byte) 0);
            admin.setAuthority2((byte) 0);
            admin.setAuthority3((byte) 0);
            admin.setAuthority4((byte) 0);
            admin.setAuthority5((byte) 0);
            admin.setAuthority6((byte) 0);
            admin.setAuthority7((byte) 0);
            if (adminMapper.insertSelective(admin) > 0) {
                logger.debug("添加员工" + "成功");
                return Status.SUCCESS;
                /*
                //为员工添加模块权限
                //首先查询出超级管理员的权限
                List<AdminModule> adminModules = adminModuleMapper.selectByAdminId("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                //在把超级管理员的权限修改成普通员工的权限，添加给普通员工
                for(AdminModule adminModule : adminModules){
                    adminModule.setId(UUIDCreator.getUUID());
                    adminModule.setAdminId(id);
                    adminModule.setStatus(0);//0--关闭，1--开启
                    adminModule.setFoundDate(date);
                }
                int i = adminModuleMapper.addAdminModuleList(adminModules);
                System.out.println(i);
                //系统预设置了10个权限模块
                if(i == 10){
                    logger.debug("添加员工的模块权限" + "成功");
                    return Status.SUCCESS;
                }
                //添加失败，回滚
                return Status.ERROR;*/
            }
            return Status.ERROR;
            
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Admin admin) {
        Admin admin2 =adminMapper.selectByPrimaryKey(admin.getId());
        if(admin2 == null){
            logger.warn("尝试更新员工"  + " ,但是员工不存在");
            return Status.NOT_EXISTS;
        }
        if (adminMapper.updateByPrimaryKeySelective(admin) > 0) {
            logger.debug("更新员工成功" + admin2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Admin admin) {
        Admin admin2 = adminMapper.selectByPrimaryKey(admin.getId());
        if (admin2 == null) {
            logger.warn("尝试删除员工，但是员工不存在");
            return Status.NOT_EXISTS;
        }
        //先把员工的模块权限删除，再删除员工
        logger.debug("删除员工的模块权限成功");
        if (adminMapper.deleteByPrimaryKey(admin.getId()) > 0 ) {
            logger.debug("删除员工成功" + admin.getId());
            return Status.SUCCESS;
        }
        //删除失败，回滚
        return Status.ERROR;
    }

    @Override
    public Admin get(String id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(adminMapper.queryAdmin(queryBase));
        queryBase.setTotalRow(adminMapper.countAdmin(queryBase));
    }

    @Override
    public int login(Admin admin) {
        Admin admin2 = adminMapper.queryByName(admin.getAdminName());
        if (admin2 == null) {
            return Status.NOT_EXISTS;
        }
        String encryptuserPass = EncryptionUtil.encrypt(admin.getAdminPass());

        if(!encryptuserPass.equals(admin2.getAdminPass())){
            return Status.PASSWD_NOT_MATCH;
        }
        return Status.SUCCESS;
    }

    @Override
    public Admin queryByName(String name) {
        return adminMapper.queryByName(name);
    }


}
