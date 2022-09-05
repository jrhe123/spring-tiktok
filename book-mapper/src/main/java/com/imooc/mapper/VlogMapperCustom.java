package com.imooc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.imooc.vo.IndexVlogVO;

@Repository
public interface VlogMapperCustom {
	
	public List<IndexVlogVO> getIndexVlogList(
			@Param("paramMap") Map<String, Object> map
			);
	
	public List<IndexVlogVO> getVlogDetailById(
			@Param("paramMap") Map<String, Object> map
			);
	
	public List<IndexVlogVO> getMyLikedVlogList(
			@Param("paramMap") Map<String, Object> map
			);
	
	public List<IndexVlogVO> getMyFollowVlogList(
			@Param("paramMap") Map<String, Object> map
			);
	
	public List<IndexVlogVO> getMyFriendVlogList(
			@Param("paramMap") Map<String, Object> map
			);
}