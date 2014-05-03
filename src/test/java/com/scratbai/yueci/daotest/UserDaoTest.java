package com.scratbai.yueci.daotest;

import static org.junit.Assert.assertEquals;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.scratbai.yueci.commons.CommonUtils;
import com.scratbai.yueci.dao.*;
import com.scratbai.yueci.pojo.WaitAuthUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
})
public class UserDaoTest {
	
	@Autowired
	UserDao userDao;
	
	//测试WaitAuthUser的添加，获取，删除操作
	@Test
	public void addAndGetAndRemoveWaitAuthUserTest() {
		WaitAuthUser user = getWaitAuthUser();
		userDao.addWaitAuthUser(user);
		WaitAuthUser findedUser = userDao.getWaitAuthUser(user.getEmailAuthCode());
		assertEquals(user.getEmailAuthCode(), findedUser.getEmailAuthCode());
		assertEquals(user.getAddDate().getTime(), findedUser.getAddDate().getTime());
		userDao.removeWaitAuthUser(user);
		WaitAuthUser findedRemovedUser = userDao.getWaitAuthUser(user.getEmailAuthCode());
		assertEquals(null, findedRemovedUser);
	}

	private WaitAuthUser getWaitAuthUser() {
		WaitAuthUser user = new WaitAuthUser();
		String uid = "testmailaddress@163.com";
		String emailAuthCode = CommonUtils.generateEmailAuthCode(uid);
		String encryptedPwd = CommonUtils.encrypt("testpassword");
		user.setAddDate(new Date(System.currentTimeMillis()));
		user.setEmailAuthCode(emailAuthCode);
		user.setEncryptedPwd(encryptedPwd);
		user.setNickname("tom");
		user.setRandomCode(CommonUtils.generateRandomCode());
		user.setUid(uid);
		return user;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
}
