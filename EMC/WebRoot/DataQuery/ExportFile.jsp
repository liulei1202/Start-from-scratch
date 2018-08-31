<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>
<%@ page import="DBConnection.*" %>
<%@ page import="DataQuery.*" %>
<%@ page import="com.fileupload.*"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
StringBuffer uploadUrl = new StringBuffer(basePath + "DataImport/FileUpload.jsp;jsessionid=" + request.getSession().getId());
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>频段扫描数据导出</title>
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
			/*margin-left:30px;*/
		}
	</style>
    <script type="text/javascript">
	//界面上选中的文件导入数据库中
	function exportFileFromPDSM(bh,type,operate) {
	
		
		
    	var frequence= document.getElementById("frequence").value;
    	if(frequence == "0.0"){
    		alert("请选择频率。");
    		return;
    	}
    	document.getElementById("response").innerHTML = "操作中。请等待...";
    	var param ;
    	if(operate == "1"){
    		param = "/rsui/DataImport/ExportFilefromPDSM.jsp?";
		}else{
			param = "/rsui/DataImport/ExportFrequencefromPDSM.jsp?";
		}
		param = param + "frequence=" + frequence;
		param = param + "&bh=" + bh;
		param = param + "&type=" + type;
		param = param + "&operate=" + operate;
		//alert(param);
		param = encodeURI(param);
		param = encodeURI(param);
		//alert(param);
		//path = path + "-" +frequence+"Hz.csv";
		//fileName = fileName + "-" +frequence+"Hz.csv";
		if(operate == "1"){
			window.location.href=param;
		}else{
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			}else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.open("GET",param, false);
			xmlhttp.send();
			
			var str = xmlhttp.responseText;
			str = str.replace(/[\r\n]/g, "");	//删除换行符
			//alert(str);
			document.getElementById("response").innerHTML = str;
		}
    	/*
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
		document.getElementById("response").innerHTML = str;
		*/
	}
	
	
	</script>
  </head>
  
  <body style="padding:30px 0 0 15px;">
	<%
	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
    Double freq = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8"));
    /*
    String base = session.getAttribute("tempWorkDir").toString();
	ImportFile infile=new ImportFile();
	String basepath=infile.getBasePath(base);
	String userID =  session.getAttribute("userID").toString();
	basepath = basepath +  userID + "\\";
	//System.out.println(basepath +  userID + fileName + bh + fileType);
	String fileName =  bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh ;
	fileName = userID + fileName;
	basepath = basepath  + fileName;
    */
    ArrayList<Double> frequence = Handle.PDSMFrequence(bh,type);
    %>
    <table>
    <tr><td><font>请选择频率（MHz）：</font></td>
	<td><select id="frequence">
	  <option value="<%=freq%>" ><%=freq/1000000%></option>
	  <%
		int size = frequence.size();
		for(int i=0;i<size;i++){%>
			<option value="<%=frequence.get(i)%>" ><%=frequence.get(i)/1000000%></option>
		<%}%>
	</select></td></tr>
	<tr style="height:60px;"><td>保存到本地：</td><td>
	<input type="button" id="import" value="确定" onclick="javascript:exportFileFromPDSM('<%=bh%>','<%=type%>','1')" />
	</td></tr>
	<tr ><td>录入到数据库：</td><td>
	<input type="button" id="import" value="确定" onclick="javascript:exportFileFromPDSM('<%=bh%>','<%=type%>','2')" />
	</td></tr>
	</table>
	<div id = "response" style="padding-left:10px;padding-top:30px; color:red;"></div>		

  </body>
</html>
