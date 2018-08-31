<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type = "WXZFQJCQuery";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星监测-转发器监测-数据分析</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="StyleSheet" type="text/css" href="/rsui/js/css/manager.css" />
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="js/css/Query.css">
	<script>
		function navigation(){
		
			$("#WX").click();
			$("#WXZFQJC").click(); 	
			$("#ZFQJCAnalysis").click();
		}
	</script>
	<style>
		#analysis{
	
		position:absolute;
		top:50px;
		bottom:0px;
		width:100%;
		overflow:auto;
	}
	</style>
  </head>
  <body>
	<div id="all">
		<div id="hr"></div>
		<%@include file="/Banner.jsp" %>
		<div id="middle">	
			<div id="menu">
				<%
				String No = request.getParameter("No");
				if(No.equals("2")){
				%>
					<%@include file="/dataManMenu.jsp" %>
				<%}
				if(No.equals("3")){
				%>
					<%@include file="/dataReqMenu.jsp" %>
				<%}
				if(No.equals("4")){
				%>
					<%@include file="/sysManMenu.jsp" %>
				<%}
				if(No.equals("7")){
				%>
					<%@include file="/satMenu.jsp" %>
				<%} %>
			<script>navigation();</script>
			</div>
			
			<div id="content">
				<input type="hidden" id="type" value="<%=type%>">
				<h3>卫星监测-转发器监测-数据分析</h3>
				<div id="analysis">
				<%
				ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		       	list = DataQuery.ZFQJCQuery("","","","","","","","",type);
		   		
		       	 %>
		<table  border="1" cellspacing="0" >
	     <tr><th style="min-width:20px;"></th><th>任务类型</th><th>任务名称</th><th>监测时间</th><th>轨位（°E）</th><th>卫星名称</th><th>国家</th><th>转发器</th><th>起始频率（MHz）</th><th>截止频率（MHz）</th><th>频段</th><th>极化方式</th><th>天线编号</th><th>rbw（kHz）</th><th>vbw（kHz）</th><th>衰减(dB)</th><th>参考电平(dBm)</th><th>监测台站</th><th>监测人</th><th>备注</th><th>频谱图</th><th>导出文件</th><th>修改</th><th>删除</th>
	     </tr>
	     	<% 
	     		DecimalFormat df = new DecimalFormat("#.0000");
	     		int size = list.size();
	     		int i = 0;
	     		ArrayList<String> aL=new ArrayList<String>();
	     		for (i=0;i<size;i++){
	     			aL=list.get(i);
	     	%>
	     <tr><td><%=i+1 %></td>
	     <td><%=aL.get(0) %></td><td><%=aL.get(1) %></td><td><%=aL.get(2) %></td><td><%=aL.get(3) %></td><td><%=aL.get(4) %></td>
	     <td><%=aL.get(5) %></td><td><%=aL.get(6) %></td><td><%=aL.get(7) %></td><td><%=aL.get(8) %></td><td><%=aL.get(9) %></td>
	     <td><%=aL.get(10) %></td><td><%=aL.get(11) %></td><td><%=aL.get(12) %></td><td><%=aL.get(13) %></td><td><%=aL.get(14) %></td>
	     <td><%=aL.get(15) %></td><td><%=aL.get(16) %></td><td><%=aL.get(17) %></td><td><%=aL.get(18) %></td>
	     <%if(aL.get(19).trim().equals("0")){%>
	     	<td>无</td>
	     <%}else{%>
	     	<td><a href="javascript:Graphic('<%=aL.get(20)%>','<%=type %>','')" >查看</a></td>
	     <%}%>
	     <td><a href="javascript:exportFile('<%=aL.get(20)%>','<%=type%>','')">导出文件</a></td>
	     <td><a href="javascript:modifyData('<%=aL.get(20)%>','<%=type %>')" >修改</a></td>
	     <td><a href="javascript:deleteData('<%=aL.get(20)%>','<%=type %>','')" >删除</a></td>
         
	     </tr>
	    	 <%}%>
	     </table>
				 </div>
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>

  </body>
</html>
