<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="DBConnection.*" %>
<%@ page import="java.sql.SQLException" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>单频占用度计算</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/DataManage/DataQuery.js" ></script>
	<style>
		input[type=button]{
			
			width:60px;
			height:20px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			/*margin-left:30px;*/
		}
    </style>
  </head>
  
  <body>
  <%
  	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8").trim();
    
    
		Double temp=0.0;
		Double max=0.0;//定义初始值
		Double min=1000.0;//定义初始值
		Double aver=0.0;
		int count = 0;
		Double sum = 0.0;
		String sql=null;
		
		if(type.equals("DBDPJCQuery")){
			
			sql = "select levels from emcdpjcdata where bh='" + bh + "' and levels > 0";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select levels from emcudpjcdata where bh='" + bh + "' and levels > 0";
		}else{
			sql = "wrong";
		}%>
		<table id="infoTable" style="display:none">
		<%
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				%>
				<tr><td><%=temp%></td></tr>
				<%
				count++;
				if(max<temp){
					max = temp;
				}
				if(min>temp){
					min = temp;
				}
				sum = sum + temp;
				aver = sum/count;
				
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
		}%>
		</table>
		<%
		//System.out.println("minlevel="+minlevel);
		//System.out.println("有效数据数量count="+count);
		if(count == 0){/*该文件中场强均为负值，文件失效。*/
			min=0.0;
			max=0.0;
			aver=0.0;
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		aver = Double.parseDouble(df.format(aver));
    /*
    ArrayList<Double> al = Handle.DPJCfilter(bh,type,0.0);//将场强小于0的全部过滤掉
    
    DecimalFormat df = new DecimalFormat("#0.00");
    String min = String.valueOf(al.get(0));
    String max = String.valueOf(al.get(1));
    String aver = df.format(al.get(2));
    */
    ArrayList<String> stringList = Handle.DPJCinfo(bh, type);
    String name = stringList.get(0);
    String frequence = stringList.get(1);
  %>
 	
     <div id="content" style="position:relative;width:100%;top:0px;bottom:30px;">
     
       <div style="position:relative;">
		   <table align="center" style="padding:15px 0 10px 10px;">
		     <tr><td nowrap style="text-align: center;">文件：</td><td style="text-align: left;"><%=name %></td></tr>
		     <tr><td nowrap style="text-align: center;">频率：</td><td style="text-align: left;"><%=frequence %> MHz</td></tr>
		   </table>
	   </div>
	   <div style="position:relative;">
		   <font style="padding-left:60px;">单频占用度计算:</font><br>
		   <font style="padding-left:60px;">(场强单位：dBμV/m)</font>
		   <table  style="padding:20px 0 0 60px;">
		   	 <tr><td>场强最小值:</td><td><%=min %></td></tr>
		   	 <tr><td>场强最大值:</td><td><%=max %></td></tr>
		   	 <tr><td>场强平均值:</td><td><%=aver %></td></tr>
			 <tr><td>最小值:</td><td><input type="text" id="minThreshold" style="width:50px;" value="<%=min %>"></td></tr>
			 <tr><td>统计门限:</td><td><input type="text" id="threshold" style="width:50px;" value="<%=aver %>"></td></tr>
			 <tr><td></td><td><input type="button" value="计算" onclick="DPJCOccupancyProcess('<%=max%>')"/></td></tr>
		  </table>
	  </div>
	</div>
	  <div style="position:relative;padding:5px 0 0 60px;height:30px;">
	  	<font id="response"></font>
	  	<input id="save" type="button" value="存储" style="margin-left:90px;display:none;" onclick="save()">
	  </div>
	<script>
		function DPJCOccupancyProcess(max){
		
		var minThreshold=document.getElementById("minThreshold").value;
		var threshold=document.getElementById("threshold").value;
		//alert(minThreshold +"/"+ threshold);
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
		if(max<=0){
			
			alert("该文件中场强均为负值，不可计算占用度。")
			return;
		}
		if(minThreshold > threshold){
			alert("设定的最小值大于阈值。请重新设定。");
			return;
		}
		if(threshold>=max){
			alert("阈值大于最大值。占用度为0%。");
			return;
		}
		//alert(bh + type + minThreshold + threshold+thresholdDefault);
			var inforTable = document.getElementById("infoTable");
			var size = infoTable.rows.length;
			//alert(size);
			//var levels = new Array();
			/*
			for (var i=0;i<size;i++){
				levels.push(parseFloat(infoTable.rows[i].cells[0].innerText));
			}*/
			var useful = 0;
			var sum = 0;
			for(var i=0;i<size;i++){
				if(parseFloat(infoTable.rows[i].cells[0].innerText) >= minThreshold){
					sum++;
					if(parseFloat(infoTable.rows[i].cells[0].innerText) >= threshold){
						useful++;
					}
				}
				
			}
			var Occupancy = useful/sum*100;
			Occupancy = Occupancy.toFixed(2);
			var result = "占用度:" + Occupancy + "%";
			document.getElementById("response").innerHTML = result;
		//document.getElementById("response").innerHTML = xmlhttp.responseText;
	
	}
	function save(){
		var bh="<%=bh%>";
		var type = "<%=type%>";
		var occupancy = document.getElementById("response").innerHTML;
		occupancy = occupancy.substring(occupancy.indexOf(":")+1,occupancy.length-1)
		
		var param = "DataQuery/DPJCOccupancySaveData.jsp?bh=" + bh + "&type=" + type
			 + "&occupancy=" + occupancy;
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
		xmlhttp.send();
		var str = xmlhttp.responseText;
		str = str.replace(/[\r\n]/g, "");	//删除换行符
		alert(str);
	}
	</script>
  </body>
</html>
