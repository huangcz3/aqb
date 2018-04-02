package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Repair;
import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.RepairCommentMapper;
import com.aqb.cn.mapper.RepairMapper;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.service.RepairService;
import com.aqb.cn.utils.GPSDistance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Service
public class RepairServiceImpl implements RepairService{
    private Log logger = LogFactory.getLog(RepairServiceImpl.class);

    @Autowired
    private RepairMapper repairMapper;
    @Autowired
    private RepairCommentMapper repairCommentMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public int add(Repair repair) {
        Repair repair_db = repairMapper.selectByPrimaryKey(repair.getId());
        if(repair_db == null) {
            if (repairMapper.insertSelective(repair) > 0) {
                logger.debug("添加商家" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Repair repair) {
        Repair repair2 =repairMapper.selectByPrimaryKey(repair.getId());
        if(repair2 == null){
            logger.warn("尝试更新商家"  + " ,但是商家不存在");
            return Status.NOT_EXISTS;
        }
        if (repairMapper.updateByPrimaryKeySelective(repair) > 0) {
            logger.debug("更新商家成功" + repair2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Repair repair) {
        Repair repair2 = repairMapper.selectByPrimaryKey(repair.getId());
        if (repair2 == null) {
            logger.warn("尝试删除商家，但是商家不存在");
            return Status.NOT_EXISTS;
        }
        if (repairMapper.deleteByPrimaryKey(repair.getId()) > 0 ) {
            logger.debug("删除商家成功" + repair.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Repair get(String id) {
        return repairMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {

        List<Repair> repairs = new ArrayList<>();

        if((int)queryBase.getParameters().get("status") == 1){
            repairs = repairMapper.queryRepair1(queryBase);
//            queryBase.setResults(repairMapper.queryRepair1(queryBase));
            queryBase.setTotalRow(repairMapper.countRepair1(queryBase));
        }else if((int)queryBase.getParameters().get("status") == 2){
            repairs = repairMapper.queryRepair2(queryBase);
            queryBase.setTotalRow(repairMapper.countRepair2(queryBase));
        }else if((int)queryBase.getParameters().get("status") == 3){
            repairs = repairMapper.queryRepair3(queryBase);
            queryBase.setTotalRow(repairMapper.countRepair3(queryBase));
        }else if((int)queryBase.getParameters().get("status") == 4){
            repairs = repairMapper.queryRepair4(queryBase);
            queryBase.setTotalRow(repairMapper.countRepair4(queryBase));
        }


        for(Repair repair : repairs){//计算距离
            double longitude1 = (double) queryBase.getParameter("longitude");//经度（前端传的）
            double latitude1 = (double) queryBase.getParameter("latitude");//纬度（前端传的）
            double longitude2 = repair.getRepairLongitude();
            double latitude2 = repair.getRepairLatitude();
            int distance = (int) GPSDistance.GetDistance(latitude1,longitude1,latitude2,longitude2);
            repair.setDistance(distance);

            List<RepairComment> repairComments = repairCommentMapper.queryByRepairId(repair.getId());
            repair.setRepairComments(repairComments);
            for (RepairComment repairComment:repairComments) {
                String userId = repairComment.getUserId();
                User user = userMapper.selectByPrimaryKey(userId);
                repairComment.setUser(user);
            }
        }
        queryBase.setResults(repairs);
    }


    @Override
    public void queryShop(QueryBase queryBase) {
        queryBase.setResults(repairMapper.queryShop(queryBase));
        queryBase.setTotalRow(repairMapper.countShop(queryBase));
    }

    @Override
    public Repair queryById(String id) {
        return repairMapper.queryById(id);
    }
}
