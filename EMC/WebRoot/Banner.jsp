<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% String EnvirUserId = session.getAttribute("userID").toString(); %>
<link rel="stylesheet" href="/rsui/js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
<script>
	function tempPasswordChange(EnvirUserId){	
		var ob = "/rsui/Admin/UserPasswordModifyForm.jsp?UserId=" + EnvirUserId;
		
		window.open(ob,	'_blank','width=300,height=230,top='+(screen.height)/4+',left='+(screen.width-300)/2+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	}
	
	function quit(){
	
		var param = "/rsui/DeleteFile.jsp";
		var xmlhttp;
		if (window.XMLHttpRequest)  {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}else  {// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	
		xmlhttp.onreadystatechange=function() {   
			if (xmlhttp.readyState==4 && xmlhttp.status==200)    {
				//document.getElementById("response").innerHTML="";
			}
		};
		param = encodeURI(param);
		param = encodeURI(param);
		xmlhttp.open("GET",param ,true);
		xmlhttp.send();
		
		window.location.href="/rsui/index.jsp";
	}
</script>
<style type="text/css">
	
	#Link a:link{color:#000000;}
	#Link a:visited{;}
	#Link a:hover{color:#800080;}
	#Link a:active{;}
	
	#grad {
	    background: -webkit-linear-gradient(left, #3281c3, #7cc0e0 , #3281c3); /* Safari 5.1 - 6.0 */
	    background: -o-linear-gradient(right, #3281c3, #7cc0e0, #3281c3); /* Opera 11.1 - 12.0 */
	    background: -moz-linear-gradient(right, #3281c3, #7cc0e0, #3281c3); /* Firefox 3.6 - 15 */
	    background: linear-gradient(to right, #3281c3, #7cc0e0 , #3281c3); /* 标准的语法（必须放在最后） */
	}
</style>
<div id="grad" style="height:48px;width:100%;positon:absolute;font-family:楷体;">
	<div style="float:left;height:100%;width:48px;border-right:1px solid #dddddd;">
	</div>
	<div style="float:left;padding-left:24px;height:100%;font-size:24px;line-height:48px;">
		电磁频谱监测数据综合管理与应用系统
	</div>
	<div style="float:right;height:100%;width:48px;border-left:1px solid #dddddd;">
	</div>
	<div style="float:right;height:100%;font-size:20px;line-height:48px;" id="Link">
		<a style="margin:0px 20px 0px 0px;text-decoration : none;" href="javascript:tempPasswordChange('<%=EnvirUserId%>')"><i class="fa fa-key"></i>修改密码</a>
		<a style="margin:0px 20px 0px 0px;text-decoration : none;" href="javascript:quit()"><i class="fa fa-home"></i>退出</a>
	</div>
</div>