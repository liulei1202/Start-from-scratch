<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="user.*" %>
<%@ page import="DBConnection.*" %>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
StringBuffer uploadUrl = new StringBuffer(basePath + "DataImport/FileUpload.jsp;jsessionid=" + request.getSession().getId());
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>图形数据修改</title>
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
	function ImportFile() {
		
		var bh = document.getElementById("bh").value;
		var type = document.getElementById("type").value;
		var grapType = document.getElementById("grapType").value;//原图形类型
		var graphicType = document.getElementById("graphicType").value;//修改后的图形类型
		var startTime = document.getElementById("startTime").value;
		var endTime = startTime;
		var frequence = document.getElementById("frequence").value;
		var station = document.getElementById("station").value;
		var monitor = document.getElementById("monitor").value;
		var monitorLocation = document.getElementById("monitorLocation").value;
		
		var businessType = document.getElementById("businessType").value;
		var businessName = document.getElementById("businessName").value;
		var signalType = document.getElementById("signalType").value;
		var longitude = document.getElementById("longitude").value;
		var latitude = document.getElementById("latitude").value;
			  	
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
			param = "&type=" + type + "&graphicType=" + graphicType + "&startTime=" + startTime +  "&endTime=" + endTime;
			param = param + "&frequence=" + frequence + "&station=" + station + "&monitor=" + monitor + "&monitorLocation=" + monitorLocation
			+"&businessType="+businessType+"&businessName="+businessName+"&signalType="+signalType
			+"&longitude="+longitude+"&latitude="+latitude + "&bh=" + bh+ "&grapType=" + grapType;
			
			param = "DataImport/GraphicFileAutoImport_DPJCXHCX.jsp?fileName=" + fileName + param;
			
			//alert(param);
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
		var rowsLength = infoTable.rows.length;
		if(rowsLength > 1){
			alert("只能上传一个文件。");
			return;
		}
		var frequence = document.getElementById("frequence").value;
		if(frequence == ""){
			alert("请输入中心频率。");
			return;
		}
		var startTime = document.getElementById("startTime").value;
		if(startTime == ""){
			alert("请输入监测时间。");
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
  
  <body onload="setupUpload();" style="padding:30px 0 0 15px;">
	<%
	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
    String grapType = java.net.URLDecoder.decode(request.getParameter("grapType"),"UTF-8");
    
    String result = null;
    String sql = null;
    EMCDB emcdb = new EMCDB();
    emcdb.dynaStm = emcdb.newStatement();
    String table = null;
    String table2 = null;
    if(type.trim().equals("DBDPJCQuery")){
			
			table = "EMCDPJCGraphic";
			table2 = "EMCDPJCParameter";
	}else if(type.trim().equals("CDBDPJCQuery")){
		
		table = "EMCUDPJCGraphic";
		table2 = "EMCUDPJCParameter";
	}else if(type.trim().equals("DBXHCXQuery")){
		
		table = "EMCDPCXGraphic";
		table2 = "EMCDPCXParameter";
	}else if(type.trim().equals("CDBXHCXQuery")){
		
		table = "EMCUDPCXGraphic";
		table2 = "EMCUDPCXParameter";
	}else if(type.trim().equals("DBPDSMQuery")){
			
			table = "EMCPDSMGraphic";
	}else if(type.trim().equals("CDBPDSMQuery")){
		
			table = "EMCUPDSMGraphic";
	}/*else if(type.trim().equals("DBLHDWQuery")){
		
		table = "EMCLHDWAudio";
	}else if(type.trim().equals("CDBLHDWQuery")){
		
		table = "EMCULHDWAudio";
	}
	*/else{
		table = "wrong";
	}
    
    int num = 0;
	if(table2 != null){
	    String sql3 = "select count(*) as num from "+table2+" where bh='"+bh+"'";
	    emcdb.dynaRs = emcdb.QuerySQL(sql3);
	    
		if(emcdb.dynaRs.next()){
			num = emcdb.dynaRs.getInt("num");
		}
		emcdb.closeRs();
	}
	if(num != 0){%>
		<%="这是信号参数登记中的数据，请在信号参数登记中修改。"%>
	<%}
    else{
    
	sql = "select * from "+table+" where bh = '"+bh+"' and graphicType='"+grapType+"'";
	emcdb.dynaRs = emcdb.QuerySQL(sql);
	if(emcdb.dynaRs.next()){
   	%>
   		
	   	<form>
	   	<input type="hidden" id="bh" value="<%=bh%>">
   		<input type="hidden" id="type" value="<%=type%>">
   		<input type="hidden" id="grapType" value="<%=grapType%>">
   		
	   	<table>
  			<tr>
  			<td>图形类型</td>
				  	<td><select id="graphicType" style="width:150px;">
				  	    <option value="<%=grapType%>" ><%=grapType%></option>
						<option value="中频图" >中频图</option>
						<option value="示向图" >示向图</option>
						<option value="定位图" >定位图</option>
						<option value="三维图" >三维图</option>
						<option value="瀑布图" >瀑布图</option>
						<option value="频率占用度图" >频率占用度图</option>
						<option value="时间占用度图" >时间占用度图</option>
						</select>
				  	</td>
		  	<td>监测时间</td><td><input type="text" id="startTime" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("starttime")) %>" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
		  	<td>中心频率(MHz)</td><td><input type="text" id="frequence" value="<%=emcdb.dynaRs.getDouble("frequence")/1000000 %>"></td>
		  	</tr><tr>
		  	<td>任务类型</td><td><input type="text" id="businessType" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("businessType")) %>"></td>
			<td>任务名称</td><td><input type="text" id="businessName" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("businessName")) %>"></td>
			<td>信号类型</td><td><input type="text" id="signalType" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("signalType")) %>"></td>
			</tr><tr>
		  	<td>经度</td><td><input type="text" id="longitude" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("longitude")) %>"></td>
			<td>纬度</td><td><input type="text" id="latitude" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("latitude")) %>"></td>
		  	<td>监测台站</td><td><input type="text" id="station"  value="<%=UserInfor.forNull(emcdb.dynaRs.getString("station")) %>"></td>
		  	</tr><tr>
			<td>操作人</td><td><input type="text" id="monitor"  value="<%=UserInfor.forNull(emcdb.dynaRs.getString("monitor")) %>" list="cars"/>
			<datalist id="cars">
				<% 
				LinkedList<String> userName  = UserInfor.getUserName();
				for(int i=0;i<userName.size();i++){
				 %>
				<option value="<%=userName.get(i)%>">
				<%} %>
			</datalist></td>
		  	<td>监测地点</td><td><input type="text" id="monitorLocation"  value="<%=UserInfor.forNull(emcdb.dynaRs.getString("monitorLocation")) %>"></td>
			<td></td><td><input type="button" value="提交" id="import" onclick="javascript:modifyGraphicProcess('<%=bh%>','<%=type%>')"/></td>
		</tr>
		</table>
		</form>
   	<%}
   		
   		emcdb.closeRs();
		emcdb.closeStm();
		%>	
			  
		  	  <div style="border: solid 1px #7FAAFF; background-color: #C5D9FF;width:400px;height:40px;margin-top:15px;">
			  	<span id="spanButtonPlaceholder"></span>
				<input id="btnUpload"  type="button" value="上  传"onclick="startUploadFile();" class="btn3_mouseout"/>
				<input id="btnCancel" type="button" value="取消所有上传"onclick="cancelUpload();" disabled="disabled" class="btn3_mouseout" />
			  	
			  </div>
			  <div id="divFileProgressContainer"></div>
			  <div id="thumbnails">
			  <table id="infoTable"></table>
			  </div>
	<%} %>
  </body>
</html>
