	
var groupCount = function(list, key) {
	var hash = {};
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		var keyValue = item[key];
		var count = hash[keyValue];
		if (count == null) {
			count = 0;
		}
		hash[keyValue] = ++count;
	}
	var result = new Array();
	for (var p in hash) {
		result.push({
			label: p,
			y: hash[p]
		});
	}
	return result;
};
var renderPie = function() {
	$("#friendsCount").html(Rmq.MemberList.length);
	$("#lb_nickname").html(Rmq.WxUser.NickName);
	$("#headerImg").attr('src', 'data:image/png;base64,' + Rmq.WxUser.HeadImgBase64);
	var pie_sexData = groupCount(Rmq.MemberList, 'Sex');
	for (var i = 0; i < pie_sexData.length; i++) {
		var sex = '男';
		if(pie_sexData[i].label == 0){
			sex ='未知';
		}else if(pie_sexData[i].label ==2){
			sex ='女';
		}
		pie_sexData[i].label = sex ;
	}
	var chart = new CanvasJS.Chart("pie_sex", {
		theme: "theme2",
		height: 300,
		title: {
			text: "性别分布"
		},
		data: [{
			type: "pie",
			showInLegend: true,
			legendText: "{label}",
			dataPoints: pie_sexData
		}]
	});
	chart.render();

	var pie_cityData = groupCount(Rmq.MemberList, 'City');
	var chart = new CanvasJS.Chart("pie_city", {
		theme: "theme2",
		height: 300,
		title: {
			text: "地区分布"
		},
		data: [{
			type: "pie",
			showInLegend: true,
			legendText: "{label}",
			dataPoints: pie_cityData
		}]
	});
	chart.render();

};





		
		
		var loadDetail = function(){
			$.ajax({
				type: "get",
				url: "getwxUserInfo",
				data:{wxid: $.url().param('wxid')},
				contentType: "application/json",
				dataType: "json"
			}).done(function(data) {
				window.Rmq = data;
				renderPie();
			}).fail(function(jqXHR, textStatus, errorThrown) {

			});
			
		};

		$(document).ready(function() {
			$.ajaxSetup({
				timeout: 10000,
				cache: false
			});
			loadDetail();



		});