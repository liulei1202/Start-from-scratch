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
    
    <title>联合定位-数据查重</title>
    
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
       	String parameter = java.net.URLDecoder.decode(request.getParameter("parameter"),"UTF-8");
       	String original = java.net.URLDecoder.decode(request.getParameter("original"),"UTF-8");
       	String graphic = java.net.URLDecoder.decode(request.getParameter("graphic"),"UTF-8");
       	String audio = java.net.URLDecoder.decode(request.getParameter("audio"),"UTF-8");
       	String video = java.net.URLDecoder.decode(request.getParameter("video"),"UTF-8");
       	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
       	
       	//System.out.println(startTime+endTime+startFrequence+endFrequence+type);
        DecimalFormat df = new DecimalFormat("#0.000");
	    DecimalFormat df4 = new DecimalFormat("#0.0000");
        if(original.equals("true")){
       	LinkedList<LHDWHead> list = new LinkedList<LHDWHead>();
       	list = DataQuery.LHDWRepeat(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
    %>
     <table  border="1" cellspacing="0" >
     <caption>原始监测数据</caption>
     <tr><th>频率（MHz）<th><th>任务类型</th><th>任务名称</th><th>起始时间</th><th>终止时间</th><th>监测台站1</th><th>监测台站2</th><th>监测台站3</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>删除</th><th>导出文件</th>
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
     			String md5=aL.MD5;
	     		String businessName  = aL.businessName;
	     		String businessType  = aL.businessType;
     			%>
  <tr>
     <td><%=frequence1%></td><td><%=businessType %></td><td><%=businessName %></td><td><%=starttime %></td><td><%=endtime %></td><td><%=Station1 %></td><td><%=Station2%></td><td><%=Station3%></td>
     <td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
     <td><a href="javascript:deleteData1('<%=bh%>','<%=type %>','')" >删除</a></td>
     <td><%=md5 %></td>
     
     </tr>
    	 <%}%>
     </table>
       <%}

	     /*是否显示视频数据*/
	    if(video.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.VideoRepeat_LHDW(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>视频数据</caption>
	     <tr><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>信号类型</th><th>经度</th><th>纬度</th><th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>删除</th><th>MD5</th>
	     </tr>
	     	<% 
	     		
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     			String starttime = aL.get(0);
	     			starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
	     			String endtime = aL.get(1);
	     			endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
	     			String frequence = aL.get(2);
	     			frequence = df4.format(Double.parseDouble(frequence)/1000000);
	     			String Station = aL.get(3);
	     			String Monitor = aL.get(4);
	     			String Monitorlocation = aL.get(5);
	     			String md5=aL.get(6);
	     			String bh = aL.get(7);
	     			String businessType = aL.get(8);
					String businessName = aL.get(9);
					String signalType = aL.get(10);
					String longitude = aL.get(11);
					String latitude = aL.get(12);
	     			%>
	     <tr><td><%=frequence %></td><td><%=businessType %></td><td><%=businessName %></td>
	     <td><%=signalType %></td><td><%=longitude %></td><td><%=latitude %></td>
	     <td><%=starttime %></td><td><%=endtime %></td>
	    <td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
	     <td><a href="javascript:deleteData1('<%=bh%>','<%=type %>','Video')" >删除</a></td>
	     <td><%=md5 %></td>
	     
	     </tr>
	    	 <%}%>
	     </table>
	     <%}%>
	     
  </body>
</html>
