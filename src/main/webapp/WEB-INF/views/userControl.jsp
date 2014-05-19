<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${user.nickname }的主页</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/userControl.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>

<script type="text/javascript" src="resources/js/lib/arttemplate.js"></script>
<script type="text/javascript" src="resources/js/lib/template-simple.js"></script>
<script type="text/javascript" src="resources/js/userControl.js"></script>
</head>
<body>
	<div class="container">
		<div class="nav-bar">
			<ul id = "sheet-list" class="nav nav-tabs">
				<li class="active"><a sheet-name = "profile-setting">用户</a></li>
				<li ><a sheet-name = "password-setting">密码</a></li>
			</ul>
		</div>
		<div class="setting-sheet" id = "profile-setting">
			<form class="form-horizontal">
				<div class="form-group setting-item">
					<label for="nickname"
						class="col-sm-1 control-label setting-item-title">昵 称</label>
					<div class="col-sm-3">
						<input class="form-control" type="text" id="nickname"  autocomplete="off"
							placeholder="昵 称" value = "${user.nickname }">
					</div>
					<div class="col-sm-2">
						<span id = "please-input-nickname"  class = "info-error hidden">请输入昵称</span>
					</div>
				</div>
				<div class="form-group setting-item">
					<c:choose>
						<c:when test = "${user.wordBookSpeechType == 'en'}">
							<label class="col-sm-1 setting-item-title">单词本发音类型</label> <span><input
						id="speech-type-en" name="speech-type" type="radio" value="en" checked><label>英式发音</label></span>
					<span><input id="speech-type-am" name="speech-type"
						type="radio" value="am"><label>美式发音</label></span>
						</c:when>
						<c:otherwise>
							<label class="col-sm-1 setting-item-title">单词本发音类型</label> <span><input
						id="speech-type-en" name="speech-type" type="radio" value="en"><label>英式发音</label></span>
					<span><input id="speech-type-am" name="speech-type"
						type="radio" value="am" checked><label>美式发音</label></span>
						</c:otherwise>
					</c:choose>
				</div>
				<div>
						<button type="submit" id="save-profile" class="btn btn-primary">保
							存</button>
						<span class = "info-success hidden" id = "save-profile-success">保存成功</span>
						<span class = "info-error hidden" id = "save-profile-failure">保存失败</span>
				</div>
			</form>
		</div>
		<div class="setting-sheet hidden" id = "password-setting">
			<form class="form-horizontal">
				<div class="form-group setting-item">
					<label for="passowrd"
						class="col-sm-1 control-label setting-item-title">旧密码</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" id="old-password"
							placeholder="旧密码">
						<span id = "password-error" class = "info-error hidden">密码错误</span>
					</div>
				</div>
				<div class="form-group setting-item">
					<label for="passowrd"
						class="col-sm-1 control-label setting-item-title">新密码</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" id="new-password"
							placeholder="新密码">
						<span id = "password-format-error" class = "info-error hidden">密码长度应该在10到20位之间，并且不可以是纯数字</span>
					</div>
				</div>
				<div class="form-group setting-item">
					<label for="passowrd"
						class="col-sm-1 control-label setting-item-title">重复新密码</label>
					<div class="col-sm-10">
						<input class="form-control" type="password" id="new-password2"
							placeholder="重复新密码">
						<span id = "two-password-not-identical" class = "info-error hidden">两次密码不一致</span>
					</div>
				</div>

				<div class="">
					<button type="submit" id="save-password" class="btn btn-primary">保
						存</button>
					<span class = "info-success hidden" id = "save-password-success">保存成功</span>
					<span class = "info-error hidden" id = "save-password-failure">保存失败</span>
				</div>
			</form>
		</div>
	</div>
</body>
</html>