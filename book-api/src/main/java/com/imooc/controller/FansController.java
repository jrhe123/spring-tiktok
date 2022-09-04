package com.imooc.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.bo.VlogBO;
import com.imooc.controller.form.FollowVlogerForm;
import com.imooc.controller.form.PublishVlogForm;
import com.imooc.controller.form.QueryMyVlogListForm;
import com.imooc.controller.form.UpdateVlogPrivateOrPublicForm;
import com.imooc.controller.form.VlogDetailForm;
import com.imooc.controller.form.VlogIndexListForm;
import com.imooc.enums.YesOrNo;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.service.FansService;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Fans controller")
@RestController
@RequestMapping("fans")
public class FansController extends BaseInfoProperties {
	
	@Autowired
    private FansService fansService;
	
	@PostMapping("follow")
	public GraceJSONResult follow(
			@RequestBody @Valid FollowVlogerForm form
			) {
		
		if (form.getMyId().equalsIgnoreCase(form.getVlogerId())) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
		}
		// do follow
		fansService.doFollow(form.getMyId(), form.getVlogerId());
		
		// vloger fans + 1, my follows + 1
		redis.increment(
				REDIS_MY_FANS_COUNTS + ":" + form.getVlogerId(),
				1
			);
		redis.increment(
				REDIS_MY_FOLLOWS_COUNTS + ":" + form.getMyId(), 
				1
			);
		// fans relation save into redis
		redis.set(
				REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + form.getMyId() + ":" + form.getVlogerId(),
				"1"
			);
		
		return GraceJSONResult.ok();
	}
	
	
	@PostMapping("cancel")
	public GraceJSONResult cancel(
			@RequestBody @Valid FollowVlogerForm form
			) {
		
		if (form.getMyId().equalsIgnoreCase(form.getVlogerId())) {
			return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
		}
		// cancel follow
		fansService.doCancel(form.getMyId(), form.getVlogerId());
		
		// vloger fans - 1, my follows 1 1
		redis.decrement(
				REDIS_MY_FANS_COUNTS + ":" + form.getVlogerId(),
				1
			);
		redis.decrement(
				REDIS_MY_FOLLOWS_COUNTS + ":" + form.getMyId(), 
				1
			);
		// fans relation remove from redis
		redis.del(
				REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + form.getMyId() + ":" + form.getVlogerId()
			);
		
		return GraceJSONResult.ok();
	}
}
