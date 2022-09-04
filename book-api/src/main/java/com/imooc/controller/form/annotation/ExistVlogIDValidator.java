package com.imooc.controller.form.annotation;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.UserService;
import com.imooc.service.VlogService;

public class ExistVlogIDValidator implements ConstraintValidator<ExistVlogID, String> {

	@Autowired
	private VlogService vlogService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Vlog vlog = null;
		if (value != null) {
			vlog = vlogService.queryVlogID(value);
		}
		return value != null && vlog != null;
	}
	
}