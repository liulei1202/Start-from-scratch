<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type = "DBJCBGQuery";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>短波-监测报告-数据分析</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<link rel="StyleSheet" type="text/css" href="/rsui/js/css/manager.css" />
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="js/css/Query.css">
	<link rel="StyleSheet" type="text/css" href="js/css/tab.css" />

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
				if(No.equals("5")){
				%>
					<%@include file="/swMenu.jsp" %>
				<%} %>
				<script>
			      $("#DB").click();
			      $("#DBJCBG").click(); 	
			      $("#DBJCBGAnalysis").click();
            	</script>
			</div>
			<div id="content">
				
			  	<h3>短波-监测报告-数据分析</h3>
				<input type="hidden" id="type" value="<%=type%>">
				<form id="myForm">
				<font>起始时间</font><input type="text" id="startTime" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<font>结束时间</font><input type="text" id="endTime" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<input type="button"  onclick ="formReset()" value="重置"/>
			    <input type="button"  onclick="analysisJCBG()" value="查询"/>
			    </form>
				<div id="response">
				</div>
				
				
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>
  </body>
</html>
