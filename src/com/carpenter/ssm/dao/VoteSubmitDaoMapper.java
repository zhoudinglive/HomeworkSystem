/**
 * 
 */
package com.carpenter.ssm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.carpenter.ssm.pojo.VoteSubmit;

/**
 * @Title: VoteSubmitDaoMapper.java
 * @Package com.carpenter.ssm.dao
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:47:31
 * @version V1.0
 */

public interface VoteSubmitDaoMapper {
	public int insert(VoteSubmit voteSubmit) throws Exception;

	public List<VoteSubmit> selectAllById(String vote_id) throws Exception;

	public List<VoteSubmit> getVoteSubmitById(String student_id) throws Exception;
	
	public VoteSubmit getOnlyVoteSubmitById(@Param("vote_id")String vote_id, @Param("student_id")String student_id) throws Exception;
	
	public int update(@Param("vote_id")String vote_id, @Param("student_id")String student_id, @Param("vote_choice")String vote_choice) throws Exception;
	
}
