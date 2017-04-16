	
		var tag2HTML = function(tag){
			return '<div><label class ="tag_label">'+ tag.Label +'</label> <span> '+ tag.Count + '</span></div>';
		};
		
		var render = function(){
			$("#headerImg").attr('src', "data:image/png;base64," + Wx.WxUser.HeadImgBase64);
			$(document).attr('title', Wx.WxUser.NickName);
			$("#lb_nickname").html(Wx.WxUser.NickName + ' ('+ (Wx.WxUser.Sex ==1?'男':'女') +')');
			$("#num_friendsCount").html(Wx.WxUser.FriendsCount);
			/*		for(var i= 0;i < Wx.SexValue.length; ++i){
				$("#sex").append($(tag2HTML(Wx.SexValue[i])));
			}
			
			for(var i= 0;i < Wx.CityValue.length; ++i){
				$("#city").append($(tag2HTML(Wx.CityValue[i])));
			}*/
			
			var pie_sexData = [];
			for(var i= 0;i < Wx.SexValue.length; ++i){
				pie_sexData.push({y:Wx.SexValue[i].Count, label:Wx.SexValue[i].Label});
			}
			
			
			var pie_cityData = [];
			for(var i= 0;i < Wx.CityValue.length; ++i){
				pie_cityData.push({y:Wx.CityValue[i].Count, label:Wx.CityValue[i].Label});
			}
						
			var chart = new CanvasJS.Chart("pie_sex",
					{
						theme: "theme2",
						title:{
							text: "性别分布"
						},
						data: [
						{
							type: "pie",
							showInLegend: true,
							legendText: "{label}",
							dataPoints: pie_sexData
						}
						]
					});
			chart.render();
			var chart = new CanvasJS.Chart("pie_city",
					{
						theme: "theme2",
						title:{
							text: "地域分布"
						},
						data: [
						{
							type: "pie",
							showInLegend: true,
							legendText: "{label}",
							dataPoints: pie_cityData
						}
						]
					});
			chart.render();
			
		};

	
		
		
		var loadDetail = function(){
			$.ajax({
		        type: "get",
		        url: "rmqValue",
		        data:{wxid: $.url().param('wxid')},
		        contentType: "application/json",
		        dataType: "json"
		    }).done(function(data) {
		    	window.Wx = data;
		    	render();
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