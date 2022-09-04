package com.imooc.service;

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
}
