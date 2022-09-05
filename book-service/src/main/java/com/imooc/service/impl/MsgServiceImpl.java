package com.imooc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.imooc.base.BaseInfoProperties;
import com.imooc.enums.MessageEnum;
import com.imooc.mo.MessageMO;
import com.imooc.pojo.Users;
import com.imooc.repository.MessageRepository;
import com.imooc.service.MsgService;
import com.imooc.service.UserService;

@Service
public class MsgServiceImpl extends BaseInfoProperties implements MsgService {
	
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

	@Override
	public List<MessageMO> queryList(
			String toUserId,
			Integer page,
			Integer pageSize
		) {
		
		Pageable pageable = PageRequest.of(
				page,
				pageSize,
				Sort.Direction.DESC,
				"createTime"
			);
		List<MessageMO> list = messageRepository.findAllByToUserIdOrderByCreateTimeDesc(
				toUserId,
				pageable
			);
		for(MessageMO msg : list) {
			// if it's follow message, check whether follow before
			if (msg.getMsgType() != null &&
					msg.getMsgType() == MessageEnum.FOLLOW_YOU.type) {
				Map map = msg.getMsgContent();
				if (map == null) {
					map = new HashMap();
				}
				
				String relationship = redis.get(
						REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + msg.getToUserId() + ":" + msg.getFromUserId()
					);
				if (StringUtils.isNoneBlank(relationship) && relationship.equalsIgnoreCase("1")) {
					map.put("isFriend", true);
				} else {
					map.put("isFriend", false);
				}
				msg.setMsgContent(map);
			}
		}
		
		return list;
	}

}
