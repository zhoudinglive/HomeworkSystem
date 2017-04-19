package com.carpenter.ssm.service;

import java.util.List;

import com.carpenter.ssm.pojo.VoteSubmit;

/**
 * @Title: VoteSubmitService.java
 * @Package com.carpenter.ssm.service
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月12日 上午9:50:34
 * @version V1.0
 */

public interface VoteSubmitService {
	public boolean insert(VoteSubmit voteSubmit) throws Exception;
	
	public List<VoteSubmit> getVoteSubmitByVoteId(String vote_id) throws Exception;
	
	public List<VoteSubmit> getVoteSubmitByStudentId(String student_id) throws Exception;
	
	public boolean updateChoice(String vote_id, String student_id, String vote_choice) throws Exception;
	
}
