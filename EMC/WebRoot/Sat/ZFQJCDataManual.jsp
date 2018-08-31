<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>

<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
StringBuffer uploadUrl = new StringBuffer(basePath + "DataImport/FileUpload.jsp;jsessionid=" + request.getSession().getId());
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>卫星监测-转发器监测-手动数据导入</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-contdol" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<%@ page import="java.text.SimpleDateFormat" %>
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/SW.js" ></script>
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="js/swfupload/handlers.js"></script>
	<link rel="StyleSheet" type="text/css" href="js/css/manager.css" />
	<style>
		input[id="import"]{
			width:90px;
			height:25px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			margin-left:30px;
		}
	</style>
    	<script type="text/javascript">
	//界面上选中的文件导入数据库中
	function ImportFile(){
				
		var bh = document.getElementById("bh").value;
		var type = document.getElementById("type").value;
		var position = document.getElementById("position").value;
    	var satName = document.getElementById("satName").value;
    	var country = document.getElementById("country").value;
    	var rbw = document.getElementById("rbw").value;
    	var vbw = document.getElementById("vbw").value;
    	var level= document.getElementById("level").value;
    	var frequence= document.getElementById("frequence").value;
    	var polar= document.getElementById("polar").value;
    	var startFrequence= document.getElementById("startFrequence").value;
    	var endFrequence= document.getElementById("endFrequence").value;
    	var station= document.getElementById("station").value;
    	var time= document.getElementById("time").value;
    	var person= document.getElementById("person").value;
    	var businessType = document.getElementById("businessType").value;
		var businessName = document.getElementById("businessName").value;
		var zfq = document.getElementById("zfq").value;
		var antenna = document.getElementById("antenna").value;
		var weaken = document.getElementById("weaken").value;
		var comments = document.getElementById("comments").value;
		var param;   
		
		var infoTable = document.getElementById("infoTable");
		var rowsLength = infoTable.rows.length;
		var fileName,xmlhttp;
		for ( var i = 0; i < rowsLength; i++) {
			
			fileName = infoTable.rows[i].cells[1].innerText;
			
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			param = "DataImport/ZFQJCFileAutoImport_Manual.jsp?fileName=" + fileName;
			param = param + "&position=" + position;
			param = param + "&satName=" + satName;
			param = param + "&country=" + country;
			param = param + "&rbw=" + rbw;
			param = param + "&vbw=" + vbw;
			param = param + "&level=" + level;
			
			param = param + "&frequence=" + frequence;
			param = param + "&polar=" + polar;
			param = param + "&startFrequence=" + startFrequence;
			param = param + "&endFrequence=" + endFrequence;
			param = param + "&station=" + station;
			
			param = param + "&time=" + time;
			param = param + "&person=" + person;
			param = param + "&bh=" + bh;
			param = param + "&type=" + type
			+"&businessType="+businessType+"&businessName="+businessName
			+"&zfq="+zfq+"&antenna="+antenna
			+"&weaken="+weaken+"&comments="+comments;
			
			param = encodeURI(param);
			param = encodeURI(param);
			//alert(param);
			xmlhttp.open("GET",param, false);
			xmlhttp.send();
			
			var str = xmlhttp.responseText;
			str = str.replace(/[\r\n]/g, "");	//删除换行符
			infoTable.rows[i].cells[2].innerText = str;
		}
	}
	
	var swfu;
	
	function setupUpload(){
		var url = "<%=uploadUrl %>";
		swfu = new SWFUpload(//初始化并将swfupload按钮替换swfupload占位符，实例化一个swfupload，传入参数配置对象
			{ //定义参数配置
				upload_url : url,//"http://lyrenz-pc:8080/DIFEMC/DataImport/FileUpload.jsp",//接收上传的服务端
				post_params : {"name" : "zwm"},
				use_query_string : true,
				// File Upload Settings   
				file_size_limit : "1000000MB", // 文件大小控制
				file_types : "*.*",
				file_types_description : "All Files",
				file_upload_limit : "0",
				
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				
				button_placeholder_id : "spanButtonPlaceholder",//上传按钮占位符id
				button_width : 180,//按钮宽
				button_height : 18,//高
				button_text : '<span class="button">请选择文件 </span>',
				button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
				button_text_top_padding : 0,
				button_text_left_padding : 18,
				button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
				button_cursor : SWFUpload.CURSOR.HAND,
				// Flash Settings
				flash_url : "js/swfupload/swfupload.swf",
				custom_settings : {	upload_target : "divFileProgressContainer"},
				// Debug Settings
				debug : false
				//是否显示调试窗口
			});
	};
	function startUploadFile() {
		var position = document.getElementById("position").value;
    	var rbw = document.getElementById("rbw").value;
    	var vbw = document.getElementById("vbw").value;
    	var startFrequence= document.getElementById("startFrequence").value;
    	var endFrequence= document.getElementById("endFrequence").value;
    	var time= document.getElementById("time").value;
    	if(time == ""){
			alert("请输入监测时间。");
			return;
		}
    	if(position == ""){
			alert("请输入轨位。");
			return;
		}
		if(startFrequence == ""){
			alert("请输入起始频率。");
			return;
		}
		if(endFrequence == ""){
			alert("请输入截止频率。");
			return;
		}
		if(rbw == ""){
			alert("请输入rbw。");
			return;
		}
		if(vbw == ""){
			alert("请输入vbw。");
			return;
		}
		
		swfu.startUpload();
	}
	</script>
	<style>
		#info th{
			text-align:left;
		}
	</style>
  </head>
  
  <body onload="setupUpload();">
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
				<script>
			      $("#WX").click();
			      $("#WXZFQJC").click(); 	
			      $("#ZFQJCData").click();
			      $("#ZFQJCDataManual").click();
            	</script>
			</div>
			<div id="content">
			  
			<h3 style="width:100%;">卫星-转发器监测-手动数据导入</h3>
			<table id="info">
			  	<tr>
			  	<td>任务类型</td><td><input type="text" id="businessType" placeholder=""></td>
				<td>任务名称</td><td><input type="text" id="businessName" placeholder=""></td>
			  	<td>监测时间</td><td><input type="text" id="time" 	       placeholder=""  class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
			  	</tr><tr>
				<td>轨位(°E)</td><td><input type="text" id="position" 	   placeholder="单位：°E"></td>
				<td>卫星名称</td><td><input type="text" id="satName" 	   placeholder=""></td>
				<td>国家</td><td><input type="text" id="country" 	   placeholder="" ></td>
				</tr><tr>
				<td>转发器</td><td><input type="text" id="zfq" placeholder=""></td>
				<td>起始频率(MHz)</td><td><input type="text" id="startFrequence" placeholder="单位：MHz" ></td>
				<td>截止频率(MHz)</td><td><input type="text" id="endFrequence"   placeholder="单位：MHz" ></td>
				</tr><tr>
				<td>频段</td><td><select id="frequence" style="width:153px;">
						<option value="" ></option>
						<option value="UHF" >UHF</option>
						<option value="L" >L</option>
						<option value="S" >S</option>
						<option value="C" >C</option>
						<option value="X" >X</option>
						<option value="Ku" >Ku</option>
						<option value="Ka" >ka</option>
						</select></td>
				<td>极化方式</td><td><select id="polar" style="width:153px;">
				 		<option value="" ></option>
						<option value="L" >L</option>
						<option value="R" >R</option>
						<option value="H" >H</option>
						<option value="V" >V</option>
						</select></td>
				<td>天线编号</td><td><select id="antenna" style="width:153px;">
						<option value="" ></option>
						<%for(int i=1;i<=12;i++){%>
						<option value="<%=i%>" ><%=i%></option>
						<%}%>
						<option value="X" >X</option>
						</select></td>
				</tr><tr>
				<td>RBW(kHz)</td><td><input type="text" id="rbw" 		   placeholder="单位：kHz"  ></td>
				<td>VBW(kHz)</td><td><input type="text" id="vbw" 		   placeholder="单位：kHz" ></td>
				<td>衰减(dB)</td><td><input type="text" id="weaken" placeholder="单位：dB"></td>
				</tr><tr>
				<td>参考电平(dBm)</td><td><input type="text" id="level"          placeholder="单位：dBm" ></td>
				<td>监测台站</td><td><input type="text" id="station" 	   placeholder="" ></td>
				<td>监测人</td><td><input type="text" id="person" 		   placeholder="" list="cars"/>
					<datalist id="cars">
						<% 
						LinkedList<String> userName  = UserInfor.getUserName();
						for(int i=0;i<userName.size();i++){
						 %>
						<option value="<%=userName.get(i)%>">
						<%} %>
					</datalist></td>
				</tr><tr>
				<td>备注</td><td><input type="text" id="comments" placeholder=""></td>
				<td></td><td></td><td></td><td><input type="button" value="导入" id="import" onclick="javascript:modifyZFQJC('0','WXZFQJCDataManual')"/></td>
				</tr>
			  </table>
			  <input type="hidden" id="type" value="WXZFQJCDataManual">
			  <input type="hidden" id="bh" value="0">
		  
		  	  <div style="border: solid 1px #7FAAFF; background-color: #C5D9FF;width:400px;height:40px;margin-top:15px;">
			  	<span id="spanButtonPlaceholder"></span>
				<input id="btnUpload"  type="button" value="上  传"onclick="startUploadFile();" class="btn3_mouseout"/>
				<input id="btnCancel" type="button" value="取消所有上传"onclick="cancelUpload();" disabled="disabled" class="btn3_mouseout" />
			  	
			  </div>
			  <div id="divFileProgressContainer"></div>
			  <div id="thumbnails">
			  <table id="infoTable"></table>
			  </div>
			  
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>
  </body>
</html>
