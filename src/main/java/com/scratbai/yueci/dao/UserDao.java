package com.scratbai.yueci.dao;

import java.util.List;

import com.scratbai.yueci.pojo.ChineseWord;
import com.scratbai.yueci.pojo.EnglishWord;
import com.scratbai.yueci.pojo.PersistentUser;
import com.scratbai.yueci.pojo.User;
import com.scratbai.yueci.pojo.WaitAuthUser;

public interface UserDao {

	String getEncryptedPwd(String uid);

	boolean isUidExist(String uid);

	void addWaitAuthUser(WaitAuthUser user);

	String getRandomCodeByEmailAuthCode(String emailAuthCode);

	boolean isWaitAuthUid(String uid);

	WaitAuthUser getWaitAuthUser(String emailCode);

	void addUser(User user);

	void removeWaitAuthUser(WaitAuthUser user);

	User getUser(String uid);

	String getSalt(String uid);

	void addWordToWordBook(User user, String word);

	void removeWordFromWordBook(User user, String word);

	void addEnglishWord(EnglishWord eWord);

	void addChineseWord(ChineseWord cWord);

	boolean isExistedInWordBook(User user, String word);

	boolean isExistedInEnglishWord(String wordName);

	boolean isExistedInChineseWord(String wordName);

	List<EnglishWord> getWordFromWordBook(String uid, int wordCountPerPage,
			int pageIndex);

	long getWordsCountInWordBook(String uid);

	void addPersistentLoginUser(PersistentUser persistentUser);

	PersistentUser getPersistentUserByPersistentId(String persistentId);

	void deletePersistentUserByPersistentId(String persistentId);

	void deletePersistentUserByUid(String uid);

}
