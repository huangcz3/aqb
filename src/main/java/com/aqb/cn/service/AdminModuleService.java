package com.aqb.cn.service;

import com.aqb.cn.bean.AdminModule;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public interface AdminModuleService extends BaseService<AdminModule> {
    List<AdminModule> queryByAdminId(String adminId);
    int updateAppointAdmin(List<AdminModule> adminModules);
}
