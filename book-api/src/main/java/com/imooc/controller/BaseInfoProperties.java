package com.imooc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.imooc.utils.RedisOperator;

public class BaseInfoProperties {

	public static final String MOBILE_SMSCODE = "mobile:smscode";
	public static final String REDIS_USER_TOKEN = "redis_user_token";
	public static final String REDIS_USER_INFO = "redis_user_info";

	@Autowired
	public RedisOperator redis;
	
}
