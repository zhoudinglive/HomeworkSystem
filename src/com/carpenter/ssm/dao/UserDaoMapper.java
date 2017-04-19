package com.carpenter.ssm.dao;

import org.apache.ibatis.annotations.Param;

import com.carpenter.ssm.pojo.User;

/**   
* @Title: UserDaoMapper.java 
* @Package com.carpenter.ssm.dao 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月24日 下午2:33:25 
* @version V1.0   
*/

public interface UserDaoMapper {
	public User getUserById(String username) throws Exception;
	
	public int updatePassword(@Param("username")String username, @Param("password")String password) throws Exception;
	
}
