package com.imooc.service;

import java.util.Map;

public interface MsgService {

	public void createMsg(
			String fromUserId,
			String toUserId,
			Integer msgType,
			Map msgContent
			);
}
