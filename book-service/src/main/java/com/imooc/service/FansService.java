package com.imooc.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imooc.utils.PagedGridResult;
import com.imooc.vo.VlogerVO;

public interface FansService {

	/**
	 * subscribe
	 */
	public void doFollow(String myId, String vlogerId);
	
	/**
	 * unsubscribe
	 */
	public void doCancel(String myId, String vlogerId);
	
	/**
	 * check follow status
	 */
	public boolean queryDoIFollowVloger(String myId, String vlogerId);
	
	/*
	 * list my follows by params
	 */
	public PagedGridResult queryMyFollows(
			String myId,
			Integer page,
			Integer pageSize
			);
	
	/*
	 * list my fans by params
	 */
	public PagedGridResult queryMyFans(
			String myId,
			Integer page,
			Integer pageSize
			);
}
