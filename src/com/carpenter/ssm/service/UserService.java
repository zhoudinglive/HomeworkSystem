package com.carpenter.ssm.service;

import com.carpenter.ssm.pojo.User;

/**   
* @Title: UserService.java 
* @Package com.carpenter.ssm.service 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月24日 下午2:44:52 
* @version V1.0   
*/

public interface UserService {
	public String getUserPasswordById(String username) throws Exception;
	
	public boolean updatePassword(String username, String oldPassword, String newPassword) throws Exception;
	
}
