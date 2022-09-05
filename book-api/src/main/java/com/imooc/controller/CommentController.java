package com.imooc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.base.MQConfig;
import com.imooc.bo.CommentBO;
import com.imooc.controller.form.CommentForm;
import com.imooc.controller.form.DeleteCommentForm;
import com.imooc.controller.form.FollowVlogerForm;
import com.imooc.controller.form.LikeCommentForm;
import com.imooc.controller.form.QueryCommentListForm;
import com.imooc.controller.form.QueryVlogCountForm;
import com.imooc.enums.MessageEnum;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.mo.MessageMO;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.MsgService;
import com.imooc.service.VlogService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
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
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private VlogService vlogService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
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
	
	@PostMapping("list")
	public GraceJSONResult list(
			@RequestBody @Valid QueryCommentListForm form
			) {
		PagedGridResult result = commentService.queryVlogComments(
				form.getVlogId(),
				form.getUserId(),
				form.getPage(),
				form.getPageSize()
			);
		return GraceJSONResult.ok(result);
	}
	
	@PostMapping("delete")
	public GraceJSONResult delete(
			@RequestBody @Valid DeleteCommentForm form
			) {
		
		String commentId = form.getCommentId();
		String commentUserId = form.getCommentUserId();
		String vlogId = form.getVlogId();
		
		commentService.deleteComment(
				commentUserId,
				commentId,
				vlogId
			);
			
		return GraceJSONResult.ok();
	}
	
	@PostMapping("like")
	public GraceJSONResult like(
			@RequestBody @Valid LikeCommentForm form
			) {
		
		String userId = form.getUserId();
		String commentId = form.getCommentId();
		
		// big key issue
		redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
		redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");	
		
		// system message
		Integer msgType = MessageEnum.LIKE_COMMENT.type;
		// msgContent
		Comment comment = commentService.queryCommentID(commentId);
		Vlog vlog = vlogService.queryVlogID(comment.getVlogId());
		Map msgContent = new HashMap();
		msgContent.put("vlogId", comment.getVlogId());
		msgContent.put("vlogCover", vlog.getCover());
		msgContent.put("commentId", commentId);

//		msgService.createMsg(
//				userId,
//				comment.getCommentUserId(),
//				msgType,
//				msgContent
//			);
		MessageMO messageMO = new MessageMO();
		messageMO.setFromUserId(userId);
		messageMO.setToUserId(comment.getCommentUserId());
		messageMO.setMsgContent(msgContent);
		rabbitTemplate.convertAndSend(
				MQConfig.EXCHANGE_MSG,
				"sys.msg." + msgType,
				JsonUtils.objectToJson(messageMO)
				);
		
		return GraceJSONResult.ok();
	}
	
	@PostMapping("unlike")
	public GraceJSONResult unlike(
			@RequestBody @Valid LikeCommentForm form
			) {
		
		String userId = form.getUserId();
		String commentId = form.getCommentId();
		
		redis.decrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
		redis.hdel(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);	
			
		// delete system message
		Integer msgType = MessageEnum.LIKE_COMMENT.type;
		Comment comment = commentService.queryCommentID(commentId);
//		msgService.deleteMsg(
//				userId,
//				comment.getCommentUserId(),
//				msgType
//			);
		
		MessageMO messageMO = new MessageMO();
		messageMO.setFromUserId(userId);
		messageMO.setToUserId(comment.getCommentUserId());
		rabbitTemplate.convertAndSend(
				MQConfig.EXCHANGE_MSG,
				"sys.msg.del" + msgType,
				JsonUtils.objectToJson(messageMO)
				);
		
		return GraceJSONResult.ok();
	}

}
