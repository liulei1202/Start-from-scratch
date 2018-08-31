<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="DBConnection.*" %>
<%

		//System.out.println("ModifyAudioProcess.jsp");
		String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
		String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8"); 
		String reportName = java.net.URLDecoder.decode(request.getParameter("reportName"),"UTF-8"); 
		String reportType = java.net.URLDecoder.decode(request.getParameter("reportType"),"UTF-8"); 
		String startFrequence = java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8"); 
		startFrequence = (Double.parseDouble(startFrequence)*1000000)+"";
		String endFrequence = java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8"); 
		endFrequence = (Double.parseDouble(endFrequence)*1000000)+"";
		String startTime = java.net.URLDecoder.decode(request.getParameter("startTime"),"UTF-8"); 
		String endTime = java.net.URLDecoder.decode(request.getParameter("endTime"),"UTF-8"); 
		String keyWords = java.net.URLDecoder.decode(request.getParameter("keyWords"),"UTF-8"); 
		String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
		String author = java.net.URLDecoder.decode(request.getParameter("author"),"UTF-8");
		String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8");
		String equipment = java.net.URLDecoder.decode(request.getParameter("equipment"),"UTF-8");
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
		String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
		
		
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = 0;
		String sql2 = null;
		String table = null;
		if(fileType.trim().equals("DBJCBGQuery")){
					
				table = "EMCJCBG";
		}else if(fileType.trim().equals("CDBJCBGQuery")){
			
			table = "EMCUJCBG";
		}else{
			table = "wrong";
		}
		
		sql2 = "update "+table+" set businessType='"+businessType+"',businessName='"+businessName
		+"',startTime='"+startTime+"',endTime='"+endTime+"',startFrequence='"+startFrequence+"',endFrequence='"+endFrequence
		+"',fileName='"+reportName+"',Type='"+reportType+"',keyWords='"+keyWords+"',equipment='"+equipment
		+"',station='"+station+"',WRITER='"+author+"',monitorLocation='"+monitorLocation+"' where bh='"+bh+"'";
		//System.out.println(sql2);
		result = emcdb.UpdateSQL(sql2);
		emcdb.closeStm();
		if(result == 0){%>		
			<%="修改失败。"%>
		<%}else{ %>
			
			<%="修改成功。"%>		
		<%} %>	