package com.scratbai.yueci.controller;

import java.io.OutputStream;

import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.scratbai.yueci.commons.CommonUtils;

public class NotLoginInterceptor implements HandlerInterceptor {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
			String accept = request.getHeader("Accept");
			if (accept.contains("json"))  {
				OutputStream out = response.getOutputStream();
				out.write(JsonStatic.STATE_NOT_LOGIN.getBytes());
			} else {
				String domain = CommonUtils.getConfigValue("webAppConfig.properties", "domain");
				response.sendRedirect(domain + "login");
			}
			return false;
		}
	}
	
	private boolean checkUserLogin(HttpSession session) {
		Object user = session.getAttribute("user");
		Object uid = session.getAttribute("uid");
		return user != null && uid != null;
	}

}
