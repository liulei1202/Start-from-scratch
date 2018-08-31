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
	  <li class="treeview"><a href="#"id="CDB"><i class="fa fa-dashboard"></i> <span>超短波监测</span> <i class="fa fa-angle-left pull-right"></i></a>
		  <ul class="treeview-menu">
		  <li><a href="#"id="CDBDPJC"><i class="fa fa-circle-o"></i>单频监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/DPJCRepeat.jsp?No=6"id="CDBDPJCRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/DPJCAnalysis.jsp?No=6"id="CDBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBDPJCDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/DPJCSignalParameterReg.jsp?No=6"id="CDBDPJCSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="USW/DPJCOriginalMonitorData.jsp?No=6"id="CDBDPJCOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/DPJCGraphicData.jsp?No=6"id="CDBDPJCGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/DPJCAudioData.jsp?No=6"id="CDBDPJCAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="USW/DPJCVideoData.jsp?No=6"id="CDBDPJCVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
				</ul>
			  </li>
			</ul>
		  </li>	
		  <li><a href="#"id="CDBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/PDSMRepeat.jsp?No=6"id="CDBPDSMRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/PDSMAnalysis.jsp?No=6"id="CDBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBPDSMDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/PDSMOriginalMonitorData.jsp?No=6"id="CDBPDSMOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/PDSMGraphicData.jsp?No=6"id="CDBPDSMGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/PDSMVideoData.jsp?No=6"id="CDBPDSMVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>
		  </li>
		  <li><a href="#"id="CDBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/XHCXRepeat.jsp?No=6"id="CDBXHCXRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/XHCXAnalysis.jsp?No=6"id="CDBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBXHCXDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/XHCXSignalParameterReg.jsp?No=6"id="CDBXHCXSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="USW/XHCXOriginalMonitorData.jsp?No=6"id="CDBXHCXOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/XHCXGraphicData.jsp?No=6"id="CDBXHCXGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/XHCXAudioData.jsp?No=6"id="CDBXHCXAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="USW/XHCXVideoData.jsp?No=6"id="CDBXHCXVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>		  
		  </li>
		  <li><a href="#"id=CDBLHDW><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/LHDWRepeat.jsp?No=6"id="CDBLHDWRepeat"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/LHDWAnalysis.jsp?No=6"id="CDBAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBLHDWDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/LHDWOriginalMonitorData.jsp?No=6"id="CDBLHDWOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/LHDWVideoData.jsp?No=6"id="CDBLHDWVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>		  
			  </ul>
		  </li>
		  <li><a href="#"id=CDBJCBG><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/JCBGQuery.jsp?No=6"id="CDBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/JCBGAnalysis.jsp?No=6"id="CDBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="USW/JCBGData.jsp?No=6"id="CDBJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>			  
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




