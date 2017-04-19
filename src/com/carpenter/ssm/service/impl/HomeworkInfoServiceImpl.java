package com.carpenter.ssm.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpenter.ssm.assist.FileMaker;
import com.carpenter.ssm.assist.StringToDate;
import com.carpenter.ssm.dao.BaseInfoDaoMapper;
import com.carpenter.ssm.dao.HomeworkInfoDaoMapper;
import com.carpenter.ssm.pojo.BaseInfo;
import com.carpenter.ssm.pojo.HomeworkInfo;
import com.carpenter.ssm.pojo.HomeworkSubmit;
import com.carpenter.ssm.service.HomeworkInfoService;

/**
 * @Title: HomeworkInfoServiceImpl.java
 * @Package com.carpenter.ssm.service.impl
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 上午10:05:47
 * @version V1.0
 */

@Service("homeworkInfoService")
public class HomeworkInfoServiceImpl implements HomeworkInfoService {
	@Autowired
	private BaseInfoDaoMapper baseInfoDao;

	@Autowired
	private HomeworkInfoDaoMapper homeworkInfoDao;

	@Autowired
	private HomeworkSubmitServiceImpl homeworkSubmitService;
	
	@Autowired
	private HomeworkInfoServiceImpl homeworkInfoService;

	@Override
	public boolean createNewHomework(HomeworkInfo homeworkInfo) throws Exception {
		if (homeworkInfo == null || homeworkInfo.getHomework_initiator() == null
				|| !StringToDate.compareToNow(homeworkInfo.getHomework_deadline())) {
			return false;
		}
		try {
			this.homeworkInfoDao.initialize();
			homeworkInfo.setHomework_savepath("/home/upload/" + homeworkInfo.getHomework_name());
			Date date = new Date();
			homeworkInfo.setHomework_createtime(date);
			String tmp_homeworkname = homeworkInfo.getHomework_name();
			String tmp_homeworkdetails = homeworkInfo.getHomework_details();
			homeworkInfo.setHomework_name(homeworkInfo.getHomework_name().replace("\"", "\\\""));
			homeworkInfo.setHomework_details(homeworkInfo.getHomework_details().replace("\"", "\\\""));
			if (this.homeworkInfoDao.insert(homeworkInfo) > 0) {
				String id = null;
				try {
					id = this.homeworkInfoDao.getIdByName(homeworkInfo.getHomework_name());
					if (id == null) {
						this.homeworkInfoDao.deleteByName(homeworkInfo.getHomework_name());
						return false;
					}
					homeworkInfo.setHomework_name(tmp_homeworkname);
					homeworkInfo.setHomework_details(tmp_homeworkdetails);
					if (FileMaker.createNewFileDir("/home/upload/" + homeworkInfo.getHomework_name())) {
						List<BaseInfo> baseInfoList = this.baseInfoDao.selectAll();
						for (BaseInfo baseInfo : baseInfoList) {
							HomeworkSubmit homeworkSubmit = new HomeworkSubmit();
							homeworkSubmit.setHomework_id(id);
							homeworkSubmit.setStudent_id(baseInfo.getBase_id());
							//System.out.println(homeworkInfo.getHomework_createtime());
							homeworkSubmit.setLast_updatetime(this.homeworkInfoService.getHomeworkById(id).getHomework_createtime());
							homeworkSubmit.setSave_path(homeworkInfo.getHomework_savepath() + File.separator
									+ baseInfo.getBase_id() + "-" + baseInfo.getBase_name());
							
							if (FileMaker.createNewFileDir(homeworkInfo.getHomework_savepath() + File.separator
									+ baseInfo.getBase_id() + "-" + baseInfo.getBase_name())) {
								this.homeworkSubmitService.insert(homeworkSubmit);
							} else {
								this.homeworkInfoDao.deleteByName(homeworkInfo.getHomework_name());
								FileMaker.deleteFileDir("/home/upload/" + homeworkInfo.getHomework_name());
								return false;
							}
						}
					} else {
						this.homeworkInfoDao.deleteByName(homeworkInfo.getHomework_name());
						return false;
					}
				} catch (Exception e) {
					this.homeworkInfoDao.deleteByName(homeworkInfo.getHomework_name());
					FileMaker.deleteFileDir("/home/upload/" + homeworkInfo.getHomework_name());
					return false;
				}
				return true;
			} else {
				FileMaker.deleteFileDir("/home/upload/" + homeworkInfo.getHomework_name());
				return false;
			}
		} catch (Exception e) {
			this.homeworkInfoDao.deleteByName(homeworkInfo.getHomework_name());
			FileMaker.deleteFileDir("/home/upload/" + homeworkInfo.getHomework_name());
			return false;
		}
	}

	@Override
	public boolean deleteHomeworkById(String homework_id) throws Exception {
		try {
			FileMaker.deleteFileDir(this.homeworkInfoDao.getHomeworkInfoById(homework_id).getHomework_savepath());
			if (homework_id != null && !homework_id.equals("") && this.homeworkInfoDao.delete(homework_id) > 0) {
				try {
					this.homeworkInfoDao.deleteAssist(homework_id);
				} catch (Exception e) {
					//e.printStackTrace();
					return false;
				}

				return true;
			}
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<HomeworkInfo> selectAll() throws Exception {
		try {
			return this.homeworkInfoDao.selectAll();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public HomeworkInfo getHomeworkById(String homework_id) throws Exception {
		try {
			HomeworkInfo homeworkInfo = this.homeworkInfoDao.getHomeworkInfoById(homework_id);
			if (homeworkInfo != null) {
				return homeworkInfo;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updateHomeworkDetailsById(String homework_id, String homework_details) throws Exception {
		if (homework_details == null) {
			return false;
		}
		try {
			HomeworkInfo homeworkInfo = new HomeworkInfo();
			homeworkInfo.setHomework_id(homework_id);
			homeworkInfo.setHomework_details(homework_details.replace("\"", "\\\""));
			if (this.homeworkInfoDao.update(homeworkInfo) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateHomeworkPhotoById(String homework_id, String homework_photo) throws Exception {
		try {
			if (Paths.get(homework_photo) == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		try {
			HomeworkInfo homeworkInfo = new HomeworkInfo();
			homeworkInfo.setHomework_id(homework_id);
			homeworkInfo.setHomework_photo(homework_photo.toString());
			if (this.homeworkInfoDao.update(homeworkInfo) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateHomeworkDeadlineById(String homework_id, String homework_deadline) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		Date date = null;
		try {
			date = sdf.parse(homework_deadline);
			if (date == null || date.toString().equals("")) {
				System.out.println(1);
				return false;
			}
			if (StringToDate.compareIfDate1BiggerThanDate2(date,
					this.homeworkInfoDao.getHomeworkInfoById(homework_id).getHomework_createtime()) <= 0) {
				System.out.println(2);
				return false;
			}
		} catch (Exception e) {
			System.out.println(3);
			return false;
		}
		try {
			HomeworkInfo homeworkInfo = new HomeworkInfo();
			homeworkInfo.setHomework_id(homework_id);

			homeworkInfo.setHomework_deadline(date);
			if (this.homeworkInfoDao.update(homeworkInfo) > 0) {
				return true;
			}
			System.out.println(4);
			return false;
		} catch (Exception e) {
			System.out.println(5);
			return false;
		}
	}

}
