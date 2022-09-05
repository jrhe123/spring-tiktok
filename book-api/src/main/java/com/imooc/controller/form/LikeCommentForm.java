package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import com.imooc.controller.form.annotation.ExistCommentID;
import com.imooc.controller.form.annotation.ExistUserID;

import lombok.Data;

@Data
public class LikeCommentForm {

	@NotBlank(message = "userId is required")
	@ExistUserID
    private String userId;
	
	@NotBlank(message = "commentId is required")
	@ExistCommentID
    private String commentId;
}
