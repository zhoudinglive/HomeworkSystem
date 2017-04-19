package com.carpenter.ssm.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.HomeworkSubmitDaoMapper;
import com.carpenter.ssm.pojo.HomeworkSubmit;
import com.carpenter.ssm.service.HomeworkSubmitService;

/**
 * @Title: HomeworkSubmitServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月12日 上午9:58:15
 * @version V1.0
 */

@Service("homeworkSubmitService")
public class HomeworkSubmitServiceImpl implements HomeworkSubmitService{
	@Autowired
	private HomeworkSubmitDaoMapper homeworkSubmitDao;
	
	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

	private HomeworkInfoServiceImpl homeworkInfoService;

	@Override
	public boolean insert(HomeworkSubmit homeworkSubmit) throws Exception {
		try {
			if(this.homeworkSubmitDao.insert(homeworkSubmit)>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<HomeworkSubmit> selectAllByHomeworkId(String homework_id) throws Exception {
		try {
			return this.homeworkSubmitDao.selectAllById(homework_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<HomeworkSubmit> selectAllByStudentId(String student_id) throws Exception {
		try {
			return this.homeworkSubmitDao.getHomeworkSubmitById(student_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public HomeworkSubmit getExactHomeworkSubmitById(String homework_id, String student_id) throws Exception {
		try {
			return this.homeworkSubmitDao.getOnlyHomeworkSubmitById(homework_id, student_id);
		} catch (Exception e) {
			
			e.printStackTrace();return null;
		}
	}

	@Override
	public boolean updateTime(String homework_id, String student_id, Date last_updatetime) throws Exception {
//		if(!StringToDate.compareToNow(last_updatetime)){
//			return false;
//		}
		try {
			if(this.homeworkSubmitDao.updateTimeFresh(homework_id, student_id, last_updatetime)>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	

}
