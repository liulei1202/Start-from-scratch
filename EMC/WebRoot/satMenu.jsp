<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
 --> 
<script type="text/javascript" src="js/navigation.js"></script>
<link rel="stylesheet" href="js/tree/sidebar-menu.css">
<link rel="stylesheet" href="js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
<aside class="main-sidebar">
	<ul class="sidebar-menu">
	  <li class="header">导航栏</li>
	  <li class="treeview"><a href="#"id="WX"><i class="fa fa-dashboard"></i> <span>卫星监测</span> <i class="fa fa-angle-left pull-right"></i></a>
			<ul class="treeview-menu">
		  <li><a href="#" id="WXZFQJC"><i class="fa fa-circle-o"></i>转发器监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/ZFQJCQuery.jsp?No=7"id="ZFQJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/ZFQJCAnalysis.jsp?No=7"id="ZFQJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#" id="ZFQJCData"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="Sat/ZFQJCDataManual.jsp?No=7"id="ZFQJCDataManual"><i class="fa fa-circle-o"></i>手动导入</a></li>
				  <li><a href="Sat/ZFQJCDataExcel.jsp?No=7"id="ZFQJCDataExcel"><i class="fa fa-circle-o"></i>Excel导入</a></li>
				</ul>
			  </li>
			  </ul>
		   </li>	
		  <li><a href="#"id="WXJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/JCBGQuery.jsp?No=7"id="WXJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/JCBGAnalysis.jsp?No=7"id="WXJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>		
			  <li><a href="Sat/JCBGData.jsp?No=7"id="WXJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>
			  </ul>
		  </li>
			</ul>
	  </li>	    
	  
	</ul>  
 </aside>

<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/tree/sidebar-menu.js"></script>
<script>
$.sidebarMenu($('.sidebar-menu'));
</script>




