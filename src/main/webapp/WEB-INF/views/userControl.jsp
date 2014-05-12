<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link rel = "stylesheet" href="resources/css/userControl.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>

<script type="text/javascript" src="resources/js/lib/arttemplate.js"></script>
<script type="text/javascript" src="resources/js/lib/template-simple.js"></script>
<script type="text/javascript" src="resources/js/userControl.js"></script>
</head>
<body>
	<div class="container">
		<div class="dropdown">
			<button id="chooseSpeech" class="btn dropdown-toggle" type="button" data-toggle="dropdown">
				<text>${speechType }</text>
				 <span class="caret"></span>
			</button>
			<ul class="dropdown-menu" role="menu">
				<li class = "speechType" role="presentation"><a role="menuitem" tabindex="-1">英式发音</a></li>
				<li class = "speechType" role="presentation"><a role="menuitem" tabindex="-1">美式发音</a></li>
			</ul>
		</div>
	</div>
</body>
</html>