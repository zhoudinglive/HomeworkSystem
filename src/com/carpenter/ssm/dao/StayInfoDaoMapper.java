package com.carpenter.ssm.dao;

import java.util.List;

import com.carpenter.ssm.pojo.StayInfo;

/**   
* @Title: StayInfoDaoMapper.java 
* @Package com.carpenter.ssm.dao 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月10日 下午6:17:50 
* @version V1.0   
*/

public interface StayInfoDaoMapper {
	public int insert(StayInfo stayInfo) throws Exception;
	
	public int delete(String stay_id) throws Exception;
	
	public int deleteByName(String stay_name) throws Exception;
	
	public List<StayInfo> selectAll() throws Exception;
	
	public StayInfo getStayInfoById(String stay_id) throws Exception;
	
	public String getIdByName(String stay_name) throws Exception;
	
	public int update(StayInfo stayInfo) throws Exception;
	
	public int initialize() throws Exception;
	
	public void deleteAssist(String stay_id) throws Exception;
	
}
