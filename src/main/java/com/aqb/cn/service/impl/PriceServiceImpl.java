package com.aqb.cn.service.impl;

import com.aqb.cn.bean.Price;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.PriceMapper;
import com.aqb.cn.service.PriceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Service
public class PriceServiceImpl implements PriceService {

    private Log logger = LogFactory.getLog(PriceServiceImpl.class);

    @Autowired
    private PriceMapper priceMapper;


    @Override
    public int add(Price price) {
        Price price_db = priceMapper.selectByPrimaryKey(price.getId());
        if(price_db == null) {
            if (priceMapper.insertSelective(price) > 0) {
                logger.debug("添加投放价格" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Price price) {
        Price price2 =priceMapper.selectByPrimaryKey(price.getId());
        if(price2 == null){
            logger.warn("尝试更新投放价格"  + " ,但是投放价格不存在");
            return Status.NOT_EXISTS;
        }
        if (priceMapper.updateByPrimaryKeySelective(price) > 0) {
            logger.debug("更新投放价格成功" + price2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Price price) {
        Price price2 = priceMapper.selectByPrimaryKey(price.getId());
        if (price2 == null) {
            logger.warn("尝试删除投放价格，但是投放价格不存在");
            return Status.NOT_EXISTS;
        }
        if (priceMapper.deleteByPrimaryKey(price.getId()) > 0 ) {
            logger.debug("删除投放价格成功" + price.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Price get(String id) {
        return priceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
    }

    @Override
    public List<Price> queryPrice(String priceCity) {
        return priceMapper.queryPrice(priceCity);
    }

    @Override
    public Price selectJiage() {
        return priceMapper.selectJiage();
    }

}
