
function formReset() {
	
	document.getElementById("myForm").reset();
	
}

function query(){
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var startFrequence = document.getElementById("startFrequence").value;
	if(startFrequence !=""){
		startFrequence = startFrequence*1000000;
	}
	var endFrequence = document.getElementById("endFrequence").value;
	if(endFrequence !=""){
		endFrequence = endFrequence*1000000;
	}
	var station = document.getElementById("station").value;
	var monitorlocation = document.getElementById("monitorlocation").value;
	var parameter = document.getElementById("parameter").checked;/*选中为true,否则为false*/
	var original = document.getElementById("original").checked;
	var graphic = document.getElementById("graphic").checked;
	var audio = document.getElementById("audio").checked;
	var video = document.getElementById("video").checked;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
			//document.getElementById("batchDownload").style.display="block";
		}
	};
	var param = "?startTime="+startTime+"&endTime="+endTime+"&startFrequence="+startFrequence
	+"&endFrequence="+endFrequence+"&station="+station+"&monitorlocation="+monitorlocation
	+"&parameter="+parameter+"&original="+original+"&graphic="+graphic+"&audio="+audio+"&video="+video+"&type="+type;
	
	if((type=="DBDPJCQuery")||(type=="CDBDPJCQuery")){
		param = "DataQuery/DPJCQueryProcess.jsp"+param;
	}if((type=="DBPDSMQuery")||(type=="CDBPDSMQuery")){
		param = "DataQuery/PDSMQueryProcess.jsp"+param;   
	}if((type=="DBXHCXQuery")||(type=="CDBXHCXQuery")){
		param = "DataQuery/XHCXQueryProcess.jsp"+param;
	}if((type=="DBLHDWQuery")||(type=="CDBLHDWQuery")){
		param = "DataQuery/LHDWQueryProcess.jsp"+param;
	}
	
		
	param = encodeURI(param);
	param = encodeURI(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function analysis(){
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var startFrequence = document.getElementById("startFrequence").value;
	if(startFrequence !=""){
		startFrequence = startFrequence*1000000;
	}
	var endFrequence = document.getElementById("endFrequence").value;
	if(endFrequence !=""){
		endFrequence = endFrequence*1000000;
	}
	var station = document.getElementById("station").value;
	var monitorlocation = document.getElementById("monitorlocation").value;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "?startTime="+startTime+"&endTime="+endTime+"&startFrequence="+startFrequence
	+"&endFrequence="+endFrequence+"&station="+station+"&monitorlocation="+monitorlocation+"&type="+type;
	
	if((type=="DBDPJCQuery")||(type=="CDBDPJCQuery")){
		param = "DataAnalysis/DPJCAnalysisProcess.jsp"+param;
	}if((type=="DBPDSMQuery")||(type=="CDBPDSMQuery")){
		param = "DataAnalysis/PDSMAnalysisProcess.jsp"+param;   
	}if((type=="DBXHCXQuery")||(type=="CDBXHCXQuery")){
		param = "DataAnalysis/XHCXAnalysisProcess.jsp"+param;
	}if((type=="DBLHDWQuery")||(type=="CDBLHDWQuery")){
		param = "DataAnalysis/LHDWAnalysisProcess.jsp"+param;
	}
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function repeat(){
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var startFrequence = document.getElementById("startFrequence").value;
	if(startFrequence !=""){
		startFrequence = startFrequence*1000000;
	}
	var endFrequence = document.getElementById("endFrequence").value;
	if(endFrequence !=""){
		endFrequence = endFrequence*1000000;
	}
	var station = document.getElementById("station").value;
	var monitorlocation = document.getElementById("monitorlocation").value;
	var parameter = document.getElementById("parameter").checked;/*选中为1,否则为0*/
	var original = document.getElementById("original").checked;
	var graphic = document.getElementById("graphic").checked;
	var audio = document.getElementById("audio").checked;
	var video = document.getElementById("video").checked;
	var type = document.getElementById("type").value;
	//var MD5=doucument.getElementById("MD5").value;
	
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "?startTime="+startTime+"&endTime="+endTime+"&startFrequence="+startFrequence
	+"&endFrequence="+endFrequence+"&station="+station+"&monitorlocation="+monitorlocation
	+"&parameter="+parameter+"&original="+original+"&graphic="+graphic+"&audio="+audio+"&video="+video+"&type="+type;
	if((type=="DBDPJCQuery")||(type=="CDBDPJCQuery")){
		param = "DataQuery/DPJCRepeatProcess.jsp"+param;
	}if((type=="DBPDSMQuery")||(type=="CDBPDSMQuery")){
		param = "DataQuery/PDSMRepeatProcess.jsp"+param;   
	}if((type=="DBXHCXQuery")||(type=="CDBXHCXQuery")){
		param = "DataQuery/XHCXRepeatProcess.jsp"+param;
	}if((type=="DBLHDWQuery")||(type=="CDBLHDWQuery")){
		param = "DataQuery/LHDWRepeatProcess.jsp"+param;
	}
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
	/*拿到type，DPJC
	使用异步打开新的jsp页面
	在jsp页面调用java函数，函数对MD5查重，重复的删除
	返回结果
	*/
}




function queryJCBG(){
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var startFrequence = document.getElementById("startFrequence").value;
	if(startFrequence !=""){
		startFrequence = startFrequence*1000000;
	}
	var endFrequence = document.getElementById("endFrequence").value;
	if(endFrequence !=""){
		endFrequence = endFrequence*1000000;
	}
	var keyWords = document.getElementById("keyWords").value;
	var station = document.getElementById("station").value;
	
	var monitorlocation = document.getElementById("monitorlocation").value;
	var author = document.getElementById("author").value;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "DataQuery/JCBGQueryProcess.jsp?startTime="+startTime+"&endTime="+endTime+"&startFrequence="+startFrequence
	+"&endFrequence="+endFrequence+"&keyWords="+keyWords+"&station="+station+"&monitorlocation="+monitorlocation
	+"&type="+type+"&author="+author;
	//alert(param);	
	param = encodeURI(param);
	param = encodeURI(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function queryJCBG_Sat(){
	
	var reportType = document.getElementById("reportType").value;
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	/*
	var startFrequence = document.getElementById("startFrequence").value;
	var endFrequence = document.getElementById("endFrequence").value;
	*/
	var keyWords = document.getElementById("keyWords").value;
	var station = document.getElementById("station").value;
	var monitorlocation = document.getElementById("monitorlocation").value;
	var author = document.getElementById("author").value;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "DataQuery/JCBGQueryProcess_Sat.jsp?startTime="+startTime+"&endTime="+endTime+"&reportType="+reportType+"&author="+author
	+"&keyWords="+keyWords+"&station="+station+"&monitorlocation="+monitorlocation+"&type="+type;
		
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}
function queryZFQJC(){
	
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var startPosition = document.getElementById("startPosition").value;
	var endPosition = document.getElementById("endPosition").value;
	
	var country = document.getElementById("country").value;
	var frequence = document.getElementById("frequence").value;
	
	var station = document.getElementById("station").value;
	var author = document.getElementById("author").value;
	
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
			//document.getElementById("batchDownload").style.display="inline";
		}
	};
	var param = "DataQuery/ZFQJCQueryProcess.jsp?startTime="+startTime+"&endTime="+endTime+"&startPosition="+startPosition+"&endPosition="+endPosition
	+"&country="+country+"&frequence="+frequence+"&station="+station+"&author="+author+"&type="+type;
		
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function analysisJCBG(){
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "?startTime="+startTime+"&endTime="+endTime+"&type="+type;
	
	param = "DataAnalysis/JCBGAnalysisProcess.jsp"+param;
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function analysisJCBG_Sat(){
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var type = document.getElementById("type").value;
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			document.getElementById("response").innerHTML="";
			document.getElementById("response").innerHTML=xmlhttp.responseText;
		}
	};
	var param = "?startTime="+startTime+"&endTime="+endTime+"&type="+type;
	
	if(type=="WXJCBGQuery"){
		param = "DataAnalysis/JCBGAnalysisProcess_Sat.jsp"+param;
	}else{
		return;
	}
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	xmlhttp.open("GET",param ,true);
	xmlhttp.send();
}

function modifyData(bh,type){
	if(type == "WXZFQJCQuery"){
	
		var param="/rsui/DataModify/ModifyZFQJCForm.jsp?bh="+bh+ "&type=" + type ;
	}else{
		
		var param="/rsui/DataModify/ModifySignalForm.jsp?bh="+bh+ "&type=" + type ;
	}
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=850px,height=600px,top='+(screen.height-600)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}
function modifyOrigianlData(bh,type){
	
		
	var param="/rsui/DataModify/ModifyOriginalForm.jsp?bh="+bh+ "&type=" + type ;
	
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=750px,height=400px,top='+(screen.height-400)/2+',left='+(screen.width-750)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}
function modifyOriginalProcess(bh,type){
	//alert(bh);
	var station= document.getElementById("station").value;
	var monitor= document.getElementById("monitor").value;
	var monitorLocation= document.getElementById("monitorLocation").value;
	var businessType = document.getElementById("businessType").value;
	var businessName = document.getElementById("businessName").value;
	
	var param = "/rsui/DataModify/ModifyOriginalProcess.jsp?";   
	
	param = param + "station=" + station;
	param = param + "&monitor=" + monitor;
	param = param + "&monitorLocation=" + monitorLocation;
	param = param + "&type=" + type;
	param = param + "&bh=" + bh
	+"&businessType="+businessType+"&businessName="+businessName;
    //alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
    //alert(param);
    
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
	if(str=="true")
		alert("修改成功。");
	else
		alert("修改失败。");
}


function modifyGraphicData(bh,type,grapType){
	if((type=="DBPDSMQuery")||(type=="CDBPDSMQuery")){
		var param="/rsui/DataModify/ModifyGraphicForm_PDSM.jsp?bh="+bh+ "&type=" + type+ "&grapType=" + grapType ;
	}else{
		var param="/rsui/DataModify/ModifyGraphicForm.jsp?bh="+bh+ "&type=" + type+ "&grapType=" + grapType ;
	}
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=850px,height=400px,top='+(screen.height-400)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}

function modifyGraphicProcess(bh,type){
	
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

	
	if(frequence == ""){
		alert("请输入中心频率。");
		return;
	}
	if(startTime == ""){
		alert("请输入监测时间。");
		return;
	}
	var param;   
	
	
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	param = "type=" + type + "&graphicType=" + graphicType + "&startTime=" + startTime +  "&endTime=" + endTime;
	param = param + "&frequence=" + frequence + "&station=" + station + "&monitor=" + monitor + "&monitorLocation=" + monitorLocation
	+"&businessType="+businessType+"&businessName="+businessName+"&signalType="+signalType
	+"&longitude="+longitude+"&latitude="+latitude + "&bh=" + bh+ "&grapType=" + grapType;
	
	param = "/rsui/DataModify/ModifyGraphicProcess.jsp?" + param;
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	alert(str);
}

function modifyGraphicProcess_PDSM(bh,type){
	
	var grapType = document.getElementById("grapType").value;//原图形类型
	var graphicType = document.getElementById("graphicType").value;//修改后的图形类型
	var startTime = document.getElementById("startTime").value;
	var endTime = startTime;
	var startFrequence = document.getElementById("startFrequence").value;
	var endFrequence = document.getElementById("endFrequence").value;
	var station = document.getElementById("station").value;
	var monitor = document.getElementById("monitor").value;
	var monitorLocation = document.getElementById("monitorLocation").value;
	
	var businessType = document.getElementById("businessType").value;
	var businessName = document.getElementById("businessName").value;
	var signalType = document.getElementById("signalType").value;
	var longitude = document.getElementById("longitude").value;
	var latitude = document.getElementById("latitude").value;
	if(startFrequence == ""){
		alert("请输入起始频率。");
		return;
	}
	if(endFrequence == ""){
		alert("请输入终止频率。");
		return;
	}
	if(startTime == ""){
		alert("请输入监测时间。");
		return;
	}
	var param;   
	
	
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	param = "type=" + type + "&graphicType=" + graphicType + "&startTime=" + startTime +  "&endTime=" + endTime;
	param = param + "&startFrequence=" + startFrequence + "&endFrequence=" + endFrequence + "&station=" + station + "&monitor=" + monitor + "&monitorLocation=" + monitorLocation
	+"&businessType="+businessType+"&businessName="+businessName+"&signalType="+signalType
	+"&longitude="+longitude+"&latitude="+latitude + "&bh=" + bh+ "&grapType=" + grapType;
	
	param = "DataModify/ModifyGraphicProcess_PDSM.jsp?" + param;
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	alert(str);
}


function modifyAudioData(bh,type){
	
		
	var param="/rsui/DataModify/ModifyAudioForm.jsp?bh="+bh+ "&type=" + type ;
	
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=850px,height=400px,top='+(screen.height-400)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}
function modifyAudioProcess(bh,type){
	
	var startTime= document.getElementById("startTime").value;
	var endTime= document.getElementById("endTime").value;
	var frequence= document.getElementById("frequence").value;
	var signalType= document.getElementById("signalType").value;
	var longitude= document.getElementById("longitude").value;
	var latitude= document.getElementById("latitude").value;
	
	var businessType= document.getElementById("businessType").value;
	var businessName= document.getElementById("businessName").value;
	var monitorLocation= document.getElementById("monitorLocation").value;
	var station = document.getElementById("station").value;
	var monitor = document.getElementById("monitor").value;
	if(frequence == ""){
		alert("请输入中心频率。");
		return;
	}
	if(startTime == ""){
		alert("请输入监测起始时间。");
		return;
	}
	if(endTime == ""){
		alert("请输入监测终止时间。");
		return;
	}
	var param = "DataModify/ModifyAudioProcess.jsp?";
	param = param + "startTime=" + startTime;
	param = param + "&endTime=" + endTime;
	param = param + "&frequence=" + frequence;
	param = param + "&signalType=" + signalType;
	param = param + "&longitude=" + longitude;
	param = param + "&latitude=" + latitude;
	
	param = param + "&station=" + station;
	param = param + "&monitor=" + monitor;
	param = param + "&monitorLocation=" + monitorLocation;
	param = param + "&bh=" + bh;
	param = param + "&type=" + type
	+"&businessType="+businessType+"&businessName="+businessName;
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	if(str=="true")
		alert("修改成功。");
	else
		alert("修改失败。");
	
}
function modifyVideoData(bh,type){
	
	if((type=="DBLHDWQuery")||(type=="CDBLHDWQuery")){
		var param="/rsui/DataModify/ModifyVideoForm_LHDW.jsp?bh="+bh+ "&type=" + type;
	}else{
		var param="/rsui/DataModify/ModifyVideoForm.jsp?bh="+bh+ "&type=" + type;
	}
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=850px,height=400px,top='+(screen.height-400)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');	
}
function modifyVideoProcess_LHDW(bh,type){
	
	var startTime= document.getElementById("startTime").value;
	var endTime= document.getElementById("endTime").value;
	var frequence= document.getElementById("frequence").value;
	
	var signalType= document.getElementById("signalType").value;
	var longitude= document.getElementById("longitude").value;
	var latitude= document.getElementById("latitude").value;
	
	var businessType= document.getElementById("businessType").value;
	var businessName= document.getElementById("businessName").value;
	var monitorLocation= document.getElementById("monitorLocation").value;
	var station = document.getElementById("station").value;
	var monitor = document.getElementById("monitor").value;

	if(frequence == ""){
		alert("请输入中心频率。");
		return;
	}
	if(startTime == ""){
		alert("请输入监测起始时间。");
		return;
	}
	if(endTime == ""){
		alert("请输入监测终止时间。");
		return;
	}
	var param;  
	var xmlhttp;
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	param = "DataModify/ModifyVideoProcess_LHDW.jsp?";
	param = param + "startTime=" + startTime;
	param = param + "&endTime=" + endTime;
	param = param + "&frequence=" + frequence;
	
	param = param + "&signalType=" + signalType;
	param = param + "&longitude=" + longitude;
	param = param + "&latitude=" + latitude;
	
	param = param + "&station=" + station;
	param = param + "&monitor=" + monitor;
	param = param + "&monitorLocation=" + monitorLocation;
	param = param + "&bh=" + bh;
	param = param + "&type=" + type
	+"&businessType="+businessType+"&businessName="+businessName;
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	alert(str);
}
function modifyVideoProcess(bh,type){
	
	var startTime= document.getElementById("startTime").value;
	var endTime= document.getElementById("endTime").value;
	var frequence= document.getElementById("frequence").value;
	var startFrequence= document.getElementById("startFrequence").value;
	var endFrequence= document.getElementById("endFrequence").value;
	var signalType= document.getElementById("signalType").value;
	var longitude= document.getElementById("longitude").value;
	var latitude= document.getElementById("latitude").value;
	
	var businessType= document.getElementById("businessType").value;
	var businessName= document.getElementById("businessName").value;
	var monitorLocation= document.getElementById("monitorLocation").value;
	var station = document.getElementById("station").value;
	var monitor = document.getElementById("monitor").value;

	
	if(startFrequence == ""){
		alert("请输入起始频率。");
		return;
	}
	if(endFrequence == ""){
		alert("请输入终止频率。");
		return;
	}
	if(frequence == ""){
		alert("请输入中心频率。");
		return;
	}			
	if(startTime == ""){
		alert("请输入监测起始时间。");
		return;
	}
	if(endTime == ""){
		alert("请输入监测终止时间。");
		return;
	}
	var param; 
	var xmlhttp;
	
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		param = "DataModify/ModifyVideoProcess.jsp?";
		param = param + "startTime=" + startTime;
		param = param + "&endTime=" + endTime;
		param = param + "&frequence=" + frequence;
		param = param + "&startFrequence=" + startFrequence;
		param = param + "&endFrequence=" + endFrequence;
		param = param + "&signalType=" + signalType;
		param = param + "&longitude=" + longitude;
		param = param + "&latitude=" + latitude;
		
		param = param + "&station=" + station;
		param = param + "&monitor=" + monitor;
		param = param + "&monitorLocation=" + monitorLocation;
		param = param + "&bh=" + bh;
		param = param + "&type=" + type
		+"&businessType="+businessType+"&businessName="+businessName;
		//alert(param);
		param = encodeURI(param);
		param = encodeURI(param);
		//alert(param);
		xmlhttp.open("GET",param, false);
		xmlhttp.send();
		
		var str = xmlhttp.responseText;
		str = str.replace(/[\r\n]/g, "");//删除换行符
		str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
		alert(str);
}

function modifyJCBGData(bh,type){
	if(type == "WXJCBGQuery"){
	
		var param="/rsui/DataModify/ModifyJCBGForm_Sat.jsp?bh="+bh+ "&type=" + type ;
	}else{
		
		var param="/rsui/DataModify/ModifyJCBGForm.jsp?bh="+bh+ "&type=" + type ;
	}
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=850px,height=600px,top='+(screen.height-600)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}
function modifyJCBGProcess(bh,type){
	
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
	if(startFrequence == ""){
		alert("请输入中心频率。");
		return;
	}
	
	if(startTime == ""){
		alert("请输入起始时间。");
		return;
	}
	if(endTime == ""){
		alert("请输入终止时间。");
		return;
	}
	var param;
	var xmlhttp;
	param ="type="+type+ "&reportName="+reportName+ "&reportType="+reportType+ "&startFrequence="+startFrequence+
	 "&endFrequence="+endFrequence+ "&startTime="+startTime+ "&endTime="+endTime+ "&keyWords="+keyWords+ 
	"&station="+station + "&author=" + author +"&monitorLocation="+monitorLocation+ "&equipment=" + equipment
	+"&businessName="+businessName+"&businessType="+businessType + "&bh=" + bh;
			  	
    param = "DataModify/ModifyJCBGProcess.jsp?" + param;
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	alert(str);
}

function modifyJCBGProcess_Sat(bh,type){
	
	var reportName = document.getElementById("reportName").value;
	var reportType = document.getElementById("reportType").value;
	
	var startTime = document.getElementById("startTime").value;
	var endTime = document.getElementById("endTime").value;
	var keyWords = document.getElementById("keyWords").value;
	var station = document.getElementById("station").value;
	var author = document.getElementById("author").value;
	var monitorLocation = document.getElementById("monitorLocation").value;
	var equipment = document.getElementById("equipment").value;
	var businessName = document.getElementById("businessName").value;
	var businessType = document.getElementById("businessType").value;
	
	
	if(startTime == ""){
		alert("请输入起始时间。");
		return;
	}
	
	if(endTime == ""){
		alert("请输入终止时间。");
		return;
	}
	var param;
	var xmlhttp;
	param ="type="+type+ "&reportName="+reportName+ "&reportType="+reportType+ "&startTime="+startTime
	+ "&endTime="+endTime+ "&keyWords="+keyWords+ 
	"&station="+station + "&author=" + author +"&monitorLocation="+monitorLocation+ "&equipment=" + equipment
	+"&businessName="+businessName+"&businessType="+businessType + "&bh=" + bh;
			  	
    param = "DataModify/ModifyJCBGProcess_Sat.jsp?" + param;
	
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open("GET",param, false);
	xmlhttp.send();
	
	var str = xmlhttp.responseText;
	str = str.replace(/[\r\n]/g, "");//删除换行符
	str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
	alert(str);
}

function modifyParameter(bh,type){
	
	//alert("modifyParamter");
	var frequence = document.getElementById("frequence").value;
	var bandWidth = document.getElementById("bandWidth").value;
	var modulate = document.getElementById("modulate").value;
	var longitude = document.getElementById("longitude").value;
	var latitude = document.getElementById("latitude").value;
	var monitorTime = document.getElementById("monitorTime").value;
	
	var businessType = document.getElementById("businessType").value;
	var launchTime = document.getElementById("launchTime").value;
	var levels = document.getElementById("levels").value;
	var testNumber = document.getElementById("testNumber").value;
	var occupancy = document.getElementById("occupancy").value;
	var threshold = document.getElementById("threshold").value;
	
	var direction = document.getElementById("direction").value;
	var monitoringStation = document.getElementById("Station").value;
	
	var noise = document.getElementById("noise").value;
	var compared = document.getElementById("compared").value;
	var comments = document.getElementById("comments").value;
	
	var businessName = document.getElementById("businessName").value;
	var signalType = document.getElementById("signalType").value;
	var city = document.getElementById("city").value;
	var monitor = document.getElementById("monitor").value;
	var monitorLocation = document.getElementById("monitorLocation").value;
	if(frequence == ""){
		alert("请输入频率。");
		return;
	}
	if(bandWidth == ""){
		alert("请输入带宽。");
		return;
	}
	
	var param = "/rsui/DataModify/ModifySignalParameterRegProcess.jsp?";   
	param = param + "frequence=" + frequence;
	param = param + "&bandWidth=" + bandWidth;
	param = param + "&modulate=" + modulate;
	param = param + "&longitude=" + longitude;
	param = param + "&latitude=" + latitude;
	param = param + "&monitorTime=" + monitorTime;
	
	param = param + "&businessType=" + businessType;
	param = param + "&launchTime=" + launchTime;
	param = param + "&levels=" + levels;
	param = param + "&testNumber=" + testNumber;
	param = param + "&occupancy=" + occupancy;
	param = param + "&threshold=" + threshold;
	
	param = param + "&direction=" + direction;
	param = param + "&monitoringStation=" + monitoringStation;
	
	param = param + "&noise=" + noise;
	param = param + "&compared=" + compared;
	param = param + "&comments=" + comments;
	param = param + "&type=" + type;
	param = param + "&businessName=" + businessName;
	param = param + "&signalType=" + signalType;
	param = param + "&city=" + city;
	param = param + "&monitor=" + monitor;
	param = param + "&monitorLocation=" + monitorLocation;
	param = param + "&bh=" + bh;
	
	param = encodeURI(param);
	param = encodeURI(param);
    //alert(param);
    
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
	if(str=="true")
		alert("修改成功。");
	else
		alert("修改失败。");
	
}

function modifyZFQJC(bh,type){
	
	//alert(bh+"/"+type);
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
	
	
	var param = "/rsui/DataModify/ModifyZFQJCProcess.jsp?";   
	param = param + "position=" + position;
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
	param = param + "&type=" + type;
	param = param + "&bh=" + bh
	+"&businessType="+businessType+"&businessName="+businessName
	+"&zfq="+zfq+"&antenna="+antenna
	+"&weaken="+weaken+"&comments="+comments;
    //alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
    //alert(param);
    
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
	if(str=="true")
		alert("导入成功。");
	else
		alert("导入失败。");
	
}

function launchTime(bh,type){
	var param = "/rsui/DataQuery/LaunchTime.jsp?bh=" + bh + "&type=" + type;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','resizable=yes,width=500px,height=400px,top='+(screen.height-400)/2+',left='+(screen.width-500)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}
/**
 * 导出文件到客户端
 * @param bh
 * @param type
 * @param dataType
 */
function exportFile(bh,type,dataType){
	
	var param = "/rsui/DataQuery/ExportServlet?bh=" + bh + "&type=" + type + "&dataType=" + dataType;
	param = encodeURI(param);
	param = encodeURI(param);
	window.location.href = param;
}

function exportFileFrequence(bh,type,frequence){
	
	var param = "/rsui/DataQuery/ExportFile.jsp?bh=" + bh + "&type=" + type + "&frequence=" + frequence;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','resizable=yes,width=500px,height=250px,top='+(screen.height-250)/2+',left='+(screen.width-500)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}

function deleteData(bh,type,dataType){
	
	if(!confirm("确认删除数据？")){
		return;
	};
	//alert(bh+type);
	var param = "DataQuery/DeleteData.jsp?bh=" + bh + "&type=" + type + "&dataType=" + dataType;
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
	if(xmlhttp.responseText.trim()=="true")
		alert("删除成功。");
	else
		alert("删除失败。");
	
	
	if((type.indexOf("DBJCBG")!=-1)){//如果来自短波和超短波监测报告页面的调用，执行queryJCBG()进行刷新
		queryJCBG();
	}else if((type.indexOf("WXJCBG")!=-1)){//如果来自卫星监测报告页面的调用，执行queryJCBG()进行刷新
		queryJCBG_Sat();
	}else if((type.indexOf("WXZFQJC")!=-1)){//如果来自转发器监测报告页面的调用，执行queryJCBG()进行刷新
		queryZFQJC();
	}else{
		query();
	}
}
/*md5码查重页面的删除,要执行的刷新函数不同*/
function deleteData1(bh,type,dataType){
	
	if(!confirm("确认删除数据？")){
		return;
	}
	//alert(bh+type);
	var param = "DataQuery/DeleteData.jsp?bh=" + bh + "&type=" + type + "&dataType=" + dataType;
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
	if(xmlhttp.responseText.trim()=="true")
		alert("删除成功。");
	else
		alert("删除失败。");
	
	
	if((type.indexOf("JCBG")!=-1)){/*如果type包含“JCBG",进入if语句*/
		queryJCBG();/*如果来自监测报告页面的调用，执行queryJCBG()进行刷新*/
	}else{
		repeat();
	}
}

function Graphic(bh,type,grapType){
	
	var param="/rsui/DataQuery/Graphic.jsp?bh="+bh+ "&type=" + type + "&grapType=" + grapType ;
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width='+(screen.width)/2+',height='+(screen.height)/2+',top='+(screen.height)/4+',left='+(screen.width)/4+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
}

function Audioplay(bh,type){
	
	var param="/rsui/DataQuery/Audioplay.jsp?bh="+bh+ "&type=" + type ;
	var IE = isIE();
	param = param + "&IE="+IE;
	//alert(param);
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','width=320,height=80,resizable=yes,top='+(screen.height)/4+',left='+(screen.width-300)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
}
function Videoplay(bh,type){
	
	var param="/rsui/DataQuery/Videoplay.jsp?bh="+bh+ "&type=" + type ;
	var IE = isIE();
	param = param + "&IE="+IE;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','width=830,height=580,resizable=yes,top='+(screen.height)/4+',left='+(screen.width)/4+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
}

function isIE() { //ie?
	 if (!!window.ActiveXObject || "ActiveXObject" in window){
	  return true;
	 }else{
	  return false;
	 }
 }


function dpjcOccupancy(bh,type){
	
	var param = "/rsui/DataQuery/DPJCOccupancy.jsp?bh=" + bh + "&type=" + type;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','width=400,height=400,resizable=yes,top='+(screen.height-330)/2+',left='+(screen.width-300)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}

function pdsmOccupancy(bh,type){
	
	var param = "/rsui/DataQuery/PDSMOccupancy.jsp?bh=" + bh + "&type=" + type;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','width=350,height=650,top='+(screen.height-620)/2+',left='+(screen.width-350)/2+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}
/*联合定位无占用度，已弃用*/
function lhdwOccupancy(bh,type){
	
	var param = "/rsui/Occupancy/LHDWOccupancy.jsp?bh=" + bh + "&type=" + type;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','width=300,height=250,resizable=yes,top='+(screen.height)/4+',left='+(screen.width-300)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}




function LHDWOccupancyProcess(bh,type){
	
	var minThreshold=document.getElementById("minThreshold").value;
	var threshold=document.getElementById("threshold").value;
	var minThresholdDefault=document.getElementById("minThreshold").placeholder//缺省值
	var thresholdDefault=document.getElementById("threshold").placeholder//缺省值
	if(minThreshold==""){
		minThreshold = minThresholdDefault;
	}
	if(threshold==""){
		threshold = thresholdDefault;
	}
	
	//alert(bh + type + minThreshold + threshold+thresholdDefault);
	
	var param = "/rsui/Occupancy/LHDWOccupancyProcess.jsp?bh=" + bh + "&type=" + type + "&minThreshold=" + minThreshold+ "&threshold=" + threshold;
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

}

function dpjcPlay(bh,type,speed){
	
	var param = "/rsui/DataQuery/DPJCPlay.jsp?bh=" + bh + "&type=" + type+ "&speed=" + speed;
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','width='+(screen.width)*3/4+',height='+(screen.height)*3/4+',left='+(screen.width)/8+',top='+(screen.height)/8+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}

function pdsmPlay(bh,type,speed){
	
	var param = "/rsui/DataQuery/PDSMPlay.jsp?bh=" + bh + "&type=" + type+ "&speed=" + speed;
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','width='+(screen.width)*3/4+',height='+(screen.height)*3/4+',left='+(screen.width)/8+',top='+(screen.height)/8+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}

function pdsmFreqPlay(bh,type,speed){
	
	var param = "/rsui/DataPlay/PDSMFreqPlayProcess.jsp?bh=" + bh + "&type=" + type + "&frequence=0&speed=" + speed;
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','width='+(screen.width)*3/4+',height='+(screen.height)*3/4+',left='+(screen.width)/8+',top='+(screen.height)/8+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	
}

function getRGB(bh){
	//alert("getRGB");
	var param = "/rsui/ImageRGB/GETRGB.jsp?bh=" + bh;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','resizable=yes,width=900px,height=750px,top=0,left='+(screen.width-900)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
}
/**
 * zfqjc数据批量下载
 */
function downloadZFQJC(){
	var infoTable = document.getElementById("infoTable");
	if(!infoTable){
		alert("请先进行查询。");
	}
	var rowsLength = infoTable.rows.length;
	var check;
	var bh = new Array();
	var temp;
	for(var i=1;i<rowsLength;i++){ //第一行是th标题
		check = infoTable.rows[i].cells[1].getElementsByTagName("input")[0].checked;
		if(check == "1"){
			temp = infoTable.rows[i].cells[1].getElementsByTagName("input")[1].value;//得到bh
			bh.push(temp);
		}
	}
	if(bh.length ==0){
		alert("请选择要导出的数据。");
		return;
	}
	var allbh = bh.join(",");
	var param = "/rsui/DataDownload/DownloadZFQJC.jsp?allbh="+allbh;
	window.location.href=param;
}
/**
 * 信号参数登记批量下载
 */
function downloadParameter(type){
	var infoTable = document.getElementById("infoTable");
	if(!infoTable){
		alert("请先进行查询。");
	}
	var rowsLength = infoTable.rows.length;
	var check;
	var bh = new Array();
	var temp;
	for(var i=1;i<rowsLength;i++){ //第一行是th标题
		check = infoTable.rows[i].cells[1].getElementsByTagName("input")[0].checked;
		if(check == "1"){
			temp = infoTable.rows[i].cells[1].getElementsByTagName("input")[1].value;//得到bh
			bh.push(temp);
		}
	}
	if(bh.length ==0){
		alert("请选择要导出的数据。");
		return;
	}
	var allbh = bh.join(",");
	var param = "/rsui/DataDownload/DownloadParameter.jsp?allbh="+allbh+"&type="+type;
	window.location.href=param;
}
/**
 * 批量导出选择框全选功能
 */
function checkbox(){
	//alert("o");
	var infoTable = document.getElementById("infoTable");
	var rowsLength = infoTable.rows.length;
	//alert(rowsLength);
	var temp;
	if(infoTable.rows[0].cells[1].getElementsByTagName("input")[0].checked=="1"){
		temp = "checked";
	}else{
		temp = "";
	}
	for(var i=1;i<rowsLength;i++){ //第一行是th标题
		infoTable.rows[i].cells[1].getElementsByTagName("input")[0].checked=temp;
	}
	
}
