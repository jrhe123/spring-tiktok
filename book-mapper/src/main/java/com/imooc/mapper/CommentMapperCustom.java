package com.imooc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.imooc.vo.CommentVO;

@Repository
public interface CommentMapperCustom {
	
	public List<CommentVO> getCommentList(
			@Param("paramMap") Map<String, Object> map
			);
}