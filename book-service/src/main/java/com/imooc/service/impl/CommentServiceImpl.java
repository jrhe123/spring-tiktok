package com.imooc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.CommentBO;
import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.CommentMapper;
import com.imooc.mapper.CommentMapperCustom;
import com.imooc.mapper.FansMapper;
import com.imooc.mapper.FansMapperCustom;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Fans;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.FansService;
import com.imooc.service.VlogService;
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
		
		// 
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
			Integer page,
			Integer pageSize
		) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);

		Map<String, Object> map = new HashMap<>();
		map.put("vlogId", vlogId);

		List<CommentVO> list = commentMapperCustom.getCommentList(map);

		return setterPagedGrid(list, page);
	}

	@Override
	public void deleteComment(
			String commentUserId,
			String commentId,
			String vlogId
		) {
		
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
