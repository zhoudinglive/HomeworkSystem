package com.carpenter.ssm.service;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import com.carpenter.ssm.pojo.HomeworkInfo;

/**
 * @Title: HomeworkInfoService.java
 * @Package com.carpenter.ssm.service
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 上午10:00:25
 * @version V1.0
 */

public interface HomeworkInfoService {
	public boolean createNewHomework(HomeworkInfo homeworkInfo) throws Exception;

	public boolean deleteHomeworkById(String homework_id) throws Exception;
	
	public List<HomeworkInfo> selectAll() throws Exception;
	
	public HomeworkInfo getHomeworkById(String homework_id) throws Exception;

	public boolean updateHomeworkDetailsById(String homework_id, String homework_details) throws Exception;

	public boolean updateHomeworkPhotoById(String homework_id, String homework_photo) throws Exception;

	public boolean updateHomeworkDeadlineById(String homework_id, String homework_deadline) throws Exception;

}
