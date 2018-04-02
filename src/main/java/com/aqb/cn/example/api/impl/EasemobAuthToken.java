package com.aqb.cn.example.api.impl;

import com.aqb.cn.example.api.AuthTokenAPI;
import com.aqb.cn.example.comm.TokenUtil;


public class EasemobAuthToken implements AuthTokenAPI{

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
