package com.scratbai.yueci.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.pojo.*;
import com.scratbai.yueci.service.*;

@Controller
public class UserControl {
	
	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("user")
	public String userSetting(HttpSession session, Model model ) {
		User user = (User)session.getAttribute("user");
		String speechType = user.getWordBookSpeechType().equals("am") ? "美式发音" : "英式发音";
		model.addAttribute("user", user);
		model.addAttribute("speechType", speechType);
		return "userControl";
	}
	
	@RequestMapping("user/setSpeechType/{speechType}")
	@ResponseBody
	public String setSpeechType(HttpSession session, @PathVariable String speechType) {
		User user = (User)session.getAttribute("user");
		userService.setSpeechType(user, speechType);
		return JsonStatic.STATE_SUCCESS;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
