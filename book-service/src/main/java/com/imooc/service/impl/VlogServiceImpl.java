package com.imooc.service.impl;

import java.util.Date;

import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.VlogMapper;
import com.imooc.pojo.Vlog;
import com.imooc.service.VlogService;

@Service
public class VlogServiceImpl implements VlogService {
		
	@Autowired
	private VlogMapper vlogMapper;
	
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

	
	
	
}
