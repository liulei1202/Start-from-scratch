<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.text.DecimalFormat" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>联合定位占用度计算</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/DataManage/DataQuery.js" ></script>
  </head>
  
  <body>
  <%
  	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
    ArrayList<Double> al = Handle.filterLHDW(bh,type,0.0);//将场强小于0的全部过滤掉
    
    DecimalFormat df = new DecimalFormat("#0.00");
    String min = String.valueOf(al.get(0));
    String max = String.valueOf(al.get(1));
    String aver = df.format(al.get(2));
  %>
 	
     <div id="content" style="margin-top:10px;">
	   <table>
	   	 <tr><td>场强最小值</td><td><%=min %></td></tr>
	   	 <tr><td>场强最大值</td><td><%=max %></td></tr>
	   	 <tr><td>场强平均值</td><td><%=aver %></td></tr>
		 <tr><td>最小值:</td><td><input type="text" id="minThreshold" value="<%=min %>"></td></tr>
		 <tr><td>阈值:</td><td><input type="text" id="threshold" value="<%=aver %>"></td></tr>
		 <tr><td></td><td><input type="button" value="计算" onclick="LHDWOccupancyProcess('<%=bh%>','<%=type%>')"/></td></tr>
	  </table>
	  <div id="response"></div>
	</div>
  </body>
</html>
