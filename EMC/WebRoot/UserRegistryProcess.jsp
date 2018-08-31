<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="user.UserInfor" %>
</head>
<body>
<%
//System.out.println("UserRegistryProcess.jsp");
String userId = java.net.URLDecoder.decode(request.getParameter("UserId"),"UTF-8").trim();
String userPassword  = java.net.URLDecoder.decode(request.getParameter("UserPassword"),"UTF-8").trim();
String userName  = java.net.URLDecoder.decode(request.getParameter("UserName"),"UTF-8").trim();
String unitName = java.net.URLDecoder.decode(request.getParameter("UnitName"),"UTF-8").trim();
String telephone = java.net.URLDecoder.decode(request.getParameter("Telephone"),"UTF-8").trim();
String userRole = java.net.URLDecoder.decode(request.getParameter("UserRole"),"UTF-8").trim();
String stationType = java.net.URLDecoder.decode(request.getParameter("StationType"),"UTF-8").trim();
int confirm = 0;//表示未审核
 int result = UserInfor.addUserFromReg(userId, userRole, stationType, userPassword, userName, unitName, telephone,confirm);
 //System.out.println("haha");
 if(result == 0 ){
 %>
 	<%="用户名(" + userId + ")已存在，请更换用户名。" %>
 <%}else if(result == 1){%>
 	<%="用户(" + userId + ")注册成功。" %>
 <%}else{%>
 	<%="用户(" + userId + ")注册失败。" %>
 <%}%>
</html>
