package com.scratbai.yueci.pojo;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity
public class PersistentUser {
	@Id
	private ObjectId id;
	private String uid;
	private String persistentId;
	private Date addTime;
	private Date endTime;
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
	public String getPersistentId() {
		return persistentId;
	}
	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
