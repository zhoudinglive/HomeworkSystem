package com.carpenter.ssm.dao;

import java.util.List;

import com.carpenter.ssm.pojo.VoteInfo;

/**   
* @Title: VoteInfoDaoMapper.java 
* @Package com.carpenter.ssm.dao 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月10日 下午10:17:39 
* @version V1.0   
*/

public interface VoteInfoDaoMapper {
	public int insert(VoteInfo voteInfo) throws Exception;
	
	public int delete(String vote_id) throws Exception;
	
	public int deleteByName(String vote_name) throws Exception;
	
	public List<VoteInfo> selcetAll() throws Exception;
	
	public VoteInfo getVoteInfoById(String vote_id) throws Exception;
	
	public String getIdByName(String vote_name) throws Exception;
	
	public int update(VoteInfo voteInfo) throws Exception;
	
	public int initialize() throws Exception;
	
	public void deleteAssist(String vote_id) throws Exception;
	
}
