package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ExistUserID;
import com.imooc.controller.form.annotation.ValidateString;

import lombok.Data;

@Data
public class QueryMessageListForm {

	@NotBlank(message = "userId is required")
	@ExistUserID
    private String userId;
	
	@NotNull(message = "page is required")
	@Min(value = 0)
    private Integer page;
	
	@NotNull(message = "pageSize is required")
	@Min(value = 1)
    private Integer pageSize;
}
