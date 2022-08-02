package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.SMSUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {
	
	@Autowired
    private SMSUtils smsUtils;
	
	@GetMapping("test")
	public GraceJSONResult test() {
		smsUtils.sendSMS("6479291623", "123456");
		return GraceJSONResult.ok();
	}

}
