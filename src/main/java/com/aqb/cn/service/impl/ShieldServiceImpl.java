package com.aqb.cn.service.impl;

import java.util.List;

import com.aqb.cn.bean.Shield;
import com.aqb.cn.mapper.ShieldMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aqb.cn.bean.Shield;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.ShieldMapper;
import com.aqb.cn.service.ShieldService;
import com.aqb.cn.utils.UUIDCreator;

@Service
public class ShieldServiceImpl implements ShieldService {
	
	private Log logger = LogFactory.getLog(ShieldServiceImpl.class);

	@Autowired
	private ShieldMapper shieldMapper;


	@Override
	public int add(Shield shield) {
		Shield shield_db = shieldMapper.selectByGuanjianci(shield.getShieldGuanjianci());
		if(shield_db == null) {
			if (shieldMapper.insertSelective(shield) > 0) {
				logger.debug("添加屏蔽关键词" + "成功");
				return Status.SUCCESS;
			}
			return Status.ERROR;
		}
		return Status.EXISTS;
	}

	@Override
	public int update(Shield shield) {
		Shield shield2 =shieldMapper.selectByPrimaryKey(shield.getId());
		if(shield2 == null){
			logger.warn("尝试更新屏蔽关键词"  + " ,但是屏蔽关键词不存在");
			return Status.NOT_EXISTS;
		}
		if (shieldMapper.updateByPrimaryKeySelective(shield) > 0) {
			logger.debug("更新屏蔽关键词成功" + shield2.getId());
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	@Override
	public int delete(Shield shield) {
		Shield shield2 = shieldMapper.selectByPrimaryKey(shield.getId());
		if (shield2 == null) {
			logger.warn("尝试删除屏蔽关键词，但是屏蔽关键词不存在");
			return Status.NOT_EXISTS;
		}
		if (shieldMapper.deleteByPrimaryKey(shield.getId()) > 0 ) {
			logger.debug("删除屏蔽关键词成功" + shield.getId());
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	@Override
	public Shield get(String id) {
		return shieldMapper.selectByPrimaryKey(id);
	}

	@Override
	public void query(QueryBase queryBase) {
		queryBase.setResults(shieldMapper.queryShield(queryBase));
		queryBase.setTotalRow(shieldMapper.countShield(queryBase));

	}

	@Override
	public List<Shield> selectShieldList() {
		return shieldMapper.selectShieldList();
	}
}
