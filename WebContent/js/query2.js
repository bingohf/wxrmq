$(function () {
    'use strict';
    var queryParam ={start:0, count:500};
	var doQuery = function(){
		queryParam.start = 0;
		var qData= {};
		$("div.filter input").each(function(index, el){
			var name = $(this).attr("name");
			var val = $(this).val();
			qData[name] = val;
		});
		queryParam = $.extend(queryParam, qData);
		$("#nodata").hide();
		$("#div_loading").show();
		$(".list-group").empty();
		$.ajax({
			url : 'query',
			type : 'GET',
			dataType: "json",
			data : queryParam
		}).done(function(a, b, c) {
			$("#dataTmpl").tmpl(a.items).appendTo(".list-group");
			if(a.items.length < 1){
				$("#nodata").show();
			}	
			$("#loadmore").show();
		}).fail(function(xhr, textStatus, errorThrown) {
			alert(xhr.responseText);
		}).always(function(){
			$("#div_loading").hide();
		});
	};
	
	var loadNextPage = function(){
		queryParam.start = 	queryParam.start  + queryParam.count;
		$("#loadmore img").show();
		$("#loadmore a").hide();
		$.ajax({
			url : 'query',
			type : 'GET',
			dataType: "json",
			data :queryParam
		}).done(function(a, b, c) {
			$("#dataTmpl").tmpl(a.items).appendTo(".list-group");
			$("#loadmore a").show();
		}).fail(function(xhr, textStatus, errorThrown) {
			alert(xhr.responseText);
		}).always(function(){
			$("#loadmore img").hide();
		});
	}
	$("#loadmore a").click(function(){
		loadNextPage();
	});
	$("div.filter .btn-group .dropdown-menu a").click(function(){
		var btnGroup = $(this).parents(".btn-group");
		btnGroup.find('.selected').html(':' + $(this).html());
		btnGroup.find("input").val($(this).attr('q'));
		doQuery();
	});
	doQuery();
	
});