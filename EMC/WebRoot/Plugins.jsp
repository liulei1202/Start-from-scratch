<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="com.fileupload.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>下载插件</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <%
	ImportFile infile=new ImportFile();
	String basepath=infile.getBasePath("");/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
	basepath = basepath + "Plugins.rar";
	Handle.download(basepath, response);
	out.clear();
	out = pageContext.pushBody();
	
%>
  </body>
</html>
