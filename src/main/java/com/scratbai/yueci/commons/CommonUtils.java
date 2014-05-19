package com.scratbai.yueci.commons;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {

	private static final char[] SYMBOLS = "qwertyuiopasdfghjklzxcvbnm1234567890"
			.toCharArray();
	private static final int SYMBOLS_LENGTH = SYMBOLS.length;
	private static final Logger logger = LoggerFactory
			.getLogger(CommonUtils.class);
	private static final Map<String, String> keyValueCache = new HashMap<String, String>();

	/*
	 * 生成六位随机码
	 */
	public static String generateRandomCode() {
		return generateRandomCode(6);
	}

	public static String md5Hex(String str) {
		return DigestUtils.md5Hex(str);
	}

	// 生成length位随机码
	public static String generateRandomCode(int length) {
		Random random = new Random(System.currentTimeMillis());
		char[] randomCode = new char[length];
		for (int i = 0; i < length; i++) {
			randomCode[i] = SYMBOLS[random.nextInt(SYMBOLS_LENGTH)];
		}
		return new String(randomCode);
	}

	public static String encrypt(String password) {
		return DigestUtils.sha1Hex(password);
	}

	public static boolean sendEmail(String topic, String content,
			String emailAddress) {
		InputStream serverConfigStream = CommonUtils.class.getClassLoader()
				.getResourceAsStream("mailServerConfig.properties");

		Properties serverConfig = new Properties();
		try {
			serverConfig.load(serverConfigStream);
			Session session = Session.getInstance(serverConfig);
			Message message = new MimeMessage(session);
			message.setSubject(topic);
			message.setText(content);
			message.setContent(content, "text/html;charset = UTF-8");
			String mailAccount = getConfigValue("mailAccountConfig.properties", "account");
			String mailPassword = getConfigValue("mailAccountConfig.properties", "password");
			message.setFrom(new InternetAddress(mailAccount));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					emailAddress));
			Transport.send(message, mailAccount, mailPassword);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * 从配置文件fileName中读取key的值 如果fileName不存在或者key不存在，将返回null
	 */
	public static String getConfigValue(String fileName, String key) {
		String cacheKey = fileName + ":" + key;
		String cacheValue = keyValueCache.get(cacheKey);
		if (cacheValue != null) {
			return cacheValue;
		}
		InputStream configStream = CommonUtils.class.getClassLoader()
				.getResourceAsStream(fileName);
		Properties config = new Properties();
		try {
			config.load(configStream);
		} catch (IOException e) {
			logger.error("config file: " + fileName + "not found");
			e.printStackTrace();
			return null;
		}
		String value = config.getProperty(key);
		if (value == null) {
			logger.debug("key: " + key + "is no exist in config file: "
					+ fileName);
		}
		keyValueCache.put(cacheKey, value);
		return value;
	}

	public static boolean isChinese(String word) {
		String isChinese = "^[\u4e00-\u9fa5]+$";
		return Pattern.matches(isChinese, word);
	}

	public static String generateSalt() {
		return generateRandomCode(16);
	}

}
