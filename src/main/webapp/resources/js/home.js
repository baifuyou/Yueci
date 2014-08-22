$(function() {
	$("#searchButton").click(function(event) {
		$.comparisonCounter = 0;
		event.preventDefault();
		var word = $("#searchWord").val();
		word = isChinese(word) ? word : word.toLowerCase();
		var searchPath = "searchWord/" + word;
		$("title").html(word + "-阅辞");
		$.getJSON(searchPath, function(response) {
			if (response.wordObject.word_name == null) {
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
		});
	});

	$("#comparisonButton").click(
			function(event) {
				event.preventDefault();
				var word = $("#searchWord").val();
				word = isChinese(word) ? word : word.toLowerCase();
				$("title").html(word + "," + $("title").html());
				var searchPath = "searchWord/" + word;
				$.getJSON(searchPath,
						function(response) {
							var html = renderEnglishSymbolsHtml(response,
									$.comparisonCounter++);
							$("<br/>").appendTo("#searchEW");
							$(html).appendTo("#searchEW");
							setClickEvent(response.wordObject,
									$.comparisonCounter - 1);
						});
			});

	$("#searchWord").keydown(function(event) {
		if ($("#comparisonButton").attr("disabled") != "disabled") {
			if (event.ctrlKey && event.keyCode == 13) { // ctrl + enter
				$("#comparisonButton").click();
			}
		}
	});

	$("#gotoLogin").click(function(event) {
		console.log("gotoLogin click" + ": " + $("#loginRef").attr("href"));
		location.href = "login"
	});

	$("#gotoRegister").click(function(event) {
		console.log("gotoLogin click" + ": " + $("#loginRef").attr("href"));
		location.href = "requestRegister";
	});

	searchRequestWord();
	
	$("#searchWord").typeahead({
		source : function(query, process) {
			var url = "fuzzySearch/" + query;
			console.log("typeahead invoke");
			$.getJSON(url, function(data) {
				console.log("getJSON success")
				process(data);
				console.log(data);
			});
		}
	});

});

/*
 * 提供服务端渲染的模拟，从searchWord标签中获取word参数，
 */
function searchRequestWord() {
	var word = $("#searchRequestWord").text();
	if (word == "" || word == null)
		return

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
	var html = template.render("wordInfoEW", [ {
		"wordName" : "抱歉，找不到您要找的单词！"
	} ]);
	$("#searchEW").html("抱歉，找不到您要找的单词！");
}

function isChinese(word) {
	var reg = /^[\u4e00-\u9fa5]+$/;
	return reg.test(word);
}

function renderEnglishSymbolsHtml(response, comparIndex) {
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
			"means" : means
		};
	});
	var starType = existInWordBook == true ? "glyphicon-star"
			: "glyphicon-star-empty";
	var starTitle = existInWordBook == true ? "从单词本移除" : "添加到单词本";
	var symbolsHtml = template.render("wordInfoEW", {
		"symbols" : symbolsData,
		"wordTense" : renderWordTense(data.exchange),
		"comparisonIndex" : comparIndex,
		"starType" : starType,
		"starTitle" : starTitle
	});

	return symbolsHtml;
}

/*
 * 在客户端渲染结束后，设置各种事件
 * 
 */
function setClickEvent(data, comparIndex) {
	for (var i = 0; i < data.symbols.length; i++) {
		// 设置英式发音
		$("#phEnPlayEW" + comparIndex + i).click({
			index : i
		}, function(event) {
			playMp3(data.symbols[event.data.index].ph_en_mp3);
		});
		// 设置美式发音
		$("#phAmPlayEW" + comparIndex + i).click({
			index : i
		}, function(event) {
			playMp3(data.symbols[event.data.index].ph_am_mp3);
		});
		// 设置折叠按钮
		$("#toggleButton" + comparIndex + i).click(
				{
					index : i,
					comparIndex : comparIndex
				},
				function(event) {
					if ($(
							"#content" + event.data.comparIndex
									+ event.data.index).hasClass("hidden")) {
						devolopMeans(event.data.comparIndex, event.data.index);
					} else {
						foldMeans(event.data.comparIndex, event.data.index);
					}
				});
		// 设置添加到单词本，和从单词本移除事件
		$("#addToWordBook" + comparIndex + i).click({
			"comparIndex" : comparIndex,
			"index" : i
		}, function(event) {
			var comIndex = event.data.comparIndex;
			var ind = event.data.index;
			var title = $("#addToWordBook" + comIndex + ind).attr("title");
			var word = $("#addToWordBook" + comIndex + ind).attr("word");
			if (title == "添加到单词本") {
				addToWordBook(word, comIndex, ind);
			} else {
				removeFromWordBook(word, comIndex, ind);
			}
		});
	}
}

function removeFromWordBook(word, comIndex, ind) {
	var path = "removeWordFromWordBook" + "/" + word;
	$.getJSON(path, function(data) {
		$.getJSON(path, function(data) {
			if (data.state == "success") {
				$("#addToWordBook" + comIndex + ind).attr("title", "添加到单词本");
				$("#addToWordBook" + comIndex + ind + ">" + "span")
						.removeClass("glyphicon-star");
				$("#addToWordBook" + comIndex + ind + ">" + "span").addClass(
						"glyphicon-star-empty");
			} else if (data.state = "not login") {
				remindLogin();
			}
		});
	});
}

function addToWordBook(word, comIndex, ind) {
	var path = "addWordToWordBook" + "/" + word;
	$.getJSON(path, function(data) {
		if (data.state == "success") {
			$("#addToWordBook" + comIndex + ind).attr("title", "从单词本移除");
			$("#addToWordBook" + comIndex + ind + ">" + "span").removeClass(
					"glyphicon-star-empty");
			$("#addToWordBook" + comIndex + ind + ">" + "span").addClass(
					"glyphicon-star");
		} else if (data.state == "not login") {
			remindLogin();
		}
	});
}

function remindLogin() {
	$("#remindLoginModal").modal('show');
}

function devolopMeans(comparIndex, index) {
	$("#toggleButton" + comparIndex + index).html("∧");
	$("#content" + comparIndex + index).removeClass("hidden");
}

function foldMeans(comparIndex, index) {
	$("#toggleButton" + comparIndex + index).html("∨");
	$("#content" + comparIndex + index).addClass("hidden");
}

function renderEnglishWord(response) {
	var symbolsHtml = renderEnglishSymbolsHtml(response, $.comparisonCounter++);
	$("#searchEW").html(symbolsHtml);
	setClickEvent(response.wordObject, $.comparisonCounter - 1);
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
			"means" : meansData
		};
	});
	var html = template.render("wordInfoCW", {
		"symbols" : symbolsData
	});
	$("#searchCW").html(html);
	for (var i = 0; i < data.symbols.length; i++) {
		$("#pronounceCW" + i).click({
			index : i
		}, function(event) {
			playMp3(data.symbols[event.data.index].symbol_mp3);
		});
	}
	$(".inlineWord").click(function(event) {
		$("#searchWord").val(event.currentTarget.text);
		console.log(event.currentTarget.text);
		$("#searchButton").trigger("click");
	});
}

function playMp3(mp3) {
	var mp3Html = template.render("mp3Template", {
		"mp3" : mp3
	});
	$("#mp3Play").html(mp3Html);
}