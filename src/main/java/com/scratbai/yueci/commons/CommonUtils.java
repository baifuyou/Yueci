package com.scratbai.yueci.commons;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;

public class CommonUtils {
	
	private static final char[] SYMBOLS = "qwertyuiopasdfghjklzxcvbnm1234567890._".toCharArray();
	private static final int SYMBOLS_LENGTH = SYMBOLS.length;

	public static String generateEmailAuthCode(String uid) {
		return encrypt(uid);
	}

	/*
	 * 生成六位随机码
	 */
	public static String generateRandomCode() {
		return generateRandomCode(6);
	}
	
	//生成length位随机码
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
		InputStream accountConfigStream = CommonUtils.class.getClassLoader()
				.getResourceAsStream("mailAccountConfig.properties");
		Properties serverConfig = new Properties();
		Properties accountConfig = new Properties();
		try {
			serverConfig.load(serverConfigStream);
			accountConfig.load(accountConfigStream);
			Session session = Session.getInstance(serverConfig);
			Message message = new MimeMessage(session);
			message.setSubject(topic);
			message.setText(content);
			message.setContent(content, "text/html;charset = UTF-8");
			String mailAccount = accountConfig.getProperty("account");
			String mailPassword = accountConfig.getProperty("password");
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

}
