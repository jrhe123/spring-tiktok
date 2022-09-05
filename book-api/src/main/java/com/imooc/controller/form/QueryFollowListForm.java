package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ValidateString;

import lombok.Data;

@Data
public class QueryFollowListForm {

	@NotBlank(message = "myId is required")
    private String myId;
	
	@NotNull(message = "page is required")
	@Min(value = 1)
    private Integer page;
	
	@NotNull(message = "pageSize is required")
	@Min(value = 1)
    private Integer pageSize;
}
