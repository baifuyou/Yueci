package com.scratbai.yueci.service;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.*;

import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.dao.UserDao;
import com.scratbai.yueci.pojo.*;

public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final Executor exec = Executors.newCachedThreadPool();
	private static final int MilliSECCONDS_OF_HOURS = 3600000;

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
	public void addWaitAuthUser(WaitAuthUser user) {
		userDao.addWaitAuthUser(user);
	}

	/*
	 * 做邮箱验证，输入参数里面包括一个邮箱表示码和一个随机码，两者必须与数据库中存储的数据相匹配
	 * 才能验证成功，如果验证失败，会删除待验证用户信息，需要重新注册
	 */
	@Override
	public boolean authEmail(String emailAuthCode, String randomCode,
			int timeLimitHours) {
		WaitAuthUser user = userDao.getWaitAuthUser(emailAuthCode);
		String rightRandomCode = user.getRandomCode();
		if (rightRandomCode == null || !rightRandomCode.equals(randomCode)) {
			// 删除待验证用户信息
			logger.debug("随机码比对失效");
			userDao.removeWaitAuthUser(user);
			return false;
		}
		Date addDate = user.getAddDate();
		long addTime = addDate.getTime();
		long nowTime = System.currentTimeMillis();
		if (addTime + 12 * MilliSECCONDS_OF_HOURS < nowTime) {
			// 删除待验证用户信息
			logger.debug("验证超时:addTime" + addTime + ",nowTime:" + nowTime);
			userDao.removeWaitAuthUser(user);
			return false;
		}
		return true;
	}

	@Override
	public void changeToUserFromWaitAuthUser(String emailCode) {
		WaitAuthUser waitAuthUser = userDao.getWaitAuthUser(emailCode);
		User user = generateUser(waitAuthUser);
		userDao.addUser(user);
		userDao.removeWaitAuthUser(waitAuthUser);
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
		StringBuilder protoData = getProtoResponseData(word);
		protoData = regulateJson(protoData);
		logger.debug(protoData.toString());
		saveWordData(word, protoData);
		StringBuilder response = buildResponse(protoData, word);
		return response.toString();
	}
	
	private void saveWordData(String word, StringBuilder protoData) {
		if (CommonUtils.isChinese(word)) {
			exec.execute(new SaveChineseWordTask(protoData.toString()));
		} else {
			exec.execute(new SaveEnglishWordTask(protoData.toString(), userDao));
		}
	}

	private StringBuilder regulateJson(StringBuilder protoData) {
		return new StringBuilder(regulateJson(protoData.toString()));
	}

	@Override
	public String searchWord(User user, String word) {
		logger.debug("user:" + user.getUid() + " request :" + word);
		StringBuilder response = getProtoResponseData(word);
		response = buildResponse(user, response, word);
		return response.toString();
	}
	
	/*
	 * iciba返回的json有些不一致，该方法替换掉不一致的地方，使所有的json都变现出相同的结构
	 */
	private String regulateJson(String json) {
		return json.replaceAll(
				"(word_(pl|er|est|third|done|past|ing)\":)\"\"", "$1[]");
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

	private StringBuilder getProtoResponseData(String word) {
		InputStream configStream = this.getClass().getClassLoader()
				.getResourceAsStream("iciba.properties");
		Properties icibaProp = new Properties();
		try {
			icibaProp.load(configStream);
		} catch (IOException e) {
			logger.error("iciba.properties配置文件找不到");
			e.printStackTrace();
			return null;
		}
		String apiKey = icibaProp.getProperty("key");
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
	private StringBuilder buildResponse(User user, StringBuilder response,
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
}
