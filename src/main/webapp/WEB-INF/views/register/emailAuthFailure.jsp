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
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel = "stylesheet" href = "resources/css/failure.css">
<script type="text/javascript" src="resources/js/lib/jquery-2.0.2.min.js"></script>
<script type="text/javascript" src="resources/js/lib/bootstrap.min.js"></script>
<title>邮箱验证失败</title>
</head>
<body>
	<div class="container">
		<div class="alert alert-danger">
			邮箱验证失败，可能是由于以下原因：
			<ul>
			<li>验证超时，您需要点击<a href="requestRegister" class="alert-link">这里</a>重新注册</li>
			<li>验证链接不正确，您需要核对您在浏览器输入的地址和邮箱中的地址是否一致！</li>
			<li>如果仍然有问题，你可以点击<a href="feedback" class="alert-link">这里</a>联系我们</li>
			</ul>
		</div>
	</div>
</body>
</html>