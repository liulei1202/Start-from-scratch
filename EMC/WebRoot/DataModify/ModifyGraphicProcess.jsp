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
		String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8").trim();
		frequence = String.valueOf((Double.parseDouble(frequence.trim())*1000000));
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
		String table2 = null;
		if(fileType.trim().equals("DBDPJCQuery")){
					
			table = "EMCDPJCGraphic";
			table2="EMCDPJCPARAMETER";
		}else if(fileType.trim().equals("CDBDPJCQuery")){
			
			table = "EMCUDPJCGraphic";
			table2="EMCUDPJCPARAMETER";
		}else if(fileType.trim().equals("DBXHCXQuery")){
			
			table = "EMCDPCXGraphic";
			table2="EMCDPCXPARAMETER";
		}else if(fileType.trim().equals("CDBXHCXQuery")){
			
			table = "EMCUDPCXGraphic";
			table2="EMCUDPCXPARAMETER";
		}else{
			table = "wrong";
		}
		

		sql2 = "update "+table+" set businessType='"+businessType+"',businessName='"+businessName
		+"',startTime='"+startTime+"',endTime='"+endTime+"',frequence='"+frequence+"',graphicType='"+graphicType
		+"',signalType='"+signalType+"',longitude='"+longitude+"',latitude='"+latitude
		+"',station='"+station+"',monitor='"+monitor+"',monitorLocation='"+monitorLocation+"' where bh='"+bh+"' and graphicType='"+grapType+"'";
		//System.out.println(sql2);
		result = emcdb.UpdateSQL(sql2);
		emcdb.closeStm();
		if(result == 0){%>		
			<%="修改失败。图形类型已存在，请更改图形类型后再修改。"%>
		<%}else{ %>
			
			<%="修改成功。"%>		
		<%}%>
		
		
		