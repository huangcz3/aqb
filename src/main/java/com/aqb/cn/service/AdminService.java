package com.aqb.cn.service;

import com.aqb.cn.bean.Admin;

/**
 * Created by Administrator on 2017/5/10.
 */
public interface AdminService extends BaseService<Admin> {

    int login(Admin admin);
    Admin queryByName(String name);

}
