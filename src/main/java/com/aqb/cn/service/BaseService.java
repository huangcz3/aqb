package com.aqb.cn.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.aqb.cn.common.QueryBase;


public interface BaseService<T> {

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public int add(T obj);

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public int update(T obj);

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public int delete(T obj);

	public T get(String id);

	public void query(QueryBase queryBase);

}
