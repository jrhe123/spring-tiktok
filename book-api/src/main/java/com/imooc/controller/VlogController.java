package com.imooc.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.bo.VlogBO;
import com.imooc.controller.form.PublishVlogForm;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.VlogService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Vlog controller")
@RestController
@RequestMapping("vlog")
public class VlogController extends BaseInfoProperties {
	
	@Autowired
    private VlogService vlogService;
	
	@PostMapping("publish")
	public GraceJSONResult publish(
			@RequestBody @Valid PublishVlogForm form
			) {
		
		VlogBO vlogBO = new VlogBO();
		BeanUtils.copyProperties(form, vlogBO);
		vlogService.createVlog(vlogBO);
		
		return GraceJSONResult.ok();
	}
}
