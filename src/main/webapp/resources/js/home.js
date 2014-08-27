$(function() {
	$("#searchButton").click(function(event) {
		event.preventDefault();
		var word = $("#searchWord").val();
		word = isChinese(word) ? word : word.toLowerCase();
		var searchPath = "searchWord/" + word;
		$("title").html(word + "-阅辞");
		$.getJSON(searchPath, function(response) {
			if (response.wordObject.word_name == null) {
				changeComparisonToDisabled();
				$("#searchEW").removeClass("hidden").addClass("show");
				$("#searchCW").removeClass("show").addClass("hidden");
				renderWordNotFound();
				return;
			}
			if (!isChinese(word)) {
				changeComparisonToEnabled();
				$("#searchEW").removeClass("hidden").addClass("show");
				$("#searchCW").removeClass("show").addClass("hidden");
				renderEnglishWord(response);
			} else {
				changeComparisonToDisabled();
				$("#searchCW").removeClass("hidden").addClass("show");
				$("#searchEW").removeClass("show").addClass("hidden");
				renderChineseWord(response);
			}
			renderHistoryList(word);
		});
	});

	$("#comparisonButton").click(function(event) {
		event.preventDefault();
		var word = $("#searchWord").val();
		word = isChinese(word) ? word : word.toLowerCase();
		$("title").html(word + "," + $("title").html());
		var searchPath = "searchWord/" + word;
		$.getJSON(searchPath, function(response) {
			var html = "";
			if (response.wordObject.word_name == null) {
				html = $("#wordNotFound").html()
			} else {
				html = renderEnglishSymbolsHtml(response);
			}
			//$(html).appendTo("#searchEW");
			$("#searchEW").append(html);
			setClickEvent(response.wordObject);
		});
		renderHistoryList(word);
	});

	$("#searchWord").keydown(function(event) {
		if ($("#comparisonButton").attr("disabled") != "disabled") {
			if (event.ctrlKey && event.keyCode == 13) { // ctrl + enter
				$("#comparisonButton").click();
			}
		}
	});

	$("#gotoLogin").click(function(event) {
		location.href = "login"
	});

	$("#gotoRegister").click(function(event) {
		location.href = "requestRegister";
	});

	searchRequestWord();

	$("#searchWord").typeahead({
		source : function(query, process) {
			var url = "fuzzySearch/" + query;
			$.getJSON(url, function(data) {
				process(data);
			});
		},
		items : 9,
		matcher : function(item) {
			return true;
		},
		sorter : function(items) {
			return items;
		},
		updater : function(item) {
			if (item == "") {
				return this.query;
			} else {
				return item;
			}
		}
	});

});

function renderHistoryList(word) {
	var newLiHtml = "<li><a>" + word + "</a></li>";
	if ($(".historyList ul").has("li").length == 0) {
		$(".historyList ul").html(newLiHtml);
	} else {
		$(".historyList li:first-child").before(newLiHtml);
	}
	while ($(".historyList li").length > 10) {
		$(".historyList li")[$(".historyList li").length - 1].remove();
	}
}

/*
 * 提供服务端渲染的模拟，从searchWord标签中获取word参数，
 */
function searchRequestWord() {
	var word = $("#searchRequestWord").text();
	if (word == "" || word == null)
		return; 
	$("#searchWord").val(word);
	$("#searchButton").click();
}

function changeComparisonToEnabled() {
	$("#comparisonButton").removeAttr("disabled");
}

function changeComparisonToDisabled() {
	$("#comparisonButton").attr("disabled", "disabled");
}

function renderWordNotFound() {
	var html = template.render("wordInfoEW", {});  //保证模板不丢失，在首次渲染前替换html会丢失模板
	console.log(html);
	$("#searchEW").html($("#wordNotFound").html());
}

function isChinese(word) {
	var reg = /^[\u4e00-\u9fa5]+$/;
	return reg.test(word);
}

function renderEnglishSymbolsHtml(response) {
	var data = response.wordObject;
	var existInWordBook = response.existInWordBook;
	// 过滤无效的symbol（修复官方api的问题）
	var symbols = data.symbols.filter(function(symbol) {
		return symbol.parts != null;
	});
	var symbolsData = symbols.map(function(symbol) {
		// 过滤parts数组下无内容的part（修复官方api的问题）
		symbol.parts = symbol.parts.filter(function(part) {
			return part.means != null;
		});
		var means = symbol.parts.map(function(part) {
			var mean = part.means.join(",");
			return part.part + mean;
		});
		return {
			"wordName" : data.word_name,
			"ph_en" : symbol.ph_en,
			"ph_am" : symbol.ph_am,
			"means" : means,
			"enMp3" : symbol.ph_en_mp3,
			"amMp3" : symbol.ph_am_mp3
		};
	});
	var starType = existInWordBook == true ? "glyphicon-star"
			: "glyphicon-star-empty";
	var starTitle = existInWordBook == true ? "从单词本移除" : "添加到单词本";
	var symbolsHtml = template.render("wordInfoEW", {
		"symbols" : symbolsData,
		"wordTense" : renderWordTense(data.exchange),
		"starType" : starType,
		"starTitle" : starTitle
	});

	return symbolsHtml;
}

