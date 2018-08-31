<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
 --> 

<link rel="stylesheet" href="js/tree/sidebar-menu.css">
<link rel="stylesheet" href="js/tree/font-awesome-4.7.0/css/font-awesome.min.css">
<aside class="main-sidebar">
	<ul class="sidebar-menu">
	  <li class="header">导航栏</li>
	  
	  
	  <li class="treeview"><a href="Admin/UserManage.jsp"><i class="fa fa-user"></i> <span>用户管理</span> </a>
	  </li>
	  
	  
	  <li class="treeview"><a href="#" id="DB"><i class="fa fa-dashboard"></i> <span>短波监测</span> <i class="fa fa-angle-left pull-right"></i></a>
		  <ul class="treeview-menu">
		  <li><a href="#" id="DBDPJC"><i class="fa fa-circle-o"></i>单频监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/DPJCQuery.jsp?No=4" id="DBDPJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/DPJCAnalysis.jsp?No=4" id="DBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBDPJCDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/DPJCSignalParameterReg.jsp?No=4" id="DBDPJCSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="SW/DPJCOriginalMonitorData.jsp?No=4"id="DBDPJCOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/DPJCGraphicData.jsp?No=4"id="DBDPJCGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/DPJCAudioData.jsp?No=4"id="DBDPJCAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="SW/DPJCVideoData.jsp?No=4"id="DBDPJCVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
				</ul>
			  </li>
			</ul>
		  </li>	
		  <li><a href="#"id="DBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/PDSMQuery.jsp?No=4"id="DBPDSMQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/PDSMAnalysis.jsp?No=4"id="DBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBPDSMDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/PDSMOriginalMonitorData.jsp?No=4"id="DBPDSMOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/PDSMGraphicData.jsp?No=4"id="DBPDSMGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/PDSMVideoData.jsp?No=4"id="DBPDSMVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>
		  </li>
		  <li><a href="#"id="DBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/XHCXQuery.jsp?No=4"id="DBXHCXQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/XHCXAnalysis.jsp?No=4" id="DBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBXHCXDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/XHCXSignalParameterReg.jsp?No=4"id="DBXHCXSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="SW/XHCXOriginalMonitorData.jsp?No=4"id="DBXHCXOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/XHCXGraphicData.jsp?No=4"id="DBXHCXGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="SW/XHCXAudioData.jsp?No=4"id="DBXHCXAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="SW/XHCXVideoData.jsp?No=4"id="DBXHCXVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>		  
		  </li>
		  <li><a href="#"id="DBLHDW"><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/LHDWQuery.jsp?No=4"id="DBLHDWQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="SW/LHDWAnalysis.jsp?No=4"id="DBLHDWAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="DBLHDWDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="SW/LHDWOriginalMonitorData.jsp?No=4"id="DBLHDWOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="SW/LHDWVideoData.jsp?No=4" id="DBLHDWVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
				</ul>
				</li>		  
			  </ul>
		  </li>
		  <li><a href="#"id="DBJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="SW/JCBGQuery.jsp?No=4"ID="DBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
		      <li><a href="SW/JCBGAnalysis.jsp?No=4"id="DBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="SW/JCBGData.jsp?No=4"id="DBJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>			  
			  </ul>
		  </li>
			</ul>
	  </li>

	  <li class="treeview"><a href="#"id="CDB"><i class="fa fa-dashboard"></i> <span>超短波监测</span> <i class="fa fa-angle-left pull-right"></i></a>
		  <ul class="treeview-menu">
		  <li><a href="#"id="CDBDPJC"><i class="fa fa-circle-o"></i>单频监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/DPJCQuery.jsp?No=4"id="CDBDPJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/DPJCAnalysis.jsp?No=4"id="CDBDPJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBDPJCDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/DPJCSignalParameterReg.jsp?No=4"id="CDBDPJCSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="USW/DPJCOriginalMonitorData.jsp?No=4"id="CDBDPJCOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/DPJCGraphicData.jsp?No=4"id="CDBDPJCGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/DPJCAudioData.jsp?No=4"id="CDBDPJCAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="USW/DPJCVideoData.jsp?No=4"id="CDBDPJCVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
				</ul>
			  </li>
			</ul>
		  </li>	
		  <li><a href="#"id="CDBPDSM"><i class="fa fa-circle-o"></i>频段扫描<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/PDSMQuery.jsp?No=4"id="CDBPDSMQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/PDSMAnalysis.jsp?No=4"id="CDBPDSMAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBPDSMDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/PDSMOriginalMonitorData.jsp?No=4"id="CDBPDSMOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/PDSMGraphicData.jsp?No=4"id="CDBPDSMGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/PDSMVideoData.jsp?No=4"id="CDBPDSMVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>
		  </li>
		  <li><a href="#"id="CDBXHCX"><i class="fa fa-circle-o"></i>信号测向<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/XHCXQuery.jsp?No=4"id="CDBXHCXQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/XHCXAnalysis.jsp?No=4"id="CDBXHCXAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBXHCXDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/XHCXSignalParameterReg.jsp?No=4"id="CDBXHCXSignalParameterReg"><i class="fa fa-circle-o"></i>信号参数登记</a></li>
				  <li><a href="USW/XHCXOriginalMonitorData.jsp?No=4"id="CDBXHCXOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/XHCXGraphicData.jsp?No=4"id="CDBXHCXGraphicData"><i class="fa fa-circle-o"></i>图形数据</a></li>
				  <li><a href="USW/XHCXAudioData.jsp?No=4"id="CDBXHCXAudioData"><i class="fa fa-circle-o"></i>音频数据</a></li>
				  <li><a href="USW/XHCXVideoData.jsp?No=4"id="CDBXHCXVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>
			  </ul>		  
		  </li>
		  <li><a href="#"id=CDBLHDW><i class="fa fa-circle-o"></i>联合定位<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/LHDWQuery.jsp?No=4"id="CDBLHDWQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/LHDWAnalysis.jsp?No=4"id="CDBAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#"id="CDBLHDWDataimport"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="USW/LHDWOriginalMonitorData.jsp?No=4"id="CDBLHDWOriginalMonitorData"><i class="fa fa-circle-o"></i>原始监测数据</a></li>
				  <li><a href="USW/LHDWVideoData.jsp?No=4"id="CDBLHDWVideoData"><i class="fa fa-circle-o"></i>视频数据</a></li>
					</ul>
				</li>		  
			  </ul>
		  </li>
		  <li><a href="#"id=CDBJCBG><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="USW/JCBGQuery.jsp?No=4"id="CDBJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="USW/JCBGAnalysis.jsp?No=4"id="CDBJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="USW/JCBGData.jsp?No=4"id="CDBJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>			  
			  </ul>
		  </li>
			</ul>
	  </li>
	    
	  <li class="treeview"><a href="#"id="WX"><i class="fa fa-dashboard"></i> <span>卫星监测</span> <i class="fa fa-angle-left pull-right"></i></a>
			<ul class="treeview-menu">
		  <li><a href="#" id="WXZFQJC"><i class="fa fa-circle-o"></i>转发器监测<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/ZFQJCQuery.jsp?No=4"id="ZFQJCQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/ZFQJCAnalysis.jsp?No=4"id="ZFQJCAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>
			  <li><a href="#" id="ZFQJCData"><i class="fa fa-circle-o"></i>数据导入<i class="fa fa-angle-left pull-right"></i></a>
			  	<ul class="treeview-menu">
				  <li><a href="Sat/ZFQJCDataManual.jsp?No=4"id="ZFQJCDataManual"><i class="fa fa-circle-o"></i>手动导入</a></li>
				  <li><a href="Sat/ZFQJCDataExcel.jsp?No=4"id="ZFQJCDataExcel"><i class="fa fa-circle-o"></i>Excel导入</a></li>
				</ul>
			  </li>
			</ul>
		   </li>	
		  <li><a href="#"id="WXJCBG"><i class="fa fa-circle-o"></i>监测报告<i class="fa fa-angle-left pull-right"></i></a>
		  	<ul class="treeview-menu">
			  <li><a href="Sat/JCBGQuery.jsp?No=4"id="WXJCBGQuery"><i class="fa fa-circle-o"></i>数据查询</a></li>
			  <li><a href="Sat/JCBGAnalysis.jsp?No=4"id="WXJCBGAnalysis"><i class="fa fa-circle-o"></i>数据分析</a></li>		
			  <li><a href="Sat/JCBGData.jsp?No=4"id="WXJCBGData"><i class="fa fa-circle-o"></i>数据导入</a></li>
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




