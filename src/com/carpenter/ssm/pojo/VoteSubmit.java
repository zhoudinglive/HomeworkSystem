package com.carpenter.ssm.pojo;

/**
 * @Title: VoteSubmit.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月11日 下午1:32:58
 * @version V1.0
 */

public class VoteSubmit {
	private String vote_id;
	private String student_id;
	private String vote_choice;

	public String getVote_id() {
		return vote_id;
	}

	public void setVote_id(String vote_id) {
		this.vote_id = vote_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getVote_choice() {
		return vote_choice;
	}

	public void setVote_choice(String vote_choice) {
		this.vote_choice = vote_choice;
	}

}
