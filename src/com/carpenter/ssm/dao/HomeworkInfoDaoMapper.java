package com.carpenter.ssm.dao;

import java.util.List;

import com.carpenter.ssm.pojo.HomeworkInfo;

/**
 * @Title: HomeworkInfoDaoMapper.java
 * @Package com.carpenter.ssm.dao
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月7日 下午6:46:00
 * @version V1.0
 */

public interface HomeworkInfoDaoMapper {
	public int insert(HomeworkInfo homeworkInfo) throws Exception;

	public int delete(String homework_id) throws Exception;
	
	public int deleteByName(String homework_name) throws Exception;

	public List<HomeworkInfo> selectAll() throws Exception;

	public HomeworkInfo getHomeworkInfoById(String homework_id) throws Exception;

	public String getIdByName(String homework_name) throws Exception;

	public int update(HomeworkInfo homeworkInfo) throws Exception;

	public int initialize() throws Exception;

	public void deleteAssist(String homework_id) throws Exception;

}
