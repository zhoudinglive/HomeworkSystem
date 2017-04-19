package com.carpenter.ssm.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.BaseInfoDaoMapper;
import com.carpenter.ssm.dao.StayInfoDaoMapper;
import com.carpenter.ssm.pojo.BaseInfo;
import com.carpenter.ssm.pojo.HomeworkInfo;
import com.carpenter.ssm.pojo.HomeworkSubmit;
import com.carpenter.ssm.pojo.StayInfo;
import com.carpenter.ssm.pojo.StaySubmit;
import com.carpenter.ssm.service.StayInfoService;

/**
 * @Title: StayInfoServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 下午9:49:46
 * @version V1.0
 */

@Service("stayInfoService")
public class StayInfoServiceImpl implements StayInfoService {
	@Autowired
	private BaseInfoDaoMapper baseInfoDao;
	
	@Autowired
	private StayInfoDaoMapper stayInfoDao;
	
	@Autowired
	private StaySubmitServiceImpl staySubmitService;

	@Override
	public boolean createNewStay(StayInfo stayInfo) throws Exception {
		if(stayInfo.getStay_initiator() == null){
			return false;
		}
		if (!StringToDate.compareToNow(stayInfo.getStay_deadline())) {
			return false;
		}
		if(StringToDate.compareIfDate1BiggerThanDate2(stayInfo.getStay_stoptime(), stayInfo.getStay_begintime())<=0){
			return false;
		}
		try {
			this.stayInfoDao.initialize();
			String tmp_stayname = stayInfo.getStay_name();
			String tmp_staydetails = stayInfo.getStay_details();
			stayInfo.setStay_name(stayInfo.getStay_name().replace("\"", "\\\""));
			stayInfo.setStay_details(stayInfo.getStay_details().replace("\"", "\\\""));
			if (this.stayInfoDao.insert(stayInfo) > 0) {
				String id = null;
				try {
					id = this.stayInfoDao.getIdByName(stayInfo.getStay_name());
					if(id==null){
						this.stayInfoDao.deleteByName(stayInfo.getStay_id());
						return false;
					}
					stayInfo.setStay_name(tmp_stayname);
					stayInfo.setStay_details(tmp_staydetails);
					List<BaseInfo> baseInfoList = this.baseInfoDao.selectAll();
					for(BaseInfo baseInfo : baseInfoList){
						StaySubmit staySubmit = new StaySubmit();
						staySubmit.setStay_id(id);
						staySubmit.setStudent_id(baseInfo.getBase_id());
						staySubmit.setStay_start(this.stayInfoDao.getStayInfoById(id).getStay_createtime());
						staySubmit.setStay_stop(this.stayInfoDao.getStayInfoById(id).getStay_createtime());
						this.staySubmitService.insert(staySubmit);
					}
				} catch (Exception e) {
					this.stayInfoDao.deleteByName(stayInfo.getStay_name());
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
	public boolean deleteStayById(String stay_id) throws Exception {
		try {
			if (stay_id != null && !stay_id.equals("") && this.stayInfoDao.delete(stay_id) > 0) {
				try {
					this.stayInfoDao.deleteAssist(stay_id);
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
	public List<StayInfo> selectAll() throws Exception {
		try {
			return this.stayInfoDao.selectAll();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public StayInfo getStayInfoById(String stay_id) throws Exception {
		try{
			StayInfo stayInfo = this.stayInfoDao.getStayInfoById(stay_id);
			return stayInfo;
		} catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean updateStayInfo(StayInfo stayInfo) throws Exception {
		try {
			stayInfo.setStay_details(stayInfo.getStay_details().replace("\"", "\\\""));
			if(this.stayInfoDao.update(stayInfo)>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}		
	}
}
