package com.imooc.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.base.MQConfig;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.SMSUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("demo")
@RefreshScope // <- dynamic load from nacos
public class DemoController {
	
	@Value("${nacos.counts}")
	private Integer nacosCounts; // define in nacos center
	
	@Autowired
    private SMSUtils smsUtils;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@GetMapping("nacosCounts")
	public GraceJSONResult nacosCounts() {
		return GraceJSONResult.ok(nacosCounts);
	}
	
	@GetMapping("test")
	public GraceJSONResult test() {
		smsUtils.sendSMS("6479291623", "123456");
		return GraceJSONResult.ok();
	}
	
	@GetMapping("producer")
	public GraceJSONResult producer() {
		// test mq
		rabbitTemplate.convertAndSend(
				MQConfig.EXCHANGE_MSG,
				"sys.msg.send",
				"this is my dummy message"
				);
		
		return GraceJSONResult.ok();
	}

}
