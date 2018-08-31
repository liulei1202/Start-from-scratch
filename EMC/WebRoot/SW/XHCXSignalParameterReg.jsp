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
    <title>短波-信号测向-信号参数登记</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-contdol" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
	<%@ page import="java.text.SimpleDateFormat" %>
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/SW.js" ></script>
	<script type="text/javascript" src="js/addoneline.js" ></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="js/swfupload/handlers.js"></script>
	<link rel="StyleSheet" type="text/css" href="js/css/manager.css" />
    	<script type="text/javascript">
	//界面上选中的文件导入数据库中
	function ImportFile() {
				
		var type = document.getElementById("type").value;
		
		
		var param ;
		
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
			
			param = "DataImport/SignalParameterRegFileAutoImport.jsp?fileName=" + fileName + "&type=" + type;
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
			      $("#DBXHCX").click(); 	
			      $("#DBXHCXDataimport").click();
			      $("#DBXHCXSignalParameterReg").click();
            	</script>
			</div>
<div id="content">
			  
			<h3 style="width:100%;">短波-信号测向-信号参数登记</h3>
			<div id="Manual" style="width:100%;position:relative;">
			<p style="background-color:#63BAD0;height:25px;font-size:20px;">手动输入信息</p>
			<div style="width:100%;height:200px;position:relative;overflow:auto;">
		  	<form method="post" accept-charset="UTF-8">
			  <table id="info" border="0" cellpadding="0" cellspacing="0" style="background-color:#ffffff;width:100%;position:absolute;overflow:auto;">                
	            <tr>
			  	<th>频率(MHz)</th><th>监测台站</th><th>带宽(kHz)</th><th>调制方式</th><th>业务类型</th><th>业务名称</th>
				<th>信号类型</th><th>发射时段</th><th>场强最大值(dBμV/m)</th><th>测试次数</th><th>背景噪声(dBμV/m·Hz)</th><th>占用度(%)</th>
				<th>统计门限(dBμV/m·Hz)</th><th>示向度 / 台站名称 / 测向质量</th><th>经度</th><th>纬度</th><th>城市</th><th>以往对比</th>
				<th>监测时间</th><th>监测人</th><th>监测地</th><th>备注</th>
				<th>原始文件</th><th>中频图</th><th>示向图</th><th>定位图</th>
				</tr>
			    <tr>
                <td><input type="text" id="frequence" name="frequence"placeholder="单位：MHz" style="width:75px;"></td>
                <td><input type="text" id="monitoringStation" name="monitoringStation" placeholder="" style="width:75px;"></td>
                <td><input type="text" id="bandWidth" name="bandWidth"placeholder="单位：kHz" style="width:75px;"></td>
                <td><input type="text" id="modulate" name="modulate"placeholder="" style="width:75px;"></td>
                <td><input type="text" id="businessType" name="businessType"placeholder="" style="width:75px;" ></td>
                <td><input type="text" id="businessName" name="businessName"placeholder="" style="width:75px;" ></td>
                
                <td><input type="text" id="signalType" name="signalType"placeholder="" style="width:75px;" ></td>
                <td><input type="text" id="launchTime" name="launchTime"placeholder="" style="width:90px;"></td>
                <td><input type="text" id="levels" name="levels"placeholder="单位：dBμV/m" style="width:90px;"></td>
                <td><input type="text" id="testNumber" name="testNumber"placeholder="" style="width:75px;"></td>
                <td><input type="text" id="noise" name="noise"placeholder="单位：dBμV/m·Hz" style="width:110px;"></td>
                <td><input type="text" id="occupancy" name="occupancy"placeholder="单位：%" style="width:75px;"></td>
                
                <td><input type="text" id="threshold" name="threshold"placeholder="单位：dBμV/m·Hz" style="width:110px;"></td>
                <td><input type="text" id="direction" name="direction"placeholder="" style="width:150px;"></td>
                <td><input type="text" id="longitude" name="longitude"placeholder="" style="width:90px;"></td>
                <td><input type="text" id="latitude" name="latitude" placeholder="" style="width:90px;"></td>
                <td><input type="text" id="city" name="city"placeholder="" style="width:75px;"></td>
                <td><input type="text" id="compared" name="compared"placeholder="" style="width:75px;"></td>
                
                <td><input type="text" id="monitorTime" name="monitorTime"class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/></td>
                <td><input type="text" id="monitor" name="monitor"placeholder="" style="width:75px;" list="cars"/>
					<datalist id="cars">
						<% 
						LinkedList<String> userName  = UserInfor.getUserName();
						for(int i=0;i<userName.size();i++){
						 %>
						<option value="<%=userName.get(i)%>">
						<%} %>
					</datalist></td>
                <td><input type="text" id="monitorLocation" name="monitorLocation"placeholder="" style="width:75px;"></td>
                <td><input type="text" id="comments" name="comments"placeholder="" style="width:75px;"></td>
                <td><input type="button" value="导入" id="original" name="original" onclick="javascript:grapInPara('原始数据')" style="width:75px;"></td>
                <td><input type="button" value="导入" id="grap1" name="grap1" onclick="javascript:grapInPara('中频图')" style="width:75px;"></td>
                <td><input type="button" value="导入" id="grap2" name="grap2" onclick="javascript:grapInPara('示向图')" style="width:75px;"></td>
                <td><input type="button" value="导入" id="grap3" name="grap3" onclick="javascript:grapInPara('定位图')" style="width:75px;"></td> 
	 	  		<td><input type="button" value="增加" id="addTable" onclick="add(this)"/></td>
          		<td><input type="button" value="删除" id="deleteTable"onclick="del(this)"/></td>
	            <td><input type="hidden" id="bh" value=""></td>   
	            </tr>
			  </table>
			  <table id="table1"></table>
			  <input type="hidden" id="type" value="DBXHCXSignalParameterReg">
			</form>
			</div>
			<input type="button" id="button" value="提交" onclick="signalParameterReg()" style="margin-top:10px;width:60px;height:30px;border-radius:5px;">
			  <div id="response1"  style="display:inline"></div>
			</div>
			  
			  
		  <div id="excel" style="width:100%;position:relative;margin-top:30px;">
		  <p style="background-color:#63BAD0;height:25px;font-size:20px;">读取Excel文件</p>
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
	<script>
  
	  function add(obj) {
	    var tr = $(obj).parent().parent();
	    tr.after(tr.clone());
	  }
	    function del(obj) {
	    $(obj).parent().parent().remove();
	  }
</script>
                    
			  
			</div>
		</div>
		<%@include file="/foot.jsp" %>
	</div>
  </body>
</html>
