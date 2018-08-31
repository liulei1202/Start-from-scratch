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
    
    <title>监测报告数据查询</title>
    
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
       	String startFrequence = java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8");
       	String endFrequence = java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8");
       	String keyWords = java.net.URLDecoder.decode(request.getParameter("keyWords"),"UTF-8");
       	String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
       	//System.out.println("station"+station);
       	String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorlocation"),"UTF-8");
       	String author = java.net.URLDecoder.decode(request.getParameter("author"),"UTF-8");
       	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
       	//System.out.println(startTime+endTime+startFrequence+endFrequence+type);
       
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.JCBGQuery(startTime,endTime,startFrequence,endFrequence,keyWords,station,monitorLocation,author,type);
   		
       	 %>
	     <table  border="1" cellspacing="0" >
	     <tr><th style="min-width:20px;"></th><th>频率（MHZ）</th><th>报告名称</th><th>报告类型</th><th>任务类型</th><th>任务名称</th><th>起始时间</th><th>终止时间</th><th>关键词</th><th>监测台站</th><th>监测地点</th><th>监测设备</th><th>操作人</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     		DecimalFormat df = new DecimalFormat("#.0000");
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     			String ReportType = aL.get(0);
	     			String StartTime = aL.get(1);
	     			String EndTime = aL.get(2);
	     			String Frequence = String.valueOf((Double.parseDouble(aL.get(3))/1000000));
	     			String KeyWords = aL.get(4);
	     			String Station = aL.get(5);
	     			String Location = aL.get(6);
	     			String equipment = aL.get(7);
	     			String writer = aL.get(8);
	     			String fileName = aL.get(9);
	     			String bh = aL.get(10);
	     		String businessName  = aL.get(12);
	     		String businessType  = aL.get(11);
	     			%>
	     <tr><td><%=i+1 %></td><td><%=Frequence %></td><td><%=fileName %></td>
	     <td><%=ReportType %></td><td><%=businessType %></td><td><%=businessName %></td><td><%=StartTime %></td><td><%=EndTime %></td><td><%=KeyWords %></td><td><%=Station %></td>
	     <td><%=Location %></td><td><%=equipment %></td><td><%=writer %></td>
	     
         <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','')">导出文件</a></td>
         <td><a href="javascript:modifyJCBGData('<%=bh%>','<%=type %>','')" >修改</a></td>
         <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','')" >删除</a></td>
	    
	     </tr>
	    	 <%}%>
	     </table>
  </body>
</html>
