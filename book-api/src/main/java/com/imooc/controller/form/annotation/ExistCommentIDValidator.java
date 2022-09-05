package com.imooc.controller.form.annotation;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.pojo.Comment;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.UserService;
import com.imooc.service.VlogService;

public class ExistCommentIDValidator implements ConstraintValidator<ExistCommentID, String> {

	@Autowired
	private CommentService commentService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Comment comment = null;
		
		if (value != null) {
			if (value.equalsIgnoreCase("0")) {
				// if it;s null, use default "0"
				return true;
			}
			comment = commentService.queryCommentID(value);
		}
		return value != null && comment != null;
	}
	
}