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
<script src="resources/js/login.js"></script>
<title>登陆-阅辞</title>
</head>
<body>
	<div class="container">

		<div id = "resetPassword" class="modal fade" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title">重置密码</h4>
					</div>
					<div class="modal-body">
						<form id = "email-form" >
							<span>您的注册邮箱：</span>
							<input class="form-control" id = "email" type = "text" name = "email">
							<span id = "info" class = "hidden"></span>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button id = "request-reset-password" type="button" class="btn btn-primary">发送邮件以重置密码</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->

		<form class="form-signin" role="form" action="validate"
			method="post">
			<h2 class="form-signin-heading">登陆以后使用更多功能</h2>
			<input name="uid" class="form-control" placeholder="邮 箱" required
				autofocus /> <input name="password" type="password"
				class="form-control" placeholder="密 码" required /> <label
				class="checkbox"> <input type="checkbox" name="rememberMe"
				value="1" />30天以内自动登陆
			</label>
			<div>${errorInfo }</div>
			<div>
				<button class="btn btn-lg btn-primary btn-block" type="submit">登
					陆</button>
			</div>
			<div>
				<span>忘记密码？<a data-toggle="modal" data-target="#resetPassword">点击这里</a></span>
			</div>
			<div>
				<span>还没有账号？<a href="requestRegister">快速注册</a></span>
			</div>
		</form>

	</div>
</body>
</html>