package com.imooc.controller.form.annotation;

import java.lang.annotation.*;
import java.lang.annotation.*;
import javax.validation.*;

@Constraint(validatedBy = ExistUserIDValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExistUserID {

	public String message() default "User not found!";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default{};

}