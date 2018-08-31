<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="DBConnection.*" %>
<%

		//System.out.println("ModifyAudioProcess.jsp");
		String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8").trim();
		String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8").trim();
		String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8").trim();
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8").trim();
		String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8").trim();
		String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8").trim();
		String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
		//System.out.println("bh:"+bh);
		String startTime = java.net.URLDecoder.decode(request.getParameter("startTime"),"UTF-8").trim();
		String endTime = java.net.URLDecoder.decode(request.getParameter("endTime"),"UTF-8").trim();
		String startFrequence = java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8").trim();
		startFrequence = String.valueOf((Double.parseDouble(startFrequence.trim())*1000000));
		String endFrequence = java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8").trim();
		endFrequence = String.valueOf((Double.parseDouble(endFrequence.trim())*1000000));
		String signalType = java.net.URLDecoder.decode(request.getParameter("signalType"),"UTF-8").trim();
		String longitude = java.net.URLDecoder.decode(request.getParameter("longitude"),"UTF-8").trim();
		String latitude = java.net.URLDecoder.decode(request.getParameter("latitude"),"UTF-8").trim();
		
		String grapType = java.net.URLDecoder.decode(request.getParameter("grapType"),"UTF-8").trim();//原图形类型
		String graphicType = java.net.URLDecoder.decode(request.getParameter("graphicType"),"UTF-8").trim();//修改后的图形类型
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = 0;
		String sql2 = null;
		String table = null;
		
		if(fileType.trim().equals("DBPDSMQuery")){
					
			table = "EMCPDSMGraphic";
			
		}else if(fileType.trim().equals("CDBPDSMQuery")){
			
			table = "EMCUPDSMGraphic";
			
		}else{
			table = "wrong";
		}
		
		
		sql2 = "update "+table+" set businessType='"+businessType+"',businessName='"+businessName
		+"',startTime='"+startTime+"',endTime='"+endTime+"',STARTfrequence='"+startFrequence+"' ,endfrequence='"+endFrequence+"',graphicType='"+graphicType
		+"',signalType='"+signalType+"',longitude='"+longitude+"',latitude='"+latitude
		+"',station='"+station+"',monitor='"+monitor+"',monitorLocation='"+monitorLocation+"' where bh='"+bh+"'";
		//System.out.println(sql2);
		result = emcdb.UpdateSQL(sql2);
		emcdb.closeStm();
		if(result == 0){%>		
			<%="修改失败。"%>
		<%}else{ %>
			
			<%="修改成功。"%>		
		<%}%>
		
		
		