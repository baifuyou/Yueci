package com.scratbai.yueci.dao;

import java.util.List;

import com.scratbai.yueci.pojo.ChineseWord;
import com.scratbai.yueci.pojo.EnglishWord;
import com.scratbai.yueci.pojo.PersistentUser;
import com.scratbai.yueci.pojo.ResetPasswordUser;
import com.scratbai.yueci.pojo.User;
import com.scratbai.yueci.pojo.WaitAuthUser;
import com.scratbai.yueci.pojo.WordsTableItem;

public interface UserDao {

	String getEncryptedPwd(String uid);

	boolean isUidExist(String uid);

	String getauthCodeByemailRecoginitionCode(String emailRecoginitionCode);

	boolean isWaitAuthUid(String uid);

	WaitAuthUser getWaitAuthUser(String emailCode);

	User getUser(String uid);

	String getSalt(String uid);

	void addWordToWordBook(User user, String word);

	void removeWordFromWordBook(User user, String word);
	
	boolean isExistedInWordBook(User user, String word);

	boolean isExistedInEnglishWord(String wordName);

	boolean isExistedInChineseWord(String wordName);

	List<EnglishWord> getWordFromWordBook(String uid, int wordCountPerPage,
			int pageIndex);

	long getWordsCountInWordBook(String uid);

	PersistentUser getPersistentUserByPersistentId(String persistentId);

	void deletePersistentUserByPersistentId(String persistentId);

	void deletePersistentUserByUid(String uid);

	<T> void saveObject(T object);

	ResetPasswordUser getResetPasswordUserByEmailRecognitionCode(
			String emailRecognitionCode);

	void removeWaitAuthUserByUid(String uid);

	void removeResetPasswordUser(String uid);

	List<WordsTableItem> fuzzySearch(String word);

	void removeWaitAuthTimeoutUser(long currentTimeMillis);

}
