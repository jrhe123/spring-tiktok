package com.imooc;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imooc.base.MQConfig;
import com.imooc.enums.MessageEnum;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.mo.MessageMO;
import com.imooc.service.MsgService;
import com.imooc.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMQConsumer {

	@Autowired
	private MsgService msgService;

	@RabbitListener(queues = { MQConfig.QUEUE_SYS_MSG }) // can add multi queue
	public void watchQueue(String payload, Message message) {

		String routingKey = message.getMessageProperties().getReceivedRoutingKey();

		System.out.println("!!!!!!!: routingKey" + routingKey);
		System.out.println("!!!!!!!: payload" + payload);
		System.out.println("!!!!!!!");
		System.out.println("!!!!!!!");
		System.out.println("!!!!!!!");
		System.out.println("!!!!!!!");

		MessageMO messageMO = JsonUtils.jsonToPojo(payload, MessageMO.class);

		if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.FOLLOW_YOU.type)) {
			msgService.createMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.FOLLOW_YOU.type, null);
		} else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.LIKE_VLOG.type)) {
			msgService.createMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.LIKE_VLOG.type,
					messageMO.getMsgContent());
		} else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.COMMENT_VLOG.type)) {
			msgService.createMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.COMMENT_VLOG.type,
					messageMO.getMsgContent());
		} else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.REPLY_YOU.type)) {
			msgService.createMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.REPLY_YOU.type,
					messageMO.getMsgContent());
		} else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.LIKE_COMMENT.type)) {
			msgService.createMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.LIKE_COMMENT.type,
					messageMO.getMsgContent());
		} else if (routingKey.equalsIgnoreCase("sys.msg.del" + MessageEnum.FOLLOW_YOU.type)) {
			msgService.deleteMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.FOLLOW_YOU.type);
		} else if (routingKey.equalsIgnoreCase("sys.msg.del" + MessageEnum.LIKE_VLOG.type)) {
			msgService.deleteMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.LIKE_VLOG.type);
		} else if (routingKey.equalsIgnoreCase("sys.msg.del" + MessageEnum.COMMENT_VLOG.type)) {
			msgService.deleteMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.COMMENT_VLOG.type);
		} else if (routingKey.equalsIgnoreCase("sys.msg.del" + MessageEnum.REPLY_YOU.type)) {
			msgService.deleteMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.REPLY_YOU.type);
		} else if (routingKey.equalsIgnoreCase("sys.msg.del" + MessageEnum.LIKE_COMMENT.type)) {
			msgService.deleteMsg(messageMO.getFromUserId(), messageMO.getToUserId(), MessageEnum.LIKE_COMMENT.type);
		} else {
			GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
		}
	}

}
