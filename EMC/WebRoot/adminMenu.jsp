<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="js/tree/sidebar-menu.css">
<link rel="stylesheet" href="js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
<aside class="main-sidebar">
	<ul class="sidebar-menu">
	  <li class="header">导航栏</li>
	  <li class="treeview"><a href="javascript:tempNewUser()"><i class="fa fa-user"></i> <span>添加新用户</span> </a>
	  </li>	  
	</ul>  
 </aside>

<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/tree/sidebar-menu.js"></script>
<script>
	$.sidebarMenu($('.sidebar-menu'));
	function tempNewUser(){
		window.open("/rsui/Admin/UserNewForm.jsp","_blank",'width=300,height=450,top='+(screen.height-450)/2+',left='+(screen.width-300)/2+',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
	}
</script>


  