package com.carpenter.ssm.service;

import java.util.List;
import com.carpenter.ssm.pojo.BaseInfo;

/**   
* @Title: BaseInfoService.java 
* @Package com.carpenter.ssm.service 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月5日 下午11:36:10 
* @version V1.0   
*/

public interface BaseInfoService {
	public boolean insert(BaseInfo baseInfo) throws Exception;
	
	public boolean deleteByBaseId(String base_id) throws Exception;
	
	public List<BaseInfo> selectAll() throws Exception;
	
	public BaseInfo getBaseInfoById(String base_id) throws Exception;
	
	public boolean updatePortrait(BaseInfo baseInfo) throws Exception;
	
	public boolean createNewUser(BaseInfo baseInfo) throws Exception; 
	
}
