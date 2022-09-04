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
import com.imooc.controller.form.LikeVlogForm;
import com.imooc.controller.form.PublishVlogForm;
import com.imooc.controller.form.QueryMyVlogListForm;
import com.imooc.controller.form.UpdateVlogPrivateOrPublicForm;
import com.imooc.controller.form.VlogDetailForm;
import com.imooc.controller.form.VlogIndexListForm;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

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
	
	
	@PostMapping("indexList")
	public GraceJSONResult indexList(
			@RequestBody @Valid VlogIndexListForm form
			) {
		String search = form.getSearch();		
		if (search == null) {
			search = "";
		}
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
				
		PagedGridResult result = vlogService.getIndexVlogList(search, page, pageSize);
		return GraceJSONResult.ok(result);
	}
	
	@PostMapping("getVlogDetailById")
	public GraceJSONResult getVlogDetailById(
			@RequestBody @Valid VlogDetailForm form
			) {
				
		IndexVlogVO vlogBO = vlogService.getVlogDetailById(form.getVlogId());
		return GraceJSONResult.ok(vlogBO);
	}
	
	@PostMapping("changeToPrivateOrPublic")
	public GraceJSONResult changeToPrivateOrPublic(
			@RequestBody @Valid UpdateVlogPrivateOrPublicForm form
			) {
		
		String userId = form.getUserId();
		String vlogId = form.getVlogId();
		String yesOrNo = form.getYesOrNo();
		Integer yesOrNoInt = YesOrNo.valueOf(yesOrNo).type;
		
		vlogService.changeToPrivateOrPublic(userId, vlogId, yesOrNoInt);
		return GraceJSONResult.ok();
	}
	
	@PostMapping("myVlogList")
	public GraceJSONResult myVlogList(
			@RequestBody @Valid QueryMyVlogListForm form
			) {
		
		String userId = form.getUserId();
		String yesOrNo = form.getYesOrNo();
		Integer yesOrNoInt = YesOrNo.valueOf(yesOrNo).type;
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
				
		PagedGridResult result = vlogService.queryMyVlogList(
				userId,
				yesOrNoInt,
				page,
				pageSize);
		return GraceJSONResult.ok(result);
	}
	
	@PostMapping("like")
	public GraceJSONResult like(
			@RequestBody @Valid LikeVlogForm form
			) {
		
		String userId = form.getUserId();
		String vlogerId = form.getVlogerId();
		String vlogId = form.getVlogId();
		
		// service
		vlogService.userLikeVlog(userId, vlogId);
		
		// redis:
		// likes + 1: vlog & vloger
		redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
		redis.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
		
		// my like video save in redis
		redis.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, "1");
		
		return GraceJSONResult.ok();
	}
}
