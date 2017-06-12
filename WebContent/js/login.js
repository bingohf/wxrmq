$(document).ready(function() {
	$("#btn_login").click(function() {
		var $btn = $(this);
		var mobile = $("#mobile").val();
		var password = $("password").val();
		if(mobile == ''){
			$("#errorMessage").html('请输入手机号码');
			$("#panel_error").show();
			return;
		}
		if(password == ''){
			$("#errorMessage").html('请输入密码');
			$("#panel_error").show();
			return;
		}
		$btn.button('loading');
		$.ajax({
			url : 'login',
			type : 'POST',
			data : $('#loginForm').serialize()
		}).done(function(a, b, c) {
			$("#panel_error").hide();
			window.location = "my_home.html?nocache=" + new Date().getTime();
		}).fail(function(xhr, textStatus, errorThrown) {
			$("#errorMessage").html(xhr.responseText);
			$("#panel_error").show();
			console.log(xhr);
			console.log(textStatus);
			console.log(errorThrown);
		}).always(function(){
			$btn.button("reset");
		});
	});
});