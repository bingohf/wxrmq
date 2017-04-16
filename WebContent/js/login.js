$(document).ready(function() {
	$("#btn_login").click(function() {
		var $btn = $(this);
		$btn.button('loading');
		$.ajax({
			url : 'login',
			type : 'POST',
			data : $('#loginForm').serialize()
		}).done(function(a, b, c) {
			$("#panel_error").hide();
			
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