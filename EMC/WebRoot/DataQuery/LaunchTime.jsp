<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>
<%@ page import="DBConnection.*" %>
<%@ page import="DataQuery.*" %>
<%@ page import="com.fileupload.*"%>
<%@ page import="java.text.DecimalFormat" %>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
StringBuffer uploadUrl = new StringBuffer(basePath + "DataImport/FileUpload.jsp;jsessionid=" + request.getSession().getId());
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>发射时段</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-contdol" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<%@ page import="java.text.SimpleDateFormat" %>
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/SW.js" ></script>
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="js/swfupload/handlers.js"></script>
	<style>
		input[id="import"]{
			width:90px;
			height:25px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			margin-left:30px;
		}
	</style>
    <script type="text/javascript">
	//界面上选中的文件导入数据库中
	function launchTimeProcess(bh,type) {
		
		
		//alert("ok");
    	var threshold= document.getElementById("threshold").value;
    	var interval= document.getElementById("interval").value;
    	if((threshold == "")||(interval == "")){
    		alert("数据不能为空。");
    		return;
    	}
    	document.getElementById("response").style.color = "red";
		document.getElementById("response").innerHTML = "操作中。请等待...";
    	var param = "/rsui/DataQuery/LaunchTimeProcess.jsp?";
		
		param = param + "threshold=" + threshold;
		param = param + "&interval=" + interval;
		param = param + "&bh=" + bh;
		param = param + "&type=" + type;
		//alert(param);
		param = encodeURI(param);
		param = encodeURI(param);
		//alert(param);
		
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.open("GET",param, false);
		xmlhttp.send();
		
		var str = xmlhttp.responseText;
		str = str.replace(/[\r\n]/g, "");	//删除换行符
		str = "发射时段：<br>"+str.replace(/\; /g, "; <br>");
		document.getElementById("response").style.color = "black";
		document.getElementById("response").innerHTML = str;
		
	}
	
	
	</script>
  </head>
  
  <body style="padding:20px 0 0 15px;">
	<%
	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
    %>
    <!-- 
    boolean repeat = Handle.isRepeat(bh, type);
    if(Handle.isRepeat(bh, type)){
		<p style="padding-bottom:20px;">已计算过该数据的发射时段。</p>
	}
	 -->
	<%
    ArrayList<Double> al = Handle.DPJCfilter(bh,type,0.0);//将场强小于0的全部过滤掉
    
    DecimalFormat df = new DecimalFormat("#0.00");
    String min = String.valueOf(al.get(0));
    String max = String.valueOf(al.get(1));
    String aver = df.format(al.get(2));
    
    ArrayList<String> stringList = Handle.DPJCinfo(bh, type);
    String name = stringList.get(0);
    String frequence = stringList.get(1);
    //PDSM不计算发射时段
    //ArrayList<Double> frequence = Handle.PDSMFrequence(bh,type);
    %>
       <div>
		   <table align="center">
		     <tr><td nowrap style="text-align: right;">文件名：</td><td style="text-align: ;"><%=name %></td></tr>
		     <tr><td nowrap style="text-align: right;">频&emsp;率：</td><td style="text-align: ;"><%=frequence %> MHz</td></tr>
	   		</table>
	   		<table style="padding-top:20px;">
		   	 <tr><td>场强最小值(dBμV/m):</td><td><%=min %></td></tr>
		   	 <tr><td>场强最大值(dBμV/m):</td><td><%=max %></td></tr>
		   	 <tr><td>场强平均值(dBμV/m):</td><td><%=aver %></td></tr>
		   <tr>
			<td ><font>请指定场强阈值(dBμV/m)：</font></td>
			<td><input type="text" id="threshold" ></td>
			</tr><tr>
			<td><font>请指定时间间隔(min)：</font></td>
			<td><input type="text" id="interval" value="10"></td>
			</tr><tr>
			<td></td><td><input type="button" id="import" value="确定" onclick="javascript:launchTimeProcess('<%=bh%>','<%=type%>')" /></td>
			</tr>
		  </table>
	  </div>
	<div id = "response" style="padding-left:10px;padding-top:30px;"></div>		
	<%%>
  </body>
</html>
