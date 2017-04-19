package com.carpenter.ssm.pojo;

import java.util.Date;

/**
 * @Title: VoteInfo.java
 * @Package com.carpenter.ssm.pojo
 * @Description: TODO
 * @author carpenter
 * @date 2016年8月10日 下午10:18:21
 * @version V1.0
 */

public class VoteInfo {
	private String vote_id;
	private String vote_name;
	private String vote_details;
	private String vote_tobeVoted;
	private Date vote_createtime;
	private Date vote_deadline;
	private String vote_initiator;

	public String getVote_id() {
		return vote_id;
	}

	public void setVote_id(String vote_id) {
		this.vote_id = vote_id;
	}

	public String getVote_name() {
		return vote_name;
	}

	public void setVote_name(String vote_name) {
		this.vote_name = vote_name;
	}

	public String getVote_details() {
		return vote_details;
	}

	public void setVote_details(String vote_details) {
		this.vote_details = vote_details;
	}

	public String getVote_tobeVoted() {
		return vote_tobeVoted;
	}

	public void setVote_tobeVoted(String vote_tobeVoted) {
		this.vote_tobeVoted = vote_tobeVoted;
	}

	public Date getVote_createtime() {
		return vote_createtime;
	}

	public void setVote_createtime(Date vote_createtime) {
		this.vote_createtime = vote_createtime;
	}

	public Date getVote_deadline() {
		return vote_deadline;
	}

	public void setVote_deadline(Date vote_deadline) {
		this.vote_deadline = vote_deadline;
	}

	public String getVote_initiator() {
		return vote_initiator;
	}

	public void setVote_initiator(String vote_initiator) {
		this.vote_initiator = vote_initiator;
	}

}
