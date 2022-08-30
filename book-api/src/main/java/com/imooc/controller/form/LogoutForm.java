package com.imooc.controller.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LogoutForm {

    @NotBlank(message = "userId is required")
    private String userId;
}
