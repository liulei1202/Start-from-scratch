<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>填加新用户</title>
<script type="text/javascript" src="/rsui/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="js/Admin.js" charset="gb2312"></script>
<script type="text/javascript" src="/rsui/js/User.js"></script>
<style type="text/css">
	td{width:80px;height:20px;padding:5px 0px;}
	input{font-size:15px;width:150px;height:100%;}
	input[type="button"]{height:30px;margin-left:25px;width:100px;border-radius:10px;}
	input[type="reset"]{height:30px;margin-left:25px;width:100px;border-radius:10px;}
	select{font-size:15px;width:155px;}
	#response{width:230px;text-align:center;}
</style>
</head>

<body>
	<div id="userForm">
		<form>
		<table>
	  		<tr><td>用&ensp;户&ensp;名:</td><td><input type="text" id="UserId" ></td></tr>
	  		<tr><td>用户类型：</td><td>
	  				<select id="UserRole">
	  					<option value="台站" selected>台站用户
	  					<option value="系统管理">系统管理员
	 					<option value="数据管理">数据管理员
						<option value="数据需求" >数据需求用户
					</select></td></tr>
			<tr><td>台站类型：</td><td>
					<select id="StationType">
						<option value="短波" selected>短波
						<option value="超短波" >超短波
						<option value="卫星">卫星
					</select></td></tr>
			<tr><td>密&emsp;&emsp;码</td>		
				<td><input type="password" class="text" id="UserPassword" value="" ></td>
			</tr>
			
			<tr><td>确认密码</td>	
				<td><input type="password" class="text" id="UserPasswordConFirm" value="" ></td>
			</tr>
			
			<tr><td>真实姓名</td>	
				<td><input type="text" class="text" id="UserName" value=""></td>
			</tr>
			
			<tr><td>单位名称</td>	
				<td><input type="text" class="text" id="UnitName" value=""></td>
			</tr>
			<tr><td>联系电话</td>	
				<td><input type="text" class="text" id="Telephone" value=""></td>
			</tr>			
	  		<tr><td></td><td><input type="button" id="UserNew" value="确定" onclick="UserRegProcess()"></td></tr>
	 		<tr><td></td><td><input type="reset" value="清空"></td></tr>
	  </table>
	  </form>
	 </div>
	 <div id="response"></div>
	 <script type="text/javascript">
		$(function(){
			
			document.getElementById("UserRole").onchange = function(){
				
				if(document.getElementById("UserRole").value !="台站"){
					//document.getElementById("StationType").disabled = "disabled";
					$("#StationType").attr("disabled",true);//disabled的值不管设置成什么，都是启用disabled
				}else{
					
					$("#StationType").removeAttr("disabled");
				};
			};
			
		});
	</script>
</body>
</html>
<!-- <td><input type="button" value="关闭窗口" onclick="self.close()"/></td></tr> -->