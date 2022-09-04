package com.imooc.controller.form.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class ValidateStringValidator implements ConstraintValidator<ValidateString, String>{

    private List<String> valueList;

    @Override
    public void initialize(ValidateString constraintAnnotation) {
        valueList = new ArrayList<String>();
        for(String val : constraintAnnotation.acceptedValues()) {
            valueList.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
    	if (StringUtils.isNotEmpty(value)) {
    		return valueList.contains(value.toUpperCase());
    	} else {
    		return false;
    	}
    }

}