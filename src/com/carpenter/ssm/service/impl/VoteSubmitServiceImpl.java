package com.carpenter.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.dao.VoteSubmitDaoMapper;
import com.carpenter.ssm.pojo.VoteSubmit;
import com.carpenter.ssm.service.VoteSubmitService;

/**
 * @Title: VoteSubmitServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月12日 上午10:17:16
 * @version V1.0
 */

@Service("voteSubmitService")
public class VoteSubmitServiceImpl implements VoteSubmitService {
	@Autowired
	private VoteSubmitDaoMapper voteSubmitDao;

	@Override
	public boolean insert(VoteSubmit voteSubmit) throws Exception {
		try {
			if (this.voteSubmitDao.insert(voteSubmit) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<VoteSubmit> getVoteSubmitByVoteId(String vote_id) throws Exception {
		try {
			return this.voteSubmitDao.selectAllById(vote_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<VoteSubmit> getVoteSubmitByStudentId(String student_id) throws Exception {
		try {
			return this.voteSubmitDao.getVoteSubmitById(student_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updateChoice(String vote_id, String student_id, String vote_choice) throws Exception {
		try {
			if (this.voteSubmitDao.update(vote_id, student_id, vote_choice) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
