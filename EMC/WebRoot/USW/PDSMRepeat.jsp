<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type = "CDBPDSMQuery";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>超短波-频段扫描-数据查重</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ page import="DataQuery.*" %>
	<link rel="StyleSheet" type="text/css" href="js/css/manager.css" />
	<link rel="StyleSheet" type="text/css" href="js/css/tab.css" />
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="js/css/Query.css">
	<script>
		function navigation(){
		
			$("#CDB").click();
			$("#CDBPDSM").click(); 	//只触发点击，不触发页面跳转
			$("#CDBPDSMRepeat").click();
		}
	</script>
 	<style>
	i.fa-bar-chart{color:blue;
	padding-left:10px;}
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
				if(No.equals("6")){
				%>
					<%@include file="/uswMenu.jsp" %>
				<%} %>
			<script>navigation();</script>
			</div>
			
			<div id="content">
			<input type="hidden" id="type" value="<%=type %>">
			
			<h3>超短波-频段扫描-数据查重</h3>
				<form id="myForm">
				<font>起始时间</font><input type="text" id="startTime"  class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    <font>结束时间</font><input type="text" id="endTime"  class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    <font>起始频率</font><input type="text" class="frequence" id="startFrequence" placeholder="单位：MHz">
			    <font>终止频率</font><input type="text" class="frequence" id="endFrequence" placeholder="单位：MHz"> 
			  	<font>监测台站</font><select id="station">
					<option value="" >
				<%
					ArrayList<String> list = new ArrayList<String>();
							String station,monitorlocation;
							int size;
							
							list = DataQuery.queryStation(type);
							size = list.size();
							for(int i=0;i<size;i++){
								station = list.get(i);
				%>
						<option value="<%=station%>" ><%=station%>
					<%
						}
					%>
					</select>
				<font>监测地点</font><select id="monitorlocation">
					<option value="" >
				<%
					list = DataQuery.querymonitorlocation(type);
							size = list.size();
							for(int i=0;i<size;i++){
								monitorlocation = list.get(i);
				%>
						<option value="<%=monitorlocation %>" ><%=monitorlocation %>
					<% } %>
					</select>	
					<p style="margin-top:-10px;"></p><!-- 换行效果 -->
				<input type="hidden" id="parameter" value="false"/>
				<i class="fa fa-bar-chart"></i><input type="checkbox" id="original" checked="checked"/><font>原始监测数据</font>
			    <i class="fa fa-bar-chart"></i><input type="checkbox" id="graphic" checked="checked"/><font>图形数据</font>
				<input type="hidden" id="audio" value="false"/>
			    <i class="fa fa-bar-chart"></i><input type="checkbox" id="video" checked="checked"/><font>视频数据</font>
				<input type="button"  onclick ="formReset()" value="重置"/>
				<input type="button" onclick="query()" value="查询"/>
				<input type="button" onclick="repeat()" value="查重"/>
				</form>
				<div id="response">
			</div>
			
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>

  </body>
</html>
