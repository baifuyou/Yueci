<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的单词本-阅辞</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/wordBook.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/wordBook.js"></script>
<script type="text/javascript" src="resources/js/lib/template-simple.js"></script>
</head>
<body>
	<div class = "container">
		<div class = "words-list">
			<div id = "wordsList" class = "list-group">
				<script id = "wordsListTemplate" type = "text/html">
					{{each words as word index}}
						<div id = "{{word.word_name}}" class = "list-group-item words-item">
								<div class = "wordInformation">
										<span class = "wordName">{{word.word_name}}</span>
										<span class = "wordPhonetic">
											[{{word.ph}}]
											<a ph_mp3 = "{{word.ph_mp3}}"><span class="glyphicon glyphicon-volume-up"></span></a>
										</span>
										<span class = "wordMeans">{{word.means}}</span> 
								</div>
								<div class = "wordOperations">
									<a class = "lookWord" word = "{{word.word_name}}">查看</a>
									<a class = "deleteWord" word = "{{word.word_name}}">删除</a>
								</div>
						</div>
					{{/each}}
				</script>
			</div>
		</div>
		<div id = "pageIndex">
			<ul class = "pagination">
				<script id = "pageIndexTemplate" type="text/html">
					<li><a index = "1">首页</a></li>
					<li><a index = "{{frontPageIndex}}">&laquo;</a></li>
					{{each pageIndexsShowed as pageIndex index }}
						{{if pageIndex != nowPageIndex}} 
							<li><a index = "{{pageIndex}}">{{pageIndex}}</a></li>
						{{else}}
							<li class = "active"><a index = "{{pageIndex}}">{{pageIndex}}</a></li>
						{{/if}}
					{{/each}}
					<li><a index = "{{nextPageIndex}}">&raquo;</a></li>
					<li><a index = "{{pageCount}}">尾页</a></li>
				</script>
			</ul>	
		</div>
	</div>
	<div id="mp3Play">
			<script id="mp3PlayTemplate" type="text/html">
				<object height="0" width="0" data="{{mp3}}" codetype = "audio/mpeg"></object>
			</script>
	</div>
</body>
</html>