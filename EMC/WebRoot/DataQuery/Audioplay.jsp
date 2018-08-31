<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DBConnection.EMCDB" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.io.File" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>音频播放</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js" ></script>
  </head>
  
  <body>
   <%
   String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
   String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
   String IE = java.net.URLDecoder.decode(request.getParameter("IE"),"UTF-8");
  
    String result = null;
    String sql = null;
    if (type.equals("DBDPJCQuery"))
    {
   		sql = "EMCDPJCAUDIO";
   	}
   	else if (type.equals("DBXHCXQuery"))
    {
   		sql = "EMCDPCXAUDIO";
   	}
   	else if (type.equals("CDBDPJCQuery"))
    {
   		sql = "EMCUDPJCAUDIO";
   	}
   	else if (type.equals("CDBXHCXQuery"))
    {
   		sql = "EMCUDPCXAUDIO";
   	}
   sql = "select data FROM " + sql +" WHERE  bh = '"+ bh +"'";
   bh = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
   String userId = session.getAttribute("userID").toString();
   String base =  session.getAttribute("tempWorkDir").toString();
   ImportFile infile=new ImportFile();
   String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
   basepath = basepath +  userId + File.separator;/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ */
   //System.out.println(basepath);
   result=EMCDB.ExportFile(sql,bh.trim(),basepath);
   //System.out.println(result);
   String source = "/rsui/" + base.replace("\\","") + "/" + userId + "/" + result;
   System.out.println("source:"+source);
   if(IE.equals("true")){
   %>
    <embed src="<%=source %>" style="margin-top:10px;padding-bottom:20px;border:solid 2px #3c8dbc;background-color:#9c9c9c"></embed><!-- IE和Chrome -->
    <%}else{ %>
    <audio src="<%=source %>" controls="controls" autoplay></audio><!-- Chrome和firefox -->
	<%}%>
  </body>
</html>
