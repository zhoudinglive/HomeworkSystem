package com.carpenter.ssm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.carpenter.ssm.pojo.HomeworkSubmit;

/**
 * @Title: HomeworkSubmitDaoMapper.java
 * @Package com.carpenter.ssm.dao
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:35:56
 * @version V1.0
 */

public interface HomeworkSubmitDaoMapper {
	public int insert(HomeworkSubmit homeworkSubmit) throws Exception;

	public List<HomeworkSubmit> selectAllById(String homework_id) throws Exception;

	public List<HomeworkSubmit> getHomeworkSubmitById(String student_id) throws Exception;
	
	public HomeworkSubmit getOnlyHomeworkSubmitById(@Param("homework_id")String homework_id, @Param("student_id")String student_id) throws Exception;
	
	public int updateTimeFresh(@Param("homework_id")String homework_id, @Param("student_id")String student_id, @Param("last_updatetime")Date last_updatetime) throws Exception;

}
