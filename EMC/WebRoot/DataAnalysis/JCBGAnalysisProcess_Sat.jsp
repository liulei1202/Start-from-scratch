<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="BaseData.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星监测报告数据分析</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script charset="utf-8" type="text/javascript" src="js/DataManage/DataQuery.js" ></script>
	<link rel="stylesheet" type="text/css" href="js/css/Query.css">
  </head>
  
  <body>
    <%
    	String startTime = java.net.URLDecoder.decode(request.getParameter("startTime"),"UTF-8");
       	String endTime = java.net.URLDecoder.decode(request.getParameter("endTime"),"UTF-8");
       	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.JCBGAnalysis_Sat(type, startTime, endTime);
       	%>
	     <table  border="1" cellspacing="0" style="padding-top:-50px;">
	     <caption>分析结果：</caption>
	     <tr><th style="min-width:20px;"></th><th style="min-width:200px;">报告类型</th><th style="min-width:200px;">报告数量</th></tr>
	     	<% 
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> al = new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			al=list.get(i);
	     			%>
		     <tr><td><%=i+1 %></td>
		     <td><%=al.get(0) %></td><td><%=al.get(1) %></td></tr>
	    	 <%}%>
	     </table>
  </body>
</html>
