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
			   <li><a href="SW/DPJCRepeat.jsp?No=5" id=DBDPJCRepeat><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/DPJCAnalysis.jsp?No=5" id="DBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
		       <li><a href="#"id="DBDPJCDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/DPJCSignalParameterReg.jsp?No=5" id="DBDPJCSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="SW/DPJCOriginalMonitorData.jsp?No=5"id="DBDPJCOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/DPJCGraphicData.jsp?No=5"id="DBDPJCGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/DPJCAudioData.jsp?No=5"id="DBDPJCAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="SW/DPJCVideoData.jsp?No=5"id="DBDPJCVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
				</ul>
			  </li>
			</ul>
		  </li>	
		  <li><a href="#"id="DBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/PDSMRepeat.jsp?No=5"id="DBPDSMRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/PDSMAnalysis.jsp?No=5"id="DBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBPDSMDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/PDSMOriginalMonitorData.jsp?No=5"id="DBPDSMOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/PDSMGraphicData.jsp?No=5"id="DBPDSMGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/PDSMVideoData.jsp?No=5"id="DBPDSMVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>
		  </li>
		  <li><a href="#"id="DBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/XHCXRepeat.jsp?No=5"id="DBXHCXRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/XHCXAnalysis.jsp?No=5" id="DBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBXHCXDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/XHCXSignalParameterReg.jsp?No=5"id="DBXHCXSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="SW/XHCXOriginalMonitorData.jsp?No=5"id="DBXHCXOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/XHCXGraphicData.jsp?No=5"id="DBXHCXGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/XHCXAudioData.jsp?No=5"id="DBXHCXAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="SW/XHCXVideoData.jsp?No=5"id="DBXHCXVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>		  
		  </li>
		  <li><a href="#"id="DBLHDW"><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/LHDWRepeat.jsp?No=5"id="DBLHDWRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/LHDWAnalysis.jsp?No=5"id="DBLHDWAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBLHDWDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/LHDWOriginalMonitorData.jsp?No=5"id="DBLHDWOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/LHDWVideoData.jsp?No=5"id="DBLHDWVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>		  
			  </ul>
		  </li>
		  <li><a href="#"id="DBJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/JCBGQuery.jsp?No=5"ID="DBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/JCBGAnalysis.jsp?No=5"id="DBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="SW/JCBGData.jsp?No=5"id="DBJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>			  
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




