package com.imooc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.base.MQConfig;
import com.imooc.bo.CommentBO;
import com.imooc.bo.VlogBO;
import com.imooc.enums.MessageEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.CommentMapper;
import com.imooc.mapper.CommentMapperCustom;
import com.imooc.mapper.FansMapper;
import com.imooc.mapper.FansMapperCustom;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.mo.MessageMO;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Fans;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.FansService;
import com.imooc.service.MsgService;
import com.imooc.service.VlogService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import com.imooc.vo.FansVO;
import com.imooc.vo.IndexVlogVO;
import com.imooc.vo.VlogerVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {
		
	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private CommentMapperCustom commentMapperCustom;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private VlogService vlogService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Sid sid;

	@Override
	public CommentVO createComment(CommentBO commentBO) {
		String id = sid.nextShort();
		
		Comment comment = new Comment();
		comment.setId(id);
		comment.setVlogId(commentBO.getVlogId());
		comment.setVlogerId(commentBO.getVlogerId());
		comment.setCommentUserId(commentBO.getCommentUserId());
		comment.setFatherCommentId(commentBO.getFatherCommentId());
		comment.setContent(commentBO.getContent());
		comment.setLikeCounts(0);
		comment.setCreateTime(new Date());
		
		commentMapper.insert(comment);
		
		// redis
		redis.increment(
				REDIS_VLOG_COMMENT_COUNTS + ":" + commentBO.getVlogId(),
				1
			);
		
		// system message (comment / reply)
		Integer msgType = MessageEnum.COMMENT_VLOG.type;
		if (StringUtils.isNotBlank(commentBO.getFatherCommentId()) &&
				!commentBO.getFatherCommentId().equalsIgnoreCase("0")) {
			msgType = MessageEnum.REPLY_YOU.type;
		}
		// msgContent
		Vlog vlog = vlogService.queryVlogID(commentBO.getVlogId());
		Map msgContent = new HashMap(); 
		msgContent.put("vlogId", commentBO.getVlogId());
		msgContent.put("vlogCover", vlog.getCover());
		msgContent.put("commentId", id);
		msgContent.put("commentContent", commentBO.getContent());
		
//		msgService.createMsg(
//				commentBO.getCommentUserId(),
//				commentBO.getVlogerId(),
//				msgType,
//				msgContent
//			);
		MessageMO messageMO = new MessageMO();
		messageMO.setFromUserId(commentBO.getCommentUserId());
		messageMO.setToUserId(commentBO.getVlogerId());
		messageMO.setMsgContent(msgContent);
		rabbitTemplate.convertAndSend(
				MQConfig.EXCHANGE_MSG,
				"sys.msg." + msgType,
				JsonUtils.objectToJson(messageMO)
				);
		
		// return vo
		CommentVO commentVO = new CommentVO();
		BeanUtils.copyProperties(comment, commentVO);
		return commentVO;
	}

	@Override
	public Comment queryCommentID(String commentId) {
		Comment comment = commentMapper.selectByPrimaryKey(commentId);
		return comment;
	}

	@Override
	public PagedGridResult queryVlogComments(
			String vlogId,
			String userId,
			Integer page,
			Integer pageSize
		) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);

		Map<String, Object> map = new HashMap<>();
		map.put("vlogId", vlogId);

		List<CommentVO> list = commentMapperCustom.getCommentList(map);

		for (CommentVO c : list) {
			String commentId = c.getCommentId();
			
			// total count
			String countStr = redis.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId);
			if (StringUtils.isBlank(countStr)) {
				countStr = "0";
			}
			Integer counts = Integer.valueOf(countStr);
			c.setLikeCounts(counts);
			
			// do i like
			String doILike = redis.getHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);
			if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
				c.setIsLike(YesOrNo.YES.type);
			}
		}
		
		return setterPagedGrid(list, page);
	}

	@Override
	public void deleteComment(
			String commentUserId,
			String commentId,
			String vlogId
		) {
				
		// delete system message
		Comment comment = queryCommentID(commentId);
		Integer msgType = MessageEnum.COMMENT_VLOG.type;
		if (StringUtils.isNotBlank(comment.getFatherCommentId()) &&
				!comment.getFatherCommentId().equalsIgnoreCase("0")) {
			msgType = MessageEnum.REPLY_YOU.type;
		}
//		msgService.deleteMsg(
//				comment.getCommentUserId(),
//				comment.getVlogerId(),
//				msgType
//			);
		MessageMO messageMO = new MessageMO();
		messageMO.setFromUserId(comment.getCommentUserId());
		messageMO.setToUserId(comment.getVlogerId());
		rabbitTemplate.convertAndSend(
				MQConfig.EXCHANGE_MSG,
				"sys.msg.del" + msgType,
				JsonUtils.objectToJson(messageMO)
				);
		
		// delete db
		Comment pendingComment = new Comment();
		pendingComment.setId(commentId);
		pendingComment.setCommentUserId(commentUserId);
		pendingComment.setVlogId(vlogId);
		
		commentMapper.delete(pendingComment);
		
		// redis
		redis.decrement(
				REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 
				1
			);
	};


	
}
