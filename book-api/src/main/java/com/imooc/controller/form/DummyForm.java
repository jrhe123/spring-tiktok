package com.imooc.controller.form;

import com.imooc.controller.form.annotation.ColorValidation;
import com.imooc.controller.form.annotation.IpAddress;
import com.imooc.controller.form.annotation.OneNotNull;

import lombok.Data;

@Data
@OneNotNull(
	fields = {"username","email"},
    message = "Either username or email must be set"
)
public class DummyForm {

	@IpAddress
	private String ipAddress;
	
    @ColorValidation
    private String colorName;
    
	private String username;
    private String email;
	
}
