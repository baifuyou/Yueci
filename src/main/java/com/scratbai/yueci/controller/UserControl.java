package com.scratbai.yueci.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.pojo.*;
import com.scratbai.yueci.service.*;

@Controller
public class UserControl {

	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("user")
	public String userSetting(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		return "userControl";
	}

	@RequestMapping("user/saveProfileSetting")
	@ResponseBody
	public String saveProfileSetting(HttpSession session,
			@RequestParam(required = true) String speechType,
			@RequestParam(required = true) String nickname) {
		User user = (User) session.getAttribute("user");
		userService.setSpeechType(user, speechType);
		userService.setNickname(user, nickname);
		return JsonStatic.STATE_SUCCESS;
	}

	@RequestMapping("user/changePassword")
	@ResponseBody
	public String changePassword(HttpSession session,
			@RequestParam(required = true) String oldPassword,
			@RequestParam(required = true) String newPassword) {
		User user = (User) session.getAttribute("user");
		ValidateResult validateResult = userService.validate(user.getUid(),
				oldPassword);
		if (validateResult != ValidateResult.SUCCESS) {
			return JsonStatic.STATE_PASSWORD_ERROR;
		}
		userService.changePassword(user, newPassword);
		return JsonStatic.STATE_SUCCESS;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
