package com.scratbai.yueci.controller;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.*;

import com.scratbai.yueci.pojo.User;
import com.scratbai.yueci.service.UserService;

public class AutoLogin implements HandlerInterceptor {

	@Autowired
	private UserService userService;

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
		//在session持久化的过程中user不会被持久化；下面重新获取user并写入session
		if (isLogin && user == null) {
			user = userService.getUser(uid);
			session.setAttribute("user", user);
		}
		return true;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
