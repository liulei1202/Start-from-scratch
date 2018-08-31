<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<% 

String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String fileName = java.net.URLDecoder.decode(request.getParameter("fileName"),"UTF-8");


String base = session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);
String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";
String filePath = basepath + userID + fileName; 

//System.out.println(filePath);
//System.out.println(grapPath);
//System.out.println(row);
String n = infile.importZFQJCExcel(filePath,fileType);

%>
<%=n%>
