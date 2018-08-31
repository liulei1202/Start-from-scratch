<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<% 

String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim(); 

String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 

String fileName = java.net.URLDecoder.decode(request.getParameter("fileName"),"UTF-8");

Double position = ImportFile.GetLongiLatiDouble(java.net.URLDecoder.decode(request.getParameter("position"),"UTF-8"));	
String satName = java.net.URLDecoder.decode(request.getParameter("satName"),"UTF-8"); 

String country = java.net.URLDecoder.decode(request.getParameter("country"),"UTF-8"); 
Double rbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("rbw"),"UTF-8").trim())*1000;
Double vbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("vbw"),"UTF-8").trim())*1000;
String levels = java.net.URLDecoder.decode(request.getParameter("level"),"UTF-8"); 

String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8"); 
String polar = java.net.URLDecoder.decode(request.getParameter("polar"),"UTF-8"); 
Double startFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8").trim())*1000000;
Double endFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8").trim())*1000000;

String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8"); 
String time = java.net.URLDecoder.decode(request.getParameter("time"),"UTF-8"); 
String person = java.net.URLDecoder.decode(request.getParameter("person"),"UTF-8"); 
String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8"); 

String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8"); 

String zfq = java.net.URLDecoder.decode(request.getParameter("zfq"),"UTF-8"); 

String antenna  = java.net.URLDecoder.decode(request.getParameter("antenna"),"UTF-8"); 

String weaken  = java.net.URLDecoder.decode(request.getParameter("weaken"),"UTF-8"); 

String comments  = java.net.URLDecoder.decode(request.getParameter("comments"),"UTF-8"); 

String base = session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);

String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";
String path = basepath + userID + fileName; 
System.out.println(path);
String n = infile.importZFQJCManual(path,bh,businessType,businessName,zfq,antenna,weaken,comments,fileType,position,satName,country,rbw,vbw,levels,frequence,polar,
		startFrequence,endFrequence,station,time,person);

%>
<%=n%>
