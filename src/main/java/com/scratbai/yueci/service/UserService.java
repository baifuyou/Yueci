package com.scratbai.yueci.service;

import com.scratbai.yueci.pojo.User;
import com.scratbai.yueci.pojo.ValidateResult;
import com.scratbai.yueci.pojo.WaitAuthUser;

public interface UserService {
	
	public final static String EMAIL_CONFIG_FILE_PATH = "mailConfig.properties";
	public final static int DEFAULT_TARGET_QUERY_TIMES = 3;
	public ValidateResult validate(String uid, String password);

	public ValidateResult validateByEncryptedPwd(String uid, String encryptedPwd);

	public String getEncryptedPwd(String uid);

	public boolean isUidExist(String uid);

	public void sendAuthEmail(String uid, String emailAuthCode,
			String randomCode, String emailAddress, String authPath);

	public boolean authEmail(String emailAuthCode, String randomCode,
			int timeLimit);

	public void changeToUserFromWaitAuthUser(String emailCode);

	public boolean isWaitAuthUidExist(String uid);

	public String searchWord(User user, String word);

	public User getUser(String uid);

	public void addWaitAuthUser(WaitAuthUser user);

	public void addWordToWordBook(User user, String word);

	public String searchWord(String word);

	public void removeWordFromWordBook(User user, String word);
}
