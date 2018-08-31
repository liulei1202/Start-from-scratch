<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*" %>
<%@ page import="DBConnection.*" %>
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
    
    <title>信号参数登记数据修改</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/rsui/js/DataManage/DataQuery.js"></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" href="/rsui/js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
	<%@ page import="java.text.SimpleDateFormat" %>
	<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/SW.js" ></script>
	<script type="text/javascript" src="js/addoneline.js" ></script>
	<script type="text/javascript" src="/rsui/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="js/swfupload/handlers.js"></script>
	<style>
		input[id="import"]{
			width:75px;
			height:20px;
			border-style:none;
			background-color:rgb(25,118,210);
			color:#ffffff;
			border-radius:5px;
			margin-left:30px;
		}
		.wrap{
		width:80px;
		white-space:nowrap;/*不换行*/
		text-overflow:ellipsis;/*超出部分用...代替*/
		overflow:hidden;
		}
		.infor td{
			margin:10px;
		}
	</style>
    <script type="text/javascript">
	//界面上选中的文件导入数据库中
	function ImportFile() {
				
		var bh = document.getElementById("bh").value;
		var type = document.getElementById("type").value;
		var fileType = document.getElementById("fileType").value;
		
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
			
			param = "DataImport/ModifyFilefromSignal.jsp?fileName=" + fileName + "&type=" + type
			+ "&bh=" + bh+ "&fileType=" + fileType;
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
		var bandWidth = document.getElementById("bandWidth").value;
		if(frequence == ""){
			alert("请输入频率。");
			return;
		}
		if(bandWidth == ""){
			alert("请输入带宽。");
			return;
		}
		swfu.startUpload();
	}
	</script>
  </head>
  <body  onload="setupUpload();" style="margin-top:35px;">
    <%
    String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
 
    String result = null;
    String sql = null;
    EMCDB emcdb = new EMCDB();
    emcdb.dynaStm = emcdb.newStatement();
    
    
   		if (type.equals("DBDPJCQuery"))
	    {
	   		sql = "select * from emcdpjcparameter where bh='"+bh+"'";
	   	}
	   	else if (type.equals("DBXHCXQuery"))
	    {
	   		sql = "select * from emcdpcxparameter where bh='"+bh+"'";
	   	}
	   	else if (type.equals("CDBDPJCQuery"))
	    {
	   		sql = "select * from emcudpjcparameter where bh='"+bh+"'";
	   	}
	   	else if (type.equals("CDBXHCXQuery"))
	    {
	   		sql = "select * from emcudpcxparameter where bh='"+bh+"'";
	   	}
	   	emcdb.dynaRs = emcdb.QuerySQL(sql);
	   	if(emcdb.dynaRs.next()){
   	 %>
   	    <input type="hidden" id="bh" value="<%=bh %>"/>
   	 	<input type="hidden" id="type" value="<%=type %>"/>
	   	<form>
	   	<table id="infor" style="padding-left:50px;">
	   	  <tr>
	   	  
	   	  	<td>频率(MHz)</td><td><input type="text" id="frequence" name="frequence" value="<%=emcdb.dynaRs.getDouble("frequence")/1000000%>" ></td>
			<td>监测台站</td><td><input type="text" id="Station" name="Station"  value="<%=UserInfor.forNull(emcdb.dynaRs.getString("Station"))%>" ></td>
		    <td><div class="wrap" title="占用带宽(kHz)">占用带宽(kHz)</div></td><td><input type="text" id="bandWidth" name="bandWidth" value="<%=emcdb.dynaRs.getDouble("bandWidth")/1000%>" ></td>
			</tr><tr>
			<td>调制方式</td><td><input type="text" id="modulate" name="modulate" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("modulate"))%>" ></td>
		    <td>业务类型</td><td><input type="text" id="businessType" name="businessType" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("businessType"))%>"  ></td>
		    <td>业务名称</td><td><input type="text" id="businessName" name="businessName" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("businessName"))%>"  ></td>
		    </tr><tr>
			<td>信号类型</td><td><input type="text" id="signalType" name="signalType" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("signalType"))%>"  ></td>
			<td>发射时段</td><td><input type="text" id="launchTime" name="launchTime" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("launchTime"))%>" ></td>
			<td><div class="wrap" title="场强最大值(dBμV/m)">场强最大值(dBμV/m)</div></td><td><input type="text" id="levels" name="levels" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("levels"))%>" ></td>
		    </tr><tr>
			<td>测试次数</td><td><input type="text" id="testNumber" name="testNumber" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("testNumber"))%>" ></td>
			<td><div class="wrap" title="背景噪声(dBμV/m·Hz)">背景噪声(dBμV/m·Hz)</div></td><td><input type="text" id="noise" name="noise" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("noise"))%>" ></td>
		    <td>占用度(%)</td><td><input type="text" id="occupancy" name="occupancy" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("occupancy"))%>" ></td>
		    </tr><tr>
			<td><div class="wrap" title="统计门限(dBμV/m·Hz)">统计门限(dBμV/m·Hz)</div></td><td><input type="text" id="threshold" name="threshold" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("threshold"))%>" ></td>
			<td><div class="wrap" title="示向度/台站名称/测向质量">示向度/台站名称/测向质量</div></td><td><input type="text" id="direction" name="direction" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("direction"))%>" ></td>
			<td>经度</td><td><input type="text" id="longitude" name="longitude" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("longitude"))%>" ></td>
			</tr><tr>
			<td>纬度</td><td><input type="text" id="latitude" name="latitude"  value="<%=UserInfor.forNull(emcdb.dynaRs.getString("latitude"))%>" ></td>
		    <td>城市</td><td><input type="text" id="city" name="city" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("city"))%>"  ></td>
		    <td>以往对比</td><td><input type="text" id="compared" name="compared" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("compared"))%>" ></td>
		    </tr><tr>
			<td>监测时间</td><td><input type="text" id="monitorTime" name="monitorTime"class="Wdate" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("monitorTime"))%>"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/></td>
			<td>监测人</td><td><input type="text" id="monitor" name="monitor"value="<%=UserInfor.forNull(emcdb.dynaRs.getString("monitor"))%>"  list="cars"/>
					<datalist id="cars">
						<% 
						LinkedList<String> userName  = UserInfor.getUserName();
						for(int i=0;i<userName.size();i++){
						 %>
						<option value="<%=userName.get(i)%>">
						<%} %>
					</datalist></td>
			<td>监测地</td><td><input type="text" id="monitorLocation" name="monitorLocation" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("monitorLocation"))%>"  ></td>
			</tr><tr>
			<td>备注</td><td> <input type="text" id="comments" name="comments" value="<%=UserInfor.forNull(emcdb.dynaRs.getString("comments"))%>" ></td>   
		    <td></td><td></td>
			<td></td><td><input type="button" value="提交" id="import" onclick="javascript:modifyParameter('<%=bh%>','<%=type%>')"/></td>
		  </tr>
	   	 </table>  
	   	    
	   	 </form>
	   	 <div id="excel" style="position:relative;padding-top:30px;padding-left:50px;">
	   	 	<font >要修改的数据类型：</font>
	   	 	<select id="fileType">
	   	    	<option value="原始数据">原始文件</option>
	   	    	<option value="中频图">中频图</option>
	   	    	<option value="示向图">示向图</option>
	   	    	<option value="定位图">定位图</option>
	   	    	
	   	    </select>
		 
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
   	<%
   		}
   		emcdb.closeRs();
   		emcdb.closeStm();
   		
     %>
  </body>
</html>
