<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<title>注册页面</title>
<script type="text/javascript" src="/rsui/js/User.js"></script>
<script type="text/javascript" src="/rsui/js/jquery-2.1.1.min.js"></script>
<link rel="stylesheet" href="/rsui/js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
<script type="text/javascript">
	document.onkeyup = function(e){	//为登录添加回车事件
		 //document.all可以判断浏览器是否是IE
		 //event=document.all?window.event:event;
		 var event=window.event||e;//IE：有window.event对象.FF：没有window.event对象。可以通过给函数的参数传递event对象。
		 if((event.keyCode || event.which) == 13){
		 	document.getElementById('UserNew').click();
		 }
	};
</script>
<style>
	body,html{
		
		margin:0;
		padding:0;
		height:100%;
	}
	td{
		
		width:100px;
		font-size:20px;
		text-align:left;
	}
	select{
		
		width:305px;
		font-size:25px;
		background-color:gdb(248,248,248);
		margin:8px 0px;
	}
	input.text{
		
		width:300px;
		height:35px;
		background-color:gdb(248,248,248);
		font-size:20px;
		margin:8px 0px;
	}
	input.button{
		
		width:180px;
		height:40px;
		font-size:20px;
		text-align:center;
		border-radius:20px;
		padding:6px 40px;
		margin:8px 60px;
	} 
	#grad {
	    background: -webkit-linear-gradient(left, #3281c3, #7cc0e0 , #3281c3); /* Safari 5.1 - 6.0 */
	    background: -o-linear-gradient(right, #3281c3, #7cc0e0, #3281c3); /* Opera 11.1 - 12.0 */
	    background: -moz-linear-gradient(right, #3281c3, #7cc0e0, #3281c3); /* Firefox 3.6 - 15 */
	    background: linear-gradient(to right, #3281c3, #7cc0e0 , #3281c3); /* 标准的语法（必须放在最后） */
	}
</style>
</head>
<body>
<div style="width:100%;min-height:100%;  position: relative;font-family:楷体;">  
	<div id="content"  style="text-align:center;width:100%;padding-bottom: 48px;">
		
		<div id="grad" style="height:48px;width:100%;">
			<div style="float:left;height:100%;width:48px;border-right:1px solid #dddddd;">
			</div>
			<div style="float:left;padding-left:24px;height:100%;font-size:24px;line-height:48px;">
				电磁频谱监测数据综合管理与应用系统
			</div>
			<div style="float:right;height:100%;width:48px;border-left:1px solid #dddddd;">
			</div>
			<div style="float:right;height:100%;font-size:20px;line-height:48px;">
				<a style="margin:0px 20px 0px 0px" href="index.jsp"><i class="fa fa-home"></i>退出</a>
			</div>
		</div>
		
		<br>
		<form id="regfrom">
		<table style="margin-left:auto;margin-right:auto;">
			<tr><td>用户类型</td>
				<td>
					<select id="UserRole">
						<option value="台站" selected>台站用户
						<option value="数据需求" >数据需求用户
						<option value="数据管理">数据管理员
						<!-- <option value="系统管理">系统管理员 -->
					</select>
				</td>
			</tr>
			<tr><td>台站类型</td>
				<td>
	 				<select id="StationType">
						<option value="短波" selected>短波
						<option value="超短波" >超短波
						<option value="卫星">卫星
					</select>
				</td>
			</tr>
			<tr><td>用  户  名</td>	
				<td><input type="text" class="text" id="UserId" value="" placeholder="*数字、字母、下划线"></td>
			</tr>
			
			<tr><td>密&nbsp;&nbsp;&nbsp;&nbsp;码</td>		
				<td><input type="password" class="text" id="UserPassword" value="" ></td>
			</tr>
			
			<tr><td>确认密码</td>	
				<td><input type="password" class="text" id="UserPasswordConFirm" value="" ></td>
			</tr>
			
			<tr><td>真实姓名</td>	
				<td><input type="text" class="text" id="UserName" value="" placeholder="*必须为汉字"></td>
			</tr>
			
			<tr><td>单位名称</td>	
				<td><input type="text" class="text" id="UnitName" value=""></td>
			</tr>
			<tr><td>联系电话</td>	
				<td><input type="text" class="text" id="Telephone" value=""></td>
			</tr>
			<tr><td></td><td><input type="button" class="button" value="确定" id="UserNew" name="UserNew" onclick="UserRegProcess()"></td></tr>
	 		<tr><td></td><td><input type="reset" class="button" value="清空"></td></tr>
	 	</table>
	 	</form>
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
	 	<br>
		<div id="response" align="center" style="color:black;font-size:18px;"></div>
		<br>
	</div>
	<%@include file="/foot.jsp" %>
</div>
</body>
</html>
