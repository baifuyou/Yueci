package com.scratbai.yueci.dao;

import com.scratbai.yueci.pojo.ChineseWord;
import com.scratbai.yueci.pojo.EnglishWord;
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

}
