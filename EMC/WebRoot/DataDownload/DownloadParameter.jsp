<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
String allbh = java.net.URLDecoder.decode(request.getParameter("allbh"),"UTF-8");
String[] bh = allbh.split(",");
//System.out.println(bh.length+","+bh[0]);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
Date dt = new Date(); 
String currentTime = sdf.format(dt);

String userId = session.getAttribute("userID").toString();
String base =  session.getAttribute("tempWorkDir").toString();
ImportFile infile=new ImportFile();
String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
basepath = basepath +  userId + "\\";/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ */
String fileName = "Signal-"+userId+"-"+ currentTime;
basepath = basepath + fileName+"\\";
String filepath = basepath+fileName+".xls";

String result = Handle.batchDownloadParameter(bh,type,basepath,filepath,response);
out.clear();
out = pageContext.pushBody();
 %>