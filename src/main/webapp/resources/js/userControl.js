$(function() {
	
	$("#sheet-list li a").click(function(event) {
		$(this).tab("show");
		var sheetName = $(this).attr("sheet-name");
		$(".setting-sheet").addClass("hidden");
		$("#" + sheetName).removeClass("hidden");
	});
	
	$("#nickname").focusout(function () {
		if (!checkNicknameInput() ) {
			$("#please-input-nickname").removeClass("hidden");
		} else {
			$("#please-input-nickname").addClass("hidden");
		}
	});
	
	$("#save-profile").click(
			function(event) {
				event.preventDefault();
				if (!checkNicknameInput() ) {
					$("#please-input-nickname").removeClass("hidden");
					return;
				} else {
					$("#please-input-nickname").addClass("hidden");
				}
				var speechType = $(
						'input[type="radio"][name="speech-type"]:checked')
						.val();
				var nickname = $("#nickname").val();
				var path = "user/saveProfileSetting";
				$.post(path, {
					"speechType" : speechType,
					"nickname" : nickname
				}, function(data) {
					console.log(JSON.stringify(data));
					if (data.state == "success") {
						show("#save-profile-success", 3000);
					} else {
						if (data.state == "not login") {
							//TODO 提示未登录
						} else {
							show("#save-profile-failure", 3000);
						}
					}
				}, "json");
			});
	
	$("#save-password").click(function(event) {
		event.preventDefault();//TODO
		if (!checkNewPasswordInput()) {
			$("#password-format-error").removeClass("hidden");
			return;
		} else {
			$("#password-format-error").addClass("hidden");
		}
		
		if (!checkNewPasswordIdentical()) {
			$("#two-password-not-identical").removeClass("hidden");
			return;
		} else {
			$("#two-password-not-identical").addClass("hidden");
		}
		var oldPassword = $("#old-password").val();
		var newPassword = $("#new-password").val();
		var path = "user/changePassword";
		$.post(path, {
			"oldPassword" : oldPassword,
			"newPassword" : newPassword
		},function (data) {
			if (data.state == "success") {
				$("#password-error").addClass("hidden");
				show("#save-password-success", 3000);
			} else {
				if (data.state == "not login") {
					//TODO 提示登陆
				} else if (data.state == "password error") {
					$("#password-error").removeClass("hidden");
				} else {
					show("#save-password-failure", 3000);
				}
			}
		}, "json");
	});
});

function checkNewPasswordIdentical() {
	var newPassword = $("#new-password").val();
	var newPassword2 = $("#new-password2").val();
	return newPassword == newPassword2;
}

function checkNewPasswordInput() {
	var password = $("#new-password").val();
	var reg = /^[0-9]+$/;
	return (password.length >= 10)  && (password.length <= 20) && (!reg.test(password));
}

function checkNicknameInput() {
	var nickname = $("#nickname").val();
	return nickname != null && nickname != "";
}

function show(target, time) {
	$(target).removeClass("hidden");
	setTimeout(function () {
		$(target).addClass("hidden");
	}, time);
}