package com.scratbai.yueci.service;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import javax.servlet.http.*;

import org.slf4j.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.dao.UserDao;
import com.scratbai.yueci.pojo.*;
import org.apache.commons.lang3.*;

public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final Executor exec = Executors.newCachedThreadPool();
	//private static final int MilliSECCONDS_OF_HOURS = 3600000;
	//private static final int EMAIL_VALID_TIMEOUT_HOURS = 12;
	public static final int EMAIL_VALID_TIMEOUT_MillSECCONDS = 12 * 3600000; // 12个小时
	private ObjectMapper objectMapper;

	@Override
	public ValidateResult validate(String uid, String password) {
		String salt = userDao.getSalt(uid);
		String encryptedPwd = CommonUtils.encrypt(salt + password);
		return validateByEncryptedPwd(uid, encryptedPwd);
	}

	@Override
	public ValidateResult validateByEncryptedPwd(String uid, String encryptedPwd) {
		String rightEncryptedPwd = userDao.getEncryptedPwd(uid);
		ValidateResult validateResult;
		if (rightEncryptedPwd == null || rightEncryptedPwd.equals("")) {
			boolean isWaitAuthUid = userDao.isWaitAuthUid(uid);
			if (isWaitAuthUid) {
				validateResult = ValidateResult.EMAIL_NOT_VALIDATE;
			}
			validateResult = ValidateResult.FAILURE_USERID_NOT_EXIST;
		} else if (rightEncryptedPwd.equals(encryptedPwd)) {
			validateResult = ValidateResult.SUCCESS;
		} else {
			validateResult = ValidateResult.FAILURE_PASSWORD_ERROR;
		}
		logger.debug(encryptedPwd + "," + rightEncryptedPwd);
		return validateResult;
	}

	@Override
	public String getEncryptedPwd(String uid) {
		return userDao.getEncryptedPwd(uid);
	}

	@Override
	public boolean isUidExist(String uid) {
		return userDao.isUidExist(uid);
	}

	@Override
	public void sendAuthEmail(String uid, String emailAuthCode,
			String randomCode, String emailAddress, String authPath) {
		String topic = "阅辞注册-邮箱验证";
		String finalPath = authPath + emailAuthCode + "/" + randomCode;
		String content = "尊敬的用户，您好！您收到本邮件是因为您在阅辞注册了会员。"
				+ "请点击以下链接激活您的账号，您也可以复制该链接在浏览器中打开<br>" + "<a href=\""
				+ finalPath + "\"/>" + finalPath + "</a>"
				+ "<br>如果非本人操作，请忽略此邮件";
		CommonUtils.sendEmail(topic, content, emailAddress);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.scratbai.yueci.service.UserService#addWaitAuthUser(com.scratbai.yueci.pojo.WaitAuthUser)
	 * 添加WaitAuthUser到数据库，如果已经存在相同uid的WaitAuthUser，则先删除之前的WaitAuthUser
	 */
	public void addWaitAuthUser(WaitAuthUser user) {
		userDao.removeWaitAuthUserByUid(user.getUid());
		userDao.saveObject(user);
	}

	/*
	 * 做邮箱验证，输入参数里面包括一个邮箱表示码和一个随机码，两者必须与数据库中存储的数据相匹配 才能验证成功
	 */
	@Override
	public boolean authEmail(String emailRecognitionCode, String randomCode,
			int timeLimitHours) {
		WaitAuthUser user = userDao.getWaitAuthUser(emailRecognitionCode);
		if (user == null) {
			return false;
		}
		String rightAuthCode = user.getAuthCode();
		if (rightAuthCode == null || !rightAuthCode.equals(randomCode)) {
			logger.debug("随机码比对失效");
			return false;
		}
		Date addDate = user.getAddDate();
		long addTime = addDate.getTime();
		long nowTime = System.currentTimeMillis();
		if (addTime + EMAIL_VALID_TIMEOUT_MillSECCONDS < nowTime) {
			logger.debug("验证超时:addTime" + addTime + ",nowTime:" + nowTime);
			// 删除过期的待验证用户
			userDao.removeWaitAuthUserByUid(user.getUid());;
			return false;
		}
		return true;
	}

	@Override
	public void changeToUserFromWaitAuthUser(String emailCode) {
		WaitAuthUser waitAuthUser = userDao.getWaitAuthUser(emailCode);
		User user = generateUser(waitAuthUser);
		String userDefaultConfig = "userDefaultConfig.properties";
		String wordBookSpeechType = CommonUtils.getConfigValue(
				userDefaultConfig, "userDefaultSpeechType");
		logger.debug(wordBookSpeechType);
		user.setWordBookSpeechType(wordBookSpeechType);
		userDao.saveObject(user);
		userDao.removeWaitAuthUserByUid(waitAuthUser.getUid());;
	}

	private User generateUser(WaitAuthUser waitAuthUser) {
		User user = new User();
		user.setUid(waitAuthUser.getUid());
		user.setNickname(waitAuthUser.getNickname());
		user.setEncryptedPwd(waitAuthUser.getEncryptedPwd());
		user.setSalt(waitAuthUser.getSalt());
		return user;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean isWaitAuthUidExist(String uid) {
		return userDao.isWaitAuthUid(uid);
	}

	@Override
	public String searchWord(String word) {
		logger.debug("not login user" + " request :" + word);
		StringBuilder protoData = getProtoData(word);
		protoData = regulateJson(protoData);
		saveWordData(word, protoData);
		StringBuilder response = buildResponse(protoData, word);
		logger.debug(response.toString());
		return response.toString();
	}
	
	@Override
	public String searchWordFromDb(String word) {
		logger.debug("not login user" + " request :" + word);
		StringBuilder protoData = getProtoDataFromDb(word);
		StringBuilder response = buildResponse(protoData, word);
		logger.debug(response.toString());
		return response.toString();
	}

	private void saveWordData(String word, StringBuilder protoData) {
		if (CommonUtils.isChinese(word)) {
			if (!userDao.isExistedInChineseWord(word)) {
				logger.debug("word :" + word + "is not existed");
				exec.execute(new SaveChineseWordTask(protoData.toString(),
						userDao, objectMapper));
			}
		} else {
			if (!userDao.isExistedInEnglishWord(word)) {
				logger.debug("word :" + word + " is not existed");
				exec.execute(new SaveEnglishWordTask(protoData.toString(),
						userDao, objectMapper));
			}
		}
	}

	private StringBuilder regulateJson(StringBuilder protoData) {
		return new StringBuilder(regulateJson(protoData.toString()));
	}

	@Override
	public String searchWord(User user, String word) {
		logger.debug("user:" + user.getUid() + " request :" + word);
		StringBuilder protoData = getProtoData(word);
		protoData = regulateJson(protoData);
		saveWordData(word, protoData);
		StringBuilder response = buildWordResponse(user, protoData, word);
		return response.toString();
	}
	
	@Override
	public String searchWordFromDb(User user, String word) {
		logger.debug("user:" + user.getUid() + " request :" + word);
		StringBuilder protoData = getProtoDataFromDb(word);
		StringBuilder response = buildWordResponse(user, protoData, word);
		return response.toString();
	}
	
	private StringBuilder getProtoDataFromDb(String wordName) {
		String result = "";
		try {
			if (CommonUtils.isChinese(wordName)) {
				ChineseWord tmp = userDao.findChineseWord(wordName);
				if (tmp == null) {
					tmp = new ChineseWord();
					tmp.setWord_name(null);
				}
				result = objectMapper.writeValueAsString(tmp);
				Arrays.toString(result.getBytes());
			} else {
				EnglishWord tmp = userDao.findEnglishWord(wordName);
				if (tmp == null) {
					tmp = new EnglishWord();
					tmp.setWord_name(null);
				}
				result = objectMapper.writeValueAsString(tmp);
			}
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return new StringBuilder(StringEscapeUtils.unescapeJava(StringEscapeUtils.escapeJson(result)));
	}

	/*
	 * iciba返回的json有些不一致，该方法替换掉不一致的地方，使所有的json都变现出相同的结构
	 */
	private String regulateJson(String json) {
		return json.replaceAll("(word_(pl|er|est|third|done|past|ing)\":)\"\"",
				"$1[]");
	}

	private StringBuilder buildResponse(StringBuilder response, String word) {
		StringBuilder newResponse = new StringBuilder("{\"wordObject\":");
		newResponse.append(response);
		newResponse.append(",");
		newResponse.append("\"existInWordBook\":");
		newResponse.append(false);
		newResponse.append("}");
		return newResponse;
	}

	private StringBuilder getProtoData(String word) {
		String apiKey = CommonUtils.getConfigValue("iciba.properties", "key");
		try {
			URL url = new URL("http://dict-co.iciba.com/api/dictionary.php?w="
					+ word + "&key=" + apiKey + "&type=json");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);

			// 设置http头 消息
			connection.setRequestProperty("Accept", "application/json");
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			StringBuilder response = new StringBuilder("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				response.append(lines);
			}
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 在金山词霸返回的数据上面添加额外的数据
	private StringBuilder buildWordResponse(User user, StringBuilder response,
			String word) {
		boolean existInWordBook = userDao.isExistedInWordBook(user, word);
		logger.debug("user:" + user.getUid() + " have " + word + "? "
				+ existInWordBook);
		StringBuilder newResponse = new StringBuilder("{\"wordObject\":");
		newResponse.append(response);
		newResponse.append(",");
		newResponse.append("\"existInWordBook\":");
		newResponse.append(existInWordBook);
		newResponse.append("}");
		return newResponse;
	}

	@Override
	public User getUser(String uid) {
		return userDao.getUser(uid);
	}

	@Override
	public void addWordToWordBook(User user, String word) {
		userDao.addWordToWordBook(user, word);
	}

	@Override
	public void removeWordFromWordBook(User user, String word) {
		userDao.removeWordFromWordBook(user, word);
	}

	@Override
	public List<EnglishWord> getWordsFromWordBook(String uid,
			int wordCountPerPage, int pageIndex) {
		return userDao.getWordFromWordBook(uid, wordCountPerPage, pageIndex);
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public long getPageCountInWordBook(String uid, int wordCountPerPage) {
		long wordsCount = userDao.getWordsCountInWordBook(uid);
		long pageCount = wordsCount % wordCountPerPage == 0 ? wordsCount
				/ wordCountPerPage : wordsCount / wordCountPerPage + 1;
		return pageCount;
	}

	@Override
	public void forgiveMe(HttpServletResponse response, String uid) {
		Cookie cookie = new Cookie("persistentId", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		userDao.deletePersistentUserByUid(uid);
	}

	@Override
	public void rememberMe(HttpServletResponse response, String uid,
			int persistentLoginEffectiveSeconds) {
		userDao.deletePersistentUserByUid(uid);
		PersistentUser persistentUser = new PersistentUser();
		long nowTimeStamp = System.currentTimeMillis();
		Date addTime = new Date(nowTimeStamp);
		long endTimeStamp = nowTimeStamp
				+ ((long) persistentLoginEffectiveSeconds) * 1000;
		Date endTime = new Date(endTimeStamp);
		logger.debug(((Long) nowTimeStamp).toString());
		logger.debug("persistentLoginEffectiveSeconds:"
				+ persistentLoginEffectiveSeconds);
		logger.debug(((Long) endTimeStamp).toString());
		persistentUser.setAddTime(addTime);
		persistentUser.setEndTime(endTime);
		persistentUser.setUid(uid);
		String salt = CommonUtils.generateRandomCode(16);
		String persistentId = uid + "" + CommonUtils.encrypt(uid + salt);
		persistentUser.setPersistentId(persistentId);
		userDao.saveObject(persistentUser);

		Cookie cookie = new Cookie("persistentId", persistentId);
		cookie.setMaxAge(persistentLoginEffectiveSeconds);
		response.addCookie(cookie);
	}

	@Override
	public PersistentUser getPersistentUserByPersistentId(String persistentId) {
		return userDao.getPersistentUserByPersistentId(persistentId);
	}

	@Override
	public void deletePersistentUserByPersistentId(String persistentId) {
		userDao.deletePersistentUserByPersistentId(persistentId);
	}

	@Override
	public void setSpeechType(User user, String speechType) {
		user.setWordBookSpeechType(speechType);
		userDao.saveObject(user);
	}

	@Override
	public void setNickname(User user, String nickname) {
		user.setNickname(nickname);
		userDao.saveObject(user);
	}

	@Override
	public void changePassword(User user, String newPassword) {
		String salt = CommonUtils.generateSalt();
		String encryptedPassword = CommonUtils.encrypt(salt + newPassword);
		user.setSalt(salt);
		user.setEncryptedPwd(encryptedPassword);
		userDao.saveObject(user);
	}

	private void sendResetPasswordEmail(String emailRecognitionCode,
			String authCode, String emailAddress, String authPath) {
		String topic = "阅辞-重置密码";
		String finalPath = authPath + "/" + emailRecognitionCode + "/" + authCode;
		String content = "尊敬的用户，您好！您收到本邮件是因为您在阅辞申请了重置密码"
				+ "请点击以下链接重置您的密码，您也可以复制该链接在浏览器中打开<br>" + "<a href=\""
				+ finalPath + "\"/>" + finalPath + "</a>";
		CommonUtils.sendEmail(topic, content, emailAddress);
	}

	@Override
	public void requestResetPassword(String email) {
		String authCode = CommonUtils.generateRandomCode(6);
		String emailRecognitionCode = email.replaceAll("[@.]", "");
		String resetPath = CommonUtils.getConfigValue(
				"webAppConfig.properties", "domain") + "authBeforeResetPassword";
		sendResetPasswordEmail(emailRecognitionCode, authCode, email, resetPath);

		ResetPasswordUser resetPasswordUser = new ResetPasswordUser();
		resetPasswordUser.setAuthCode(authCode);
		resetPasswordUser.setEmailRecognitionCode(emailRecognitionCode);
		resetPasswordUser.setUid(email);
		resetPasswordUser.setDeadline(new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
		userDao.removeResetPasswordUser(email);  //删除之前的重置密码信息，如果有的话
		userDao.saveObject(resetPasswordUser);
	}

	@Override
	public ResetPasswordUser getResetPasswordUserByEmailRecognitionCode(
			String emailRecognitionCode) {
		return userDao
				.getResetPasswordUserByEmailRecognitionCode(emailRecognitionCode);
	}

	@Override
	public void enableResetPassword(ResetPasswordUser resetPasswordUser) {
		resetPasswordUser.setResetEnable(true);
		resetPasswordUser.setAuthCode(CommonUtils.generateRandomCode(6));
		resetPasswordUser.setDeadline(new Date(System.currentTimeMillis() + 1800000));
		userDao.saveObject(resetPasswordUser);
	}

	@Override
	public void removeResetPasswordUserByUid(String uid) {
		userDao.removeResetPasswordUser(uid);
	}

	@Override
	public void register(String uid, String password, String nickname) {
		String emailRecognitionCode = uid.replaceAll("[@.]", "");
		String salt = CommonUtils.generateSalt();
		String authCode = CommonUtils.generateRandomCode();
		String encryptedPwd = CommonUtils.encrypt(salt + password);
		
		WaitAuthUser user = new WaitAuthUser();
		user.setUid(uid);
		user.setEncryptedPwd(encryptedPwd);
		user.setSalt(salt);
		user.setNickname(nickname);
		user.setEmailRecognitionCode(emailRecognitionCode);
		user.setAuthCode(authCode);
		user.setAddDate(new Date(System.currentTimeMillis()));
		
		addWaitAuthUser(user);
		
		String domain = CommonUtils.getConfigValue("webAppConfig.properties", "domain");
		String authPath = domain + "authRegisterEmail/";
		sendAuthEmail(uid, emailRecognitionCode, authCode, uid, authPath);
	}

	@Override
	public void addFeedback(Feedback feedback) {
		userDao.saveObject(feedback);
	}

	@Override
	public String fuzzySearch(String word) {
		List<WordsTableItem> words = userDao.fuzzySearch(word);
		List<String> wordsString = new ArrayList<String>();
		wordsString.add("");
		for (WordsTableItem wordItem : words) {
			wordsString.add(wordItem.getName());
		}
		String wordsJson = null;
		try {
			wordsJson = objectMapper.writer().writeValueAsString(wordsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return wordsJson;
	}
}
