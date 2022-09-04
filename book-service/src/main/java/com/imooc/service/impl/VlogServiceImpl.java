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
import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
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
		return setterPagedGrid(list, page);
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

	
	
	
}
