<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link href="resources/css/login.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<title>登陆-阅辞</title>
</head>
<body>
	<div class="container">

		<form id = "form" class="form-signin" role="form" action="validate" method="post">
			<h2 class="form-signin-heading">登陆以后使用更多功能</h2>
			<input name = "uid" class="form-control"
				placeholder="邮 箱" required
				autofocus/>
			<input name= "password" type="password" class="form-control"
				placeholder="密 码" required/>
			<label class="checkbox">
			<input type = "checkbox" name = "rememberMe" value = "1" />30天以内自动登陆
			</label>
			<div>${errorInfo }</div>
			<div>
				<button class="btn btn-lg btn-primary btn-block" type="submit">登
					陆</button>
			</div>
			<div>
				<label>忘记密码？<a href="findPassword">点击这里</a></label>
			</div>
			<div>
				<label>还没有账号？<a href="requestRegister">快速注册</a></label>
			</div>
		</form>

	</div>
</body>
</html>