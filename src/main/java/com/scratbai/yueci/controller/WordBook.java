package com.scratbai.yueci.controller;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;

import com.scratbai.yueci.service.UserService;

@Controller
public class WordBook {
	
	@Autowired
	private UserService userService;
	
	public String wordBook(HttpSession session, Model model) { //TODO 这个实现可能有严重的性能问题
		model.addAttribute(session.getAttribute("user"));
		return "wordBook";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
