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
<script type="text/javascript" src="resources/js/lib/jquery-2.0.2.min.js"></script>
<script type="text/javascript" src="resources/js/lib/bootstrap.min.js"></script>
<title>您还没有注册</title>
</head>
<body>
<div class = "container">
	<div class = "alert alert-info">您还没有注册，点击<a href= "rerequestEmailAuth" class = "alert-link">这里</a>马上注册</div>
</div>

</body>
</html>