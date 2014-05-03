<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/home.css">
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/home.js"></script>
<script type="text/javascript" src="resources/js/lib/arttemplate.js"></script>
<script type="text/javascript" src="resources/js/lib/template-simple.js"></script>
<script type="text/javascript" src="resources/js/lib/artDialog.js?skin=default"></script>
<title>Home-阅辞</title>
</head>
<body>
	<div class="container">
		<div id="remindLogin">
			<c:if test="${isLogin ne true}">
				<div>
					<a href="login"><span class="label label-primary">登陆</span></a>
				</div>
			</c:if>
			<c:if test="${isLogin eq true }">
				<div>
					欢迎您，<a href="user/${user.nickname }">${user.nickname }</a>
				</div>
			</c:if>
		</div>
		<div id="searchBox">
			<form class="form-inline" action="#">
				<div class="form-group">
					<input class="form-control wordInput" id="searchWord" type="text">
				</div>
				<div class="form-group">
					<input id="searchButton" type="submit" class="btn btn-primary"
						value="搜索"> <input id="comparisonButton" type="submit"
						class="btn btn-primary" disabled="disabled" value="对比" title = "Ctrl + Enter">
				</div>
			</form>
		</div>
		<div id="mp3Play">
			<script id="mp3Template" type="text/html">
				<object height="0" width="0" data="{{mp3}}"></object>
			</script>
		</div>
		<div id="searchResult">
			<div id="searchEW" class="hidden">
				<script id="wordInfoEW" type="text/html">
				{{each symbols as symbol index}}
				<div class="wordInfoEn">
				<div class = "wordName">
					<p>{{symbol.wordName}}
						<a id="addToWordBook{{comparisonIndex}}{{index}}" title = "{{starTitle}}" word = "{{symbol.wordName}}">
							<span  class = "glyphicon {{starType}}"></span>
						</a>
					</p>
				</div>
				<a class = "toggle" id = "toggleButton{{comparisonIndex}}{{index}}"><span>∧</span></a><hr/>
				<div id = "content{{comparisonIndex}}{{index}}">
				<div>
					英<span>:[{{symbol.ph_en}}]</span>
					<a id="phEnPlayEW{{comparisonIndex}}{{index}}" class = "pronounceButton">
						<span class="glyphicon glyphicon-volume-up"></span>
					</a>
					美<span>:[{{symbol.ph_am}}]</span>
					<a id="phAmPlayEW{{comparisonIndex}}{{index}}" class = "pronounceButton">
						<span class="glyphicon glyphicon-volume-up"></span>
					</a>
				</div>
				<div>
					{{each symbol.means as wordMean index}}
					<ul>
						{{wordMean}}
					</ul>
					{{/each}}
				</div>
				<div>{{wordTense}}</div>
				</div>
				{{/each}}
			</div>
			</script>
			</div>
			<div id="searchCW" class="hidden col-md-8">
				<script id="wordInfoCW" type="text/html">
			{{each symbols as symbol index}}
			<div class="wordInfoCW">
				<div>{{symbol.wordPronounce}}
				<a id="pronounceCW{{index}}" class = "pronounceButton">
					<span class="glyphicon glyphicon-volume-up"></span>
				</a>
				</div>
				<div class = "wordName">{{symbol.wordName}}</div>
				<hr></hr>
				<div>
					{{each symbol.means as wordMean wordIndex}}
					<ul>
						<li class="meanItemCW">
							{{wordMean.partName}}.
							{{each wordMean.inlineMeans as singleMean singleIndex}} 
								{{if singleIndex == wordMean.inlineMeans.length - 1}}
									<a class = "inlineWord">{{singleMean}}</a>
								{{else}}
									 <a class = "inlineWord">{{singleMean}}</a>,
								{{/if}}
							{{/each}}
						</li>
					</ul>
					{{/each}}
				</div>
			</div>
			{{/each}}
			</script>
			</div>
		</div>
		<div id="relatedWordCW"></div>
	</div>
</body>
</html>
