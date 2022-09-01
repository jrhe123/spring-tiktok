package com.imooc.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.bo.UpdatedUserBO;
import com.imooc.controller.form.UpdateUserForm;
import com.imooc.controller.form.UserInfoForm;
import com.imooc.enums.UserInfoModifyType;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.vo.UsersVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Api(tags = "User info controller")
@RestController
@RequestMapping("userInfo")
public class UserInfoController extends BaseInfoProperties {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("modifyUserInfo")
	public GraceJSONResult modifyUserInfo(
			@RequestBody @Valid UpdateUserForm form
			) {
		UserInfoModifyType.checkUserInfoTypeIsRight(form.getType());
		
		UpdatedUserBO userBO = new UpdatedUserBO();
		BeanUtils.copyProperties(form, userBO);
		
		Users user = userService.updateUserInfo(userBO, form.getType());
		return GraceJSONResult.ok(user);
	}
	
	@PostMapping("query")
	public GraceJSONResult query(
			@RequestBody @Valid UserInfoForm form) {
		Users user = userService.queryUserID(form.getUserId());
		
		UsersVO usersVO = new UsersVO();
		BeanUtils.copyProperties(user, usersVO);
		
		// store likes in redis & counts
		String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + form.getUserId());
		String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + form.getUserId());
		String myLikedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + form.getUserId());
		String myLikedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + form.getUserId());
		
		Integer myFollowsCounts = 0;
		Integer myFansCounts = 0;
		Integer myLikedVlogCounts = 0;
		Integer myLikedVlogerCounts = 0;
		Integer totalLikeMeCounts = 0;
		
		if (StringUtils.isNotBlank(myFollowsCountsStr)) {
			myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
		}
		if (StringUtils.isNotBlank(myFansCountsStr)) {
			myFansCounts = Integer.valueOf(myFansCountsStr);
		}
		if (StringUtils.isNotBlank(myLikedVlogCountsStr)) {
			myLikedVlogCounts = Integer.valueOf(myLikedVlogCountsStr);
		}
		if (StringUtils.isNotBlank(myLikedVlogerCountsStr)) {
			myLikedVlogerCounts = Integer.valueOf(myLikedVlogerCountsStr);
		}
		totalLikeMeCounts = myLikedVlogCounts + myLikedVlogerCounts;
		usersVO.setMyFollowsCounts(myFollowsCounts);
		usersVO.setMyFansCounts(myFansCounts);
		usersVO.setTotalLikeMeCounts(totalLikeMeCounts);
		
		return GraceJSONResult.ok(usersVO);
	}

}
