<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DBConnection.EMCDB" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>图片查看</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js" ></script>
	<style>
		input[id="import"]{
			width:90px;
			height:25px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			
			border-radius:5px;
			
			/*margin-right:50%;*/
		}
	</style>
  </head>
  
  <body>
   <%
   String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
   String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8").trim();
   String grapType = java.net.URLDecoder.decode(request.getParameter("grapType"),"UTF-8").trim();
 
    String result = null;
    String sql = null;
    if (type.equals("DBDPJCQuery"))
    {
   		sql = "EMCDPJCGRAPHIC";
   	}
   	else if (type.equals("DBPDSMQuery"))
    {
   		sql = "EMCPDSMGRAPHIC";
   	}
   	else if (type.equals("DBXHCXQuery"))
    {
   		sql = "EMCDPCXGRAPHIC";
   	}
   	else if(type.equals("DBLHDWQuery"))
   	{
   	    sql="EMCLHDWGRAPHIC";
   	    }
   	    
   	else if (type.equals("CDBDPJCQuery"))
    {
   		sql = "EMCUDPJCGRAPHIC";
   	}
   	else if (type.equals("CDBPDSMQuery"))
    {
   		sql = "EMCUPDSMGRAPHIC";
   	}
   	else if (type.equals("CDBXHCXQuery"))
    {
   		sql = "EMCUDPCXGRAPHIC";
   	}
   	else if(type.equals("CDBLHDWQuery"))
   	{
   	    sql="EMCULHDWGRAPHIC";
   	}else if(type.equals("WXZFQJCQuery"))
   	{
   	    sql="EMCWZFQJC";
   	}
   	sql = "select data FROM " + sql +" WHERE  bh = '"+ bh +"'";
   	String regEX = "[\\u4e00-\\u9fa5]";
    Pattern p = Pattern.compile(regEX);
    Matcher m = p.matcher(grapType);
	if(m.find()){//说明存在汉字，为图形数据文件。汉字是图形类型。
   		sql = sql + " and GRAPHICTYPE='"+ grapType+"'";
   	}
   //System.out.println(grapType);
   //System.out.println(sql);
   String fileName = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
   String userId = session.getAttribute("userID").toString();
   String base =  session.getAttribute("tempWorkDir").toString();
   ImportFile infile=new ImportFile();
   String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
   basepath = basepath +  userId + File.separator;/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ */
   
   result=EMCDB.ExportFile(sql,fileName.trim(),basepath);
   if(result.equals("0")){//说明blob中无数据%>
   		本条数据无图片。
   <%}else{
   String source = "/rsui/" + base.replace("\\","") + "/" + userId + "/" + result;
   System.out.println(source);
   if(source.contains("%")){
   	   String temp = basepath+result;//绝对路径
	   File file1 = new File(temp);
	   File file2 = new File(temp.replaceAll("%", ""));
	   if(file1.renameTo(file2)){
	   		System.out.println("renamed");
	   }
	   source = source.replaceAll("%","");
   }
   %>
	 <div style="position:absolute;width:98%; height:95%;">
	 	<img id="img" src = "<%=source%>" alt ="未找到资源" style="width:100%; height:100%;max-width: 100%;max-height: 90%;">
	 	<center style="padding-top:20px;"><input type="button"  value="导出文件" id="import" onclick="javascript:exportFile('<%=bh%>','<%=type%>','<%=grapType%>')"></center>
	 </div>
   <%} %>	 
  </body>
</html>
