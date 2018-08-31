<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="DBConnection.*" %>
<%@ page import="net.sf.json.JSONArray" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>频段扫描数据播放</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js" ></script>
	<script type="text/javascript" src="js/DataManage/highcharts.js"></script>
    <script type="text/javascript" src="js/DataManage/exporting.js"></script>
    <link type="text/css" rel="stylesheet" href="js/css/slider.css">
	<style>
    	input[type=button]{
    		width:80px;
    		height:30px;
    		border-radius:10px;
    	}
    	input[id="import"]{
			float:right;
			width:90px;
			height:25px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			/*margin-left:30px;*/
		}
		input#choose{
			
			width:70px;
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
  	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
	int speed = Integer.parseInt(java.net.URLDecoder.decode(request.getParameter("speed"),"UTF-8"));
	%>
	<input type="hidden" id="bh" value="<%=bh %>">
    <input type="hidden" id="type" value="<%=type %>">
	  <div style="margin:10px 50px 0 0;float:right;">
	  <font>播放速度：</font><select id="speed">
	  		<option value="<%=speed %>" >x<%=speed %></option>
	  		<option value="100" >x100</option>
	  		<option value="10" >x10</option>
	  		<option value="1" >x1</option>
	  </select>
	  <input type="button" id="choose" value="确定" onclick="speed()">
	  </div>
	<%
	String sql = null;
	String sql2 = null;
	String sql3 = null;
	if(type.equals("DBPDSMQuery")){
		sql = "select time,levels from  emcPDSMdata  where bh='" + bh + "' order by to_timestamp(time,'yyyy-MM-dd HH24:MI:ss.ff'), frequence";
		sql2 = "select starttime,endtime from emcPDSM where bh='" + bh + "'";
		sql3 = "select distinct frequence from emcPDSMdata where bh='" + bh + "' order by frequence";
	}else if(type.equals("CDBPDSMQuery")){
		sql = "select time,levels from  emcuPDSMdata  where bh='" + bh + "' order by to_timestamp(time,'yyyy-MM-dd HH24:MI:ss.ff'), frequence";
		sql2 = "select starttime,endtime from emcuPDSM where bh='" + bh + "'";
		sql3 = "select distinct frequence from emcuPDSMdata where bh='" + bh + "' order by frequence";
	}
	
	String temp = null;
	EMCDB emcdb = new EMCDB();
	emcdb.dynaStm = emcdb.newStatement();
	//获取监测所有频点
	ArrayList<Double> frequence = new ArrayList<Double>();
	emcdb.dynaRs = emcdb.QuerySQL(sql3);
	while(emcdb.dynaRs.next()){
		frequence.add(emcdb.dynaRs.getDouble("frequence")/1000000);
	}
	int size = frequence.size();
	String frequenceJson = JSONArray.fromObject(frequence).toString();
	emcdb.closeRs();
	//获取监测数据
	emcdb.dynaRs = emcdb.QuerySQL(sql);
	%>
	<table id="infoTable" style="display:none;">
	<%
	if(speed == 1){
		while(emcdb.dynaRs.next()){
			//temp = emcdb.dynaRs.getString("time").trim();
			//temp = temp.contains(" ") ? temp.substring(temp.lastIndexOf(" ")).trim() : temp;//去掉年月日，保留时间
			//temp = temp.contains(".") ? temp.substring(0,temp.lastIndexOf(".")).trim() : temp;//去掉毫秒
		%>
			<tr><td><%=emcdb.dynaRs.getString("time").trim()%></td>
			<td><%=emcdb.dynaRs.getDouble("levels")%></td></tr>
		<%}
	}else{
		int i=-1;
		int scale = size*speed;
		int count =0;
		while(emcdb.dynaRs.next()){
			i++;
			if(i%scale==0){
				count = i/scale;
			}
			if((i>=scale*count)&&(i<=scale*count+size-1)){%>
				<tr><td><%=emcdb.dynaRs.getString("time").trim()%></td>
				<td><%=emcdb.dynaRs.getDouble("levels")%></td></tr>
		  	<%}
		}
	}%>
	</table>
	<%
	emcdb.closeRs();
	//获取监测起止时间
	String date = null;
	emcdb.dynaRs = emcdb.QuerySQL(sql2);
	while(emcdb.dynaRs.next()){
		temp = emcdb.dynaRs.getString("starttime").trim();
		temp = temp.contains(".") ? temp.substring(0,temp.lastIndexOf(".")).trim() : temp;//去掉毫秒
		date = "监测时间：" + temp + " 至 ";
		
		temp = emcdb.dynaRs.getString("endtime").trim();
		temp = temp.contains(".") ? temp.substring(0,temp.lastIndexOf(".")).trim() : temp;//去掉毫秒
		date = date + temp;
	}
	emcdb.closeRs();
	emcdb.closeStm();
	%>
  	
    <div id="gridTable" style="min-width: 300px; height: 400px; margin: 0 auto;padding-right:30px;padding-top:3%"></div>
    <div id="control">
    	<div style="margin:0 auto;">
    		<input type="range" name="progress" id="progress" min="0" max="100" value="0" style="margin-left:10%;width:80%;"/><br>
    		<font id="currentTime" style="padding-left:10%;">00:00:00</font>
    	</div>
	    <table style="margin:0 auto;margin-top:-10px;"><tr>
		    <td><input type="button" value = "播放" onclick="javascript:begin()"/></td>
		    <td><input type="button" value = "暂停" onclick="javascript:pause()"/></td>
		    <td><input type="button" value = "重置" onclick="javascript:reset()"/></td></tr>
		</table>
		<div style="padding-right:120px;"><input type="button" value="导出文件" id="import" onclick="javascript:exportFile('<%=bh%>','<%=type%>','')"></div>
    </div>
    <!-- <a href="/rsui/DataPlay/PDSMFreqPlay.jsp?bh=<%=bh %>&type=<%=type%>" style="padding-left:20px;">单频数据播放</a> -->
  <script type="text/javascript">
    
	var inforTable = document.getElementById("infoTable");
	var size = infoTable.rows.length;
	//alert(size);
	var time = new Array();
	var levels = new Array();
	for (var i=0;i<size;i++){
		time.push(infoTable.rows[i].cells[0].innerText);
		levels.push(parseFloat(infoTable.rows[i].cells[1].innerText));
	}
	
	var scale = <%=size%>;
	//alert(scale+ "/" +size);
	var count=-1;
	var group = 0;
	var Max_group = Math.ceil(size/scale);//最大组数，Math.ceil向上取整
	document.getElementById("progress").max = Max_group;
	var mode = new Boolean(false);//当前所处模式，true为播放模式,false为暂停模式。用来判断拖动进度条后播放还是暂停。
	var interval = null;
	var xData = new Array();
	var yData = new Array();
	reset();//初始化
	
	function getYData(){
	
		if(count<(size-1)){//数播放完之后，不再更新xData和yData
			//alert(count);
			group++;
			document.getElementById("progress").value = group;
			//alert("group:"+group);
			for(var j=0;j<scale;j++){
				count++;
				if(count<size){
					xData[group] = time[count];
					yData[j] = levels[count];
				}else{
					//走到这，说明数据完整性有问题
					yData[j] = 0;
				}
			}
		}
	}
	/**
	*run=1,正常播放，run=0,显示当前页面内容后不继续播放，run=-1，x,y轴重置,不播放
	*/
	function play(run){
		
		if(run == -1){
			for(var j=0;j<scale;j++){
				xData[group] = "00:00:00";
			}
			for(var j=0;j<scale;j++){
				yData[j] = 0;
			}
		}else{
			count = group*scale - 1;
			getYData();
		}
		document.getElementById("currentTime").innerHTML = xData[group];
		$(function() {
		new Highcharts.Chart({
		chart : {
			renderTo : 'gridTable', // 放置图表的DIV容器对应页面的id属性
			type : 'spline', // 图表类型line, spline, area, areaspline,column, bar, pie , scatter
			 // 事件
			 events : {
				 load : function() {
				 
					 if(run == 1){
						 var series = this.series[0];
						 var i = 0;
						 //group--;//在单点更新时使用
						 interval = setInterval(function() {
							/*
							 if(i%scale==0){
							 	getYData();
							 }
							 series.data[i].update(yData[i]);
							 i=(i+1)%scale;
							*/
							 getYData();
							 document.getElementById("currentTime").innerHTML = xData[group];
							 series.update(yData);
						 }, 1000/*document.getElementById("fast").value*/);
					 }
				},
			}
		},
		
		title : {
			text : '频段扫描数据播放' // 图表标题
		},
		subtitle : {
			text : '<%=date%>' // 副标题
		},
		// x轴
		xAxis : {
			//tickInterval : 20, 
			categories : <%=frequenceJson%>,
			labels : {
				//step : 10,  10个数据显示一个横坐标
				rotation: -70 ,
				y : 20
			}
		},
		// 右下角显示的LOGO
		credits : {
			text : '', // 设置LOGO区文字
			href : '' // 设置LOGO链接地址
		},
		// 是否启用导出功能，默认为true
		exporting : {
			enabled : false
		},
		// y轴
		yAxis : {
			title : {
			text : '场强(dBμV/m)'
			},
			//max : 60, // Y轴最大值
			//min : 0
		},
		//图标对齐方式
		legend : {
			layout : 'vertical',
			backgroundColor : '#FFFFFF',
			align : 'left',
			verticalAlign : 'top',
			x : 100,
			y : 70,
			floating : true,
			shadow : false
		},
		// 当鼠标悬置数据点时的格式化提示
		tooltip : {
			formatter : function() {
				return temp = '频率：' + this.x + ' 场强：' + this.y;
			}
		},
		//样式
		plotOptions : {
			spline : {
				dataLabels : {
				enabled : false//不显示数值
				},
				marker:{
					enabled:false//不显示节点
				},
				pointPadding : 0.2,
				borderWidth : 0
			}
		},
		// 显示的数据,JSON数据格式，最重要的是name和data元素
		series : [{
			name : '频率单位：MHZ',
			data : yData
		}]
		});
	    });
	}
	function begin(){
			mode = true;
			play(1);
		}
	
		function pause(){
			if(interval != null){
				clearInterval(interval);
			}
			if(group>0){
				group--;
			}
			mode = false;
		}
		
		function reset(){
			group = 0;
			document.getElementById("progress").value = group;
			if(interval != null){
				clearInterval(interval);
			}
			mode = false;
			play(-1);
		}
	
		document.getElementById("progress").onchange = function(){
			
			if(interval != null){
				clearInterval(interval);
			}
			group = document.getElementById("progress").value;
			if(group>0){
				group--;
			}
			if(Boolean(mode)){
				play(1);
			}else{
				play(0);
			}
		};
		
		function speed(){
		
			var bh = document.getElementById("bh").value;
			var type = document.getElementById("type").value;
			var speed = document.getElementById("speed").value;
			
			var param = "/rsui/DataQuery/PDSMPlay.jsp?bh=" + bh + "&type=" + type + "&speed=" + speed;
			//alert(param);
			param = encodeURI(param);
			param = encodeURI(param);
			window.location.href=param;
			
		}
  </script>
  </body>
</html>
