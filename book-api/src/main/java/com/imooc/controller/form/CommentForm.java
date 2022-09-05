package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.imooc.controller.form.annotation.ExistCommentID;
import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ExistVlogID;

import lombok.Data;

@Data
public class CommentForm {

	@NotBlank(message = "vlogerId is required")
	@ExistUserID
	private String vlogerId;
	
	@ExistCommentID
	private String fatherCommentId = "0";
	
	@NotBlank(message = "vlogId is required")
	@ExistVlogID
	private String vlogId;
	
	@NotBlank(message = "commentUserId is required")
	@ExistUserID
	private String commentUserId;
	
	@NotBlank(message = "vlogerId is required")
	@Length(max = 50, message = "max length is 50")
	private String content;
}
