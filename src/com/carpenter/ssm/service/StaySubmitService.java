package com.carpenter.ssm.service;

import java.util.Date;
import java.util.List;

import com.carpenter.ssm.pojo.StaySubmit;

/**
 * @Title: StaySubmitService.java
 * @Package com.carpenter.ssm.service
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月12日 上午9:36:46
 * @version V1.0
 */

public interface StaySubmitService {
	public boolean insert(StaySubmit staySubmit) throws Exception;
	
	public List<StaySubmit> selectAllByStayId(String stay_id) throws Exception;
	
	public List<StaySubmit> selectAllByStudentId(String student_id) throws Exception;
	
	public StaySubmit getExactStaySubmitById(String stay_id, String student_id) throws Exception;
	
	public boolean updateInterval(StaySubmit staySubmit) throws Exception;
	
}
