package com.imooc.service;

import java.util.List;

import com.imooc.bo.VlogBO;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

public interface VlogService {

	/*
	 * create vlog
	 */
	public void createVlog(VlogBO vlogBO);
	
	/*
	 * list vlog by params
	 */
	public PagedGridResult getIndexVlogList(
			String search,
			Integer page,
			Integer pageSizes
			);
	
	/*
	 * show vlog detail by id
	 */
	public IndexVlogVO getVlogDetailById(
			String vlogId
			);
	
	/*
	 * toggle vlog private
	 */
	public void changeToPrivateOrPublic(
			String userId,
			String vlogId,
			Integer yesOrNo
			);
}
