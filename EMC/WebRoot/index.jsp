<!DOCTYPE html>
<%--@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="DBConnection.*" %>
<%@ page import="user.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>电磁频谱监测数据综合管理与应用系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/jquery-1.3.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="js/css/textStyle.css" > <!-- 艺术字-->
	<link rel="stylesheet" type="text/css" href="js/css/index.css" >	<!-- 主页样式 -->
  </head>
  <body>
    <%
    ServletContext sc;
    sc = getServletContext();
    String driver = sc.getInitParameter("ORACLEdriver");//"driver"
    String url = sc.getInitParameter("ORACLEurl");
    String user = sc.getInitParameter("ORACLEuser");
    String pwd = sc.getInitParameter("ORACLEpwd");
    String noOfFileUploaded = sc.getInitParameter("noOfFileUpload");
    String tempWordDir = sc.getInitParameter("tempWorkDir");
    String tempCalDir = sc.getInitParameter("tempCalDir");
             
   	tempWordDir = tempWordDir + "\\";
   
    EMCDB emcDB;
    emcDB = new EMCDB();
    boolean ret = emcDB.init(driver, url, user, pwd);
    
    session.setAttribute("userID",null);
	session.setAttribute("userType",null);  
	session.setAttribute("dbConn",emcDB);
	session.setAttribute("noOfFileUpload",noOfFileUploaded);
	session.setAttribute("tempWorkDir",tempWordDir);
	session.setAttribute("tempCalDir",tempCalDir);
	if ( ret ) {
	%>
	 <div id="sysName">
	 	
	 	<font>电磁频谱监测数据综合管理与应用系统</font>
	 	
	 </div>
	 <div id="login">
		<form id="userForm" method="post" action="loginProcess/LoginProcess.jsp">
			<table class="list">
			<tr></tr>
				<tr>
					<td><font>用户名:</font></td>
					<td><input type="text" name="userID" id="userID"></td>
				</tr>
				<tr>
					<td><font>密&emsp;码:</font></td>
					<td><input type="password" name="password" id="password"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="button" style="float:left; width:70px;" id="butn" value="登录" onclick="register()">
						<input type="button" style="float:right;width:70px;" value="注册" onclick="window.location.href='loginProcess/RegisterForm.jsp'">
					</td>
				</tr>
			</table>
		</form>
    </div>
    <div id="foot"><a href="javascript:downloadPlugins()"><font style="font-size:25px;">下载插件</font></a></div>
	<script>
		 document.onkeyup = function(e){	//为登录添加回车事件
			 //document.all可以判断浏览器是否是IE
			 //event=document.all?window.event:event;
			 var event=window.event||e;//IE：有window.event对象.FF：没有window.event对象。可以通过给函数的参数传递event对象。
			 if((event.keyCode || event.which) == 13){
			 	document.getElementById('butn').click();
			 }
		 };
		 
	    
		function register(){
			var userID =document.getElementById("userID").value; //判断用户名和口令是否正确
			var password =document.getElementById("password").value;
			if(userID==""){
				alert("请输入用户账号");
				return;
			} 
			if(password==""){
				alert("请输入用户密码");
				return;
			}
			
			
	    	var param = "/rsui/loginProcess/LoginProcess.jsp?"+"userID="+ userID + "&password=" + password;
	    	param = encodeURI(param);
			param = encodeURI(param);
			//alert("User New Process:" + param);
			var xmlhttp;
			if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else  {// code for IE6, IE5
		 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.open("GET",param,false);
			xmlhttp.send();
			var str = xmlhttp.responseText;
			str = str.replace(/[\r\n]/g, "");//去换行符
			str = str.replace(/\s+/g,"");    //去空格
			//alert(str);
    		if(str=="0"){
    			alert("用户申请未审核。");
    		}else if(str=="-1"){
    			alert("用户申请被拒绝。");
    		}else if(str=="1"){
    			window.location.href="loginProcess/Login.jsp";
    		}else{
    			window.location.href="index.jsp";
    		}
		}
		
		function downloadPlugins(){
			window.location.href="/rsui/Plugins.jsp";
		}
		
		
  	</script>		
  <%}else { %>
  <div id="wrapper">
  	 	<%@include file="Banner.jsp" %>
  	 	<div id="middle" style="display:block float:left;width:100%">
  	 		<font size=5>
  	 			与数据库连接的配置文件有误，联系系统管理人员进行配置信息的修改。<br>
  	 			修改方法如下：
  	 		</font>
  	 		<br>
			<ul>
			<li>在"WEB-INF"子目录下找到文件"web.xml";</li>
			<li>用文本编辑器打开文件"web.xml",典型的文件编辑器有notepad记事本，ultraedit超文本编辑器等</li>
			<li>在文件web.xml中可以看到如下示例的信息</li>
  			<li>对于文件中&lt;param-value&gt;和&lt;/param-value&gt;之间的值进行修改，修改完成后，保存文件</li>
  			<li>然后重新起动web服务器</li>
			</ul>  			
			&lt; context-param &gt;<br>
  			&lt;param-name&gt; driver &lt;/param-name&gt;<br>
  			&lt;param-value&gt; oracle.jdbc.driver.OracleDriver &lt;/param-value&gt;<br>
  			&lt;/context-param&gt;<br>
  			&lt;context-param&gt;<br>
  			&lt;param-name&gt; url &lt;/param-name&gt;<br>
  			&lt;param-value&gt; 192.168.0.107:1521/orcl &lt;/param-value&gt;<br>
  			&lt;/context-param&gt;<br>
  			&lt;context-param&gt;<br>
  			&lt;param-name&gt; user &lt;/param-name&gt;<br>
  			&lt;param-value&gt; system &lt;/param-value&gt;<br>
  			&lt;/context-param&gt;<br>
  			&lt;context-param&gt;<br>
  			&lt;param-name&gt; pwd &lt;/param-name&gt;<br>
  			&lt;param-value&gt;orcl&lt;/param-value&gt;<br>
  			&lt;/context-param&gt;<br>
  			对其中的driver项，url项，user项，pwd项的值进行修改，使其符合具体的配置值，<br>
  			driver:表示连接数据库的驱动程序<br>
  			url:表示数据库服务器的ip地址，端口号以及实例名称，格式是ip:端口地址/实例名<br>
  			user:表示连接数据库的用户名<br>
  			pwd:表示连接数据库用户的口令<br>
  		</div>
  </div>
  <%} %>
  </body>
</html>
