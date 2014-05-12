$(function () {
	$(".speechType").click(function (event) {
		var speechType;
		var tagText = $(this).text();
		if (tagText == "英式发音") {
			speechType = "en";
		} else if (tagText == "美式发音") {
			speechType = "am";
		} else {
			return;
		}
		var url = "user/setSpeechType/" + speechType;
		$.get(url, function (data) {
			$("#chooseSpeech text").text(tagText);
		});
	});
});