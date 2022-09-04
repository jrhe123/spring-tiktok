package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VlogIndexListForm {
	
	private String search;

	@NotNull(message = "page is required")
	@Min(value = 1)
    private Integer page;
	
	@NotNull(message = "pageSize is required")
	@Min(value = 1)
    private Integer pageSize;
}
