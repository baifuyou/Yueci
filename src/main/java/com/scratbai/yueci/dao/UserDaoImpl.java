package com.scratbai.yueci.dao;

import java.util.*;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.*;

import com.mongodb.*;
import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.pojo.*;

public class UserDaoImpl implements UserDao {

	private Datastore datastore;
	// MongoDB的DB对象，为了更加灵活的操作数据库，混合使用MongoDB Java Driver 和 Morphia
	private DB db;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getEncryptedPwd(String uid) {
		DBCollection collection = db.getCollection("users");
		DBObject object = collection.findOne(new BasicDBObject("uid", uid),
				new BasicDBObject("encryptedPwd", 1));
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
		DBObject object = collection.findOne(new BasicDBObject("emailAuthCode",
				emailAuthCode), new BasicDBObject("randomCode", "1"));
		return object == null ? null : (String) object.get("randomCode");
	}

	@Override
	public boolean isWaitAuthUid(String uid) {
		DBCollection collection = db.getCollection("waitAuthUsers");
		DBObject object = collection.findOne(new BasicDBObject("uid", uid),
				new BasicDBObject("uid", 1));
		return object != null;
	}

	@Override
	public WaitAuthUser getWaitAuthUser(String emailAuthCode) {
		WaitAuthUser user = datastore.find(WaitAuthUser.class,
				"emailAuthCode =", emailAuthCode).get();
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
		datastore.delete(datastore.find(WaitAuthUser.class, "uid =",
				user.getUid()));
	}

	@Override
	public User getUser(String uid) {
		return datastore.find(User.class, "uid =", uid).get();
	}

	@Override
	public String getSalt(String uid) {
		DBCollection collection = db.getCollection("users");
		DBObject object = collection.findOne(new BasicDBObject("uid", uid),
				new BasicDBObject("salt", 1));
		return object == null ? null : (String) object.get("salt");
	}

	@Override
	public void addWordToWordBook(User user, String word) {
		boolean isChinese = CommonUtils.isChinese(word);
		WordReference wordRef = new WordReference(word, user.getUid());
		wordRef.setIsChinese(isChinese);
		datastore.save(wordRef);
	}

	@Override
	public void removeWordFromWordBook(User user, String word) {
		Query<WordReference> query = datastore.createQuery(WordReference.class)
				.filter("word =", word).filter("uid =", user.getUid());
		datastore.delete(query);
	}

	@Override
	public void addEnglishWord(EnglishWord eWord) {
		datastore.save(eWord);
	}

	@Override
	public void addChineseWord(ChineseWord cWord) {
		datastore.save(cWord);
	}

	@Override
	public boolean isExistedInWordBook(User user, String word) {
		Query<WordReference> query = datastore.createQuery(WordReference.class)
				.filter("word =", word).filter("uid =", user.getUid())
				.retrievedFields(true, "word");
		return query.get() != null;
	}

	@Override
	public boolean isExistedInChineseWord(String wordName) {
		Query<ChineseWord> query = datastore.createQuery(ChineseWord.class)
				.filter("word_name =", wordName)
				.retrievedFields(true, "word_name");
		return query.get() != null;
	}

	@Override
	public boolean isExistedInEnglishWord(String wordName) {
		Query<EnglishWord> query = datastore.createQuery(EnglishWord.class)
				.filter("word_name =", wordName)
				.retrievedFields(true, "word_name");
		return query.get() != null;
	}

	@Override
	public List<EnglishWord> getWordFromWordBook(String uid,
			int wordCountPerPage, int pageIndex) {
		Query<WordReference> queryWordReference = datastore
				.createQuery(WordReference.class).filter("uid =", uid)
				.offset((pageIndex - 1) * wordCountPerPage)
				.limit(wordCountPerPage);
		List<WordReference> wordReferences = queryWordReference.asList();
		List<EnglishWord> englishWords = new ArrayList<EnglishWord>();
		for (WordReference wordRef : wordReferences) {
			EnglishWord englishWord = datastore.find(EnglishWord.class,
					"word_name =", wordRef.getWord()).get();
			englishWords.add(englishWord);
		}
		return englishWords;
	}

	@Override
	public long getWordsCountInWordBook(String uid) {
		return datastore.find(WordReference.class).countAll();
	}

	@Override
	public void deletePersistentUserByUid(String uid) {
		Query<PersistentUser> query = datastore.createQuery(PersistentUser.class)
				.filter("uid =", uid);
		datastore.delete(query);
	}

	@Override
	public void addPersistentLoginUser(PersistentUser persistentUser) {
		datastore.save(persistentUser);
	}

	@Override
	public PersistentUser getPersistentUserByPersistentId(String persistentId) {
		PersistentUser persistentUser =  datastore.find(PersistentUser.class, "persistentId =", persistentId).get();
		return persistentUser;
	}

	@Override
	public void deletePersistentUserByPersistentId(String persistentId) {
		Query<PersistentUser> query = datastore.createQuery(PersistentUser.class)
				.filter("persistentId =", persistentId);
		datastore.delete(query);
	}

}
