<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>
<%
	String userID = java.net.URLDecoder.decode(request.getParameter("userID"),"UTF-8");
	String userPassword = java.net.URLDecoder.decode(request.getParameter("password"),"UTF-8");
	
	int result;
	UserInfor loginUser = new UserInfor(userID,userPassword);
	result = loginUser.getRole();
	if(result==1){//1说明审核通过
	
		session.setAttribute("userID",userID);
		session.setAttribute("userType",loginUser.userRole);
		session.setAttribute("stationType",loginUser.stationType);
		session.setAttribute("unitName",loginUser.unitName);
		
		loginUser.userRole = loginUser.userRole.trim();
		int role;
		if (loginUser.userRole.equals("admin"))
			role = 1;
		else if (loginUser.userRole.equals("数据管理"))
			role = 2;
		else if (loginUser.userRole.equals("数据需求"))
			role = 3;
		else if (loginUser.userRole.equals("系统管理"))
			role = 4;
		else if (loginUser.userRole.equals("台站")){
			if(loginUser.stationType.trim().equals("短波"))			
				role = 5;
			else if(loginUser.stationType.trim().equals("超短波"))
				role = 6;
			else if(loginUser.stationType.trim().equals("卫星"))
				role = 7;
			else
				role = 9;
		}
		else{
			role = 9;	
		}
		session.setAttribute("role",role);
	}%>
	<%=result %>
		



		
	