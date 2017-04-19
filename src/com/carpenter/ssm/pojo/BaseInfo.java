package com.carpenter.ssm.pojo;

/**
 * @Title: BaseInfo.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月5日 下午10:59:40
 * @version V1.0
 */


public class BaseInfo {
	private String base_id;
	private String base_name;
	private String base_sex;
	private String base_portrait;
	private String personal_call;
	private String base_address;
	private String home_tel;

	public String getBase_id() {
		return base_id;
	}

	public void setBase_id(String base_id) {
		this.base_id = base_id;
	}

	public String getBase_name() {
		return base_name;
	}

	public void setBase_name(String base_name) {
		this.base_name = base_name;
	}

	public String getBase_sex() {
		return base_sex;
	}

	public void setBase_sex(String base_sex) {
		this.base_sex = base_sex;
	}

	public String getBase_portrait() {
		return base_portrait;
	}

	public void setBase_portrait(String base_portrait) {
		this.base_portrait = base_portrait;
	}

	public String getPersonal_call() {
		return personal_call;
	}

	public void setPersonal_call(String personal_call) {
		this.personal_call = personal_call;
	}

	public String getBase_address() {
		return base_address;
	}

	public void setBase_address(String base_address) {
		this.base_address = base_address;
	}

	public String getHome_tel() {
		return home_tel;
	}

	public void setHome_tel(String home_tel) {
		this.home_tel = home_tel;
	}
}
