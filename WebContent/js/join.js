$(document).ready(function() {
	$("#signup_button").click(function() {
		var $btn = $(this);
		if($("#password").val() != $("input#confrimPassword").val()){
			alert('确认密码不正确');
			return;
		}
		$btn.button('loading');
		$.ajax({
			url : 'join',
			type : 'POST',
			data : $('#joinForm').serialize()
		}).done(function(a, b, c) {
			window.location = 'login.html';
			
		}).fail(function(xhr, textStatus, errorThrown) {
			alert(xhr.responseText);
			console.log(xhr);
			console.log(textStatus);
			console.log(errorThrown);
		}).always(function(){
			$btn.button("reset");
		});
	});
	
	$("#img_validationCode").click(function(){
		$(this).attr("src", "validateCode?v=" + (new Date().getTime()));
	});
	
	$("#btn_mobile_code").click(function(){
		
		var $btn = $(this);
		$btn.button('loading');
		$.ajax({
			url : 'mobileValidationCode',
			type : 'POST',
			data : $('#joinForm').serialize()
		}).done(function(a, b, c) {
			$("#img_validationCode").click();
			alert(a);
		}).fail(function(xhr, textStatus, errorThrown) {
			alert(xhr.responseText);
			console.log(xhr);
			console.log(textStatus);
			console.log(errorThrown);
		}).always(function(){
			$btn.button("reset");
		});
		
	});
	
	
	
});