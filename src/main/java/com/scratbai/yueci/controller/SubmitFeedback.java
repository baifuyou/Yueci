package com.scratbai.yueci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.pojo.Feedback;
import com.scratbai.yueci.service.UserService;

@Controller
public class SubmitFeedback {

	@Autowired
	private UserService userService;

	@RequestMapping("feedback")
	public String showFeedback() {
		return "feedback";
	}

	@RequestMapping("submitFeedback")
	public String saveFeedback(@RequestParam(required = true) String title,
			@RequestParam(required = true) String content,
			@RequestParam(required = true) String type) {
		Feedback feedback = new Feedback();
		feedback.setTitle(title);
		feedback.setContent(content);
		feedback.setType(type);
		userService.addFeedback(feedback);
		return "feedbackSuccess";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
