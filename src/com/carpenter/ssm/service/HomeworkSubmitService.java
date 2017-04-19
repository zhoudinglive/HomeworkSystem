package com.carpenter.ssm.service;

import java.util.Date;
import java.util.List;

import com.carpenter.ssm.pojo.HomeworkSubmit;

/**
 * @Title: HomeworkSubmitService.java
 * @Package com.carpenter.ssm.service
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午3:14:17
 * @version V1.0
 */

public interface HomeworkSubmitService {
	public boolean insert(HomeworkSubmit homeworkSubmit) throws Exception;

	public List<HomeworkSubmit> selectAllByHomeworkId(String homework_id) throws Exception;

	public List<HomeworkSubmit> selectAllByStudentId(String student_id) throws Exception;

	public HomeworkSubmit getExactHomeworkSubmitById(String homework_id, String student_id) throws Exception;

	public boolean updateTime(String homework_id, String student_id, Date last_updatetime) throws Exception;

}
