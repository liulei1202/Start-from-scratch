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
 	String userId = java.net.URLDecoder.decode(request.getParameter("userId"),"UTF-8");
	String userRole = java.net.URLDecoder.decode(request.getParameter("userRole"),"UTF-8");
	String stationType = java.net.URLDecoder.decode(request.getParameter("stationType"),"UTF-8");
	//System.out.println("User new process:" + userId + userRole + stationType);
	int result = UserInfor.addUser(userId,userRole,stationType);%><!-- 新添加用户密码默认为666666 -->
	<%if (result == 0 ){
%>
 	<%="对用户(" + userId + ")的添加不成功。" %>
	<%}else{%>
 	<%="用户(" + userId + ")添加成功。" %>
	 <%} 
 	%>
</body>
</html>
 

		
	