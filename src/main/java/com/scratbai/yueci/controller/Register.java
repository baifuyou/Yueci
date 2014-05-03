package com.scratbai.yueci.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.pojo.WaitAuthUser;
import com.scratbai.yueci.service.UserService;

@Controller
public class Register {

	@Autowired
	private UserService userService;
	private final static int MAX_TIME_AUTH_EMAIL_HOURS = 12;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping("/requestRegister")
	public String requestRegister() {
		return "register/register";
	}

	@RequestMapping("/register")
	public String register(@RequestParam String uid,
			@RequestParam String password, String nickname) {
		if (userService.isUidExist(uid) || userService.isWaitAuthUidExist(uid)) {
			return "register/registerFailure";
		}
		String emailAuthCode = CommonUtils.generateEmailAuthCode(uid);
		String salt = CommonUtils.generateRandomCode(16);
		String randomCode = CommonUtils.generateRandomCode();
		String encryptedPwd = CommonUtils.encrypt(salt + password);
		
		WaitAuthUser user = new WaitAuthUser();
		user.setUid(uid);
		user.setEncryptedPwd(encryptedPwd);
		user.setSalt(salt);
		user.setNickname(nickname);
		user.setEmailAuthCode(emailAuthCode);
		user.setRandomCode(randomCode);
		user.setAddDate(new Date(System.currentTimeMillis()));
		
		userService.addWaitAuthUser(user);
		
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("webAppConfig.properties");
		Properties webAppConfig = new Properties();
		try {
			webAppConfig.load(stream);
		} catch (IOException e) {
			logger.error("webAppConfig文件不能找到。该文件配置有网站的部署信息");
			e.printStackTrace();
			return "errorHandlerPage";
		}
		String domain = webAppConfig.getProperty("domain");
		String authPath = domain + "authRegisterEmail/";
		userService.sendAuthEmail(uid, emailAuthCode, randomCode, uid, authPath);
		return "register/registerSuccess";
	}
	
	@RequestMapping("/validateEmailUsable")
	@ResponseBody
	public String validateEmailUsable(@RequestParam String uid) {
		return userService.isUidExist(uid) || userService.isWaitAuthUidExist(uid) ? "unusable" : "usable";	
	}

	@RequestMapping("authRegisterEmail/{emailCode}/{randomCode}")
	public String authRegisterEmail(@PathVariable String emailCode,
			@PathVariable String randomCode) {
		if (!userService.authEmail(emailCode, randomCode, MAX_TIME_AUTH_EMAIL_HOURS)) 
			return "register/emailAuthFailure";
		userService.changeToUserFromWaitAuthUser(emailCode);
		return "register/emailAuthSuccess";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
