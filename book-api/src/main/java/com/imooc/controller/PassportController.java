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

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Passport api")
@RestController
@Validated
@RequestMapping("passport")
public class PassportController extends BaseController {

	@Autowired
	private SMSUtils smsUtils;
	
	@PostMapping("getSMSCode")
	public GraceJSONResult getSMSCode(
			@RequestBody @Valid GetSMSCodeForm form,
			HttpServletRequest request
			) {
		// get ip addr
		String userIp = IPUtil.getRequestIp(request);
		// sms in 60 sec
		redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
		
		// generate random code
		String code = (int)((Math.random() * 9 + 1) * 100000) + "";
		// send sms
		// smsUtils.sendSMS(form.getMobile(), code);
		log.info("!!!!!!!!! sms code: " + code);
		// save code into redis, timeout 30 min
		redis.set(MOBILE_SMSCODE + ":" + form.getMobile(), code, 30 * 60);
		// return
		return GraceJSONResult.ok();
	}
}
