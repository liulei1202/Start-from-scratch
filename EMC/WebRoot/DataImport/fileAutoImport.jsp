<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<% 
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8");
String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
String fileName = java.net.URLDecoder.decode(request.getParameter("path"),"UTF-8");

String base = session.getAttribute("tempWorkDir").toString();

ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);

String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";
//System.out.println(basepath +  userID + fileName + bh + fileType);

fileName = basepath + userID + fileName; 
String  n= infile.importCSV(fileName, fileType,station,monitor,monitorLocation,businessType,businessName,bh);

%>
<%=n%>
