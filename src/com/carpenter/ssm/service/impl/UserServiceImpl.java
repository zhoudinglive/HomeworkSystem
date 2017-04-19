package com.carpenter.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.dao.UserDaoMapper;
import com.carpenter.ssm.pojo.User;
import com.carpenter.ssm.service.UserService;

/**   
* @Title: UserServiceImpl.java 
* @Package com.carpenter.ssm.service.impl 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月24日 下午2:47:42 
* @version V1.0   
*/

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDaoMapper userDaoMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public String getUserPasswordById(String username) throws Exception {
		try {
			User user = this.userDaoMapper.getUserById(username);
			if(user!=null){
				return user.getPassword();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updatePassword(String username, String oldPassword, String newPassword) throws Exception {
		if(username==null||oldPassword==null||newPassword==null){
			return false;
		}
		String dbPassword = getUserPasswordById(username);
		if(dbPassword==null){
			return false;
		}
		if(!this.passwordEncoder.matches(oldPassword, dbPassword)){
			return false;
		}
		if(this.userDaoMapper.updatePassword(username, this.passwordEncoder.encode(newPassword))>0){
			return true;
		}
		return false;
	}

}
