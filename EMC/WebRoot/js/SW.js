/*
str = str.replace(/[\r\n]/g, "");//删除换行符
str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
 */
function signalParameterReg(){
  
	var result = true;
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	var type = document.getElementById("type").value;
	//列
    var cellNums = document.getElementById("info").rows[0].cells.length;
    
    //行
    var rowNums = document.getElementById("info").rows.length;  
    
    
    for(var m=1;m<rowNums;m++){
    	var frequence = document.getElementById("info").rows[m].cells[0].getElementsByTagName("input")[0].value;
    	var monitoringStation= document.getElementById("info").rows[m].cells[1].getElementsByTagName("input")[0].value;
    	var bandWidth = document.getElementById("info").rows[m].cells[2].getElementsByTagName("input")[0].value;
    	var modulate = document.getElementById("info").rows[m].cells[3].getElementsByTagName("input")[0].value;
    	var businessType= document.getElementById("info").rows[m].cells[4].getElementsByTagName("input")[0].value;
    	var businessName= document.getElementById("info").rows[m].cells[5].getElementsByTagName("input")[0].value;
    	
    	var signalType= document.getElementById("info").rows[m].cells[6].getElementsByTagName("input")[0].value;
    	var launchTime= document.getElementById("info").rows[m].cells[7].getElementsByTagName("input")[0].value;
    	var levels= document.getElementById("info").rows[m].cells[8].getElementsByTagName("input")[0].value;
    	var testNumber= document.getElementById("info").rows[m].cells[9].getElementsByTagName("input")[0].value;
    	var noise= document.getElementById("info").rows[m].cells[10].getElementsByTagName("input")[0].value;
    	var occupancy= document.getElementById("info").rows[m].cells[11].getElementsByTagName("input")[0].value;
    	
    	var threshold= document.getElementById("info").rows[m].cells[12].getElementsByTagName("input")[0].value;
    	var direction= document.getElementById("info").rows[m].cells[13].getElementsByTagName("input")[0].value;
    	var longitude = document.getElementById("info").rows[m].cells[14].getElementsByTagName("input")[0].value;
    	var latitude = document.getElementById("info").rows[m].cells[15].getElementsByTagName("input")[0].value;
    	var city= document.getElementById("info").rows[m].cells[16].getElementsByTagName("input")[0].value;
    	var compared= document.getElementById("info").rows[m].cells[17].getElementsByTagName("input")[0].value;
    	
    	var monitorTime= document.getElementById("info").rows[m].cells[18].getElementsByTagName("input")[0].value;
    	var monitor= document.getElementById("info").rows[m].cells[19].getElementsByTagName("input")[0].value;
    	var monitorLocation= document.getElementById("info").rows[m].cells[20].getElementsByTagName("input")[0].value;
    	var comments= document.getElementById("info").rows[m].cells[21].getElementsByTagName("input")[0].value;
    	
		if(frequence == ""){
			alert("请输入频率。");
			return;
		}
		if(bandWidth == ""){
			alert("请输入带宽。");
			return;
		}
    	//alert(document.getElementById("info").rows[m].cells[28].getElementsByTagName("input")[0].value);
    	//alert("kong");
	var param = "/rsui/DataImport/SignalParameterRegProcess.jsp?";   
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
	param = param + "&businessName=" + businessName;
	
	param = param + "&signalType=" + signalType;
	param = param + "&monitor=" + monitor;
	param = param + "&monitorLocation=" + monitorLocation;
	param = param + "&city=" + city;
	param = param + "&type=" + type;
    
	param = encodeURI(param);
	param = encodeURI(param);
    //alert(param);
	xmlhttp.onreadystatechange=function() {   
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
			var str = xmlhttp.responseText;//成功返回bh，失败返回false
			str = str.replace(/[\r\n]/g, "");//删除换行符
			str = str.replace(/(^\s*)|(\s*$)/g, "");//删除左右两边空格
			//alert(str);
			if(str == "false"){
				result = false;
			}else{
				document.getElementById("info").rows[m].cells[28].getElementsByTagName("input")[0].value = str;//替换掉隐藏的bh值
				//alert(document.getElementById("info").rows[m].cells[28].getElementsByTagName("input")[0].value);
			}
    		//document.getElementById("response1").innerHTML=xmlhttp.responseText;
    	}
    };
	xmlhttp.open("GET",param,false);
	xmlhttp.send();
    }
    if(result){
    	alert("上传成功。");
    }else{
    	alert("上传失败。");
    }
}

