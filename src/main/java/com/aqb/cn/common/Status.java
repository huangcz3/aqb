package com.aqb.cn.common;


public class Status {

	public static final short SUCCESS = 0;//成功

	public static final short ERROR = 1;//失败

	public static final short NO_REMOVE = 2;//

	public static final short NO_DELETE = 3;//不能删除

	public static final short NO_UPDATE = 4;//

	public static final short LOCK = 5;//不能删除自己

	public static final short EXISTS = 6;//已存在

	public static final short NOT_EXISTS = 7;//不存在

	public static final short VERSION_NOT_MATCH = 8;//

	public static final short PASSWD_NOT_MATCH = 9;//密码错误

	public static final short CODE_NOT_MATCH = 10;//验证码错误

	public static final short BLOCKED = 11;//

	public static final short NO_ENOUGH = 12;//余额不足

	public static final short SESSION_GUOQI = 14;//登录失效

	public static final short SESSION_CHONGFU = 15;//有人重复登录

	/**
	 * 验证码为空
	 */
	public static final short VALIDATE_CODE_EMPTY = 12;

	/**
	 * 非法操作
	 */
	public static final short INVALID_OPERATION = 13;

}
