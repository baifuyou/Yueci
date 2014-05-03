package com.scratbai.yueci.pojo;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("waitAuthUsers")
public class WaitAuthUser {
	@Id
	private ObjectId id;
	private String uid;
	private String salt;
	private String encryptedPwd;
	private String nickname;
	private String emailAuthCode;
	private String randomCode;
	private Date addDate;
	
	public WaitAuthUser( ) {
		
	}
	
	public WaitAuthUser(String uid, String encryptedPwd, String nickname,
			String emailAuthCode, String randomCode, Date addDate) {
		this.uid = uid;
		this.encryptedPwd = encryptedPwd;
		this.nickname = nickname;
		this.emailAuthCode = emailAuthCode;
		this.randomCode = randomCode;
		this.addDate = addDate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEncryptedPwd() {
		return encryptedPwd;
	}

	public void setEncryptedPwd(String encryptedPwd) {
		this.encryptedPwd = encryptedPwd;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmailAuthCode() {
		return emailAuthCode;
	}

	public void setEmailAuthCode(String emailAuthCode) {
		this.emailAuthCode = emailAuthCode;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
