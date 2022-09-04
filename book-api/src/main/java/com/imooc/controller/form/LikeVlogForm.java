package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ExistVlogID;

import lombok.Data;

@Data
public class LikeVlogForm {

	@NotBlank(message = "userId is required")
	@ExistUserID
    private String userId;
	
	@NotBlank(message = "vlogerId is required")
	@ExistUserID
    private String vlogerId;
	
	@NotBlank(message = "vlogId is required")
	@ExistVlogID
    private String vlogId;
}
