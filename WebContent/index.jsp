<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="js/jquery-3.1.1.min.js"></script>

<title>Welcome</title>
</head>
<body>

	<img id="qrcode" src="" height=300 width=300 />

	<script>
		window.QRLogin = {
			uuid : ''
		};
		
		window.Rmq ={curUser:{}, MemberList:[]};
		var getContact = function(){
			$.ajax({
				type : "get",
				url : "getContact",
				contentType : "application/json",
				dataType:"json"
			}).done(function(data) {
				
			}).fail(function(jqXHR, textStatus, errorThrown) {

			});
		}
		var recordRmq = function(curUser) {
			$.ajax({
				type : "post",
				url : "recordRM",
				contentType : "application/json",
				data : JSON.stringify(curUser)
			}).done(function(data) {
				
			}).fail(function(jqXHR, textStatus, errorThrown) {

			});

		}

		var init = function() {
			$.get("initWx", function(data, status) {
				Rmq.curUser = data.User;
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
			var login = function() {
				console.log(redirect_uri);
				$.get("loginWx", {
					redirect_url : redirect_uri
				}, function(data, status) {
					init();
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
	</script>
</body>
</html>