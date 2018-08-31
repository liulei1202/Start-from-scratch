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
    
    <title>频段扫描-数据查询</title>
    
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
       	LinkedList<PDSMHead> list = new LinkedList<PDSMHead>();
       	list = DataQuery.PDSMQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
    %>
     
     
     <table  border="1" cellspacing="0" >
     <caption>原始监测数据</caption>
     <tr><th style="min-width:20px;"></th><th>起始频率（MHz）</th><th>终止频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>经度</th><th>纬度</th><th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>频段播放</th><th>单频播放</th><th>计算占用度</th><th>导出文件</th><th>导出频点</th><th>修改</th><th>删除</th>
     </tr>
     	<% 
     		
     		int size = list.size();
     		int i = 0;
     		PDSMHead aL=new PDSMHead();
     		for (i=0;i<size;i++){
     			aL=list.get(i);
     			String StartFrequence = df4.format(aL.StartFrequence/1000000);
     			String EndFrequence = df4.format(aL.EndFrequence/1000000);
     			String StartTime = aL.StartTime;
     			StartTime = StartTime.contains(".") ? StartTime.substring(0,StartTime.indexOf(".")) : StartTime;
     			String EndTime = aL.EndTime;
     			EndTime = EndTime.contains(".") ? EndTime.substring(0,EndTime.indexOf(".")) : EndTime;
     			String Longitude = String.valueOf(aL.Longitude);
     			//Longitude = df.format(Double.parseDouble(Longitude));
     			String Latitude = String.valueOf(aL.Latitude);
     			//Latitude = df.format(Double.parseDouble(Latitude));
     			String Bandwidth = aL.Bandwidth;
     			String RadioFreattenuation = aL.RadioFreattenuation;
     			String MidFreattenuation = aL.MidFreattenuation;
     			String DeModen = aL.DeModen;
     			String Detector = aL.Detector;
     			String Station = aL.Station;
     			String Monitor = aL.Monitor;
     			String Monitorlocation = aL.MonitorLocation;
     			String bh = aL.BH;
	     		String businessName  = aL.businessName;
	     		String businessType  = aL.businessType;
     	         %>
     <tr><td><%=i+1 %></td>
     <td><%=StartFrequence %></td><td><%=EndFrequence %></td><td><%=businessType %></td><td><%=businessName %></td><td><%=Longitude %></td>
     <td><%=Latitude %></td><td><%=StartTime %></td><td><%=EndTime %></td><td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
     <td><a href="javascript:pdsmPlay('<%=bh%>','<%=type %>','100')">频段播放</a></td>
     <td><a href="javascript:pdsmFreqPlay('<%=bh%>','<%=type %>','100')">单频播放</a></td>
     <td><a href="javascript:pdsmOccupancy('<%=bh%>','<%=type %>')">计算</a></td>
     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','')">导出文件</a></td>
     <td><a href="javascript:exportFileFrequence('<%=bh%>','<%=type%>','0')">导出频点</a></td>
     <td><a href="javascript:modifyOrigianlData('<%=bh%>','<%=type %>')" >修改</a></td>
     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','')" >删除</a></td>
     </tr>
    	 <%}%>
     </table>
     <%} 
	    /*是否显示图形数据*/
	    if(graphic.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.GraphicQuery_PDSM(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>图形数据</caption>
	     <tr><th style="min-width:20px;"></th><th>图形类型</th><th>起始频率（MHz）</th><th>终止频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>信号类型</th><th>经度</th><th>纬度</th><th>监测时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>查看</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     	
	     		
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     			String GraphicType = aL.get(0);
	     			String starttime = aL.get(1);
	     			starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
	     			String endtime = aL.get(2);
	     			endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
	     			String StartFrequence = aL.get(3);
	     			StartFrequence = df4.format(Double.parseDouble(StartFrequence)/1000000);
	     			String EndFrequence = aL.get(4);
	     			EndFrequence = df4.format(Double.parseDouble(EndFrequence)/1000000);
	     			String Station = aL.get(5);
	     			String Monitor = aL.get(6);
	     			String MonitorLocation = aL.get(7);
	     			String bh = aL.get(8);
	     			String businessType = aL.get(9);
					String businessName = aL.get(10);
					String signalType = aL.get(11);
					String longitude = aL.get(12);
					String latitude = aL.get(13);
	     			%>
	     			
	     <tr><td><%=i+1 %></td>
	     <td><%=GraphicType %></td><td><%=StartFrequence %></td><td><%=EndFrequence%></td><td><%=businessType %></td><td><%=businessName %></td>
	     <td><%=signalType %></td><td><%=longitude %></td><td><%=latitude %></td><td><%=starttime %></td><td><%=Station %></td>
	     <td><%=Monitor%></td><td><%=MonitorLocation %></td>
	     <td><a href="javascript:Graphic('<%=bh%>','<%=type %>','Graphic')" >查看</a>
	     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','<%=GraphicType%>')">导出文件</a></td>
	     <td><a href="javascript:modifyGraphicData('<%=bh%>','<%=type %>','<%=GraphicType%>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','Graphic')" >删除</a></td>
	     
	     </tr>
	    	 <%}%>
	     </table>
	     <%}
	     /*是否显示视频数据*/
	    if(video.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.VideoQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>视频数据</caption>
	     <tr><th style="min-width:20px;"></th><th>频率（MHz）</th><th>起始频率（MHz）</th><th>终止频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>信号类型</th><th>经度</th><th>纬度</th><th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>播放</th><th>导出文件</th><th>修改</th><th>删除</th>
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
	     			String startfrequence = aL.get(3);
	     			startfrequence = df4.format(Double.parseDouble(startfrequence)/1000000);
	     			String endfrequence = aL.get(4);
	     			endfrequence = df4.format(Double.parseDouble(endfrequence)/1000000);
	     			String Station = aL.get(5);
	     			String Monitor = aL.get(6);
	     			String Monitorlocation = aL.get(7);
	     			String bh = aL.get(8);
	     			String businessType = aL.get(9);
					String businessName = aL.get(10);
					String signalType = aL.get(11);
					String longitude = aL.get(12);
					String latitude = aL.get(13);
	     			%>
	     <tr><td><%=i+1 %></td>
	     <td><%=frequence %></td><td><%=startfrequence %></td>
	     <td><%=endfrequence %></td><td><%=businessType %></td><td><%=businessName %></td>
	     <td><%=signalType %></td><td><%=longitude %></td><td><%=latitude %></td><td><%=starttime %></td><td><%=endtime %></td><td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
	     <td><a href="javascript:Videoplay('<%=bh%>','<%=type %>','Video')" >播放</a></td>
	 	 <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','Video')">导出文件</a></td>
	     <td><a href="javascript:modifyVideoData('<%=bh%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','Video')" >删除</a></td>
	     
	     
	     </tr>
	    	 <%}%>
	     </table>
	     <%}%>
  </body>
</html>
