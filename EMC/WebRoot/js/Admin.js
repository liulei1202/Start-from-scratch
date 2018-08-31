/*用户审核跳转*/
function userConfirm(userId){
	
	var param = "/rsui/Admin/UserConfirm.jsp?userId=" + userId;
	param = encodeURI(param);
	param = encodeURI(param);
	
	window.open(param,	'_blank','height=220,width=450,top='+(screen.height-220)/2+',left='+(screen.width-400)/2+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	//window.location.href="/rsui/Admin/UserManage.jsp";
}
/*用户审核状态修改*/
function userConfirmProcess(){
	
	var userId=document.getElementById("userId").innerHTML;
	var confirm = document.getElementById("confirm").value;
	var initialValue = document.getElementById("initialValue").value;
	if(confirm != initialValue){//审核状态改变，则修改数据库，状态未改变，直接关闭页面。
		var param = "Admin/UserConfirmProcess.jsp?userId="+userId+"&confirm="+confirm;
		param = encodeURI(param);
		param = encodeURI(param);
		//alert(param);
		var xmlhttp;
		if (window.XMLHttpRequest)  {
	  		xmlhttp=new XMLHttpRequest();
		}else  {
	  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
			
		xmlhttp.onreadystatechange=function() {
			if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
				var str = xmlhttp.responseText;
				str = str.replace(/[\r\n]/g, "");//去换行符
				str = str.replace(/\s+/g,"");    //去空格
	    		if(str=="true"){
	    			alert("审核状态修改成功。");
	    			window.opener=null;
	    			window.open('','_self');
	    			window.close();
	    			
	    		}else{
	    			alert("审核状态修改失败。");
	    		}
	    	}
	    };
		xmlhttp.open("GET",param,true);
		xmlhttp.send();
		}
	else{
		window.opener=null;
		window.open('','_self');
		window.close();
	}
}


/**
 * 对增加新用户按钮的响应函数。
 * 该函数是被"UserNewForm.jsp"的表单提交按钮调用。
 * 对用户信息中的按钮响应函数是UserNewProcess.jsp
 * 当用户在表单中输入所要建立的新用户信息后，函数进行数据库插入的操作，并且将执行结果"成功/不成功"显示在response中。
 * 该函数从form表单中获取UserId,和UserType两项数据。
 */
function UserNewProcess(){
		
	var userId = document.getElementById("UserId").value;
	if(userId==""){
		
		alert("用户名不能为空。");
		return;
	}
	var userRole = document.getElementById("UserRole").value;
	var stationType = document.getElementById("StationType").value;
	var param="Admin/UserNewProcess.jsp?";
	param=param+"userId="+userId+"&userRole="+userRole+"&stationType="+stationType;
	param = encodeURI(param);
	param = encodeURI(param);
	//alert("User New Process:" + param);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
 		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
		
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200){
    		document.getElementById("response").innerHTML=xmlhttp.responseText;
    	}
    };
	xmlhttp.open("GET",param,true);
	xmlhttp.send();
}
/**
 * 对用户指定的userID表示的用户在数据库中重置密码，并且将执行结果"成功/不成功"显示在response中。
 * 重置过程是通过调用 UserPasswordResetProcess。jsp完成。
 * @param userID 需要重置密码的用户的用户标识
 * 其他用户密码重置
 */
function UserPasswordReset(userID){
	
	var param = "Admin/UserPasswordResetProcess.jsp?userID="+userID;
	param = encodeURI(param);
	param = encodeURI(param);
	//alert("User password reset.js: " + userID);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
   			document.getElementById("response").innerHTML=xmlhttp.responseText;
   		}
   	};
	xmlhttp.open("GET",param,true);
	xmlhttp.send();
}

/**
 * 对用户指定的userID表示的用户在数据库中删除，并且将执行结果"成功/不成功"显示在response中。
 * 删除过程是通过调用 UserDeleteProcess。jsp完成。
 * @param userID 需要的用户标识
 * 删除用户
 */
function UserDelete(userID){
	
	if(!confirm("确认删除用户'"+userID+"'？")){
		return;
	}
	var param = "Admin/UserDeleteProcess.jsp?userID="+userID;
	param = encodeURI(param);
	param = encodeURI(param);
	//alert("UserDelete"+userID);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
  		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
		
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
    		document.getElementById("response").innerHTML=xmlhttp.responseText;
    	}
    };
	xmlhttp.open("GET",param,true);
	xmlhttp.send();
}

/**
 * 当用户在表单中输入所要修改的密码后，函数进行数据库修改的操作，并且将执行结果"成功/不成功"显示在response中。
 * 该函数从form表单中获取两次输入的密码password1,和password2两项数据。
 * 如果两次输入的密码不一致，函数返回提示信息，要求重新输入。
 * 修改admin密码
 */
function UserPasswordModifyProcess(){
	
	var password1 = document.getElementById("password1").value;
	var password2 = document.getElementById("password2").value;
	if(password1 == ""){
		
		alert("密码不能为空。");
		return;
	}
	if (password1 != password2){
		alert("两次输入密码不一致，请重新输入。");
		document.getElementById("password1").value = "";
		document.getElementById("password2").value = "";
		return;
	}
	var userId= document.getElementById("UserId").value;
	var param = "userId="+userId +"&passWord="+password1;
	param = "Admin/UserPasswordModifyProcess.jsp?"+param;
	param = encodeURI(param);
	param = encodeURI(param);
	//alert(param);
	var xmlhttp;
	if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}else  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
   			document.getElementById("response").innerHTML=xmlhttp.responseText;
   		}
   	};
	xmlhttp.open("GET",param,true);
	xmlhttp.send();
}
