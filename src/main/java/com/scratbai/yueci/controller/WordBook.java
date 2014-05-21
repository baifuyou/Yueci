package com.scratbai.yueci.controller;

import java.util.*;

import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratbai.yueci.pojo.*;
import com.scratbai.yueci.service.UserService;

@Controller
public class WordBook {
	
	private final static int WORD_COUNT_PER_PAGE = 10;
	
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper objectMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//pageIndex 从1开使
	@RequestMapping(value = "wordBook/list/{pageIndex}", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String listWordsFromWordBook(HttpSession session, @PathVariable int pageIndex, Model model) {  
		String uid = (String) session.getAttribute("uid");
		User user = (User) session.getAttribute("user");
		List<EnglishWord> words = userService.getWordsFromWordBook(uid, WORD_COUNT_PER_PAGE, pageIndex);
		long pageCount = userService.getPageCountInWordBook(uid, WORD_COUNT_PER_PAGE);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("words", words);
		response.put("speechType", user.getWordBookSpeechType());
		response.put("pageCount", pageCount);
		response.put("pageIndex", pageIndex);
		try {
			return objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			logger.error("object mapper error, message:" + e.getMessage());
			e.printStackTrace();
			return "{\"state\":\"json error\"}";
		}
	}
	
	@RequestMapping("wordBook")
	public String wordBook(HttpSession session) {
		return  "wordBook";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
