package com.imooc.controller.form.annotation;

import java.util.Arrays;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class OneNotNullValidator implements ConstraintValidator<OneNotNull, Object> {
    private String[] fields;

    @Override
    public void initialize(final OneNotNull combinedNotNull) {
        fields = combinedNotNull.fields();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
        
        int check = (int) Arrays.stream(fields)
                .map(beanWrapper::getPropertyValue)
                .filter(Objects::isNull)
                .count();
                
        return check == fields.length - 1;
    }
}
