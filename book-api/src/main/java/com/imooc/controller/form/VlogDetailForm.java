package com.imooc.controller.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VlogDetailForm {
	
	@NotNull(message = "vlogId is required")
	private String vlogId;
}
