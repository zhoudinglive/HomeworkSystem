package com.carpenter.ssm.dao;

import java.util.List;

import com.carpenter.ssm.pojo.BaseInfo;

/**   
* @Title: BaseInfoDaoMapper.java 
* @Package com.carpenter.ssm.dao 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月6日 上午11:10:43 
* @version V1.0   
*/

public interface BaseInfoDaoMapper {
	public int insert(BaseInfo baseInfo) throws Exception;

	public int delete(String base_id) throws Exception;

	public List<BaseInfo> selectAll() throws Exception;

	public BaseInfo getBaseInfoById(String base_id) throws Exception;

	public int update(BaseInfo baseInfo) throws Exception;
}
