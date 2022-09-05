package com.imooc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.m;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.MyLikedVlogMapper;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.pojo.MyLikedVlog;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.FansService;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VlogServiceImpl extends BaseInfoProperties implements VlogService {
		
	@Autowired
	private VlogMapper vlogMapper;
	
	@Autowired
	private VlogMapperCustom vlogMapperCustom;
	
	@Autowired
	private MyLikedVlogMapper myLikedVlogMapper;
	
	@Autowired
	private FansService fansService;
	
	@Autowired
	private Sid sid;

	@Transactional
	@Override
	public void createVlog(VlogBO vlogBO) {
		Vlog vlog = new Vlog();
		String vid = sid.nextShort();
		BeanUtils.copyProperties(vlogBO, vlog);
		vlog.setId(vid);
		
		vlog.setCommentsCounts(0);
		vlog.setLikeCounts(0);
		vlog.setIsPrivate(YesOrNo.NO.type);
		vlog.setCreatedTime(new Date());
		vlog.setUpdatedTime(new Date());
		
		vlogMapper.insert(vlog);
	}

	@Override
	public PagedGridResult getIndexVlogList(
			String userId,
			String search,
			Integer page,
			Integer pageSize
			) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotBlank(search)) {
			map.put("search", search);
		}
		
		List<IndexVlogVO> list = vlogMapperCustom.getIndexVlogList(map);
		
		// check vlog is liked by current user, in redis
		for (IndexVlogVO v : list) {
			String vlogId = v.getVlogId();
			// 1. is like or not
			boolean isLike = doILikeVlog(userId, vlogId);
			v.setDoILikeThisVlog(isLike);
			
			// 2. total count
			Integer totalLikeCount = getVlogBeLikedCounts(vlogId);
			v.setLikeCounts(totalLikeCount);
			
			// 3. do i follow
			boolean doIfollow = fansService.queryDoIFollowVloger(userId, v.getVlogerId());
			v.setDoIFollowVloger(doIfollow);
		}
		
		return setterPagedGrid(list, page);
	}
	
	@Override
	public Integer getVlogBeLikedCounts(String vlogId) {
		String countStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
		if (StringUtils.isBlank(countStr)) {
			countStr = "0";
		}
		return Integer.valueOf(countStr);
	}
	
	private boolean doILikeVlog(String myId, String vlogId) {
		String doILike = redis.get(REDIS_USER_LIKE_VLOG + ":" + myId + ":" + vlogId);
		boolean isLike = false;
		if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
			isLike = true;
		}
		return isLike;
	}

	@Override
	public IndexVlogVO getVlogDetailById(String vlogId) {
		Map<String, Object> map = new HashMap<>();
		map.put("vlogId", vlogId);
					
		List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);
				
		if (list != null && list.size() > 0 && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Transactional
	@Override
	public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {
		Example vlogExample = new Example(Vlog.class);
		Criteria criteria = vlogExample.createCriteria();
		criteria.andEqualTo("vlogerId", userId);
		criteria.andEqualTo("id", vlogId);
		
		Vlog pendingVlog = new Vlog();
		pendingVlog.setIsPrivate(yesOrNo);
		
		vlogMapper.updateByExampleSelective(pendingVlog, vlogExample);
	}

	@Override
	public PagedGridResult queryMyVlogList(String userId, Integer yesOrNo, Integer page, Integer pageSize) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);
		
		Example vlogExample = new Example(Vlog.class);
		Criteria criteria = vlogExample.createCriteria();
		criteria.andEqualTo("vlogerId", userId);
		criteria.andEqualTo("isPrivate", yesOrNo);
		
		List<Vlog> list = vlogMapper.selectByExample(vlogExample);
		
		return setterPagedGrid(list, page);
	}

	@Transactional
	@Override
	public void userLikeVlog(String userId, String vlogId) {
		String id = sid.nextShort();
		MyLikedVlog myLikedVlog = new MyLikedVlog();
		myLikedVlog.setId(id);
		myLikedVlog.setUserId(userId);
		myLikedVlog.setVlogId(vlogId);
		
		myLikedVlogMapper.insert(myLikedVlog);
	}
	
	@Transactional
	@Override
	public void userUnLikeVlog(String userId, String vlogId) {
		MyLikedVlog myLikedVlog = new MyLikedVlog();
		myLikedVlog.setUserId(userId);
		myLikedVlog.setVlogId(vlogId);
		
		myLikedVlogMapper.delete(myLikedVlog);
	}

	@Override
	public Vlog queryVlogID(String vlogId) {
		Vlog vlog = vlogMapper.selectByPrimaryKey(vlogId);
		return vlog;
	}

	@Override
	public PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);
				
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
				
		List<IndexVlogVO> list = vlogMapperCustom.getMyLikedVlogList(map);
		return setterPagedGrid(list, page);
	}

	@Override
	public PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize) {
		// intercept: auto add limit, offset to query
		PageHelper.startPage(page, pageSize);
						
		Map<String, Object> map = new HashMap<>();
		map.put("myId", myId);
						
		List<IndexVlogVO> list = vlogMapperCustom.getMyFollowVlogList(map);
		
		// check vlog is liked by current user, in redis
		for (IndexVlogVO v : list) {
			String vlogId = v.getVlogId();
			// 1. is like or not
			boolean isLike = doILikeVlog(myId, vlogId);
			v.setDoILikeThisVlog(isLike);
					
			// 2. total count
			Integer totalLikeCount = getVlogBeLikedCounts(vlogId);
			v.setLikeCounts(totalLikeCount);
			
			// 3. do i follow
			v.setDoIFollowVloger(true);
		}
		
		return setterPagedGrid(list, page);
	}



	
	
	
}
