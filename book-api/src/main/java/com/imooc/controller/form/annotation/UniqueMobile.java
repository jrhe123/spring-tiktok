package com.imooc.controller.form.annotation;

import java.lang.annotation.*;
import java.lang.annotation.*;
import javax.validation.*;

@Constraint(validatedBy = UniqueMobileValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueMobile {

	public String message() default "There is already user with this mobile!";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default{};

}