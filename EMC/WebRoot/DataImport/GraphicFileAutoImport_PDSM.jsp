<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<%@ page import="java.lang.*" %>
<% 
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8"); 
String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String graphicType = java.net.URLDecoder.decode(request.getParameter("graphicType"),"UTF-8"); 
String startTime = java.net.URLDecoder.decode(request.getParameter("startTime"),"UTF-8"); 
String endTime = java.net.URLDecoder.decode(request.getParameter("endTime"),"UTF-8"); 
String startFrequence = java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8"); 
startFrequence = (Double.parseDouble(startFrequence)*1000000)+"";
String endFrequence = java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8");
endFrequence = (Double.parseDouble(endFrequence)*1000000)+""; 
String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8");
String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
String fileName = java.net.URLDecoder.decode(request.getParameter("fileName"),"UTF-8");
String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
String signalType  = java.net.URLDecoder.decode(request.getParameter("signalType"),"UTF-8");
String longitude  = java.net.URLDecoder.decode(request.getParameter("longitude"),"UTF-8");
String latitude  = java.net.URLDecoder.decode(request.getParameter("latitude"),"UTF-8");

String base = session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);

String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";
String path = basepath + userID + fileName; 
//System.out.println(path);
String  n= infile.importGraphicData_PDSM(path,fileType,businessType,businessName,signalType,longitude,latitude, graphicType, startTime, endTime, startFrequence, endFrequence,station, monitor, monitorLocation,bh);

%>
<%=n%>
