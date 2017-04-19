package com.carpenter.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.dao.BaseInfoDaoMapper;
import com.carpenter.ssm.pojo.BaseInfo;
import com.carpenter.ssm.service.BaseInfoService;

/**
 * @Title: BaseInfoServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月5日 下午11:40:10
 * @version V1.0
 */

@Service("baseInfoService")
public class BaseInfoServiceImpl implements BaseInfoService {
	@Autowired
	private BaseInfoDaoMapper baseInfoDao;

	@Override
	public boolean insert(BaseInfo baseInfo) throws Exception {
		if(baseInfo == null){
			return false;
		}
		try {
			if (this.baseInfoDao.insert(baseInfo) > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteByBaseId(String base_id) throws Exception {
		try {
			if (base_id != null && !base_id.equals("") && this.baseInfoDao.delete(base_id) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<BaseInfo> selectAll() throws Exception {
		try {
			return this.baseInfoDao.selectAll();
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public BaseInfo getBaseInfoById(String base_id) throws Exception {
		try {
			BaseInfo baseInfo = this.baseInfoDao.getBaseInfoById(base_id);
			if (baseInfo != null) {
				return baseInfo;
			}
			System.out.println(1);
			return null;
		} catch (Exception e) {
			System.out.println(2);
			return null;
		}
	}

	@Override
	public boolean updatePortrait(BaseInfo baseInfo) throws Exception {
		try {
			if (this.baseInfoDao.update(baseInfo) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean createNewUser(BaseInfo baseInfo) throws Exception {
		return false;

	}

}
