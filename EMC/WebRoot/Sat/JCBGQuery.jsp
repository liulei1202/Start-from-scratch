<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type = "WXJCBGQuery";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星监测-监测报告-数据查询</title>
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
	<script>
		function navigation(){
		
			$("#WX").click();
			$("#WXJCBG").click(); 	
			$("#WXJCBGQuery").click();
		}
	</script>
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
			
				<h3>卫星监测-监测报告-数据查询</h3>
				<form id="myForm">	
				<font>报告类型</font><select id="reportType">
					<option value="" ></option>
					<option value="专项监测报告" >专项监测报告</option>
					<option value="卫星干扰定位报告" >卫星干扰定位报告</option>
					<option value="卫星普查监测报告" >卫星普查监测报告</option>
					<option value="其他报告" >其他报告</option>
				</select>
				<font>起始时间</font><input type="text" id="startTime" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<font>结束时间</font><input type="text" id="endTime" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<!-- <font>起始频率</font><input type="text" id="startFrequence" class="frequence"  placeholder="单位：MHz">
				<font>终止频率</font><input type="text" id="endFrequence" class="frequence"  placeholder="单位：MHz"> -->
				<font>任务关键词</font><input type="text" id="keyWords" class="keywords">
				<br><font>监测台站</font><select id="station">
					<option value="" ></option>
				<%
					ArrayList<String> list = new ArrayList<String>();
							String station,monitorlocation,author;
							int size;
							
							list = DataQuery.queryStation(type);
							size = list.size();
							for(int i=0;i<size;i++){
								station = list.get(i);
				%>
						<option value="<%=station%>" ><%=station%></option>
					<%
						}
					%>
					</select>
				<font>监测地点</font><select id="monitorlocation">
					<option value="" ></option>
				<%
					list = DataQuery.querymonitorlocation(type);
							size = list.size();
							for(int i=0;i<size;i++){
								monitorlocation = list.get(i);
				%>
						<option value="<%=monitorlocation%>" ><%=monitorlocation%></option>
					<% } %>
					</select>
				<font>监测人</font><select id="author">
					<option value="" ></option>
				<%
					list = DataQuery.queryauthor(type);
							size = list.size();
							for(int i=0;i<size;i++){
								author = list.get(i);
				%>
						<option value="<%=author%>" ><%=author%></option>
					<% } %>
					</select>
				
				<input type="button"  onclick ="formReset()" value="重置"/>
			    <input type="button"  onclick="queryJCBG_Sat()" value="查询"/>
			    </form>
				<div id="response">
				</div>
					
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>

  </body>
</html>
