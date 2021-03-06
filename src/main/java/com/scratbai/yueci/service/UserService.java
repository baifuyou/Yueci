package com.scratbai.yueci.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.scratbai.yueci.pojo.EnglishWord;
import com.scratbai.yueci.pojo.Feedback;
import com.scratbai.yueci.pojo.PersistentUser;
import com.scratbai.yueci.pojo.ResetPasswordUser;
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

	public void sendAuthEmail(String uid, String emailRecognitionCode,
			String authCode, String emailAddress, String authPath);

	public boolean authEmail(String emailAuthCode, String randomCode,
			int timeLimit);

	public void changeToUserFromWaitAuthUser(String emailCode);

	public boolean isWaitAuthUidExist(String uid);

	public String searchWord(User user, String word);
	
	public String searchWordFromDb(User user, String word);

	public User getUser(String uid);

	public void addWaitAuthUser(WaitAuthUser user);

	public void addWordToWordBook(User user, String word);

	public String searchWord(String word);
	
	public String searchWordFromDb(String word);

	public void removeWordFromWordBook(User user, String word);

	public List<EnglishWord> getWordsFromWordBook(String uid,
			int wordCountPerPage, int pageIndex);

	public long getPageCountInWordBook(String uid, int wordCountPerPage);

	public void forgiveMe(HttpServletResponse response, String uid);

	public PersistentUser getPersistentUserByPersistentId(String persistentId);

	public void deletePersistentUserByPersistentId(String persistentId);

	public void rememberMe(HttpServletResponse response, String uid,
			int persistentLoginEffectiveSeconds);

	void setSpeechType(User user, String speechType);

	public void setNickname(User user, String nickname);

	public void changePassword(User user, String newPassword);

	public void requestResetPassword(String email);

	public ResetPasswordUser getResetPasswordUserByEmailRecognitionCode(
			String emailRecognitionCode);

	public void enableResetPassword(ResetPasswordUser resetPasswordUser);

	public void removeResetPasswordUserByUid(String uid);

	public void register(String uid, String password, String nickname);

	public void addFeedback(Feedback feedback);

	public String fuzzySearch(String word);
}
