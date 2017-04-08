		window.QRLogin = {
			uuid : ''
		};
		window.Rmq ={CurUser:{}, MemberList:[]};
		
		var login = function() {
			console.log(redirect_uri);
			$.get("loginWx", {
				redirect_url : redirect_uri
			}, function(data, status) {
				init();
			});
		}
		

		var getContact = function(){
			$.ajax({
				type : "get",
				url : "getContact",
				contentType : "application/json",
				dataType:"json"
			}).done(function(data) {
				window.Rmq.MemberList = data.MemberList;
				recordRmq();
			}).fail(function(jqXHR, textStatus, errorThrown) {
					
			});
		}
		var recordRmq = function() {
			$.ajax({
				type : "post",
				url : "recordRM",
				contentType : "application/json",
				data : JSON.stringify(Rmq)
			}).done(function(data) {
				
			}).fail(function(jqXHR, textStatus, errorThrown) {

			});
		}

		var init = function() {
			$.get("initWx", function(data, status) {
				Rmq.CurUser = data.User;
				getContact();
			}, "json").fail(function(jqXHR, textStatus, errorThrown) {
				debugger;
			});
		}

		var reset = function() {
			var p = $.get("uuid").done(function(data){
				eval(data);
				$("#qrcode").attr("src",
						"https://login.weixin.qq.com/qrcode/" + QRLogin.uuid);
				listenScan();
			});
			var listenScan = function() {
				$.get("listenState", {
					uuid : QRLogin.uuid
				}, function(data, status) {
					eval(data);
					if (code == 201) {
						listenScan();
					} else if (code == 200) {
						login();
					} else if (code ==408) {
						listenScan();
					}else if (code =400){
						
					}
				});
			}

		}
		
		$(document).ready(function() {
			$.ajaxSetup({
				timeout : 180000,
				cache : false
			});
			reset();
		});