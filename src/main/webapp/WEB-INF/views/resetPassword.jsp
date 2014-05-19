<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置密码</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/resetPassword.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/resetPassword.js"></script>
</head>
<body>
<div class = "container">
	<form class="form-horizontal">
				<div class="form-group">
					<label for="passowrd"
						class="col-sm-1 control-label">新密码</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" id="new-password"
							placeholder="新密码">
						<span id = "password-format-error" class = "info-error hidden">密码长度应该在10到20位之间，并且不可以是纯数字</span>
					</div>
				</div>
				<input id = "auth-code" type = "hidden" value = "${authCode }">
				<input id = "email-recognition-code" type ="hidden" value = "${emailRecognitionCode }">
				<div class="form-group">
					<label for="passowrd"
						class="col-sm-2 control-label">重复新密码</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" id="new-password2"
							placeholder="重复新密码">
						<span id = "two-password-not-identical" class = "info-error hidden">两次密码不一致</span>
					</div>
				</div>

				<div class="">
					<button type="submit" id="save-password" class="btn btn-primary">重置</button>
					<span class = "hidden" id = "reset-password-info"></span>
				</div>
	</form>
</div>
</body>
</html>