package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Binding;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BindingMapper;
import com.aqb.cn.mapper.BindingNumberMapper;
import com.aqb.cn.service.BindingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
@Service
public class BindingServiceImpl implements BindingService{
    private Log logger = LogFactory.getLog(BindingServiceImpl.class);

    @Autowired
    private BindingMapper bindingMapper;
    @Autowired
    private BindingNumberMapper bindingNumberMapper;

    @Override
    public int add(Binding binding) {
        //验证该设备的成功绑定是否已经存在，如果存在，则不能进行重复绑定
        Binding Binding_db = bindingMapper.selectByStatus(binding.getBindingNumber());
        if(Binding_db == null) {
            if (bindingMapper.insertSelective(binding) > 0) {
                logger.debug("添加绑定设备设置" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Binding binding) {
        Binding binding2 =bindingMapper.selectByPrimaryKey(binding.getId());
        if(binding2 == null){
            logger.warn("尝试更新绑定设备设置"  + " ,但是绑定设备设置不存在");
            return Status.NOT_EXISTS;
        }
        if (bindingMapper.updateByPrimaryKeySelective(binding) > 0) {
            logger.debug("更新绑定设备设置成功" + binding2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Binding binding) {
        Binding binding2 = bindingMapper.selectByPrimaryKey(binding.getId());
        if (binding2 == null) {
            logger.warn("尝试删除绑定设备设置，但是绑定设备设置不存在");
            return Status.NOT_EXISTS;
        }
        if (bindingMapper.deleteByPrimaryKey(binding.getId()) > 0 ) {
            logger.debug("删除绑定设备设置成功" + binding.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Binding get(String id) {
        return bindingMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(bindingMapper.queryBinding(queryBase));
        queryBase.setTotalRow(bindingMapper.countBinding(queryBase));
    }

    @Override
    public Binding queryByUserId(String userId) {
        return bindingMapper.queryByUserId(userId);
    }

    @Override
    public Binding queryByUserId1(String userId) {
        return bindingMapper.queryByUserId1(userId);
    }

    @Override
    public List<Binding> queryByUserId2(String userId) {
        return bindingMapper.queryByUserId2(userId);
    }

    @Override
    public Binding queryByUserId3(String userId) {
        return bindingMapper.queryByUserId3(userId);
    }

    @Override
    public List<Binding> queryByUserId4(String userId) {
        return bindingMapper.queryByUserId4(userId);
    }

    @Override
    public Binding queryByBindingNumber(String bindingNumber) {
        return bindingMapper.queryByBindingNumber(bindingNumber);
    }

    @Override
    public List<Binding> adminBinding() {
        return bindingMapper.adminBinding();
    }

    @Override
    public void queryBindingAdmin(QueryBase queryBase) {
        queryBase.setResults(bindingNumberMapper.queryBindingAdmin(queryBase));
        queryBase.setTotalRow(bindingNumberMapper.countBindingAdmin(queryBase));
    }

    @Override
    public Binding selectByBN(String bindingNumber) {
        return bindingMapper.selectByBN(bindingNumber);
    }

    @Override
    public List<Binding> queryRelieveByUserId(String userId) {
        return bindingMapper.queryRelieveByUserId(userId);
    }

    @Override
    public List<Binding> queryUnRelieveByUserId(String userId) {
        return bindingMapper.queryUnRelieveByUserId(userId);
    }

    @Override
    public List<Binding> unboundAuditList() {
        return bindingMapper.unboundAudit();
    }
}
