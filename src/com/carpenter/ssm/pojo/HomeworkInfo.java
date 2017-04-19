package com.carpenter.ssm.pojo;

import java.nio.file.Path;
import java.util.Date;

/**
 * @Title: HomeworkInfo.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月7日 下午6:42:22
 * @version V1.0
 */

public class HomeworkInfo {
	private String homework_id;
	private String homework_name;
	private String homework_details;
	private String homework_photo;
	private Date homework_createtime;
	private Date homework_deadline;
	private String homework_savepath;
	private String homework_initiator;

	public String getHomework_id() {
		return homework_id;
	}

	public void setHomework_id(String homework_id) {
		this.homework_id = homework_id;
	}

	public String getHomework_name() {
		return homework_name;
	}

	public void setHomework_name(String homework_name) {
		this.homework_name = homework_name;
	}

	public String getHomework_details() {
		return homework_details;
	}

	public void setHomework_details(String homework_details) {
		this.homework_details = homework_details;
	}

	public String getHomework_photo() {
		return homework_photo;
	}

	public void setHomework_photo(String homework_photo) {
		this.homework_photo = homework_photo;
	}

	public Date getHomework_createtime() {
		return homework_createtime;
	}

	public void setHomework_createtime(Date homework_createtime) {
		this.homework_createtime = homework_createtime;
	}

	public Date getHomework_deadline() {
		return homework_deadline;
	}

	public void setHomework_deadline(Date homework_deadline) {
		this.homework_deadline = homework_deadline;
	}

	public String getHomework_savepath() {
		return homework_savepath;
	}

	public void setHomework_savepath(String homework_savepath) {
		this.homework_savepath = homework_savepath;
	}

	public String getHomework_initiator() {
		return homework_initiator;
	}

	public void setHomework_initiator(String homework_initiator) {
		this.homework_initiator = homework_initiator;
	}

}
