<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="DBConnection.*" %>
<%

		//System.out.println("DBDPJCSignalParameterRegProcess.jsp");
		String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8").trim();
		String bandWidth = java.net.URLDecoder.decode(request.getParameter("bandWidth"),"UTF-8").trim();
		String modulate = java.net.URLDecoder.decode(request.getParameter("modulate"),"UTF-8").trim();
		String longitude = java.net.URLDecoder.decode(request.getParameter("longitude"),"UTF-8").trim();
		String latitude = java.net.URLDecoder.decode(request.getParameter("latitude"),"UTF-8").trim();		
		String monitorTime = java.net.URLDecoder.decode(request.getParameter("monitorTime"),"UTF-8").trim();
			
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8").trim();
		String launchTime = java.net.URLDecoder.decode(request.getParameter("launchTime"),"UTF-8").trim();
     	String levels = java.net.URLDecoder.decode(request.getParameter("levels"),"UTF-8").trim();
		String testNumber = java.net.URLDecoder.decode(request.getParameter("testNumber"),"UTF-8").trim();
		String occupancy = java.net.URLDecoder.decode(request.getParameter("occupancy"),"UTF-8").trim();
		String threshold = java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8").trim();
		
		String direction = java.net.URLDecoder.decode(request.getParameter("direction"),"UTF-8").trim();
		String Station = java.net.URLDecoder.decode(request.getParameter("monitoringStation"),"UTF-8").trim();
		
		String noise = java.net.URLDecoder.decode(request.getParameter("noise"),"UTF-8").trim();
		String compared = java.net.URLDecoder.decode(request.getParameter("compared"),"UTF-8").trim();
		String comments = java.net.URLDecoder.decode(request.getParameter("comments"),"UTF-8").trim();
		String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8").trim();
		String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
		
		String businessName = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8").trim();
		String signalType = java.net.URLDecoder.decode(request.getParameter("signalType"),"UTF-8").trim();
		String city = java.net.URLDecoder.decode(request.getParameter("city"),"UTF-8").trim();
		String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8").trim();
		String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
		
		if(levels.contains("null")){
			levels = "";
		}
		if(testNumber.contains("null")){
			testNumber = "";
		}
		if(occupancy.contains("null")){
			occupancy = "";
		}
		if(threshold.contains("null")){
			threshold = "";
		}
		if(direction.contains("null")){
			direction = "";
		}
		if(noise.contains("null")){
			noise = "";
		}
		//businessName,signalType,city,monitor,monitorLocation
		
		Double Frequence = Double.parseDouble(frequence)*1000000;/*输入的频率是MHz*/
		Double BandWidth = Double.parseDouble(bandWidth)*1000;/*输入的频率是kHz*/
		/*经纬度样式应该为116°25'56.0E或者直接为数字*/
		//Double Longitude = ImportFile.GetLongiLatiDouble(longitude.trim()); 
		//Double Latitude = ImportFile.GetLongiLatiDouble(latitude.trim());
		String sql = " set businessName='"+businessName+"', signalType='"+signalType+"', city='"+city+ 
		"',monitor='"+monitor+"', monitorLocation='"+monitorLocation+"',frequence="+Frequence+", bandWidth="+BandWidth+", modulate='"+modulate
			+"', Longitude='"+longitude+"', Latitude='"+latitude+"', monitorTime='"+monitorTime+"', businessType='"+businessType
			+"', launchTime='"+launchTime+"', levels='"+levels+"', testNumber='"+testNumber+"', occupancy='"+occupancy
			+"', threshold='"+threshold+"', direction='"+direction+"', Station='"+Station
			+"', noise='"+noise+"', compared='"+compared+"', comments='"+comments+"'where bh='"+bh+"'";
			
		String sql2 = " set businessName='"+businessName+"',monitor='"+monitor+"', monitorLocation='"+monitorLocation
			+"', businessType='"+businessType+"', Station='"+Station+"'where bh='"+bh+"'";
			
		String sql3 =  " set businessName='"+businessName+"', signalType='"+signalType+ 
		"',monitor='"+monitor+"', monitorLocation='"+monitorLocation+"', frequence="+Frequence
			+", Longitude='"+longitude+"', Latitude='"+latitude+"', starttime='"+monitorTime+"', businessType='"+businessType
			+"', Station='"+Station+"', comments='"+comments+"' where bh='"+bh+"' and graphictype='中频图'";
			
		String sql4 =  " set businessName='"+businessName+"', signalType='"+signalType+ 
		"',monitor='"+monitor+"', monitorLocation='"+monitorLocation+"',frequence="+Frequence
			+", Longitude='"+longitude+"', Latitude='"+latitude+"', starttime='"+monitorTime+"', businessType='"+businessType
			+"', Station='"+Station+"', comments='"+comments+"' where bh='"+bh+"' and graphictype='示向图'";
		String sql5 =  " set businessName='"+businessName+"', signalType='"+signalType+ 
		"',monitor='"+monitor+"', monitorLocation='"+monitorLocation+"',frequence="+Frequence
			+", Longitude='"+longitude+"', Latitude='"+latitude+"', starttime='"+monitorTime+"', businessType='"+businessType
			+"', Station='"+Station+"', comments='"+comments+"' where bh='"+bh+"' and graphictype='定位图'";
		
		
		
		if(type.equals("DBDPJCQuery")){	//短波-单频检测-信号参数登记
			
			sql = "update EMCDPJCPARAMETER " +sql;
			sql2 = "update EMCDPJC " +sql2;
			sql3 = "update EMCDPJCGRAPHIC " +sql3;
			sql4 = "update EMCDPJCGRAPHIC " +sql4;
			sql5 = "update EMCDPJCGRAPHIC " +sql5;
		}else if(type.equals("DBXHCXQuery")){	//短波-信号测向-信号参数登记
			
			sql = "update EMCDPCXPARAMETER " +sql;
			sql2 = "update EMCDPCX " +sql2;
			sql3 = "update EMCDPCXGRAPHIC " +sql3;
			sql4 = "update EMCDPCXGRAPHIC " +sql4;
			sql5 = "update EMCDPCXGRAPHIC " +sql5;
		}else if(type.equals("CDBDPJCQuery")){	//超短波-单频检测-信号参数登记
			
			sql = "update EMCUDPJCPARAMETER " +sql;
			sql2 = "update EMCUDPJC " +sql2;
			sql3 = "update EMCUDPJCGRAPHIC " +sql3;
			sql4 = "update EMCUDPJCGRAPHIC " +sql4;
			sql5 = "update EMCUDPJCGRAPHIC " +sql5;
		}else if(type.equals("CDBXHCXQuery")){	//超短波-信号测向-信号参数登记
			
			sql = "update EMCUDPCXPARAMETER " +sql;
			sql2 = "update EMCUDPCX " +sql2;
			sql3 = "update EMCUDPCXGRAPHIC " +sql3;
			sql4 = "update EMCUDPCXGRAPHIC " +sql4;
			sql5 = "update EMCUDPCXGRAPHIC " +sql5;
		}else{
			
			sql = null;
		}
		System.out.println(sql);
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = emcdb.UpdateSQL(sql);
		emcdb.UpdateSQL(sql2);
		emcdb.UpdateSQL(sql3);
		emcdb.UpdateSQL(sql4);
		emcdb.UpdateSQL(sql5);
		emcdb.closeStm();
		
		
		if(result == 0){%>		
			<%="false"%>
		<%}else{ %>
			
			<%="true"%>		
		<%} %>	