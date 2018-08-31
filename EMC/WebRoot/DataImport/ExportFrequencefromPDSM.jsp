<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*"%>
<%@ page import="DataQuery.*"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileOutputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.math.BigDecimal"%>

<% 
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
BigDecimal bd = new BigDecimal(frequence);
String str = bd.toPlainString();
//System.out.println(str);

String operate = java.net.URLDecoder.decode(request.getParameter("operate"),"UTF-8");
String base = session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);
String userID =  session.getAttribute("userID").toString();
basepath = basepath +  userID + "\\";
//System.out.println(basepath +  userID + fileName + bh + fileType);
String fileName =  bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh ;
fileName = userID + fileName +"-"+ frequence+ "Hz.csv";
String path = basepath  + fileName ; 

boolean repeat =  Handle.isRepeat(fileName,fileType);
String result = null;
if(repeat){
	result="已将本条数据的当前频点写入数据库。";
}else{
	result = Handle.writeCSVfromCSV(bh, fileType,str,path,fileName,response,operate);
}
%>
<%=result%>