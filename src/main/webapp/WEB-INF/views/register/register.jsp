<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link href="resources/css/register.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script type="text/javascript" src = "resources/js/register.js"></script>
<title>注册-阅辞</title>
</head>
<body>
	<div class="container">

		<form class="form-signin" role="form" action="register" method="post">
			<h2 class="form-signin-heading">现在注册，成为阅辞会员</h2>
			<div><input id= "inputEmail" type="text" name="uid" class="form-control" placeholder="邮 箱">
				<p id = "emailInvalid" class = "hidden alert alert-danger">请输入合法的邮件地址</p>
				<p id = "emailUnusable" class = "hidden alert alert-danger">邮箱已被注册</p>
			</div>
			<input " id = "inputNickname" type="text" name="nickname" class="form-control"
				placeholder="昵 称"> 
			<p id = "nicknameNotInput" class = "hidden alert alert-danger">必须输入昵称</p>
			<input id = "inputPassword" type="password" name="password"
				class="form-control" placeholder="密 码">
			<p id = "passwordNotInput" class = "hidden alert alert-danger">密码长度应该在10到20位之间，并且不可以是纯数字</p>
			<input id = "reinputPassword" type="password"
				class="form-control" placeholder="确认密码">
			<p id = "passwordNotMatch" class = "hidden alert alert-danger">两次密码不一致</p>
			<div>
				<input id = "submit" class="btn btn-lg btn-primary btn-block" type="submit" value = "注册">
			</div>
		</form>

	</div>
</body>
</html>