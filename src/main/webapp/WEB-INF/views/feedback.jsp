<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报告错误或者给予建议</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/feedback.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/userControl.js"></script>
</head>
<body>
	<div class="container">
		<form class="form-horizontal" action="submitFeedback">
			<div class="form-group">
				<label for="text" class="col-sm-1 control-label">标题</label>
				<div class="col-sm-10">
					<input class="form-control" name="title" type="text"
						required="required" placeholder="标题">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1">反馈类型</label> <span class = "radio-span"><input
					name="type" type="radio" value="suggestion" checked><label>改进建议</label></span>
				<span><input name="type" type="radio" value="error"
					><label>错误报告</label></span>
			</div>
			<div class="form-group" rows="5">
				<label for="text" class="col-sm-1 control-label">详细描述</label>
				<div class="col-sm-10">
					<textarea class="form-control" name="content" type="text" rows = "6"
						required="required" placeholder="详细描述"></textarea>
			</div>
			</div>

			<div class="">
				<button type="submit"class="btn btn-primary">保
					存</button>
			</div>
		</form>
	</div>
</body>
</html>