package com.imooc.controller.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.imooc.controller.form.annotation.OneNotNull;

import lombok.Data;

@Data
@OneNotNull(
	fields = {"nickname", "imoocNum", "sex", "birthday", "description", "country", "province", "city", "district"},
    message = "Either nickname or imoocNum or sex or birthday or description or country or province or city or district must be set"
)
public class UpdateUserForm {
	
	@NotNull(message = "type is required")
	@Range(min = 1, max = 6, message = "must be in range of 1 - 6")
	private Integer type;

	@NotBlank(message = "id is required")
    private String id;
	
	// at least one
    private String nickname;
    
    private String imoocNum;
    
    private Integer sex;
    
    private Date birthday;
    
    private String description;
    
    private String country;
    
    private String province;
    
    private String city;
    
    private String district;
    
    //
    private String face;
    
    private String bgImg;
    
    private Integer canImoocNumBeUpdated;
}
