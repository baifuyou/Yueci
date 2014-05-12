package com.scratbai.yueci.controller;

import javax.servlet.http.*;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class NotLoginFilter implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		HttpSession session = request.getSession();
		if (checkUserLogin(session)) {
			return true;
		} else {
			response.sendRedirect("../login");
			return false;
		}
	}
	
	private boolean checkUserLogin(HttpSession session) {
		Object user = session.getAttribute("user");
		Object uid = session.getAttribute("uid");
		return user != null && uid != null;
	}

}
