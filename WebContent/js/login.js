$(document).ready(function() {
	$("#btn_login").click(function() {
		var $btn = $(this);
		$btn.button('loading');
		$.ajax({
			url : 'login',
			type : 'POST',
			data : $('#loginForm').serialize()
		}).done(function(a, b, c) {
			
			debugger;
		}).fail(function(xhr, textStatus, errorThrown) {
			console.log(xhr);
			console.log(textStatus);
			console.log(errorThrown);
		}).always(function(){
			$btn.button("reset");
		});
		

	});
});