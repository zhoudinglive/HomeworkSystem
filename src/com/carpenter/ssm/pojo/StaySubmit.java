/**
 * 
 */
package com.carpenter.ssm.pojo;

import java.util.Date;

/**
 * @Title: StaySubmit.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:31:26
 * @version V1.0
 */
public class StaySubmit {
	private String stay_id;
	private String student_id;
	private Date stay_start;
	private Date stay_stop;
	private String leave_details;

	public String getStay_id() {
		return stay_id;
	}

	public void setStay_id(String stay_id) {
		this.stay_id = stay_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public Date getStay_start() {
		return stay_start;
	}

	public void setStay_start(Date stay_start) {
		this.stay_start = stay_start;
	}

	public Date getStay_stop() {
		return stay_stop;
	}

	public void setStay_stop(Date stay_stop) {
		this.stay_stop = stay_stop;
	}

	public String getLeave_details() {
		return leave_details;
	}

	public void setLeave_details(String leave_details) {
		this.leave_details = leave_details;
	}

}
