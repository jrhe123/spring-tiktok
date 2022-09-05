package com.imooc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.imooc.controller.form.DeleteCommentForm;
import com.imooc.controller.form.FollowVlogerForm;
import com.imooc.controller.form.LikeCommentForm;
import com.imooc.controller.form.QueryCommentListForm;
import com.imooc.controller.form.QueryMessageListForm;
import com.imooc.controller.form.QueryMyVlogListForm;
import com.imooc.controller.form.QueryVlogCountForm;
import com.imooc.enums.MessageEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.mo.MessageMO;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.MsgService;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.CommentVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "Message controller")
@RestController
@RequestMapping("msg")
public class MessageController extends BaseInfoProperties{

	@Autowired
	private MsgService msgService;
	
	
	@PostMapping("list")
	public GraceJSONResult list(
			@RequestBody @Valid QueryMessageListForm form
			) {
		
		String userId = form.getUserId();
		// mongo start at index 0
		Integer page = form.getPage();
		Integer pageSize = form.getPageSize();
				
		List<MessageMO> list = msgService.queryList(userId, page, pageSize);
		
		return GraceJSONResult.ok(list);
	}
	
}
