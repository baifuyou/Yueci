package com.scratbai.yueci.controller;

import java.util.*;

import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.*;

import com.scratbai.yueci.pojo.*;
import com.scratbai.yueci.service.UserService;

public class AutoLogin implements HandlerInterceptor {

	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		
		HttpSession session = request.getSession();
		boolean isLogin = (Boolean) session.getAttribute("isLogin") == null ? false
				: (Boolean) session.getAttribute("isLogin");
		User user = (User) session.getAttribute("user");
		String uid = (String) session.getAttribute("uid");
		if (isLogin) {
			//在session持久化的过程中user不会被持久化；下面重新获取user并写入session
			logger.debug(uid + " is login in");
			if (user == null) {
				user = userService.getUser(uid);
				session.setAttribute("user", user);
			}
		} else {
			String persistentId = getCookie(request,"persistentId");
			if (persistentId == null) {
				return true;
			} else {
				autoLogin(session, persistentId);
			}
		}
		return true;
	}

	private void autoLogin(HttpSession session, String persistentId) {
		PersistentUser persistentUser = userService.getPersistentUserByPersistentId(persistentId);
		Date now = new Date(System.currentTimeMillis());
		logger.debug("persistentUser == null :" + (persistentUser == null));
		Date endTime = persistentUser.getEndTime();
		if (endTime.after(now)) {
			String uid = persistentUser.getUid();
			User user = userService.getUser(uid);
			session.setAttribute("user", user);
			session.setAttribute("uid", uid);
			session.setAttribute("isLogin", true);
		} else {
			//userService.deletePersistentUserByPersistentId(persistentId);
		}
	}

	private String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) 
			return null;
		for (Cookie cookie : cookies) {
			String nowName = cookie.getName();
			if (nowName.equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
