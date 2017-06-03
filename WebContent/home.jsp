<%@page import="wxrmq.domain.Account"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
  <link rel="stylesheet" href="bootstrap/3.3.7/css/bootstrap.min.css">
  <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
  <link rel="stylesheet" href="bootstrap/3.3.7/css/bootstrap-theme.min.css">
  <script src="js/jquery-3.1.1.min.js"></script>
  <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
  <script src="bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="css/main.css">
  <title>微营力</title>
</head>

<body>
  <div style="height:50px;width:100%">
    <div style="float: right;margin-right: 13%;">
      <p style="color: #eca95b; font-size: 14px; margin-left: 10px;    line-height: 50px; vertical-align: middle">
        <%if(session.getAttribute("account") == null){ %>
        <a href="login.html" target="">登录</a>
        <% }else{%>
        <a href="my_home.html" target=""><%=((Account)session.getAttribute("account")).getMobile()%></a>
        <%} %>
        &nbsp;&nbsp;&nbsp;<a href="step-join.html" target="">免费注册</a>
      </p>
    </div>
  </div>



  <div class="page-heading">
    <img src="img/banner.png" style="width: 100%; height: 100%;" alt="">
  </div>


  <script>
  
  </script>
</body>

</html>