/*
 * 在客户端渲染结束后，设置各种事件
 * 
 */
function setClickEvent() {
	// 设置英式发音
	$(".playPhEnEW").unbind("click");
	$(".playPhEnEW").click(function(event) {
		playMp3($(this).find(".enMp3").text());
	});
	// 设置美式发音
	$(".playPhAmEW").unbind("click");
	$(".playPhAmEW").click(function(event) {
		playMp3($(this).find(".amMp3").text());
	});
	// 设置折叠按钮
	$(".toggleButton").unbind("click");
	$(".toggleButton").click(function(event) {
		var contentDiv = $(this).parent().find(".content");
		if (contentDiv.hasClass("hidden")) {
			devolopMeans($(this), contentDiv);
		} else {
			foldMeans($(this), contentDiv);
		}
	});
	// 设置添加到单词本，和从单词本移除事件
	$(".addToWordBook").unbind("click");
	$(".addToWordBook").click(function(event) {
		var title = $(this).attr("title");
		var word = $(this).attr("word");
		if (title == "添加到单词本") {
			addToWordBook(word, $(this));
		} else {
			removeFromWordBook(word, $(this));
		}
	});
	
	$(".historyList a").unbind("click");
	$(".historyList a").click(function(event) {
		var wordName = $(this).text();
		$("#searchWord").val(wordName);
		$("#searchButton").click();
	});
}

function removeFromWordBook(word, addToWordBookA) {
	var path = "removeWordFromWordBook" + "/" + word;
	$.getJSON(path, function(data) {
		$.getJSON(path, function(data) {
			if (data.state == "success") {
				addToWordBookA.attr("title", "添加到单词本");
				addToWordBookA.find("span").removeClass("glyphicon-star");
				addToWordBookA.find("span").addClass("glyphicon-star-empty");
			} else if (data.state = "not login") {
				remindLogin();
			}
		});
	});
}

function addToWordBook(word, addToWordBookA) {
	var path = "addWordToWordBook" + "/" + word;
	$.getJSON(path, function(data) {
		if (data.state == "success") {
			addToWordBookA.attr("title", "从单词本移除");
			addToWordBookA.find("span").removeClass("glyphicon-star-empty");
			addToWordBookA.find("span").addClass("glyphicon-star");
		} else if (data.state == "not login") {
			remindLogin();
		}
	});
}

function remindLogin() {
	$("#remindLoginModal").modal('show');
}

function devolopMeans(toggleA, contentDiv) {
	toggleA.html("∧");
	contentDiv.removeClass("hidden");
}

function foldMeans(toggleA, contentDiv) {
	toggleA.html("∨");
	contentDiv.addClass("hidden");
}

function renderEnglishWord(response) {
	var symbolsHtml = renderEnglishSymbolsHtml(response);
	$("#searchEW").html(symbolsHtml);
	setClickEvent();
}

function renderWordTense(tenseJson) {
	var tenseHtml = "";
	var tensesArray = Object.keys(tenseJson).map(function(key) {
		switch (key) {
		case "word_pl":
			return [ key, "复数", tenseJson[key] ];
		case "word_past":
			return [ key, "过去式", tenseJson[key] ];
		case "word_done":
			return [ key, "过去分词", tenseJson[key] ];
		case "word_ing":
			return [ key, "现在分词", tenseJson[key] ];
		case "word_third":
			return [ key, "第三人称单数", tenseJson[key] ];
		case "word_er":
			return [ key, "比较级", tenseJson[key] ];
		case "word_est":
			return [ key, "最高级", tenseJson[key] ];
		}
	});
	tensesArray.forEach(function(tense) {
		if (tense[2] != "" && tense[2] != null) {
			tenseHtml += tense[1];
			tenseHtml += ":";
			tenseHtml += tense[2];
			tenseHtml += "; ";
		}
	});
	tenseHtml = tenseHtml.substring(0, tenseHtml.length - 2);
	return tenseHtml;
}

function renderChineseWord(response) {
	var data = response.wordObject;
	var symbolsData = data.symbols.map(function(symbol) {
		// 过滤parts数组下无内容的part（官方api的问题）
		symbol.parts = symbol.parts.filter(function(part) {
			return typeof part.means != "undefined";
		});
		var meansData = symbol.parts.map(function(part) {
			var mean = part.means.map(function(mean) {
				return mean.word_mean;
			});
			return {
				"partName" : part.part_name,
				"inlineMeans" : mean
			};
		});
		return {
			"wordName" : data.word_name,
			"wordPronounce" : symbol.word_symbol,
			"means" : meansData,
			"mp3" : symbol.symbol_mp3
		};
	});
	var html = template.render("wordInfoCW", {
		"symbols" : symbolsData
	});
	$("#searchCW").html(html);
	$(".playPhCW").click(function(event) {
		playMp3($(this).find(".mp3CW").text());
	});
	$(".inlineWord").click(function(event) {
		$("#searchWord").val(event.currentTarget.text);
		$("#searchButton").trigger("click");
	});
}

function playMp3(mp3) {
	var mp3Html = template.render("mp3Template", {
		"mp3" : mp3
	});
	$("#mp3Play").html(mp3Html);
}