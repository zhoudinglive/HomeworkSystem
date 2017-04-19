package com.carpenter.ssm.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.StayInfoDaoMapper;
import com.carpenter.ssm.dao.StaySubmitDaoMapper;
import com.carpenter.ssm.pojo.StaySubmit;
import com.carpenter.ssm.service.StaySubmitService;

/**
 * @Title: StaySubmitServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月12日 上午10:10:05
 * @version V1.0
 */

@Service("staySubmitService")
public class StaySubmitServiceImpl implements StaySubmitService {
	@Autowired
	private StaySubmitDaoMapper staySubmitDao;
	
	@Autowired
	private StayInfoDaoMapper stayInfoDao;

	@Override
	public boolean insert(StaySubmit staySubmit) throws Exception {
		try {
			if (this.staySubmitDao.insert(staySubmit) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<StaySubmit> selectAllByStayId(String stay_id) throws Exception {
		try {
			return this.staySubmitDao.selectAllById(stay_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<StaySubmit> selectAllByStudentId(String student_id) throws Exception {
		try {
			return this.staySubmitDao.getStaySubmitById(student_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public StaySubmit getExactStaySubmitById(String stay_id, String student_id) throws Exception {
		try {
			return this.staySubmitDao.getOnlyStaySubmitById(stay_id, student_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updateInterval(StaySubmit staySubmit) throws Exception {
		if(!StringToDate.compareToNow(stayInfoDao.getStayInfoById(staySubmit.getStay_id()).getStay_deadline())
				|| StringToDate.compareIfDate1BiggerThanDate2(staySubmit.getStay_start(), staySubmit.getStay_stop())!=0
				|| StringToDate.compareIfDate1BiggerThanDate2(staySubmit.getStay_stop(), stayInfoDao.getStayInfoById(staySubmit.getStay_id()).getStay_stoptime()) !=0
				|| StringToDate.compareIfDate1BiggerThanDate2(stayInfoDao.getStayInfoById(staySubmit.getStay_id()).getStay_begintime(), staySubmit.getStay_start()) !=0){
			return false;
		}
		try {
			if (this.staySubmitDao.update(staySubmit) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
