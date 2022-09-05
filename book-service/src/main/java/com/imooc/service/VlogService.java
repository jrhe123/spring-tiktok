package com.imooc.service;

import java.util.List;

import com.imooc.bo.VlogBO;
import com.imooc.pojo.Users;
import com.imooc.pojo.Vlog;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

public interface VlogService {

	/*
	 * create vlog
	 */
	public void createVlog(VlogBO vlogBO);
	
	/*
	 * search vlog by id
	 */
	public Vlog queryVlogID(String vlogId);
	
	/*
	 * list vlog by params
	 */
	public PagedGridResult getIndexVlogList(
			String userId,
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
	
	/*
	 * query my vlog list (private / public)
	 */
	public PagedGridResult queryMyVlogList(
			String userId,
			Integer yesOrNo,
			Integer page,
			Integer pageSize
			);
	
	/*
	 * user like vlog
	 */
	public void userLikeVlog(
			String userId,
			String vlogId
			);
	
	/*
	 * user UNlike vlog
	 */
	public void userUnLikeVlog(
			String userId,
			String vlogId
			);
	
	/*
	 * get like counts
	 */
	public Integer getVlogBeLikedCounts(String vlogId);
	
	/*
	 * my liked list vlog by params
	 */
	public PagedGridResult getMyLikedVlogList(
			String userId,
			Integer page,
			Integer pageSize
			);
}
