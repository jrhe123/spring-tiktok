package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ExistVlogID;

import lombok.Data;

@Data
public class CountVlogLikeForm {

	@NotBlank(message = "vlogId is required")
	@ExistVlogID
    private String vlogId;
}
