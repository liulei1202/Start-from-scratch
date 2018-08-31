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
    
    <title>联合定位-数据查询</title>
    
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
       
       	LinkedList<LHDWHead> list = new LinkedList<LHDWHead>();
       	list = DataQuery.LHDWQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
    %>
     <table  border="1" cellspacing="0" >
     
     <tr><th style="min-width:20px;"></th><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>起始时间</th><th>终止时间</th><th>监测台站1</th><th>监测台站2</th><th>监测台站3</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>导出文件</th><th>修改</th><th>删除</th>
     </tr>
     	<% 
     		
     		int size = list.size();
     		int i = 0;
     		LHDWHead aL=new LHDWHead();
     		for (i=0;i<size;i++){
     			aL=list.get(i);
     			String starttime = aL.StartTime;
     			starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
     			String endtime = aL.EndTime;
     			endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
     			String Station1 = aL.Station1;
     			String frequence1 = df4.format(aL.Frequence1/1000000);	//三站频率一致，所以取1个就可以
     			String longitude1 = String.valueOf(aL.Longitude1);
     			//longitude1 = df.format(Double.parseDouble(longitude1));
     			String latitude1 = String.valueOf(aL.Latitude1);
     			//latitude1 = df.format(Double.parseDouble(latitude1));
     			String Station2 = aL.Station2;
     			String longitude2 = String.valueOf(aL.Longitude2);
     			//longitude2 = df.format(Double.parseDouble(longitude2));
     			String latitude2 = String.valueOf(aL.Latitude2);
     			//latitude2 = df.format(Double.parseDouble(latitude2));
     			String Station3 = aL.Station3;
     			String longitude3 = String.valueOf(aL.Longitude3);
     			//longitude3 = df.format(Double.parseDouble(longitude3));
     			String latitude3 = String.valueOf(aL.Latitude3);
     			//latitude3 = df.format(Double.parseDouble(latitude3));
     			String Station = aL.Station;
     			String Monitor = aL.Monitor;
     			String Monitorlocation = aL.MonitorLocation;
     			String bh = aL.BH;
	     		String businessName  = aL.businessName;
	     		String businessType  = aL.businessType;
     			%>
  	 <tr><td><%=i+1 %></td><td><%=frequence1%></td><td><%=businessType %></td><td><%=businessName %></td>
     <td><%=starttime %></td><td><%=endtime %></td><td><%=Station1 %></td><td><%=Station2%></td><td><%=Station3%></td>
     <td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
      <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','')">导出文件</a></td>
     <td><a href="javascript:modifyOrigianlData('<%=bh%>','<%=type %>')" >修改</a></td>
     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','')" >删除</a></td>
    
     </tr>
    	 <%}%>
     </table>
      
	     
  </body>
</html>