package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import com.imooc.controller.form.annotation.ValidateString;

import lombok.Data;

@Data
public class UpdateVlogPrivateOrPublicForm {

	@NotBlank(message = "userId is required")
    private String userId;
	
	@NotBlank(message = "vlogId is required")
    private String vlogId;
	
	@ValidateString(acceptedValues={"NO", "YES"}, message="Invalid enum type")
	private String yesOrNo;
}
