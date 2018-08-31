<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.UserInfor" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/Admin.js"></script>
	<link rel="StyleSheet" type="text/css" href="js/css/register.css" />
  </head>
  
  <body>
	<div id="all">
		<div id="hr"></div>
		<%@include file="/Banner.jsp" %>
		<div id="middle">	
			<div id="menu">
				<%@include file="/sysManMenu.jsp" %>
			</div>
			<div id="content">
				<div style="height:40px;line-height:40px;">
					<div style="float:left;">
						<font style="font-size:18px;font-weight:bold;">用户列表</font>
					</div>
					<div id="response" style="float:right;font-size:18px;margin-right:100px;">
					</div>
				</div>
				<div style="clear:both;padding-left:20px;">
					<table class="userinfo">
			 			<tr><th style="width:30px;text-align:left;"></th><th>用户名</th><th>用户类型</th><th>台站类型</th><th>真实姓名</th><th>单位名称</th><th>联系电话</th><th>审核状态</th><th>重置密码 </th><th>删除用户 </th><th>审核用户</th></tr>
			 			<% 
			 			LinkedList<UserInfor> ll = UserInfor.userList();
						
						int i,size;
						UserInfor userNode;
						String userID,userRole, stationType,realName,unitName,phoneNo,temp;
						int confirm;
						size = ll.size();
						try {
							for(i=0; i < size; i++ ){
								userNode = ll.get(i);
								userID = userNode.userID;
								userID = (userID == null)? "": userID.trim();
								
								userRole = userNode.userRole; 
								userRole = (userRole == null)? "":userRole.trim();
								
								stationType = userNode.stationType;
								stationType = (stationType == null)? "": stationType.trim();
								
								realName = userNode.userName;
								realName = (realName == null)? "": realName.trim();
								
								unitName = userNode.unitName;
								unitName = (unitName == null)? "": unitName.trim();
								
								phoneNo = userNode.phoneNo;
								phoneNo = (phoneNo == null)? "": phoneNo.trim();
								
								confirm = userNode.confirm;
								if(confirm==1){
									temp = "通过";
								}else if(confirm==-1){
									temp = "拒绝";
								}else{
									temp = "未审核";
								}
						%>
						<tr>
			  	 		<td style="width:20px;text-align:left;"><%=i+1%></td><td><%=userID %></td><td><%=userRole %></td><td><%=stationType %></td><td><%=realName %></td>
			  	 		<td><%=unitName %></td><td><%=phoneNo %></td><td><%=temp %></td>	
			  	 		<td><input type="button" onclick="javascript:UserPasswordReset('<%=userID%>')" value="重置密码"/></td>
			  	 		<td><input type="button" onclick="javascript:UserDelete('<%=userID%>')" value="删除用户" >	</td>
			  	 		<%if(userRole.equals("admin")||userRole.equals("系统管理")){%>
			  	 		<td></td>
			  	 		<%}else{%>
			  	 		<td><input type="button" onclick="javascript:userConfirm('<%=userID%>')" value="审核用户"></td>
			  	 		<%}%>
			  	 		</tr>
			  	 		<%	}
						}catch(IndexOutOfBoundsException ex){
							ex.printStackTrace();
						}catch(NullPointerException exNull){
							exNull.printStackTrace();
						}				
			 			%>	
			 		</table>
				</div>
			</div>
		</div>
		<%@include file="../foot.jsp" %>
	</div>
  </body>
</html>
