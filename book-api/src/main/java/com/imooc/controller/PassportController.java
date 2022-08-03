package com.imooc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.controller.form.GetSMSCodeForm;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("passport")
public class PassportController {

	@Autowired
	private SMSUtils smsUtils;
	
	@PostMapping("getSMSCode")
	public GraceJSONResult getSMSCode(
			@RequestBody @Valid GetSMSCodeForm form,
			HttpServletRequest request
			) {
		// get ip addr
		String userIp = IPUtil.getRequestIp(request);
		// TODO check redis (60sec)
		
		// generate random code
		String code = (int)((Math.random() * 9 + 1) * 100000) + "";
		// send sms
		// smsUtils.sendSMS(form.getMobile(), code);
		log.info("!!!!!!!!! sms code: " + code);
		// TODO save code into redis
		
		// return
		return GraceJSONResult.ok();
	}
}
