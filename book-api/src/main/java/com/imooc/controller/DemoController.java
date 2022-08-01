package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.grace.result.GraceJSONResult;

@RestController
@RequestMapping("demo")
public class DemoController {
	
	@GetMapping("test")
	public GraceJSONResult test() {
		return GraceJSONResult.ok();
	}

}
