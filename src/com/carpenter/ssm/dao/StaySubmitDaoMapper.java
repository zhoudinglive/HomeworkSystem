package com.carpenter.ssm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.carpenter.ssm.pojo.StaySubmit;

/**
 * @Title: StaySubmitDaoMapper.java
 * @Package com.carpenter.ssm.dao
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:43:13
 * @version V1.0
 */

public interface StaySubmitDaoMapper {
	public int insert(StaySubmit staySubmit) throws Exception;

	public List<StaySubmit> selectAllById(String stay_id) throws Exception;

	public List<StaySubmit> getStaySubmitById(String student_id) throws Exception;
	
	public StaySubmit getOnlyStaySubmitById(@Param("stay_id")String stay_id, @Param("student_id")String student_id) throws Exception;
	
	public int update(StaySubmit staySubmit) throws Exception;
	
}
