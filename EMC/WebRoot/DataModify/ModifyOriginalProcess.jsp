<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="DBConnection.*" %>
<%

		//System.out.println("DBDPJCSignalParameterRegProcess.jsp");
		String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8").trim();
		String monitor = java.net.URLDecoder.decode(request.getParameter("monitor"),"UTF-8").trim();
		String monitorLocation = java.net.URLDecoder.decode(request.getParameter("monitorLocation"),"UTF-8").trim();
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8").trim();
		String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8").trim();
		String fileType = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8").trim();
		String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8").trim();
		//System.out.println("bh:"+bh);
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int result = 0;
		String sql2 = null;
		String table = null;
		if(fileType.trim().equals("DBDPJCQuery")){
					
				table = "EMCDPJC";
		}else if(fileType.trim().equals("CDBDPJCQuery")){
			
			table = "EMCUDPJC";
		}else if(fileType.trim().equals("DBXHCXQuery")){
			
			table = "EMCDPCX";
		}else if(fileType.trim().equals("CDBXHCXQuery")){
			
			table = "EMCUDPCX";
		}else if(fileType.trim().equals("DBPDSMQuery")){
				
			table = "EMCPDSM";
		}else if(fileType.trim().equals("CDBPDSMQuery")){
			
			table = "EMCUPDSM";
		}else if(fileType.trim().equals("DBLHDWQuery")){
			
			table = "EMCLHDW";
		}else if(fileType.trim().equals("CDBLHDWQuery")){
			
			table = "EMCULHDW";
		}else{
			table = "wrong";
		}
		
		sql2 = "update "+table+" set businessType='"+businessType+"',businessName='"+businessName
		+"',station='"+station+"',monitor='"+monitor+"',monitorLocation='"+monitorLocation+"' where bh='"+bh+"'";
		
		result = emcdb.UpdateSQL(sql2);
		emcdb.closeStm();
		if(result == 0){%>		
			<%="false"%>
		<%}else{ %>
			
			<%="true"%>		
		<%} %>	