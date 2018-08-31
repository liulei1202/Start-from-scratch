<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type = "WXZFQJCQuery";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星监测-转发器监测-数据查询</title>
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
			$("#WXZFQJC").click(); 	
			$("#ZFQJCQuery").click();
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
			
				<h3>卫星监测-转发器监测-数据查询</h3>
				<form id="myForm">	
				
				<font>起始时间</font><input type="text" id="startTime" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<font>截止时间</font><input type="text" id="endTime"  class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<font>起始轨位</font><input type="text" id="startPosition"/>
				<font>截止轨位</font><input type="text" id="endPosition"/><br>
				<font>国家</font><select id="country">
					<option value="" ></option>
				<%
							ArrayList<String> list = new ArrayList<String>();
							String country,frequence,station,author;
							int size;
							list = DataQuery.queryCountry(type);
							size = list.size();
							for(int i=0;i<size;i++){
								country = list.get(i);
				%>
						<option value="<%=country%>" ><%=country%></option>
					<%
						}
					%>
					</select>
				
				<font>频段</font><select id="frequence">
					<option value="" ></option>
				<%
							list = DataQuery.queryFrequence(type);
							size = list.size();
							for(int i=0;i<size;i++){
								frequence = list.get(i);
				%>
						<option value="<%=frequence%>" ><%=frequence%></option>
					<%
						}
					%>
					</select>
				<font>监测台站</font><select id="station">
					<option value="" ></option>
				<%
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
				<font>监测人</font><select id="author">
					<option value="" ></option>
				<%
							list = DataQuery.queryPerson(type);
							size = list.size();
							for(int i=0;i<size;i++){
								author = list.get(i);
				%>
						<option value="<%=author%>" ><%=author%></option>
					<% } %>
					</select>
				<input type="button"  onclick ="formReset()" value="重置"/>
			    <input type="button"  onclick="queryZFQJC()" value="查询"/>
			    <input type="button"  onclick="downloadZFQJC()" value="批量导出" id="batchDownload"/>
			    </form>
				<div id="response">
				</div>
					
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>
  </body>
</html>
