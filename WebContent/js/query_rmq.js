	
		var tag2HTML = function(item){
			var htmlTemplate = '<div class="list_item"><img class ="head" id="headerImg"><div class ="main"> <a id ="link_detail" href="#"><span id="nickName">bingo</span></a> <br/>  <span id ="tag_label"></span>:<span id="tagCount"></span> <br/><span>好友数:</span><span id="friendsCount"></span></div> </div>';
			var p = $(htmlTemplate);
			p.find("#link_detail").attr("href", "wx.html?wxid=" + item.Unid);
			p.find("#headerImg").attr("src","data:image/png;base64," + item.HeadImgBase64);
			p.find("#friendsCount").html(item.FriendsCount);
			p.find("#nickName").html(item.NickName);
			p.find("#tag_label").html(item.QueryTag);
			p.find("#tagCount").html(item.TagCount);
			return p;
			
		};
		
		var render = function(){
			$("#rmqList").empty();
			for(var i= 0;i < listData.Items.length; ++i){
				$("#rmqList").append($(tag2HTML(listData.Items[i])));
			}
			
		};
		var doQuery = function(query){
			$.ajax({
		        type: "get",
		        url: "query",
		        data:{query: query},
		        contentType: "application/json",
		        dataType: "json"
		    }).done(function(data) {
		    	window.listData = data;
		    	render();
		    }).fail(function(jqXHR, textStatus, errorThrown) {

		    });
		}
		
		$(document).ready(function() {
		    $.ajaxSetup({
		        timeout: 10000,
		        cache: false
		    });
		    $("#input_query").bind("keypress", function(event){
		    	if(event.keyCode == 13){
		    		doQuery($(this).val());
		    	}
		    });
		});