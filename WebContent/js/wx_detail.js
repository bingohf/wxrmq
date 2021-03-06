	
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
var renderPie = function(data) {
	var detail = '好友数:' + data.friendInfo.friendsCount;
	if (data.sex == 1){
		detail += ' 男';
	}else if(data.sex == 2){
		detail += ' 女';
	}
	
	$("#detail").html(detail);
	$(".media-heading").html(data.nickName);
	var sexs = data.friendInfo.sexs;
    var citys = data.friendInfo.citys;
    for (var i = 0; i < sexs.length; i++) {
    	sexs[i].label = sexs[i].subTag? sexs[i].subTag:sexs[i].tag;
    }
    
    for (var i = 0; i < citys.length; i++) {
    	citys[i].label = citys[i].subTag ? citys[i].subTag:citys[i].tag;
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
        dataPoints: sexs
      }]
    });
    chart.render();

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
        dataPoints: citys
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
				$("#dataTmpl").tmpl(data).appendTo("#basic-info .panel-body");
				var detailInfo = eval('(' + data.infoJson+ ')');
				renderPie(detailInfo);
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