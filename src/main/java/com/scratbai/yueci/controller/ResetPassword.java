package com.scratbai.yueci.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scratbai.yueci.pojo.*;
import com.scratbai.yueci.service.UserService;

/*
 * 实现重置密码功能
 * requestResetPassword会发送一封邮件（邮件中的验证链接包含了一个能唯一表示email地址的emailRecognitionCode和一个随机的authCode）
 * 并且在数据库中添加一条ResetPasswordUser的记录。
 * 用户受到邮件后点击邮件中的链接，可以请求到resetPassword（authBeforeResetPassword方法）。authBeforeResetPassword根据用户请求中的
 * emailRecognitionCode和authCode进行验证，验证必须满足emailRecoginitionCode和authCode分别相等，并且验证时的时间在deadline
 * （deadline存放在ResetPasswordUser中）之前。如果验证不通过，转向404页面，验证通过则将ResetPasswordUser的resetEnable设置成
 * true，将deadline设置位当前时间向后推迟30分钟的时间，重新设置authCode。然后在数据库和中保存ResetPasswordUser。
 * 然后向用户返回重置密码的页面（该页面携带了隐藏的authCode),用户在重置页面提交新密码到resetPassword（doResetPassword方法）。
 * doResetPassword方法再次验证authCode和deadline，成功后重置密码！
 */
@Controller
public class ResetPassword {

	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("requestResetPassword")
	@ResponseBody
	public String requestResetPassword(String email, Model model) {
		String response = "";
		if (userService.isUidExist(email)) {
			logger.debug(email + " : matchs success");
			userService.requestResetPassword(email);
			response = JsonStatic.STATE_SUCCESS;
		} else {
			logger.debug(email + " : matchs failure");
			response = JsonStatic.STATE_INVALID_DATA;
		}
		return response;
	}

	@RequestMapping("authBeforeResetPassword/{emailRecognitionCode}/{authCode}")
	public String authBeforeResetPassword(
			@PathVariable String emailRecognitionCode,
			@PathVariable String authCode, Model model) {

		String page = null;
		ResetPasswordUser resetPasswordUser = userService
				.getResetPasswordUserByEmailRecognitionCode(emailRecognitionCode);
		Date now = new Date(System.currentTimeMillis());
		if (resetPasswordUser != null
				&& resetPasswordUser.getAuthCode().equals(authCode)
				&& resetPasswordUser.getDeadline().after(now)
				&& (!resetPasswordUser.getResetEnable())) {

			userService.enableResetPassword(resetPasswordUser);
			page = "redirect:/setNewPassword" + "/" + resetPasswordUser.getEmailRecognitionCode();
		} else if (resetPasswordUser != null) {
			userService
					.removeResetPasswordUserByUid(resetPasswordUser.getUid());
			page = "redirect:/resetPasswordFailure";
		} else {
			page = "redirect:/resetPasswordFailure";
		}
		return page;
	}

	@RequestMapping("setNewPassword/{emailRecognitionCode}")
	public String showResetPasswordPage(Model model, @PathVariable String emailRecognitionCode) {
		ResetPasswordUser resetPasswordUser = userService
				.getResetPasswordUserByEmailRecognitionCode(emailRecognitionCode);
		model.addAttribute("authCode", resetPasswordUser.getAuthCode());
		model.addAttribute("emailRecognitionCode",
				resetPasswordUser.getEmailRecognitionCode());
		return "resetPassword";
	}

	@RequestMapping("doResetPassword")
	@ResponseBody
	public String doResetPassword(String authCode, String newPassword,
			String emailRecognitionCode) {
		logger.debug("emailRecognitionCode" + emailRecognitionCode);
		ResetPasswordUser resetPasswordUser = userService
				.getResetPasswordUserByEmailRecognitionCode(emailRecognitionCode);
		Date now = new Date(System.currentTimeMillis());
		String response = null;
		String uid = "";
		if (resetPasswordUser != null && resetPasswordUser.getAuthCode().equals(authCode)
				&& resetPasswordUser.getDeadline().after(now)) {
			uid = resetPasswordUser.getUid();
			User user = userService.getUser(uid);
			userService.changePassword(user, newPassword);
			response = JsonStatic.STATE_SUCCESS;
		} else {
			response = JsonStatic.STATE_ERROR;
		}
		userService.removeResetPasswordUserByUid(uid);
		return response;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
