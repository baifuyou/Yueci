package com.scratbai.yueci.service;

import com.scratbai.yueci.dao.UserDao;

public class DealValidTimeOutUid {
	
	private UserDao userDao;
	
	public void removeValidTimeOutUid() {
		userDao.removeWaitAuthTimeoutUser(System.currentTimeMillis() - UserServiceImpl.EMAIL_VALID_TIMEOUT_MillSECCONDS);
		System.out.println("task running");
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
}
