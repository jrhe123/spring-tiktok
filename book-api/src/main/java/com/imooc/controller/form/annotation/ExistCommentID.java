package com.imooc.controller.form.annotation;

import java.lang.annotation.*;
import java.lang.annotation.*;
import javax.validation.*;

@Constraint(validatedBy = ExistCommentIDValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExistCommentID {

	public String message() default "Comment not found!";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default{};

}