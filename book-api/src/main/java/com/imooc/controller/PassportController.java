package com.imooc.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.controller.form.GetSMSCodeForm;
import com.imooc.controller.form.LogoutForm;
import com.imooc.controller.form.RegisterLoginForm;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.UsersVO;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Passport api")
@RestController
@Validated
@RequestMapping("passport")
public class PassportController extends BaseInfoProperties {

	@Autowired
	private SMSUtils smsUtils;
	
	@Autowired
	private UserService userService;
	
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
		log.info("sms code: " + code);
		// save code into redis, timeout 30 min
		redis.set(MOBILE_SMSCODE + ":" + form.getMobile(), code, 30 * 60);
		// return
		return GraceJSONResult.ok();
	}
	
	
	@PostMapping("login")
	public GraceJSONResult login(
			@RequestBody @Valid RegisterLoginForm form
			){
		String mobile = form.getMobile();
		String code = form.getVerifyCode();
		
		// 1. validate code
		String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
		if (StringUtils.isBlank(redisCode) || !code.equalsIgnoreCase(redisCode)) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
		}
		
		// 2. check user exist
		Users user = userService.queryMobileIsExist(mobile);
		if (user == null) {
			user = userService.createUser(mobile);
		}
		
		// 3. token
		String uToken = UUID.randomUUID().toString();
		redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
		
		// 4. delete redis code
		redis.del(MOBILE_SMSCODE + ":" + mobile);
		
		// 5. prepare response
		UsersVO usersVO = new UsersVO();
		BeanUtils.copyProperties(user, usersVO);
		usersVO.setUserToken(uToken);
		
		return GraceJSONResult.ok(usersVO);
	}
	
	
	@PostMapping("logout")
	public GraceJSONResult logout(
			@RequestBody @Valid LogoutForm form,
			HttpServletRequest request
			) {
		// check user
		Users user = userService.queryUserID(form.getUserId());
		if (user == null) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
		}
		
		// remove token
		redis.del(REDIS_USER_TOKEN + ":" + form.getUserId());
		
		return GraceJSONResult.ok();
	}
}
