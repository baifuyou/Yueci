package com.scratbai.yueci.controller;

import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.pojo.User;
import com.scratbai.yueci.service.UserService;

@Controller
public class Home {

	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("home")
	public String home(HttpSession session, Model model) {
		Object object = session.getAttribute("isLogin");
		boolean isLogin = object == null ? false : (Boolean) object;
		model.addAttribute("isLogin", session.getAttribute("isLogin"));
		User user = (User) session.getAttribute("user");
		if (isLogin) {
			model.addAttribute("user", user);
		}
		return "home";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletResponse response, HttpSession session, Model model) {
		String uid = (String)session.getAttribute("uid");
		session.removeAttribute("user");
		session.removeAttribute("uid");
		userService.forgiveMe(response, uid);
		return "home";
	}

	@RequestMapping("home/search/{word}")
	public String homeSearchBody(@PathVariable String word,
			HttpSession session, Model model) {
		model.addAttribute("word", word);
		return home(session, model);
	}

	@RequestMapping("searchWord/{word}")
	@ResponseBody
	public String searchWord(@PathVariable String word, HttpSession session) {
		logger.debug("request search:" + word);
		User user = session.getAttribute("user") == null ? null
				: (User) session.getAttribute("user");
		String response = null;
		if (user == null) {
			response = userService.searchWord(word);
		} else {
			response = userService.searchWord(user, word);
		}
		return response;
	}

	@RequestMapping("addWordToWordBook/{word}")
	@ResponseBody
	public String addWordToWordBook(HttpSession session,
			@PathVariable String word) {
		User user = (User) session.getAttribute("user");
		userService.addWordToWordBook(user, word);
		return JsonStatic.STATE_SUCCESS;
	}

	@RequestMapping("removeWordFromWordBook/{word}")
	@ResponseBody
	public String removeWordFromWordBook(HttpSession session,
			@PathVariable String word) {
		User user = (User) session.getAttribute("user");
		logger.debug("user:" + user.getUid() + " remove " + word);
		userService.removeWordFromWordBook(user, word);
		return JsonStatic.STATE_SUCCESS;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
