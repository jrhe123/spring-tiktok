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
import com.imooc.mapper.FansMapper;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.pojo.Fans;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.service.FansService;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class FansServiceImpl extends BaseInfoProperties implements FansService {
		
	@Autowired
	private FansMapper fansMapper;
	
	@Autowired
	private Sid sid;

	@Transactional
	@Override
	public void doFollow(String myId, String vlogerId) {
		String id = sid.nextShort();
		
		Fans fans = new Fans();
		fans.setId(id);
		fans.setFanId(myId);
		fans.setVlogerId(vlogerId);
		// check if follow each other, then set friend
		Fans fansMe = queryFansRelationship(vlogerId, myId);
		if (fansMe != null) {
			// set to 1
			fans.setIsFanFriendOfMine(YesOrNo.YES.type);
			// update vloger fans record as well
			fansMe.setIsFanFriendOfMine(YesOrNo.YES.type);
			fansMapper.updateByPrimaryKey(fansMe);
		} else {
			fans.setIsFanFriendOfMine(YesOrNo.NO.type);
		}
		fansMapper.insert(fans);
	}
	
	public Fans queryFansRelationship(String fanId, String vlogerId) {
		Example fansExample = new Example(Fans.class);
		Criteria criteria = fansExample.createCriteria();
		criteria.andEqualTo("fanId", fanId);
		criteria.andEqualTo("vlogerId", vlogerId);
		List<Fans> list = fansMapper.selectByExample(fansExample);
		
		Fans fan = null;
		if (list != null && list.size() > 0 && !list.isEmpty()) {
			fan = list.get(0);
		}
		return fan;
	}

	@Transactional
	@Override
	public void doCancel(String myId, String vlogerId) {
		// 1. check friend relation
		Fans fans = queryFansRelationship(myId, vlogerId);
		if (fans != null && fans.getIsFanFriendOfMine().equals(YesOrNo.YES.type)) {
			// 2. remove friend from vloger relation
			Fans pendingFans = queryFansRelationship(vlogerId, myId);
			pendingFans.setIsFanFriendOfMine(YesOrNo.NO.type);
			fansMapper.updateByPrimaryKeySelective(pendingFans);
		}
		// 3. delete my relation
		fansMapper.delete(fans);
	}

	@Override
	public boolean queryDoIFollowVloger(String myId, String vlogerId) {
		Fans fans = queryFansRelationship(myId, vlogerId);
		return fans != null;
	}

	
	
}
