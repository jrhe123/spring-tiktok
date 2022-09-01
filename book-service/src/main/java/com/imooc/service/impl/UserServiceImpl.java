package com.imooc.service.impl;

import java.util.Date;

import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.bo.UpdatedUserBO;
import com.imooc.enums.Sex;
import com.imooc.enums.UserInfoModifyType;
import com.imooc.enums.YesOrNo;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
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

	@Transactional
	@Override
	public Users updateUserInfo(UpdatedUserBO userBO) {
		Users pendingUser = new Users();
		BeanUtils.copyProperties(userBO, pendingUser);
		
		int result = usersMapper.updateByPrimaryKeySelective(pendingUser);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
		}
		
		return usersMapper.selectByPrimaryKey(userBO.getId());
	}

	@Transactional
	@Override
	public Users updateUserInfo(UpdatedUserBO userBO, Integer type) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		
		if (type == UserInfoModifyType.NICKNAME.type) {
			criteria.andEqualTo("nickname", userBO.getNickname());
			Users user = usersMapper.selectOneByExample(userExample);
			if (user != null) {
				GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
			}
		}
		
		if (type == UserInfoModifyType.IMOOCNUM.type) {
			criteria.andEqualTo("imoocNum", userBO.getImoocNum());
			Users user = usersMapper.selectOneByExample(userExample);
			if (user != null) {
				GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_IMOOCNUM_EXIST_ERROR);
			}
			
			Users tempUser = queryUserID(userBO.getId());
			if (tempUser.getCanImoocNumBeUpdated() == YesOrNo.NO.type) {
				GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR);
			}
			
			// allow update once
			userBO.setCanImoocNumBeUpdated(YesOrNo.NO.type);
		}
		
		return updateUserInfo(userBO);
	}

	
	
	
	
	
	
	
	
	
}
