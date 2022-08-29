package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.utils.RedisOperator;

public class BaseInfoProperties {

	public static final String MOBILE_SMSCODE = "mobile:smscode";
	public static final String REDIS_USER_TOKEN = "redis_user_token";
	public static final String REDIS_USER_INFO = "redis_user_info";

	@Autowired
	public RedisOperator redis;
}
