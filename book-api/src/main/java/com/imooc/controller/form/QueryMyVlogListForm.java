package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.imooc.controller.form.annotation.ValidateString;

import lombok.Data;

@Data
public class QueryMyVlogListForm {

	@NotBlank(message = "userId is required")
    private String userId;
	
	@ValidateString(acceptedValues={"NO", "YES"}, message="Invalid enum type")
	private String yesOrNo;
	
	@NotNull(message = "page is required")
	@Min(value = 1)
    private Integer page;
	
	@NotNull(message = "pageSize is required")
	@Min(value = 1)
    private Integer pageSize;
}
