package com.imooc.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.bo.CommentBO;
import com.imooc.controller.form.CommentForm;
import com.imooc.controller.form.FollowVlogerForm;
import com.imooc.controller.form.QueryVlogCountForm;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.CommentService;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.CommentVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Comment controller")
@RestController
@RequestMapping("comment")
public class CommentController extends BaseInfoProperties{

	@Autowired
	private CommentService commentService;
	
	@PostMapping("create")
	public GraceJSONResult create(
			@RequestBody @Valid CommentForm form
			) {
		CommentBO commentBO = new CommentBO();
		BeanUtils.copyProperties(form, commentBO);
		
		CommentVO commentVO = commentService.createComment(commentBO);
		return GraceJSONResult.ok(commentVO);
	}
	
	@PostMapping("count")
	public GraceJSONResult count(
			@RequestBody @Valid QueryVlogCountForm form
			) {
		String countStr = redis.get(
				REDIS_VLOG_COMMENT_COUNTS + ":" + form.getVlogId()
			);
		if (StringUtils.isBlank(countStr)) {
			countStr = "0";
		}
		return GraceJSONResult.ok(Integer.valueOf(countStr));
	}

}
