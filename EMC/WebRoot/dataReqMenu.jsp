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
	  
	  <li class="treeview"><a href="#" id="DB"><i class="fa fa-dashboard"></i> <span>短波监测</span> <i class="fa fa-angle-left pull-right"></i></a>
		  <ul class="treeview-menu">
		  <li><a href="#" id="DBDPJC"><i class="fa fa-circle-o"></i>单频监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/DPJCQuery.jsp?No=3" id="DBDPJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/DPJCAnalysis.jsp?No=3" id="DBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			</ul>
		  </li>	
		  <li><a href="#"id="DBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/PDSMQuery.jsp?No=3"id="DBPDSMQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/PDSMAnalysis.jsp?No=3"id="DBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>
		  </li>
		  <li><a href="#"id="DBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/XHCXQuery.jsp?No=3"id="DBXHCXQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/XHCXAnalysis.jsp?No=3" id="DBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>		  
		  </li>
		  <li><a href="#"id="DBLHDW"><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/LHDWQuery.jsp?No=3"id="DBLHDWQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>	
			  <li><a href="SW/LHDWAnalysis.jsp?No=3"id="DBLHDWAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>	  
			  </ul>
		  </li>
		  <li><a href="#"id="DBJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/JCBGQuery.jsp?No=3"ID="DBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/JCBGAnalysis.jsp?No=3"id="DBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>
		  </li>
			</ul>
	  </li>

	  <li class="treeview"><a href="#"id="CDB"><i class="fa fa-dashboard"></i> <span>超短波监测</span> <i class="fa fa-angle-left pull-right"></i></a>
		  <ul class="treeview-menu">
		  <li><a href="#"id="CDBDPJC"><i class="fa fa-circle-o"></i>单频监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/DPJCQuery.jsp?No=3"id="CDBDPJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/DPJCAnalysis.jsp?No=3"id="CDBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			</ul>
		  </li>	
		  <li><a href="#"id="CDBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/PDSMQuery.jsp?No=3"id="CDBPDSMQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/PDSMAnalysis.jsp?No=3"id="CDBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>
		  </li>
		  <li><a href="#"id="CDBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/XHCXQuery.jsp?No=3"id="CDBXHCXQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/XHCXAnalysis.jsp?No=3"id="CDBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>		  
		  </li>
		  <li><a href="#"id=CDBLHDW><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/LHDWQuery.jsp?No=3"id="CDBLHDWQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/LHDWAnalysis.jsp?No=3"id="CDBAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>		  
			  </ul>
		  </li>
		  <li><a href="#"id=CDBJCBG><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/JCBGQuery.jsp?No=3"id="CDBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/JCBGAnalysis.jsp?No=3"id="CDBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  </ul>
		  </li>
			</ul>
	  </li>
	    
	  <li class="treeview"><a href="#"id="WX"><i class="fa fa-dashboard"></i> <span>卫星监测</span> <i class="fa fa-angle-left pull-right"></i></a>
			<ul class="treeview-menu">
		  <li><a href="#" id="WXZFQJC"><i class="fa fa-circle-o"></i>转发器监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/ZFQJCQuery.jsp?No=3"id="ZFQJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/ZFQJCAnalysis.jsp?No=3"id="ZFQJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			</ul>
		   </li>	
		  <li><a href="#"id="WXJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/JCBGQuery.jsp?No=3"id="WXJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/JCBGAnalysis.jsp?No=3"id="WXJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>	
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




