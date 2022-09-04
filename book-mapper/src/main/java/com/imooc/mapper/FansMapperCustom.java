package com.imooc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.imooc.vo.FansVO;
import com.imooc.vo.VlogerVO;

@Repository
public interface FansMapperCustom {
	
	public List<VlogerVO> queryMyFollows(
			@Param("paramMap") Map<String, Object> map
			);
	
	public List<FansVO> queryMyFans(
			@Param("paramMap") Map<String, Object> map
			);	
}