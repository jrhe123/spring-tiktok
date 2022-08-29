package com.imooc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.imooc.interceptor.PassportInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Bean
	public PassportInterceptor passportInterceptor () {
		return new PassportInterceptor();
	};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(passportInterceptor())
			.addPathPatterns("/passport/getSMSCode");
	}
}
