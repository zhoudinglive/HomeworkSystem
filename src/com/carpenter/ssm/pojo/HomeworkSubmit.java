package com.carpenter.ssm.pojo;

import java.util.Date;

/**
 * @Title: HomeworkSubmit.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:29:19
 * @version V1.0
 */

public class HomeworkSubmit {
	private String homework_id;
	private String student_id;
	private String save_path;
	private Date last_updatetime;

	public String getHomework_id() {
		return homework_id;
	}

	public void setHomework_id(String homework_id) {
		this.homework_id = homework_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getSave_path() {
		return save_path;
	}

	public void setSave_path(String save_path) {
		this.save_path = save_path;
	}

	public Date getLast_updatetime() {
		return last_updatetime;
	}

	public void setLast_updatetime(Date last_updatetime) {
		this.last_updatetime = last_updatetime;
	}
	
}
