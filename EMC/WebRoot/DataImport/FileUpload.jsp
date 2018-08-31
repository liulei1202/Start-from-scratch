<!DOCTYPE HTML>
<html>
<head>
<title>My JSP 'FileChoice.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="EMCTools.*"%>
<%@ page import="com.fileupload.*"%>
<%
String path1 = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path1+"/";
%>
<base href="<%=basePath%>">
<script src="js/DataManage/DataImport.js"></script>
<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="js/swfupload/handlers.js"></script>
</head>
<body>
<%
int n=0;
String base = session.getAttribute("tempWorkDir").toString();	/* tempDir\  */
ImportFile infile=new ImportFile();

String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
basepath = basepath.replaceAll("%20", " ");
String user=session.getAttribute("userID").toString(); 
basepath = basepath +  user + "\\";
EMCFileUpload fu;
fu = new EMCFileUpload(basepath);
//System.out.println(basepath);
fu.fetchFiles(request, user);	/* request=org.apache.catalina.connector.RequestFacade@626fd2 */
%>
</body>
