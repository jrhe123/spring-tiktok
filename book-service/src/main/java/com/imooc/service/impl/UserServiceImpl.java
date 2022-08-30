package com.imooc.service.impl;

import java.util.Date;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.enums.Sex;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	public static final String USER_FACE1 = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/User-avatar.svg/1024px-User-avatar.svg.png";
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private Sid sid;

	@Override
	public Users queryMobileIsExist(String mobile) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("mobile", mobile);
		Users user = usersMapper.selectOneByExample(userExample);
		return user;
	}

	@Transactional
	@Override
	public Users createUser(String mobile) {
		// unique id
		String userId = sid.nextShort();
		
		Users user = new Users();
		user.setId(userId);
		user.setMobile(mobile);
		user.setNickname("User: " + DesensitizationUtil.commonDisplay(mobile));
		user.setImoocNum("User: " + DesensitizationUtil.commonDisplay(mobile));
		user.setFace(USER_FACE1);
		
		user.setBirthday(DateUtil.stringToDate("2000-01-01"));
		user.setSex(Sex.secret.type);
		
		user.setCountry("canada");
		user.setProvince("");
		user.setCity("");
		user.setDistrict("");
		user.setDescription("I'm new tiktok");
		user.setCanImoocNumBeUpdated(YesOrNo.YES.type);
		
		user.setCreatedTime(new Date());
		user.setUpdatedTime(new Date());
		
		usersMapper.insert(user);
		return user;
	}

	@Override
	public Users queryUserID(String userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	
	
	
	
	
	
	
	
	
}
