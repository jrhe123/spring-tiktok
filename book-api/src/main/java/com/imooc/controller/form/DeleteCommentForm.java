package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import com.imooc.controller.form.annotation.ExistCommentID;
import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ExistVlogID;

import lombok.Data;

@Data
public class DeleteCommentForm {

	@NotBlank(message = "commentId is required")
	@ExistCommentID
    private String commentId;
	
	@NotBlank(message = "commentUserId is required")
	@ExistUserID
    private String commentUserId;
	
	@NotBlank(message = "vlogId is required")
	@ExistVlogID
    private String vlogId;
}
