<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>已弃用，最老的ZFQJC,无图片，类型不完整</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ page import="java.util.*" %>
	<%@ page import="DBConnection.*" %>
  </head>
  
  <body>
    <%
		//System.out.println("DBDPJCSignalParameterRegProcess.jsp");
		//轨位样式应该为116°25'56.0E或者直接为数字
		Double position = ImportFile.GetLongiLatiDouble(java.net.URLDecoder.decode(request.getParameter("position"),"UTF-8").trim());	
		String satName = java.net.URLDecoder.decode(request.getParameter("satName"),"UTF-8");
		String country = java.net.URLDecoder.decode(request.getParameter("country"),"UTF-8");
		Double rbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("rbw"),"UTF-8"))*1000;
		Double vbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("vbw"),"UTF-8"))*1000;
		
		String levels = java.net.URLDecoder.decode(request.getParameter("level"),"UTF-8");	
		String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
		String polar = java.net.URLDecoder.decode(request.getParameter("polar"),"UTF-8");
     	Double startFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8"))*1000000;
		Double endFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8"))*1000000;
		
		String dataType = java.net.URLDecoder.decode(request.getParameter("dataType"),"UTF-8");
		String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
		String time = java.net.URLDecoder.decode(request.getParameter("time"),"UTF-8");
		String person = java.net.URLDecoder.decode(request.getParameter("person"),"UTF-8");
		String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime = sdf.format(dt);
		DecimalFormat df = new DecimalFormat("#.000");
		
		String sql;
		String bh = time.contains(" ") ? time.substring(0,time.lastIndexOf(" ")).trim() : time;
		bh = bh + "-" + position  + "-" + startFrequence  + "-" + currentTime;
		
		if(type.equals("WXZFQJCData")){	
			
			bh = "ZFQJC" + "-" + bh;
			sql = "insert into EMCWZFQJC (bh, position, satName, country, rbw,vbw,levels,frequence,polar,startFrequence,endFrequence,dataType,station,time,person) "+
			" values('" + bh + "'," + position + ",'" + satName + "','"+ country +"'," + rbw + "," + vbw + "," + levels + ",'" + frequence + "','" +
			polar + "'," + startFrequence + "," + endFrequence + ",'" + dataType + "','" + station + "','" + time + "','" + person + "')";
		}else{
			
			sql = null;
		}
	    //System.out.println(sql);
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = emcdb.UpdateSQL(sql);
		emcdb.closeStm();
		
		
		if(result == 0){%>		
			<%="提交失败" %>
		<%}else{ %>
			
			<%="提交成功" %>		
		<%} %>	
  </body>
</html>
