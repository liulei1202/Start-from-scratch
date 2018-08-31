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
    
    <title>短波-监测报告-数据导入</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<%@ page import="java.text.SimpleDateFormat" %>
	<%@ page import="EMCTools.*"%>
	<%@ page import="com.fileupload.*"%>
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="js/swfupload/handlers.js"></script>
	<link rel="StyleSheet" type="text/css" href="js/css/manager.css" />
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
	//界面上选中的文件导入数据库中
	function ImportFile() {	
		var bh = "0";
		var type = document.getElementById("type").value;
		var reportName = document.getElementById("reportName").value;
		var reportType = document.getElementById("reportType").value;
		var startFrequence = document.getElementById("startFrequence").value;
		var endFrequence = startFrequence;
		var startTime = document.getElementById("startTime").value;
		var endTime = document.getElementById("endTime").value;
		var keyWords = document.getElementById("keyWords").value;
		var station = document.getElementById("station").value;
		var author = document.getElementById("author").value;
		var monitorLocation = document.getElementById("monitorLocation").value;
		var equipment = document.getElementById("equipment").value;
		var businessName = document.getElementById("businessName").value;
		var businessType = document.getElementById("businessType").value;
		
		var param;
		
		var infoTable = document.getElementById("infoTable");
		var rowsLength = infoTable.rows.length;
		var path,xmlhttp;
		
		for ( var i = 0; i < rowsLength; i++) {
	
			path = infoTable.rows[i].cells[1].innerText;//文件名
			param ="&type="+type+ "&reportName="+reportName+ "&reportType="+reportType+ "&startFrequence="+startFrequence+
			 "&endFrequence="+endFrequence+ "&startTime="+startTime+ "&endTime="+endTime+ "&keyWords="+keyWords+ 
			"&station="+station + "&author=" + author +"&monitorLocation="+monitorLocation+ "&equipment=" + equipment
			+"&businessName="+businessName+"&businessType="+businessType + "&bh=" + bh;
			
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			var myUrl = "/rsui/DataImport/JCBGfileAutoImport.jsp?path=" + path + param;
			myUrl = encodeURI(myUrl);
			myUrl = encodeURI(myUrl);
			//alert(myUrl);
	
			xmlhttp.open("GET",myUrl, false);
			xmlhttp.send(null);
			var str = xmlhttp.responseText;
			str = str.replace(/[\r\n]/g, "");
			
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
		
		var startFrequence = document.getElementById("startFrequence").value;
		if(startFrequence == ""){
			alert("请输入中心频率。");
			return;
		}
		var startTime = document.getElementById("startTime").value;
		if(startTime == ""){
			alert("请输入起始时间。");
			return;
		}
		var endTime = document.getElementById("endTime").value;
		if(endTime == ""){
			alert("请输入终止时间。");
			return;
		}
		swfu.startUpload();
	}
	</script>
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
				if(No.equals("5")){
				%>
					<%@include file="/swMenu.jsp" %>
				<%} %>
				 <script>
			      $("#DB").click();
			      $("#DBJCBG").click(); 	
			      $("#DBJCBGData").click();
            	 </script>
			</div>
			<div id="content">
			  
			  <h3>短波-监测报告-数据导入</h3>
				  	<table id="info">
				  	<tr>
				  	<td>报告类型</td>
				  	<td><select id="reportType">
						<option value="专项监测报告" >专项监测报告</option>
						<option value="干扰查处报告" >干扰查处报告</option>
						<option value="电磁环境监测报告" >电磁环境监测报告</option>
						<option value="其他报告" >其他报告</option>
						</select>
				  	</td>
				  	<td>报告名称</td><td><input type="text" id="reportName"></td>
				  	<td>中心频率(MHz)</td><td><input type="text" id="startFrequence" placeholder="单位：MHz"></td><!-- 不确定是否有起始终止频率，暂时只输入中心频率，令终止频率等于中心频率 -->
					</tr><tr>
					<td>任务类型</td><td><input type="text" id="businessType" placeholder=""></td>
					<td>任务名称</td><td><input type="text" id="businessName" placeholder=""></td>
				  	<td>起始时间</td><td><input type="text" id="startTime" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
					</tr><tr>
					<td>终止时间</td><td><input type="text" id="endTime" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
					<td>关键词</td><td><input type="text" id="keyWords" placeholder=""></td>
					<td>监测台站</td><td><input type="text" id="station"></td>
					</tr><tr>
					<td>监测地点</td><td><input type="text" id="monitorLocation"></td>
					<td>监测设备</td><td><input type="text" id="equipment"></td>					
					<td>操作人</td><td><input type="text" id="author" list="cars"/>
					<datalist id="cars">
						<% 
						LinkedList<String> userName  = UserInfor.getUserName();
						for(int i=0;i<userName.size();i++){
						 %>
						<option value="<%=userName.get(i)%>">
						<%} %>
					</datalist></td>
					</tr>
				  </table>
				  <input type="hidden" id="type" value="DBJCBGData">
			  
			  	  <div style="border: solid 1px #7FAAFF; background-color: #C5D9FF;width:400px;height:40px;margin-top:15px;">
				  	<span id="spanButtonPlaceholder"></span>
					<input id="btnUpload"  type="button" value="上  传"onclick="startUploadFile();" class="btn3_mouseout"/>
					<input id="btnCancel" type="button" value="取消所有上传"onclick="cancelUpload();" disabled="disabled" class="btn3_mouseout" />
				  </div>
				  <div id="divFileProgressContainer"></div>
				  <div id="thumbnails">
					<table id="infoTable"></table>
				  </div>
			  
			  <input type="hidden" id="uploadurl" value="<%=uploadUrl%>"/>
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>
  </body>
</html>
