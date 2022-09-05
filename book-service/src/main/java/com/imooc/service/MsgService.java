package com.imooc.service;

import java.util.List;
import java.util.Map;

import com.imooc.mo.MessageMO;

public interface MsgService {

	public void createMsg(
			String fromUserId,
			String toUserId,
			Integer msgType,
			Map msgContent
			);
	
	public List<MessageMO> queryList(
			String toUserId,
			Integer page,
			Integer pageSize
		);
}
