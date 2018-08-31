<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>修改密码页面</title>
<script type="text/javascript" src="/rsui/js/Admin.js" ></script>
<style type="text/css">
	td{width:80px;height:30px;padding:5px 0px;}
	input{font-size:15px;width:150px;height:100%;padding-left:10px;}
	input[type="button"]{margin-left:25px;width:100px;border-radius:10px;}
	#response{width:230px;text-align:center;}
</style>
</head>

<body >
 <%
 String userId = java.net.URLDecoder.decode(request.getParameter("UserId"),"UTF-8");
 %>
 <div id="content" style="margin-top:10px;">
   <table>
	 <tr><td>用户名:</td><td><input type="text" id="UserId"  value="<%=userId%>" disabled="disabled"></td></tr>
	 <tr><td>新密码:</td><td><input type=password id="password1" ></td></tr>
	 <tr><td>确认密码:</td><td><input type="password" id="password2" ></td></tr>
	 <tr><td></td><td><input type="button" name="PasswordModify" value="确定" onclick="UserPasswordModifyProcess()"/></td></tr>
  </table>
  <div id="response"></div>
</div>
</body>
</html>