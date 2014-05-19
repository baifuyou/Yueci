package com.scratbai.yueci.pojo;

import java.util.*;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/*
 * 重置密码时使用的持久类,包含uid（email）和验证码信息
 */
@Entity
public class ResetPasswordUser {
	@Id
	private ObjectId id;
	private String uid;
	private String authCode;
	private String emailRecognitionCode; //在重置链接中识别用户的字段，
	private boolean resetEnable = false;
	private Date deadline;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getEmailRecognitionCode() {
		return emailRecognitionCode;
	}

	public void setEmailRecognitionCode(String emailRecognitionCode) {
		this.emailRecognitionCode = emailRecognitionCode;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public boolean getResetEnable() {
		return resetEnable;
	}

	public void setResetEnable(boolean resetEnable) {
		this.resetEnable = resetEnable;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

}
