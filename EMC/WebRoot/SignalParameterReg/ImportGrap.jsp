<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
StringBuffer uploadUrl = new StringBuffer(basePath + "DataImport/FileUpload.jsp;jsessionid=" + request.getSession().getId());
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>图形数据导入</title>
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
	<script type="text/javascript">
	//界面上选中的文件导入数据库中
	function ImportFile() {
				
		var bh = document.getElementById("bh").value;
		var type = document.getElementById("type").value;
		var grapType = document.getElementById("grapType").value;
		
		var param;
		var infoTable = document.getElementById("infoTable");
		var rowsLength = infoTable.rows.length;
		var fileName,xmlhttp;
		for ( var i = 0; i < rowsLength; i++) {
		
			param = "&bh=" + bh +"&type=" + type+"&grapType=" + grapType;
			
			fileName = infoTable.rows[i].cells[1].innerText;
			
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			param = "DataImport/ImportGrapfromSignal.jsp?fileName=" + fileName + param;
			param = encodeURI(param);
			param = encodeURI(param);
			//alert(param);
			xmlhttp.open("GET",param, false);
			xmlhttp.send(null);
			
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
		var rowsLength = infoTable.rows.length;
		if(rowsLength > 1){
			alert("只能上传一个文件。");
			return;
		}
		swfu.startUpload();
	}
	</script>
  </head>
  
  <body onload="setupUpload();" style="margin:30px 0 0 30px;">
	  <%
	  	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8"); 
		String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
		String grapType = java.net.URLDecoder.decode(request.getParameter("grapType"),"UTF-8");
	   %>
	    <input type="hidden" id="bh" value="<%=bh%>"/>
		<input type="hidden" id="type" value="<%=type%>"/>
		<input type="hidden" id="grapType" value="<%=grapType%>"/>	 
			 
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
			
  </body>
</html>
