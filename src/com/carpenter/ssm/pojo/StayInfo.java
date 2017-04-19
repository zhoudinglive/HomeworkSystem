package com.carpenter.ssm.pojo;

import java.util.Date;

/**
 * @Title: StayInfo.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 下午6:04:26
 * @version V1.0
 */

public class StayInfo {
	private String stay_id;
	private String stay_name;
	private String stay_details;
	private Date stay_begintime;
	private Date stay_stoptime;
	private Date stay_createtime;
	private Date stay_deadline;
	private String stay_initiator;

	public String getStay_id() {
		return stay_id;
	}

	public void setStay_id(String stay_id) {
		this.stay_id = stay_id;
	}

	public String getStay_name() {
		return stay_name;
	}

	public void setStay_name(String stay_name) {
		this.stay_name = stay_name;
	}

	public String getStay_details() {
		return stay_details;
	}

	public void setStay_details(String stay_details) {
		this.stay_details = stay_details;
	}

	public Date getStay_begintime() {
		return stay_begintime;
	}

	public void setStay_begintime(Date stay_begintime) {
		this.stay_begintime = stay_begintime;
	}

	public Date getStay_stoptime() {
		return stay_stoptime;
	}

	public void setStay_stoptime(Date stay_stoptime) {
		this.stay_stoptime = stay_stoptime;
	}

	public Date getStay_createtime() {
		return stay_createtime;
	}

	public void setStay_createtime(Date stay_createtime) {
		this.stay_createtime = stay_createtime;
	}

	public Date getStay_deadline() {
		return stay_deadline;
	}

	public void setStay_deadline(Date stay_deadline) {
		this.stay_deadline = stay_deadline;
	}

	public String getStay_initiator() {
		return stay_initiator;
	}

	public void setStay_initiator(String stay_initiator) {
		this.stay_initiator = stay_initiator;
	}

}
