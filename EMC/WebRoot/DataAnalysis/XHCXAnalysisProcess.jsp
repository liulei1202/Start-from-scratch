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
    
    <title>信号测向-数据查询</title>
    
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
       	String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
       	String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorlocation"),"UTF-8");
       	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
       	//System.out.println(startTime+endTime+startFrequence+endFrequence+type);
       	DecimalFormat df = new DecimalFormat("#0.000");
	    DecimalFormat df4 = new DecimalFormat("#0.0000");
       	
       	LinkedList<DPCXHead> list = new LinkedList<DPCXHead>();
       	list = DataQuery.DPCXQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
    %>
     <table  border="1" cellspacing="0" >
     <tr><th style="min-width:20px;"></th><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>经度</th><th>纬度</th><th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>导出文件</th><th>修改</th><th>删除</th>
     </tr>
     	<% 
     		
     		int size = list.size();
     		int i = 0;
     		DPCXHead aL=new DPCXHead();
     		for (i=0;i<size;i++){
     			aL=list.get(i);
     			String frequence = df4.format(aL.Frequence/1000000);
     			String starttime = aL.StartTime;
     			starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
     			String endtime = aL.EndTime;
     			endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
     			String longitude = String.valueOf(aL.Longitude);
     			//longitude = df.format(Double.parseDouble(longitude));
     			String latitude = String.valueOf(aL.Latitude);
     			//latitude = df.format(Double.parseDouble(latitude));
     			String bandwidth = aL.Bandwidth;
     			String Radiowidth = aL.Radiowidth;
     			String Decode = aL.Decode;
     			String schema = aL.schema;
     			String Station = aL.Station;
     			String Monitor = aL.Monitor;
     			String Monitorlocation = aL.MonitorLocation;
     			String bh = aL.BH;
	     		String businessName  = aL.businessName;
	     		String businessType  = aL.businessType;
     			%>
     <tr><td><%=i+1 %></td>
     <td><%=frequence %></td><td><%=businessType %></td><td><%=businessName %></td><td><%=longitude %></td><td><%=latitude %></td>
     <td><%=starttime %></td><td><%=endtime %></td><td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
     
     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','')">导出文件</a></td>
     <td><a href="javascript:modifyOrigianlData('<%=bh%>','<%=type %>')" >修改</a></td>
     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','')" >删除</a></td>
     </tr>
    	 <%}%>
    	 
     </table>
     
  </body>
</html>
