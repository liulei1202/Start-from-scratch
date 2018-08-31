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
    
    <title>视频播放</title>
    
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
   		sql = "EMCDPJCVIDEO";
   	}
   	 else if(type.equals("DBPDSMQuery")){
     sql ="EMCPDSMVIDEO";
    }
     else if(type.equals("DBXHCXQuery")){
     sql ="EMCDPCXVIDEO";
    }
     else if(type.equals("DBLHDWQuery")){
     sql ="EMCLHDWVIDEO";
    }
    else if(type.equals("CDBDPJCQuery")){
    sql="EMCUDPJCVIDEO";
    }
    else if(type.equals("CDBPDSMQuery")){
     sql ="EMCUPDSMVIDEO";
    }
    else if(type.equals("CDBXHCXQuery")){
   sql ="EMCUDPCXVIDEO";
    }
    else if(type.equals("CDBLHDWQuery")){
     sql ="EMCULHDWVIDEO";
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
   %>
    <div style="position:absolute;width:95%; height:95%;">
    <%if(IE.equals("true")){
   %>
    	<embed src="<%=source %>" style="width:100%; height:100%;max-width: 100%;max-height: 100%;"></embed><!-- IE的wav和wmv，firefox的wmv -->
    <%}else{ 
    	if(source.substring(source.lastIndexOf(".")).equals(".wav")){%>
    		<audio src="<%=source %>" controls="controls" autoplay></audio><!-- Chrome和firefox的wav -->
    		
    	<%}else{
    	%>
    		<embed id="video" src="<%=source %>"  style="width:100%; height:100%;max-width: 100%;max-height: 100%;"></embed><!-- IE的wav和wmv，firefox的wmv -->
    		<script>  

				//document.getElementById("video").setAttribute("src","<%=source %>");
    			//document.getElementById("video").src="<%=source %>";
    		</script>
    	<%}
    }%>
 	</div>
  </body>
</html>
