package com.imooc.service;

import com.imooc.pojo.Users;

public interface UserService {

	/**
	 * check user exists, return User if exists
	 */
	public Users queryMobileIsExist(String mobile);
	
	/*
	 * create user
	 */
	public Users createUser(String mobile);
	
	/*
	 * search user by id
	 */
	public Users queryUserID(String userId);
}
