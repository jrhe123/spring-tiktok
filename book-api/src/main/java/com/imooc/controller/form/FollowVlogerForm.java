package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ValidateString;

import lombok.Data;

@Data
public class FollowVlogerForm {

	@NotBlank(message = "myId is required")
	@ExistUserID
    private String myId;
	
	@NotBlank(message = "vlogerId is required")
	@ExistUserID
    private String vlogerId;
}
