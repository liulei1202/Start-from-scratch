<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DBConnection.EMCDB" %>
<%@ page import="user.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.io.File" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星图片</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	<script type="text/javascript" src="/rsui/js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="/rsui/js/jquery.colorPicker.js"/></script>
	<link rel="stylesheet" href="/rsui/js/css/colorPicker.css" type="text/css" />
	
	
	<script src="js/DataManage/highcharts.js"></script>
    <script src="js/DataManage/exporting.js"></script>
    <script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js" ></script>
	<link type="text/css" rel="stylesheet" href="js/css/slider.css">
	<link rel="stylesheet" href="/rsui/js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
	
	<style type="text/css">
	
		.simpleColorDisplay {
			float: left;
			font-family: Helvetica;
		}
		input[type="text"]{
			width:80px;
		}
		input[type="button"]{
			width:90px;
			height:25px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			
			border-radius:5px;
			
			/*margin-right:50%;*/
		}
	</style>
  </head>
  
  <body>
    <%
   String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
   String sql = "select data FROM EMCWZFQJC WHERE  bh = '"+ bh +"'";
   
   String fileName = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
   String userId = session.getAttribute("userID").toString();
   String base =  session.getAttribute("tempWorkDir").toString();
   ImportFile infile=new ImportFile();
   String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
   basepath = basepath +  userId + File.separator;/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ */
   
   String result=EMCDB.ExportFile(sql,fileName.trim(),basepath);
   if(result.equals("0")){//说明blob中无数据%>
   		本条数据无图片。
   <%}else{
   String source = "/rsui/" + base.replace("\\","") + "/" + userId + "/" + result;
   System.out.println(source);
   String temp = basepath+result;//绝对路径
   if(source.contains("%")){
	   File file1 = new File(temp);
	   File file2 = new File(temp.replaceAll("%", ""));
	   if(file1.renameTo(file2)){
	   		System.out.println("renamed");
	   }
	   source = source.replaceAll("%","");
	   temp = temp.replaceAll("%", "");
   }
   %>
	<center><img id="img" src = "<%=source%>" alt ="未找到资源" style="margin:0 auto;height:300px;max-height:300px;width:auto;">
	<input type="button"  value="导出文件" id="import" onclick="javascript:exportFile('<%=bh%>','WXZFQJCQuery','')"></center>
	<input type="hidden" id="bh" value="<%=bh%>">
	<input type="hidden" id="path" value="<%=temp%>"><!-- 图片的真实路径，获取像素点要用到 -->
   <%} %>	
     <%
       String sql2= "select minlevels,maxlevels,startfrequence,endfrequence,color,threshold from emcwzfqjc where bh='"+bh+"'";
	   EMCDB emcdb = new EMCDB();
	   emcdb.dynaStm = emcdb.newStatement();
	   emcdb.dynaRs = emcdb.QuerySQL(sql2);
	   while(emcdb.dynaRs.next()){%>
	   	<input type="hidden" id="min" value="<%=emcdb.dynaRs.getDouble("minlevels")%>">
	   	<input type="hidden" id="max" value="<%=emcdb.dynaRs.getDouble("maxlevels")%>">
	   	<form style="margin-left:30px;">
	    <table>
     	<tr>
	    <%if(emcdb.dynaRs.getDouble("minlevels") == 0.0){%>
     	<td><font>最小场强（dBm）</font></td><td><input type="text" id="minLevels" value=""></td>
     	<%}else{%>
     	<td><font>最小场强（dBm）</font></td><td><input type="text" id="minLevels" value="<%=emcdb.dynaRs.getDouble("minlevels")%>"></td>
     	<%}
     	
     	if(emcdb.dynaRs.getDouble("maxlevels") == 0.0){%>
     	<td><font>最大场强（dBm）</font></td><td><input type="text" id="maxLevels" value=""></td>
     	<%}else{%>
     	<td><font>最大场强（dBm）</font></td><td><input type="text" id="maxLevels" value="<%=emcdb.dynaRs.getDouble("maxlevels")%>"></td>
     	<%}%>
     	
     	<td><font>起始频率（MHz）</font></td><td><input type="text" id="minFrequences" value="<%=emcdb.dynaRs.getDouble("startfrequence")/1000000%>"></td>
     	<td><input type='button' id='alert_button' value='计算' onclick="javascript:showValue()"/></td>
     	</tr><tr>
     	<td><font>截止频率（MHz）</font></td><td><input type="text" id="maxFrequences" value="<%=emcdb.dynaRs.getDouble("endfrequence")/1000000%>"></td>
     	
     	<%if(emcdb.dynaRs.getString("color") == null){%>
     	<td><font>颜色</font></td><td><input type="text" id="selectColor" value="#ffff00"></td>
     	<%}else{%>
     	<td><font>颜色</font></td><td><input type="text" id="selectColor" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("color"))%>"></td>
     	<%}%>
     	
     	<%if(emcdb.dynaRs.getDouble("threshold") == 0.0){%>
     	<td><font>阈值</font></td><td><input type="text" id="Threshold" value="20"></td>
     	<%}else{%>
     	<td><font>阈值</font></td><td><input type="text" id="Threshold" value="<%=emcdb.dynaRs.getDouble("threshold")%>"></td>
     	<%}%>
     	
     	<td><input type='button' id='save' value='存储' onclick="javascript:saveData()"/></td>
     	
     	<%}
	   emcdb.closeRs();
	   emcdb.closeStm();
      %>
     </tr>
     </table>
     </form>
     <div id="response"></div>
     
     <div id="gridTable" style="min-width: 300px; height: 350px; margin: 0 auto;padding-right:30px;"></div>
     <script>
     jQuery(document).ready(function($) {
     	$('#selectColor').colorPicker();
	 });
     
     if((document.getElementById("minLevels").value !='')&&(document.getElementById("maxLevels").value !='')){
     	showValue();
     }
     
     function showValue(){
     	
		var path = document.getElementById("path").value;
		var minLevels = document.getElementById("minLevels").value;
		if(minLevels ==''){
			alert("请输入最小场强。");
			return;
		}
		var maxLevels = document.getElementById("maxLevels").value;
		if(maxLevels ==''){
			alert("请输入最大场强。");
			return;
		}
		var minFrequences = document.getElementById("minFrequences").value;
		if(minFrequences ==''){
			alert("请输入最小频率。");
			return;
		}
		var maxFrequences = document.getElementById("maxFrequences").value;
		if(maxFrequences ==''){
			alert("请输入最大频率。");
			return;
		}
		var threshold = document.getElementById("Threshold").value;
		if(threshold ==''){
			alert("请输入阈值。");
			return;
		}
		//选中的颜色，格式为#aabbcc
		var targetColor = document.getElementById("selectColor").value;//$('input.simple_color')[0].value;
		if(targetColor ==''){
			alert("请选择颜色。");
			return;
		}
		//alert(targetColor);
	    //十六进制颜色值的正则表达式
	    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
	    // 如果是16进制颜色
	    if (reg.test(targetColor)) {
	        //处理六位的颜色值,转换位数字
	        var ColorChange = [];
	        for (var i=1; i<7; i+=2) {
	            ColorChange.push(parseInt("0x"+targetColor.slice(i, i+2)));    
	        }
	        //alert(ColorChange);
	    }
	    var color = ColorChange.join(",");
		var param = "ImageRGB/GETRGBProcess.jsp?color=" + color + "&threshold=" + threshold + "&path=" + path
		 + "&minLevels=" + minLevels + "&maxLevels=" + maxLevels + "&minFrequences=" + minFrequences + "&maxFrequences=" + maxFrequences;
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
		document.getElementById("response").innerHTML = xmlhttp.responseText;
		//if(xmlhttp.responseText.trim()=="true")
		show();
		}
		
		
			
		function show(){
		
    	//alert("show");
		var inforTable = document.getElementById("infoTable");
		var size = infoTable.rows.length;
		//alert(size);
		var xData = new Array();
		var yData = new Array();
		for (var i=0;i<size;i++){
			xData.push(infoTable.rows[i].cells[0].innerText);
			yData.push(parseFloat(infoTable.rows[i].cells[1].innerText));
		}
		var scale = size;//一次显示scale个数据
		
			new Highcharts.Chart({
			chart : {
				renderTo : 'gridTable', // 放置图表的DIV容器对应页面的id属性
				type : 'spline', // 图表类型line, spline, area, areaspline,column, bar, pie , scatter
				 // 事件
				 events : {
					 load : function() {
					},
				}
			},
			title : {
				text : '卫星数据播放' // 图表标题
			},
			subtitle : {
				text : '' // 副标题
			},
			// x轴
			xAxis : {
				//tickInterval : 20, 
				categories : xData,
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
				text : '场强(dBm)'
				},
				//max : 100, // Y轴最大值
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
					return temp = '频率：' + this.x + 'MHz， 场强：' + this.y + 'dBμV/m';
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
				name : '频率:MHz',
				data : yData
			}]
			});
		}
		function saveData(){
			//alert("save");
			if((document.getElementById("min").value != 0.0)&&(document.getElementById("max").value != 0.0)){//如果数据库里已经存储的有数据
		     	if(!confirm("已存有该数据，是否改存新的数据？")){
					return;
				}
     		}
			
			var bh = document.getElementById("bh").value;
			var minLevels = document.getElementById("minLevels").value;
			if(minLevels ==''){
				alert("请输入最小场强。");
				return;
			}
			var maxLevels = document.getElementById("maxLevels").value;
			if(maxLevels ==''){
				alert("请输入最大场强。");
				return;
			}
			var minFrequences = document.getElementById("minFrequences").value;
			if(minFrequences ==''){
				alert("请输入最小频率。");
				return;
			}
			var maxFrequences = document.getElementById("maxFrequences").value;
			if(maxFrequences ==''){
				alert("请输入最大频率。");
				return;
			}
			var threshold = document.getElementById("Threshold").value;
			if(threshold ==''){
				alert("请输入阈值。");
				return;
			}
			//选中的颜色，格式为#aabbcc
			var targetColor = document.getElementById("selectColor").value;//$('input.simple_color')[0].value;
			if(targetColor ==''){
				alert("请选择颜色。");
				return;
			}
			targetColor = targetColor.substring(1);
			
			var param = "ImageRGB/SaveData.jsp?targetColor=" + targetColor + "&threshold=" + threshold
			 + "&minLevels=" + minLevels + "&maxLevels=" + maxLevels + "&bh=" + bh + "&minFrequences=" + minFrequences + "&maxFrequences=" + maxFrequences;
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
