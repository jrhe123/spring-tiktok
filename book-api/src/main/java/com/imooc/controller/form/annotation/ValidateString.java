package com.imooc.controller.form.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ValidateStringValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ValidateString {

    String[] acceptedValues();

    String message() default "Enum type is invalid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { }; 
}
