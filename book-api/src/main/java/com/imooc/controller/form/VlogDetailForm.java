package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ExistUserID;

import lombok.Data;

@Data
public class VlogDetailForm {
	
	@NotBlank(message = "userId is required")
	@ExistUserID
    private String userId;
	
	@NotNull(message = "vlogId is required")
	private String vlogId;
}
