package com.carpenter.ssm.service;

import java.util.List;

import com.carpenter.ssm.pojo.VoteInfo;

/**
 * @Title: VoteInfoService.java
 * @Package com.carpenter.ssm.service
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 下午11:03:27
 * @version V1.0
 */

public interface VoteInfoService {
	public boolean createNewVote(VoteInfo voteInfo) throws Exception;

	public boolean deleteVoteById(String vote_id) throws Exception;

	public List<VoteInfo> selectAll() throws Exception;

	public boolean updateNameById(String vote_id, String vote_name) throws Exception;

	public boolean updateDetailsById(String vote_id, String vote_details) throws Exception;

	//public boolean updateVotedById(String vote_id, String vote_tobeVoted) throws Exception;

	public boolean updateDeadlineById(String vote_id, String vote_deadline) throws Exception;

}
