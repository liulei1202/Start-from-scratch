<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<%@ page import="DataQuery.*"%>
<%@ page import="DBConnection.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.io.File" %>
<% 
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
Double threshold = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8"));
Double interval = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("interval"),"UTF-8"));
/*
ImportFile infile=new ImportFile();
String userId = session.getAttribute("userID").toString();
String base =  session.getAttribute("tempWorkDir").toString();
String basepath=infile.getBasePath(base);// E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  
basepath = basepath +  userId + File.separator;// E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ 
*/

String result = Handle.launchTime(bh,fileType,threshold,interval);

%>
<%=result%>