<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="DBConnection.*" %>
    <%
		//System.out.println("DBDPJCSignalParameterRegProcess.jsp");
		String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");	
		String bandWidth = java.net.URLDecoder.decode(request.getParameter("bandWidth"),"UTF-8");
		String modulate = java.net.URLDecoder.decode(request.getParameter("modulate"),"UTF-8");
		String longitude = java.net.URLDecoder.decode(request.getParameter("longitude"),"UTF-8");
		String latitude = java.net.URLDecoder.decode(request.getParameter("latitude"),"UTF-8");		
		String monitorTime = java.net.URLDecoder.decode(request.getParameter("monitorTime"),"UTF-8");
			
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
		String launchTime = java.net.URLDecoder.decode(request.getParameter("launchTime"),"UTF-8");
     	String levels = java.net.URLDecoder.decode(request.getParameter("levels"),"UTF-8");
		String testNumber = java.net.URLDecoder.decode(request.getParameter("testNumber"),"UTF-8");
		String occupancy = java.net.URLDecoder.decode(request.getParameter("occupancy"),"UTF-8");
		String threshold = java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8");
		
		String direction = java.net.URLDecoder.decode(request.getParameter("direction"),"UTF-8");
		String Station = java.net.URLDecoder.decode(request.getParameter("monitoringStation"),"UTF-8");
		String noise = java.net.URLDecoder.decode(request.getParameter("noise"),"UTF-8");
		String compared = java.net.URLDecoder.decode(request.getParameter("compared"),"UTF-8");
		String comments = java.net.URLDecoder.decode(request.getParameter("comments"),"UTF-8");
		String businessName = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
		
		String signalType = java.net.URLDecoder.decode(request.getParameter("signalType"),"UTF-8");
		String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8");
		String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
		String city = java.net.URLDecoder.decode(request.getParameter("city"),"UTF-8");
		
		String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
		
		Double Frequence = Double.parseDouble(frequence)*1000000;/*输入的频率是MHz*/
		Double BandWidth = Double.parseDouble(bandWidth)*1000;/*输入的频率是kHz*/
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime = sdf.format(dt);
		DecimalFormat df = new DecimalFormat("#.000");
		/*经纬度样式应该为116°25'56.0E或者直接为数字*/
		//Double Longitude = ImportFile.GetLongiLatiDouble(longitude.trim()); 
		//Double Latitude = ImportFile.GetLongiLatiDouble(latitude.trim());
		String sql;
		String bh = monitorTime.contains(" ") ? monitorTime.substring(0,monitorTime.lastIndexOf(" ")).trim() : monitorTime;
		bh = bh + "-" + frequence + "-" + longitude + "-" + latitude+ "-" +Station+ "-" +noise+ "-" + currentTime;
		
		
		if(type.equals("DBDPJCSignalParameterReg")){	//短波-单频检测-信号参数登记
			
			bh = "DPJC" + "-" + bh;
			sql = "insert into EMCDPJCPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station  + "','" + noise + "','" + compared + "','" + comments + "')";
		}else if(type.equals("DBXHCXSignalParameterReg")){	//短波-信号测向-信号参数登记
			
			bh = "XHCX" + "-" + bh;
			sql = "insert into EMCDPCXPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station  + "','" + noise + "','" + compared + "','" + comments + "')";
		}else if(type.equals("CDBDPJCSignalParameterReg")){	//超短波-单频检测-信号参数登记
			
			bh = "DPJC" + "-" + bh;
			sql = "insert into EMCUDPJCPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station  + "','" + noise + "','" + compared + "','" + comments + "')";
		}else if(type.equals("CDBXHCXSignalParameterReg")){	//超短波-信号测向-信号参数登记
			
			bh = "XHCX" + "-" + bh;
			sql = "insert into EMCUDPCXPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station  + "','" + noise + "','" + compared + "','" + comments + "')";
		}else{
			
			sql = null;
		}
	    //System.out.println(sql);
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = emcdb.UpdateSQL(sql);
		emcdb.closeStm();
		
		
		if(result == 0){%>		
			<%="false" %>
		<%}else{ %>
			
			<%=bh%>		
		<%} %>