package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class RegisterLoginForm {

    @NotBlank(message = "mobile is required")
    @Pattern(regexp="(^$|[0-9]{10})", message = "mobile is invalid")
    private String mobile;
	
    @NotBlank(message = "verifyCode is required")
	@Length(min = 6, max = 6, message = "verifyCode length must be 6")
    private String verifyCode;
}
