package com.imooc.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.mo.MessageMO;
import com.imooc.pojo.Users;
import com.imooc.repository.MessageRepository;
import com.imooc.service.MsgService;
import com.imooc.service.UserService;

@Service
public class MsgServiceImpl implements MsgService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public void createMsg(
			String fromUserId,
			String toUserId,
			Integer msgType,
			Map msgContent
		) {
		
		Users fromUser = userService.queryUserID(fromUserId);
		
		MessageMO messageMO = new MessageMO();
		messageMO.setFromUserId(fromUserId);
		messageMO.setFromNickname(fromUser.getNickname());
		messageMO.setFromFace(fromUser.getFace());
		
		messageMO.setToUserId(toUserId);
		messageMO.setMsgType(msgType);
		if (msgContent != null) {
			messageMO.setMsgContent(msgContent);
		}
		messageMO.setCreateTime(new Date());
		
		messageRepository.save(messageMO);
	}

}
