<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户审核</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="/rsui/js/Admin.js"></script>
	<style>
		input{
			width:75px;
			height:20px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			margin-left:30px;
		}
	</style>
  </head>
  
  <body>
  <%
    String userId = java.net.URLDecoder.decode(request.getParameter("userId"),"UTF-8");
    UserInfor user = new UserInfor(userId);
    String temp1,temp2,temp3;
    if(user.getInformation()){
    	if(user.confirm==0){
    		temp1="未审核";
    		temp2="通过";
    		temp3="拒绝";
    	}else if(user.confirm==1){
    		temp1="通过";
    		temp2="未审核";
    		temp3="拒绝";
    	}else if(user.confirm==-1){
    		temp1="拒绝";
    		temp2="通过";
    		temp3="未审核";
    	}else{
    		temp1 = temp2 = temp3 = "状态出错";
    	}
    %>	
    	<h3>用户审核</h3>
    	<form>
		<table cellspacing="10px">
			<tr><td><font>用&ensp;户&ensp;名：</font></td><td id="userId"><%=user.userID %></td><td><font>真实姓名：</font></td><td><%=user.userName %></td></tr>
			<tr><td><font>用户类型：</font></td><td><%=user.userRole %></td><td><font>台站类型：</font></td><td><%=(user.stationType == null)? "": user.stationType.trim() %></td></tr>
			<tr><td><font>单位名称：</font></td><td><%=user.unitName %></td><td><font>联系电话：</font></td><td><%=user.phoneNo %></td></tr>
			<tr><td><font>审核状态：</font></td>
			<td><select id="confirm">
			<option><%=temp1 %></option>
			<option><%=temp2 %></option>
			<option><%=temp3 %></option>
			</select></td><td colspan="2"><input type="button" value="确定" onclick="userConfirmProcess()"></td></tr>
		</table>
		<input type="hidden" id="initialValue" value="<%=temp1 %>">
		</form>
    <%}else{%>
    <font>获取用户信息失败。</font>
    <%}
  %>
  </body>
</html>
