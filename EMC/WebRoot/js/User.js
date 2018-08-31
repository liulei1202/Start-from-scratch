
function UserRegProcess(){
	
	var id = document.getElementById("UserId").value;
	var pw = document.getElementById("UserPassword").value; 
	var pwcf= document.getElementById("UserPasswordConFirm").value;
	var realname = document.getElementById("UserName").value;
	var unitname = document.getElementById("UnitName").value;
	var telephone = document.getElementById("Telephone").value;
	if(id==""){
		
		alert("请输入用户名");
		return;
	}
	if(/.*[\u4e00-\u9fa5]+.*$/.test(id)){
	  
		alert("不能含有汉字！");  
		return;  
	}

	if(pw==""){
		
		alert("请输入密码");
		return;
	}
	if (pw != pwcf){
		alert("两次输入密码不一致");
		return;
	}
	if (realname == ""){
		alert("请输入真实姓名");
		return;
	}
	var reg = new RegExp("[u4E00-u9FFF]+","g");
	　　if(reg.test(realname)){     
		alert("真实姓名必须为汉字");
		return;
	　　}
	
	if (unitname == ""){
		alert("请输入单位名称");
		return;
	}
	if (telephone == ""){
		alert("请输入联系电话");
		return;
	}
	tempString = document.getElementById("UserId").value;
	//alert(tempString);
	var param = "UserId=" + tempString;
	
	tempString  = document.getElementById("UserPassword").value;
	param = param + "&UserPassword="+tempString;
	
	tempString  = document.getElementById("UserName").value;
	param = param + "&UserName="+tempString;

	tempString = document.getElementById("UnitName").value;
	param = param + "&UnitName="+tempString;
	
	tempString = document.getElementById("Telephone").value;
	param = param + "&Telephone="+tempString;
	
	tempString = document.getElementById("UserRole").value;
	param = param + "&UserRole="+tempString;
	
	if(document.getElementById("UserRole").value == "台站"){
		tempString = document.getElementById("StationType").value;
	}else{
		tempString = "";
	}
	//alert(document.getElementById("StationType").value);
	param = param + "&StationType="+tempString;
	param = "UserRegistryProcess.jsp?"+param;
	//alert(param);
	
	param = encodeURI(param);
	param = encodeURI(param);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}
	else  {// code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
    		document.getElementById("response").innerHTML=xmlhttp.responseText;
    	}	
    };
    
	//xmlhttp.open("GET","UserRegistryProcess.jsp",true);
    xmlhttp.open("GET",param,true);
	xmlhttp.send();
	
}
