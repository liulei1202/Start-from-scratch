<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="DBConnection.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.sql.SQLException" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>频段占用度计算</title>
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
    
    String sql = null;
		String table = null;
		if(type.equals("DBPDSMQuery")){
			table = "emcpdsmdata";
		}else if(type.equals("CDBPDSMQuery")){
			table = "emcupdsmdata";
		}else{
			table="wrong";
		}
		sql = "select distinct frequence from "+table+" where bh='" + bh + "' order by frequence";
		ArrayList<Double> frequences = new ArrayList<Double>();//frequence保存所有频点
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				frequences.add(emcdb.dynaRs.getDouble("frequence"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		emcdb.closeRs();
		//emcdb.closeStm();
		int sizes = frequences.size();
		System.out.println("共有频点："+sizes);
		%>
		<table id="infoTable" style="display:none">
		<%
		//levels存储每个频点下的最大场强值
		ArrayList<Double> levels = new ArrayList<Double>();
		for(int i=0;i<sizes;i++){
			sql = "select max(levels) as num from "+table+" where bh='"+bh+"' and frequence="+frequences.get(i);
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				if(emcdb.dynaRs.next()){%>
					<tr><td><%=emcdb.dynaRs.getDouble("num")%></td></tr>
					<%
					//System.out.println(emcdb.dynaRs.getDouble("num"));
					levels.add(emcdb.dynaRs.getDouble("num"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			emcdb.closeRs();
		}
		%>
		</table>
		<%
		emcdb.closeStm();
		//计算levels中的最大值，最小值，平均值
		Double max=levels.get(0);
		Double min=levels.get(0);
		Double aver=0.0;
		Double sum = 0.0;
		for(int j =0;j<sizes;j++){
			//al.add(levels.get(j));
			if(levels.get(j)>max)
				max = levels.get(j);
			if(levels.get(j)<min)
				min = levels.get(j);
			sum = sum + levels.get(j);
		}
		aver = sum/sizes;
		DecimalFormat df = new DecimalFormat("#0.00");
		aver = Double.parseDouble(df.format(aver));
    /*
    ArrayList<Double> al = Handle.PDSMfilter(bh,type,0.0);
    
    DecimalFormat df = new DecimalFormat("#0.00");
    String min = String.valueOf(al.get(0));
    String max = String.valueOf(al.get(1));
    String aver = df.format(al.get(2));
    */
    ArrayList<String> stringList = Handle.PDSMinfo(bh, type);
    String name = stringList.get(0);
    String freq = stringList.get(1);
  %>
 	 <input type="hidden" id="bh" value="<%=bh %>">
 	 <input type="hidden" id="type" value="<%=type %>">
     <div id="content" style="position:relative;width:100%;top:0px;">
     	<div style="position:relative;">
		   <table align="center" style="padding:5px 0 15px;">
		     <tr><td nowrap style="text-align: center;">文件：</td><td style="text-align: left;"><%=name %></td></tr>
		     <tr><td nowrap style="text-align: center;">频率：</td><td style="text-align: left;"><%=freq %> MHz</td></tr>
		   </table>
	   </div>
	   <div style="position:relative;">
	     <font style="padding-left:60px;">频段占用度计算</font><br>
	     <font style="padding-left:60px;">(场强单位：dBμV/m)</font>
		   <table style="padding:10px 0 0 60px;">
		   	 <tr><td>场强最小值：</td><td><%=min %></td></tr>
		   	 <tr><td>场强最大值：</td><td><%=max %></td></tr>
		   	 <tr><td>场强平均值：</td><td><%=aver %></td></tr>
			 <tr><td>统计门限:</td><td><input type="text" id="threshold" style="width:50px;" value="<%=aver %>"></td></tr>
			 <tr><td></td><td><input type="button" style="border-radius:5px;height:30px;width:50px;" value="计算" onclick="PDSMOccupancyProcess()"/></td></tr>
		  </table>
		  <div id="response" style="position:relative;padding:10px 0 0 60px;height:30px;"></div>
	   </div>
	   <hr>
	   <div style="position:relative;">
		  <font style="padding-left:60px;">单频占用度计算</font><br>
		  <font style="padding-left:60px;">(场强单位：dBμV/m)</font><br><br>
		  <font style="padding-left:60px;">频率（MHz）：</font><select id="frequence">
		  <option value="" ></option>
		  <%
		  	ArrayList<Double> frequence = Handle.PDSMFrequence(bh, type);
	 
			int size;
			size = frequence.size();
			for(int i=0;i<size;i++){%>
				<option value="<%=frequence.get(i)%>" ><%=frequence.get(i)/1000000%></option>
			<%}%>
		  </select>
		  <!-- <input type="button" value="确定" onclick="PDSMfrequenceOccupancy()"> -->
		  
		  <table style="padding:0 0 0 60px;">
		   	 <tr><td>场强最小值：</td><td id="freqMin"></td></tr>
		   	 <tr><td>场强最大值：</td><td  id="freqMax"></td></tr>
		   	 <tr><td>场强平均值：</td><td  id="freqAver"></td></tr>
		   	 <tr><td>最小值:</td><td><input type="text" id="freqMinThreshold" style="width:50px;" value=""></td></tr>
			 <tr><td>统计门限:</td><td><input type="text" id="freqThreshold" style="width:50px;" value=""></td></tr>
			 <tr><td></td><td><input type="button" style="border-radius:5px;height:30px;width:50px;" value="计算" onclick="PDSMFrequenceOccupancyProcess('<%=bh%>','<%=type%>')" /></td></tr>
		  </table>
		  <div id="response1" style="position:relative;padding:10px 0 0 60px;height:30px;"></div>
		  
	   </div>
	</div>
		<script>
		function PDSMOccupancyProcess(){
			var max = <%=max%>;
			if(max<=0.0){
				alert("该文件中场强均为负值，不可计算占用度。");
				return;
			}
			//var bh = document.getElementById("bh").value;
			//var type = document.getElementById("type").value;
			//alert("pdsmoccupancyprocess()");
			var threshold=document.getElementById("threshold").value;
			if(threshold==""){
				alert("阈值不能为空。");
				return;
			}
			
			threshold = parseFloat(threshold);
			if(threshold>=max){
				alert("阈值大于最大值。占用度为0%。");
				return;
			}
			var inforTable = document.getElementById("infoTable");
			var size = infoTable.rows.length;
			//alert(size);
			var levels = new Array();
			for (var i=0;i<size;i++){
				levels.push(parseFloat(infoTable.rows[i].cells[0].innerText));
			}
			var useful = 0;
			for(var i=0;i<size;i++){
				if(levels[i] >= threshold){
					useful++;
				}
			}
			var Occupancy = useful/size*100;
			Occupancy = Occupancy.toFixed(2);
			var result = "占用度：" + Occupancy + "%";
			document.getElementById("response").innerHTML = result;
			
			/*
			param = "/rsui/DataQuery/PDSMOccupancyProcess.jsp?threshold=" + threshold;
			param = param + "&bh="+bh+"&type="+type; 
			//alert(param);
			param = encodeURI(param);
			param = encodeURI(param);
			
			var xmlhttp;
			if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else  {// code for IE6, IE5
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.open("GET",param,false);
			//xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			xmlhttp.send(param);
			*/
			
		
		}
		
		document.getElementById("frequence").onchange = function(){
			
			//alert(document.getElementById("frequence").value);
			if(document.getElementById("frequence").value !=""){
				PDSMfrequenceOccupancy();
			}
		};
		
		
		function PDSMfrequenceOccupancy(){
			
			var bh = document.getElementById("bh").value;
			var type = document.getElementById("type").value;		
			var frequence = document.getElementById("frequence").value;	
			var param = "/rsui/Occupancy/PDSMFrequenceOccupancy.jsp?bh=" + bh + "&type=" + type + "&frequence=" + frequence;
			param = encodeURI(param);
			param = encodeURI(param);
			var xmlhttp;
			if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else  {// code for IE6, IE5
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.open("GET",param,false);
			xmlhttp.send();
			var str = xmlhttp.responseText;
			str = str.replace(/[\r\n]/g, "");//删除换行符
			str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
			//alert("0"+str+"0");
			var value = new Array();
			value = str.split(",");
			//alert("0"+value+"0");
			document.getElementById("freqMin").innerHTML = value[0];
			document.getElementById("freqMax").innerHTML = value[1];
			document.getElementById("freqAver").innerHTML = value[2];
			document.getElementById("freqMinThreshold").value = value[0];
			document.getElementById("freqThreshold").value = value[2];
			
			//document.getElementById("response").innerHTML = xmlhttp.responseText;
		}
		
	
		function PDSMFrequenceOccupancyProcess(bh,type){
			
			var max = document.getElementById("freqMax").innerHTML;
			if(max<=0){
		
				alert("该频率场强均为负值，不可计算占用度。");
				return;
			}
			
			
			var frequence = document.getElementById("frequence").value;	
			var minThreshold=document.getElementById("freqMinThreshold").value;
			var threshold=document.getElementById("freqThreshold").value;
			if(minThreshold==""){
				alert("最小值不能为空。");
				return;
			}
			if(threshold==""){
				alert("阈值不能为空。");
				return;
			}
			minThreshold = parseFloat(minThreshold);
			threshold = parseFloat(threshold);
			if(minThreshold > threshold){
			alert("设定的最小值大于阈值。请重新设定。");
			return;
			}
			if(threshold>=max){
				alert("阈值大于最大值。占用度为0%。");
				return;
			}
			//alert(bh + type + minThreshold + threshold+thresholdDefault);
			
			var param = "Occupancy/PDSMFrequenceOccupancyProcess.jsp?bh=" + bh + "&type=" + type + "&frequence=" 
			+ frequence + "&minThreshold=" + minThreshold+ "&threshold=" + threshold;
			param = encodeURI(param);
			param = encodeURI(param);
			
			var xmlhttp;
			if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else  {// code for IE6, IE5
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.open("GET",param,false);
			xmlhttp.send();
			document.getElementById("response1").innerHTML = xmlhttp.responseText;
	}
	</script>
  </body>
</html>
