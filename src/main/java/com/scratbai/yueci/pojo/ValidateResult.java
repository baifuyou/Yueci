package com.scratbai.yueci.pojo;

public enum ValidateResult {
	SUCCESS, FAILURE_USERID_NOT_EXIST, FAILURE_PASSWORD_ERROR, EMAIL_NOT_VALIDATE;
	
	@Override
	public String toString () {
		if (this == SUCCESS) {
			return "登陆成功";
		} 
		
		if (this == FAILURE_PASSWORD_ERROR) {
			return "用户密码错误";
		}
		
		if (this == FAILURE_USERID_NOT_EXIST) {
			return "用户名不存在";
		}
		if (this == EMAIL_NOT_VALIDATE) {
			return "邮箱未验证";
		}
		return null;
	}
}
