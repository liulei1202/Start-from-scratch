<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<%@ page import="java.util.*" %>
	<%@ page import="user.UserInfor" %>
  </head>
  
  <body>
  <% 
  String userID = java.net.URLDecoder.decode(request.getParameter("userID"),"UTF-8");
  //System.out.println("User Password reset process:" + userName);
 int result = UserInfor.resetUserPassword(userID);
 if (result == 0 ){
 %>
 	<%="对用户(" + userID + ")的密码重置不成功。" %>
 <%}else{%>
 	<%="用户(" + userID + ")重置密码成功。" %>
 <%} 
 	%>
  </body>
</html>


		
	