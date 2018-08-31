<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<% 

String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String grapType = java.net.URLDecoder.decode(request.getParameter("grapType"),"UTF-8"); 
String fileName = java.net.URLDecoder.decode(request.getParameter("fileName"),"UTF-8");

String base = session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);
String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";

//System.out.println(basepath +  userID + fileName);

fileName = basepath + userID + fileName;
String n = "导入失败。"; 
if(grapType.trim().equals("原始数据")){
n = infile.importOriginalfromSignal(fileName, bh,type);
}else{
n= infile.importGrapfromSignal(fileName, bh,type,grapType);
}
%>
<%=n%>

