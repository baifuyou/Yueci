package com.scratbai.yueci.dao;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;

import com.mongodb.*;
import com.scratbai.yueci.pojo.*;

public class UserDaoImpl implements UserDao {
	
	private Datastore datastore;
	//MongoDB的DB对象，为了更加灵活的操作数据库，混合使用MongoDB Java Driver 和 Morphia
	private DB db;
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public String getEncryptedPwd(String uid) {
		DBCollection collection = db.getCollection("users");
		DBObject object = collection.findOne(new BasicDBObject("uid",uid), new BasicDBObject("encryptedPwd",1));
		return object == null ? null : (String) object.get("encryptedPwd");
	}

	@Override
	public boolean isUidExist(String uid) {
		String encryptedPwd = getEncryptedPwd(uid);
		return encryptedPwd != null && !encryptedPwd.equals("");
	}

	@Override
	public void addWaitAuthUser(WaitAuthUser user) {
		datastore.save(user);
	}

	@Override
	public String getRandomCodeByEmailAuthCode(String emailAuthCode) {
		DBCollection collection = db.getCollection("waitAuthUsers");
		DBObject object = collection.findOne(new BasicDBObject("emailAuthCode",emailAuthCode), new BasicDBObject("randomCode","1"));
		return object == null ? null :(String) object.get("randomCode");
	}

	@Override
	public boolean isWaitAuthUid(String uid) {
		DBCollection collection = db.getCollection("waitAuthUsers");
		DBObject object = collection.findOne(new BasicDBObject("uid", uid), new BasicDBObject("uid", 1));
		return object != null;
	}

	@Override
	public WaitAuthUser getWaitAuthUser(String emailAuthCode) {
		WaitAuthUser user = datastore.find(WaitAuthUser.class, "emailAuthCode =", emailAuthCode).get();
		return user;
	}

	@Override
	public void addUser(User user) {
		datastore.save(user);
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	@Override
	public void removeWaitAuthUser(WaitAuthUser user) {
		datastore.delete(datastore.find(WaitAuthUser.class, "uid =", user.getUid()));
	}

	@Override
	public User getUser(String uid) {
		return datastore.find(User.class, "uid =", uid).get();
	}

	@Override
	public String getSalt(String uid) {
		DBCollection collection = db.getCollection("users");
		DBObject object = collection.findOne(new BasicDBObject("uid",uid), new BasicDBObject("salt",1));
		return object == null ? null : (String) object.get("salt");
	}

	@Override
	public void addWordToWordBook(User user,String word) {
		user.addFrequencyWord(word);
		datastore.save(user);
	}

	@Override
	public void removeWordFromWordBook(User user, String word) {
		user.removeFrequencyWord(word);
		datastore.save(user);
	}

}
