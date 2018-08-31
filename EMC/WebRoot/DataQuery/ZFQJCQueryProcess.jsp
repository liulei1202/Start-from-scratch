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
    
    <title>转发器监测数据查询</title>
    
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
       	String startPosition = java.net.URLDecoder.decode(request.getParameter("startPosition"),"UTF-8");
       	String endPosition = java.net.URLDecoder.decode(request.getParameter("endPosition"),"UTF-8");
       	
       	String country = java.net.URLDecoder.decode(request.getParameter("country"),"UTF-8");
       	String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
       	System.out.println("频段："+frequence);
       	String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
       	String author = java.net.URLDecoder.decode(request.getParameter("author"),"UTF-8");
       	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
       	
        
       	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
       	list = DataQuery.ZFQJCQuery(startTime,endTime,startPosition,endPosition,country,frequence,station,author,type);
   		
       	 %>
	     <table id="infoTable"  border="1" cellspacing="0" >
	     <tr><th style="min-width:20px;"></th><th><input type="checkbox" id="allcheckbox" onclick="checkbox()"/></th><th>任务类型</th><th>任务名称</th><th>监测时间</th><th>轨位（°E）</th><th>卫星名称</th><th>国家</th><th>转发器</th><th>起始频率（MHz）</th><th>截止频率（MHz）</th><th>频段</th><th>极化方式</th><th>天线编号</th><th>rbw（kHz）</th><th>vbw（kHz）</th><th>衰减(dB)</th><th>参考电平(dBm)</th><th>监测台站</th><th>监测人</th><th>备注</th><th>频谱图</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     		DecimalFormat df = new DecimalFormat("#.0000");
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     	%>
	     <tr><td><%=i+1 %></td><td><input type="checkbox"/><input type="hidden" value="<%=aL.get(20).trim() %>"></td>
	     <td><%=aL.get(0) %></td><td><%=aL.get(1) %></td><td><%=aL.get(2) %></td><td><%=aL.get(3) %></td><td><%=aL.get(4) %></td>
	     <td><%=aL.get(5) %></td><td><%=aL.get(6) %></td><td><%=aL.get(7) %></td><td><%=aL.get(8) %></td><td><%=aL.get(9) %></td>
	     <td><%=aL.get(10) %></td><td><%=aL.get(11) %></td><td><%=aL.get(12) %></td><td><%=aL.get(13) %></td><td><%=aL.get(14) %></td>
	     <td><%=aL.get(15) %></td><td><%=aL.get(16) %></td><td><%=aL.get(17) %></td><td><div class="wrap" title='<%=aL.get(18)%>'><%=aL.get(18)%></div></td>
	     
	     <%if(aL.get(19).trim().equals("0")){%>
	     	<td>无</td>
	     <%}else{%>
	     	<td><a href="javascript:getRGB('<%=aL.get(20)%>')" >查看</a></td>
	     <%}%>
	     <%if(aL.get(19).trim().equals("0")){%>
	     	<td>无</td>
	     <%}else{%>
	     	<td><a href="javascript:exportFile('<%=aL.get(20)%>','<%=type%>','')">导出文件</a></td>
	     <%}%>
	     <td><a href="javascript:modifyData('<%=aL.get(20)%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=aL.get(20)%>','<%=type %>','')" >删除</a></td>
         
	     </tr>
	    	 <%}%>
	     </table>
	     
  </body>
</html>
