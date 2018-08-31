<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<%@ page import="java.lang.*" %>
<% 
//System.out.println("JCBGFileAutoImport.jsp");
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String reportName = java.net.URLDecoder.decode(request.getParameter("reportName"),"UTF-8"); 
String reportType = java.net.URLDecoder.decode(request.getParameter("reportType"),"UTF-8"); 
String startFrequence = java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8"); 
startFrequence = (Double.parseDouble(startFrequence)*1000000)+"";
String endFrequence = java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8"); 
endFrequence = (Double.parseDouble(endFrequence)*1000000)+"";
String startTime = java.net.URLDecoder.decode(request.getParameter("startTime"),"UTF-8"); 
String endTime = java.net.URLDecoder.decode(request.getParameter("endTime"),"UTF-8"); 
String keyWords = java.net.URLDecoder.decode(request.getParameter("keyWords"),"UTF-8"); 
String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
String author = java.net.URLDecoder.decode(request.getParameter("author"),"UTF-8");
String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
String equipment = java.net.URLDecoder.decode(request.getParameter("equipment"),"UTF-8");
String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
String fileName = java.net.URLDecoder.decode(request.getParameter("path"),"UTF-8");

String base = session.getAttribute("tempWorkDir").toString();

ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);

String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";

System.out.println(basepath +  userID + fileName);

String path = basepath + userID + fileName; 
String  n= infile.importJCBGData(path,fileType,reportName,reportType,
	startTime, endTime,startFrequence,endFrequence,keyWords, station, author, monitorLocation,equipment,businessType,businessName,bh);

%>
<%=n%>
