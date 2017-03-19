$(document).ready(function() {
	$("#signup_button").click(function() {
		var $btn = $(this);
		$btn.button('loading');
		$.ajax({
			url : 'join',
			type : 'POST',
			data : $('#joinForm').serialize()
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