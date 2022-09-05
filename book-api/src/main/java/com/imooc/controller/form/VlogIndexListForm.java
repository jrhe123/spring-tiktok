package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ExistUserID;

import lombok.Data;

@Data
public class VlogIndexListForm {
	
	@NotBlank(message = "userId is required")
	@ExistUserID
	private String userId;
	
	private String search;

	@NotNull(message = "page is required")
	@Min(value = 1)
    private Integer page;
	
	@NotNull(message = "pageSize is required")
	@Min(value = 1)
    private Integer pageSize;
}
