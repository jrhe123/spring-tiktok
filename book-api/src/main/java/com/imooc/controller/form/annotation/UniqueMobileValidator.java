package com.imooc.controller.form.annotation;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;

public class UniqueMobileValidator implements ConstraintValidator<UniqueMobile, String> {

	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Users user = null;
		if (value != null) {
			user = userService.queryMobileIsExist(value);
		}
		return value != null && user != null;
	}
	
}