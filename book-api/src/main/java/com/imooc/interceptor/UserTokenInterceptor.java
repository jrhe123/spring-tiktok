package com.imooc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.imooc.controller.BaseInfoProperties;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.IPUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTokenInterceptor extends BaseInfoProperties implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// check userId & Token from headers
		String userId = request.getHeader("headerUserId");
		String userToken = request.getHeader("headerUserToken");
		if (StringUtils.isNoneBlank(userId) && StringUtils.isNoneBlank(userToken)) {
			String redisToken = redis.get(REDIS_USER_TOKEN + ":" + userId);
			if (StringUtils.isBlank(redisToken)) {
				GraceException.display(ResponseStatusEnum.UN_LOGIN);
				return false;
			} else {
				// allow only one device logged in
				if (!redisToken.equalsIgnoreCase(userToken)) {
					GraceException.display(ResponseStatusEnum.TICKET_INVALID);
					return false;
				}
			}
		} else {
			GraceException.display(ResponseStatusEnum.UN_LOGIN);
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
