$(function() {
	loadWordsList(1);
});

function loadWordsList(pageIndex) {
	var requestPath = "wordBook/list/" + pageIndex;
	$.getJSON(requestPath, function(data) { // TODO 添加页码支持
		var words = data.words.map(function(word) {
			var ph = data.speechType == "en" ? word.symbols[0].ph_en
					: word.symbols[0].ph_am;
			var ph_mp3 = data.speechType == "en" ? word.symbols[0].ph_en_mp3
					: word.symbols[0].ph_am_mp3;
			var means = word.symbols[0].parts.map(function(part) {
				return part.part + " " + part.means.join(",");
			}).join(";");
			return {
				"word_name" : word.word_name,
				"ph" : ph,
				"ph_mp3" : ph_mp3,
				"means" : means
			};
		});
		var pageCount = data.pageCount;
		var nowPageIndex = data.pageIndex;
		var pageIndexsShowed = [];
		if (nowPageIndex > 5) {
			pageIndexsShowed = range(nowPageIndex - 5, nowPageIndex + 5);
		} else {
			var start = 1;
			var end = pageCount <= 11 ? pageCount : 11;
			pageIndexsShowed = range(start, end);
		}
		var html = template.render("wordsListTemplate", {
			"words" : words
		});
		$("#wordsList").html(html);
		var pageIndexsHtml = template.render("pageIndexTemplate", {
			"pageIndexsShowed" : pageIndexsShowed,
			"pageCount" : pageCount,
			"nowPageIndex": nowPageIndex,
			"frontPageIndex": nowPageIndex - 1,
			"nextPageIndex": nowPageIndex + 1
		});
		$("#pageIndex ul").html(pageIndexsHtml);
		setPageIndexEvent();
		setPronounceEvent();
		setLookEvent();
		setDeleteEvent();
	});
}

function setPageIndexEvent() {
	$("#pageIndex li a").click(function (event) {
		var pageIndex = $(this).attr("index");
		var pageCount = $("#pageIndex li a").last().attr("index");
		if (pageIndex < 1 || pageIndex > pageCount)
			return;
		loadWordsList(pageIndex);
	});
}

function range(start, end) {
	var numbers = [];
	for (var i = start; i <= end; i++) {
		numbers[i - start] = i;
	}
	return numbers;
}

function setDeleteEvent() {
	$(".wordOperations .deleteWord").click(function(event) {
		console.log("delete button click");
		var word = $(this).attr("word");
		var path = "removeWordFromWordBook" + "/" + word;
		$.getJSON(path, function(data) {
			if (data.state == "success") {
				$("#" + word).remove();
			}
		});
	});
}

function setLookEvent() {
	$(".wordOperations .lookWord").click(function(event) {
		var word = $(this).attr("word");
		window.open("home/search/" + word);
	});
}

function setPronounceEvent() {
	$(".wordPhonetic a").click(function(event) {
		var ph_mp3 = $(this).attr("ph_mp3");
		console.log(ph_mp3);
		var html = template.render("mp3PlayTemplate", {
			"mp3" : ph_mp3
		});
		$("#mp3Play").html(html);
	});
}
