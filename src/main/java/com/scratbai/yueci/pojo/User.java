package com.scratbai.yueci.pojo;

import java.util.*;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("users")
public class User{
	@Id
	private ObjectId id;
	private String uid;
	private String encryptedPwd;
	private String nickname;
	private String salt;

	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id = id;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
