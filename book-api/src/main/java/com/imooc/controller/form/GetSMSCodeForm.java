package com.imooc.controller.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class GetSMSCodeForm {
	
    @NotNull(message = "mobile is required")
    @Pattern(regexp="(^$|[0-9]{10})", message = "mobile is invalid")
    private String mobile;
    
}