package com.imooc.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.imooc.bo.CommentBO;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Vlog;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import com.imooc.vo.VlogerVO;

public interface CommentService {

	public CommentVO createComment(CommentBO commentBO);
	
	public Comment queryCommentID(String commentId);
}
