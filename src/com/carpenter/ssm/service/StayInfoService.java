package com.carpenter.ssm.service;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import com.carpenter.ssm.pojo.StayInfo;

/**   
* @Title: StayInfoService.java 
* @Package com.carpenter.ssm.service 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月10日 下午9:42:07 
* @version V1.0   
*/

public interface StayInfoService {
	public boolean createNewStay(StayInfo stayInfo) throws Exception;
	
	public boolean deleteStayById(String stay_id) throws Exception;
	
	public List<StayInfo> selectAll() throws Exception;
	
	public StayInfo getStayInfoById(String stay_id) throws Exception; 
	
	public boolean updateStayInfo(StayInfo stayInfo) throws Exception;
	
}
