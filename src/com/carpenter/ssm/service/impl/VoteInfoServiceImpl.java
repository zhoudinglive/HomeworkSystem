package com.carpenter.ssm.service.impl;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.BaseInfoDaoMapper;
import com.carpenter.ssm.dao.VoteInfoDaoMapper;
import com.carpenter.ssm.pojo.BaseInfo;
import com.carpenter.ssm.pojo.HomeworkSubmit;
import com.carpenter.ssm.pojo.StayInfo;
import com.carpenter.ssm.pojo.VoteInfo;
import com.carpenter.ssm.pojo.VoteSubmit;
import com.carpenter.ssm.service.VoteInfoService;

/**   
* @Title: VoteInfoServiceImpl.java 
* @Package com.carpenter.ssm.service.impl 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月10日 下午11:13:15 
* @version V1.0   
*/

@Service("voteInfoService")
public class VoteInfoServiceImpl implements VoteInfoService{
	@Autowired
	private BaseInfoDaoMapper baseInfoDao;
	
	@Autowired
	private VoteInfoDaoMapper voteInfoDao;
	
	@Autowired
	private VoteSubmitServiceImpl voteSubmitService;
	
	@Override
	public boolean createNewVote(VoteInfo voteInfo) throws Exception {
		if(voteInfo.getVote_initiator() == null || !StringToDate.compareToNow(voteInfo.getVote_deadline())){
			return false;
		}
		try {
			this.voteInfoDao.initialize();
			if (this.voteInfoDao.insert(voteInfo) > 0) {
				String id = null;
				try {
					id = this.voteInfoDao.getIdByName(voteInfo.getVote_name());
					if(id==null){
						this.voteInfoDao.deleteByName(voteInfo.getVote_name());
						return false;
					}
					
					List<BaseInfo> baseInfoList = this.baseInfoDao.selectAll();
					for(BaseInfo baseInfo : baseInfoList){
						VoteSubmit voteSubmit = new VoteSubmit();
						voteSubmit.setVote_id(id);
						voteSubmit.setStudent_id(baseInfo.getBase_id());
						
						this.voteSubmitService.insert(voteSubmit);
					}
				} catch (Exception e) {
					this.voteInfoDao.deleteByName(voteInfo.getVote_name());
					return false;
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteVoteById(String vote_id) throws Exception {
		try {
			if (vote_id != null && !vote_id.equals("") && this.voteInfoDao.delete(vote_id) > 0) {
				try {
					this.voteInfoDao.deleteAssist(vote_id);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<VoteInfo> selectAll() throws Exception {
		try {
			return this.voteInfoDao.selcetAll();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updateNameById(String vote_id, String vote_name) throws Exception {
		if(vote_name==null||vote_name.equals("")){
			return false;
		}
		try {
			VoteInfo voteInfo = new VoteInfo();
			voteInfo.setVote_id(vote_id);
			voteInfo.setVote_name(vote_name);
			if(this.voteInfoDao.update(voteInfo)>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateDetailsById(String vote_id, String vote_details) throws Exception {
		if(vote_details==null||vote_details.equals("")){
			return false;
		}
		try {
			VoteInfo voteInfo = new VoteInfo();
			voteInfo.setVote_id(vote_id);
			voteInfo.setVote_details(vote_details);
			if(this.voteInfoDao.update(voteInfo)>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

//	@Override
//	public boolean updateVotedById(String vote_id, String vote_tobeVoted) throws Exception {
//		if(vote_tobeVoted==null||vote_tobeVoted.equals("")){
//			return false;
//		}
//		try {
//			VoteInfo voteInfo = new VoteInfo();
//			voteInfo.setVote_id(vote_id);
//			voteInfo.setVote_tobeVoted(vote_tobeVoted);
//			if(this.voteInfoDao.update(voteInfo)>0){
//				return true;
//			}
//			return false;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	@Override
	public boolean updateDeadlineById(String vote_id, String vote_deadline) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		Date date = null;
		try {
			date = sdf.parse(vote_deadline);
			if (date == null || date.toString().equals("")) {
				return false;
			}
			if(StringToDate.compareIfDate1BiggerThanDate2(date, 
					this.voteInfoDao.getVoteInfoById(vote_id).getVote_createtime())<=0){
			return false;
		}
		} catch (Exception e) {
			return false;
		}
		try {
			VoteInfo voteInfo = new VoteInfo();
			voteInfo.setVote_id(vote_id);

			voteInfo.setVote_deadline(date);
			if (this.voteInfoDao.update(voteInfo) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
