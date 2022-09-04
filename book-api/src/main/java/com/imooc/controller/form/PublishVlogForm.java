package com.imooc.controller.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.imooc.controller.form.annotation.ExistUserID;

import lombok.Data;

@Data
public class PublishVlogForm {

	@NotNull(message = "vlogerId is required")
	@ExistUserID
    private String vlogerId;
	
	@NotNull(message = "url is required")
	@Pattern(
			regexp="^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
			message = "url is invalid"
	)
    private String url;
	
	@NotNull(message = "cover is required")
	@Pattern(
			regexp="^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
			message = "cover is invalid"
	)
    private String cover;
	
	@NotNull(message = "title is required")
    private String title;
	
	@NotNull(message = "width is required")
	@Min(value = 1)
    private Integer width;
	
	@NotNull(message = "height is required")
	@Min(value = 1)
    private Integer height;
}
