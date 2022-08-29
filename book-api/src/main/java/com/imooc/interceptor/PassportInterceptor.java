package com.imooc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.imooc.controller.BaseInfoProperties;
import com.imooc.utils.IPUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// get ip addr
		String userIp = IPUtil.getRequestIp(request);
		// check key in redis
		boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);
		
		if (keyIsExist) {
			log.info("Cannot send sms twice in 60sec");
			return false;
		}
		
		// continue
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}

}
