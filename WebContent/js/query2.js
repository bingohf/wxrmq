$(function () {
    'use strict';
	
	var doQuery = function(){
		var qData= {};
		$("div.filter input").each(function(index, el){
			var name = $(this).attr("name");
			var val = $(this).val();
			qData[name] = val;
		});
		$("#nodata").hide();
		$("#div_loading").show();
		$(".list-group").empty();
		$.ajax({
			url : 'query',
			type : 'GET',
			dataType: "json",
			data : qData
		}).done(function(a, b, c) {
			$("#dataTmpl").tmpl(a.items).appendTo(".list-group");
			if(a.items.length < 1){
				$("#nodata").show();
			}	
		}).fail(function(xhr, textStatus, errorThrown) {
			alert(xhr.responseText);
		}).always(function(){
			$("#div_loading").hide();
		});
	};
	
	$("div.filter .btn-group .dropdown-menu a").click(function(){
		var btnGroup = $(this).parents(".btn-group");
		btnGroup.find('.selected').html(':' + $(this).html());
		btnGroup.find("input").val($(this).attr('q'));
		doQuery();
	});
	doQuery();
	
});