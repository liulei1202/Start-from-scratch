<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<%@ page import="java.util.*" %>
<%@ page import="user.UserInfor" %>
</head>
  
<body>
<% 
  String userId = java.net.URLDecoder.decode(request.getParameter("userId"),"UTF-8");
  String passWord = java.net.URLDecoder.decode(request.getParameter("passWord"),"UTF-8");
  //System.out.println("User password modify  process:" + userId + passWord);
 int result = UserInfor.changeUserPassword(userId,passWord);
 if (result == 0 ){
 %>
 	<%="对用户(" + userId + ")的密码修改不成功。" %>
 <%}else{%>
 	<%="用户(" + userId + ")密码修改成功。" %>
 <%} 
%>
</body>
</html>


		
	