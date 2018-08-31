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
    
    <title>单频监测数据查询</title>
    
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
       	//System.out.println(startTime+endTime+startFrequence+endFrequence+station+monitorLocation+type);
       	DecimalFormat df = new DecimalFormat("#0.000");
	    DecimalFormat df4 = new DecimalFormat("#0.0000");
       	 /*是否显示信号参数登记数据*/
	    if(parameter.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.ParameterQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
   		 
	     <table  border="1" cellspacing="0" id="infoTable">
	     <caption>信号参数登记</caption>
	     <tr><th style="min-width:20px;"></th><th><input type="checkbox" id="allcheckbox" onclick="checkbox()"/></th>
	     <th>频率(MHz)</th><th>监测台站</th><th>占用带宽(kHz)</th><th>调制方式</th><th>业务类型</th><th>业务名称</th>
		 <th>信号类型</th><th>发射时段</th><th>场强最大值</th><th>测试次数</th><th>背景噪声</th><th>占用度</th>
		 <th>统计门限</th><th>示向度/台站名称/测向质量</th><th>经度</th><th>纬度</th><th>城市</th><th>以往对比</th>
	     <th>监测时间</th><th>监测人</th><th>监测地</th><th>备注</th>
		 <th>原始文件</th><th>中频图</th><th>示向图</th><th>定位图</th>
	     <th>修改</th><th>删除</th>
	     
	     </tr>
	     	<% 
	     		
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     			//System.out.println(list.get(i).size());
	     	%>
	     <tr><td><%=i+1 %></td><td><input type="checkbox"/><input type="hidden" value="<%=aL.get(22).trim() %>"></td>
	     <td><%=aL.get(0)%></td><td><%=aL.get(1)%></td><td><%=aL.get(2) %></td><td><%=aL.get(3) %></td><td><%=aL.get(4) %></td><td><%=aL.get(5) %></td>
	     <td><%=aL.get(6)%></td><td><div class="wrap" title='<%=aL.get(7)%>'><%=aL.get(7)%></div></td><td><%=aL.get(8) %></td><td><%=aL.get(9) %></td><td><%=aL.get(10) %></td><td><%=aL.get(11) %></td>
	     <td><%=aL.get(12)%></td><td><%=aL.get(13)%></td><td><%=aL.get(14) %></td><td><%=aL.get(15) %></td><td><%=aL.get(16) %></td><td><%=aL.get(17) %></td>
	     <td><%=aL.get(18)%></td><td><%=aL.get(19)%></td><td><%=aL.get(20) %></td><td><div class="wrap" title='<%=aL.get(21)%>'><%=aL.get(21)%></div></td>
	     <%
	     	if(aL.get(23).trim().equals("1")){
	      %>
	     <td><a href="javascript:dpjcPlay('<%=aL.get(22)%>','<%=type %>','100')" >播放</a></td>
	     <%}else{ %>
	     <td>无</td>
	     <%} %>
	     <%
	     	if(aL.get(24).trim().equals("1")){
	      %>
	     <td><a href="javascript:Graphic('<%=aL.get(22)%>','<%=type %>','中频图')" >查看</a></td>
	     <%}else{ %>
	     <td>无</td>
	     <%} %>
	     <%
	     	if(aL.get(25).trim().equals("1")){
	      %>
	     <td><a href="javascript:Graphic('<%=aL.get(22)%>','<%=type %>','示向图')" >查看</a></td>
	     <%}else{ %>
	     <td>无</td>
	     <%} %>
	     <%
	     	if(aL.get(26).trim().equals("1")){
	      %>
	     <td><a href="javascript:Graphic('<%=aL.get(22)%>','<%=type %>','定位图')" >查看</a></td>
	     <%}else{ %>
	     <td>无</td>
	     <%} %>
	     <td><a href="javascript:modifyData('<%=aL.get(22)%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=aL.get(22)%>','<%=type %>','Parameter')" >删除</a></td>
	     
	     </tr>
	     
	    	 <%}%>
	     </table>
	     
	     <%}
       	/*是否显示原始监测数据*/
       	if(original.equals("true")){
       	LinkedList<DPJCHead> list = new LinkedList<DPJCHead>();
       	list = DataQuery.DPJCQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>原始监测数据</caption>
	     <tr><th style="min-width:20px;"></th><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>经度</th><th>纬度</th>
	     <th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>播放</th><th>计算占用度</th><th>发射时段</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     		int size = list.size();
	     		int i = 0;
	     		DPJCHead aL=new DPJCHead();
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
	     			String RadioFreattenuation = aL.RadioFreattenuation;
	     			String midFreattenuation = aL.MidFreattenuation;
	     			String DeModen = aL.DeModen;
	     			String Detector = aL.Detector;
	     			String Station = aL.Station;
	     			String Monitor = aL.Monitor;
	     			String Monitorlocation = aL.MonitorLocation;
	     			String bh = aL.BH.trim();
	     			String businessName  = aL.businessName;
	     			String businessType  = aL.businessType;
	     			%>
	     <tr><td><%=i+1 %></td>
	     <td><%=frequence %></td><td><%=businessType %></td><td><%=businessName %></td><td><%=longitude %></td><td><%=latitude %></td>
	     <td><%=starttime %></td><td><%=endtime %></td><td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
	     <td><a href="javascript:dpjcPlay('<%=bh%>','<%=type %>','100')">播放</a></td>
	     <td><a href="javascript:dpjcOccupancy('<%=bh%>','<%=type %>')">计算</a></td>
	     <td><a href="javascript:launchTime('<%=bh%>','<%=type%>')">发射时段</a></td>
	     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','')">导出文件</a></td>
	     <td><a href="javascript:modifyOrigianlData('<%=bh%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','')" >删除</a></td>
	     </tr>
	    	 <%}%>
	     </table>
	     <%} 
	   
	    /*是否显示图形数据*/
	    if(graphic.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.GraphicQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>图形数据</caption>
	     <tr><th style="min-width:20px;"></th><th>图形类型</th><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>信号类型</th><th>经度</th><th>纬度</th><th>监测时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>查看</th><th>导出文件</th><th>修改</th><th>删除</th>
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
	     			//endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
	     			String frequence = aL.get(3).trim();
	     			frequence = df4.format(Double.parseDouble(frequence)/1000000);
	     			String Station = aL.get(4);
	     			String Monitor = aL.get(5);
	     			String Monitorlocation = aL.get(6);
	     			String bh = aL.get(7).trim();
	     			String businessType = aL.get(8);
					String businessName = aL.get(9);
					String signalType = aL.get(10);
					String longitude = aL.get(11);
					String latitude = aL.get(12);
	     			
	     			%>
	     <tr><td><%=i+1 %></td>
	     <td><%=GraphicType %></td><td><%=frequence %></td><td><%=businessType %></td><td><%=businessName %></td><td><%=signalType %></td>
		 <td><%=longitude %></td><td><%=latitude %></td><td><%=starttime %></td><td><%=Station %></td>
	     <td><%=Monitor %></td><td><%=Monitorlocation %></td>
	     <td><a href="javascript:Graphic('<%=bh%>','<%=type %>','<%=GraphicType%>')" >查看</a></td>
	     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','<%=GraphicType%>')">导出文件</a></td>
	     <td><a href="javascript:modifyGraphicData('<%=bh%>','<%=type %>','<%=GraphicType%>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','<%=GraphicType%>')" >删除</a></td>
	     </tr>
	    	 <%}%>
	     </table>
	     <%}
	     /*是否显示音频数据*/
	    if(audio.equals("true")){
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.AudioQuery(startTime,endTime,startFrequence,endFrequence,station,monitorLocation,type);
   		 %>
	     <table  border="1" cellspacing="0" >
	     <caption>音频数据</caption>
	     <tr><th style="min-width:20px;"></th><th>频率（MHz）</th><th>任务类型</th><th>任务名称</th><th>信号类型</th><th>经度</th><th>纬度</th><th>起始时间</th><th>终止时间</th><th>监测台站</th><th>操作人</th><th>监测地点</th><th>播放</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     		
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     			String starttime = aL.get(0);
	     			//starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
	     			String endtime = aL.get(1);
	     			//endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
	     			String frequence = aL.get(2).trim();
	     			frequence = df4.format(Double.parseDouble(frequence)/1000000);
	     			String Station = aL.get(3);
	     			String Monitor = aL.get(4);
	     			String Monitorlocation = aL.get(5);
	     			String bh = aL.get(6).trim();
	     			String businessType = aL.get(7);
					String businessName = aL.get(8);
					String signalType = aL.get(9);
					String longitude = aL.get(10);
					String latitude = aL.get(11);
	     			%>
	     <tr><td><%=i+1 %></td><td><%=frequence %></td><td><%=businessType %></td><td><%=businessName %></td>
	     <td><%=signalType %></td><td><%=longitude %></td><td><%=latitude %></td>
	     <td><%=starttime %></td><td><%=endtime %></td><td><%=Station%></td>
	     <td><%=Monitor %></td><td><%=Monitorlocation %></td>
	     <td><a href="javascript:Audioplay('<%=bh%>','<%=type %>','Audio')" >播放</a>
	     <td><a href="javascript:exportFile('<%=bh%>','<%=type%>','Audio')">导出文件</a></td>
	     <td><a href="javascript:modifyAudioData('<%=bh%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=bh%>','<%=type %>','Audio')" >删除</a></td>
	     
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
	     			//starttime = starttime.contains(".") ? starttime.substring(0,starttime.indexOf(".")) : starttime;
	     			String endtime = aL.get(1);
	     			//endtime = endtime.contains(".") ? endtime.substring(0,endtime.indexOf(".")) : endtime;
	     			String frequence = aL.get(2).trim();
	     			frequence = df4.format(Double.parseDouble(frequence)/1000000);
	     			String startfrequence = aL.get(3).trim();
	     			startfrequence = df4.format(Double.parseDouble(startfrequence)/1000000);
	     			String endfrequence = aL.get(4).trim();
	     			endfrequence = df4.format(Double.parseDouble(endfrequence)/1000000);
	     			String Station = aL.get(5);
	     			String Monitor = aL.get(6);
	     			String Monitorlocation = aL.get(7);
	     			String bh = aL.get(8).trim();
	     			String businessType = aL.get(9);
					String businessName = aL.get(10);
					String signalType = aL.get(11);
					String longitude = aL.get(12);
					String latitude = aL.get(13);
	     			%>
	     <tr><td><%=i+1 %></td>
	     <td><%=frequence %></td><td><%=startfrequence %></td><td><%=endfrequence %></td><td><%=businessType %></td><td><%=businessName %></td>
	     <td><%=signalType %></td><td><%=longitude %></td><td><%=latitude %></td>
	     <td><%=starttime %></td><td><%=endtime %></td><td><%=Station %></td><td><%=Monitor %></td><td><%=Monitorlocation %></td>
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