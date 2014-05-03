package com.scratbai.yueci.commonstest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.scratbai.yueci.commons.CommonUtils;

import static org.junit.Assert.assertEquals;

public class CommonUtilsTest {


	//测试生成的是不是六位字母
	@Test
	public void testGenerateRandomCode() {
		String randomCode = CommonUtils.generateRandomCode();
		assertEquals(randomCode.length(),6);
		Pattern pattern = Pattern.compile("^[a-z]{6}$");
		Matcher matcher = pattern.matcher(randomCode);
		assertEquals(true, matcher.matches());
	}
	
	@Test
	public void testEncrypt() {
		String password = "baifuyou2013@163.com";
		String encryptedPwd1 = CommonUtils.encrypt(password);
		String encryptedPwd2 = CommonUtils.encrypt(password);
		assertEquals(encryptedPwd1, encryptedPwd2);
	}
	
	@Test
	public void testSendMail() {
		boolean isSendSuccess = CommonUtils.sendEmail("test mail",
				"this is a test mail from javamail", "baifuyou2013@163.com");
		assertEquals(isSendSuccess, true);
	}
}