function grapInPara(grapType){
	
	
	var e = arguments.callee.caller.arguments[0] || window.event;
	var target = e.target||e.srcElement;
	var rowIndex = target.parentNode.parentNode.rowIndex;//获取行号
	//alert(rowIndex);
	var bh = document.getElementById("info").rows[rowIndex].cells[28].getElementsByTagName("input")[0].value
	//alert(bh);
	if(bh == ""){
		alert("请先输入信号参数登记数据并提交。");
		return;
	}
	var type = document.getElementById("type").value;
	var param="/rsui/SignalParameterReg/ImportGrap.jsp?bh="+bh+ "&type=" + type+ "&grapType=" + grapType ;
	param = encodeURI(param);
	param = encodeURI(param);
	//window.open(param);
	window.open(param,	'_blank','resizable=yes,width=500px,height=250px,top='+(screen.height)/4+',left='+(screen.width)/4+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

}


//已无用
function ZFQJCData(){
  
	
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	type = document.getElementById("type").value;
	//列
    var cellNums = document.getElementById("info").rows[0].cells.length;
    
    //行
    var rowNums = document.getElementById("info").rows.length;  
    
    
    for(var m=1;m<rowNums;m++){
    	position = document.getElementById("info").rows[m].cells[0].getElementsByTagName("input")[0].value;
    	satName = document.getElementById("info").rows[m].cells[1].getElementsByTagName("input")[0].value;
    	country = document.getElementById("info").rows[m].cells[2].getElementsByTagName("input")[0].value;
    	rbw = document.getElementById("info").rows[m].cells[3].getElementsByTagName("input")[0].value;
    	vbw = document.getElementById("info").rows[m].cells[4].getElementsByTagName("input")[0].value;
    	level= document.getElementById("info").rows[m].cells[5].getElementsByTagName("input")[0].value;
    	frequence= document.getElementById("info").rows[m].cells[6].getElementsByTagName("input")[0].value;
    	polar= document.getElementById("info").rows[m].cells[7].getElementsByTagName("input")[0].value;
    	startFrequence= document.getElementById("info").rows[m].cells[8].getElementsByTagName("input")[0].value;
    	endFrequence= document.getElementById("info").rows[m].cells[9].getElementsByTagName("input")[0].value;
    	dataType= document.getElementById("info").rows[m].cells[10].getElementsByTagName("input")[0].value;
    	station= document.getElementById("info").rows[m].cells[11].getElementsByTagName("input")[0].value;
    	time= document.getElementById("info").rows[m].cells[12].getElementsByTagName("input")[0].value;
    	person= document.getElementById("info").rows[m].cells[13].getElementsByTagName("input")[0].value;
    	
    	
	var param = "/rsui/DataImport/ZFQJCProcess.jsp?";   
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
	param = param + "&dataType=" + dataType;
	param = param + "&station=" + station;
	
	param = param + "&time=" + time;
	param = param + "&person=" + person;
	param = param + "&type=" + type;
    
	param = encodeURI(param);
	param = encodeURI(param);
    //alert(param);
	xmlhttp.onreadystatechange=function() {   
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
    		document.getElementById("response1").innerHTML=xmlhttp.responseText;
    	}
    };
	xmlhttp.open("GET",param,false);
	xmlhttp.send();
    }
}