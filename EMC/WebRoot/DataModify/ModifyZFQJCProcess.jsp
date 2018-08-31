<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.fileupload.ImportFile" %>
<%@ page import="java.lang.*" %>
<%@ page import="DBConnection.*" %>
<%

		//System.out.println("DBDPJCSignalParameterRegProcess.jsp");
		Double position = ImportFile.GetLongiLatiDouble(java.net.URLDecoder.decode(request.getParameter("position"),"UTF-8").trim());	
		String satName = java.net.URLDecoder.decode(request.getParameter("satName"),"UTF-8");
		String country = java.net.URLDecoder.decode(request.getParameter("country"),"UTF-8");
		Double rbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("rbw"),"UTF-8").trim())*1000;
		Double vbw = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("vbw"),"UTF-8").trim())*1000;
				
		String levels = java.net.URLDecoder.decode(request.getParameter("level"),"UTF-8");	
		String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
		String polar = java.net.URLDecoder.decode(request.getParameter("polar"),"UTF-8");
		Double startFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("startFrequence"),"UTF-8").trim())*1000000;
		Double endFrequence = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("endFrequence"),"UTF-8").trim())*1000000;
				
		String station = java.net.URLDecoder.decode(request.getParameter("station"),"UTF-8");
		String time = java.net.URLDecoder.decode(request.getParameter("time"),"UTF-8").trim();
		String person = java.net.URLDecoder.decode(request.getParameter("person"),"UTF-8");
		String businessType = java.net.URLDecoder.decode(request.getParameter("businessType"),"UTF-8");
		String businessName  = java.net.URLDecoder.decode(request.getParameter("businessName"),"UTF-8");
		String zfq = java.net.URLDecoder.decode(request.getParameter("zfq"),"UTF-8");
		String antenna  = java.net.URLDecoder.decode(request.getParameter("antenna"),"UTF-8");
		String weaken = java.net.URLDecoder.decode(request.getParameter("weaken"),"UTF-8");
		String comments  = java.net.URLDecoder.decode(request.getParameter("comments"),"UTF-8");
		String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
		String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
		//System.out.println("bh:"+bh);
		if(weaken.contains("null")){
			weaken = "";
		}
		if(levels.contains("null")){
			levels = "";
		}
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		/*
			String sql1 = "delete from EMCWZFQJC where bh='"+bh+"'";
			emcdb.UpdateSQL(sql1);
		*/
		int result = 0;
		String sql2 = null;
		if(bh.trim().equals("0")){//导入数据时
			String md5 = null;
			Date dt = new Date(); 
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String currentTime = sdf.format(dt);
		    
			bh = time.contains(" ") ? time.substring(0,time.lastIndexOf(" ")).trim() : time;
			bh = bh + "-" + position  + "-" + startFrequence + "-" + polar+ "-" + md5 + "-" + currentTime;
			
				
			bh = "ZFQJC" + "-" + bh;
			sql2 = "insert into EMCWZFQJC (bh,businessType,businessName,zfq,antenna,weaken,comments, position, satName, country, rbw,vbw,levels,frequence,polar,startFrequence,endFrequence,station,time,person,MD5,data) "+
			" values('" + bh +"','"+businessType+"','"+businessName+"','"+zfq+"','"+antenna+"','"+weaken+"','"+comments + "'," + position + ",'" + satName + "','"+ country +"'," + rbw + "," + vbw + ",'" + levels + "','" + frequence + "','" +
			polar + "'," + startFrequence + "," + endFrequence + ",'" + station + "','" + time + "','" + person + "','"+md5+"',empty_blob())";
			System.out.println(sql2);
		}else{//修改数据时
			sql2 = "update emcwzfqjc set businessType='"+businessType+"',businessName='"+businessName
			+"',zfq='"+zfq+"',antenna='"+antenna+"',weaken='"+weaken+"',comments='"+comments
			+"', position='"+position+"', satName='"+satName+"', country='"+country+"', rbw='"+rbw
			+"',vbw='"+vbw+"',levels='"+levels+"',frequence='"+frequence+"',polar='"+polar
			+"',startFrequence='"+startFrequence+"',endFrequence='"+endFrequence+"',station='"+station
			+"',time='"+time+"',person='"+person+"' where bh='"+bh+"'";
		}
		result = emcdb.UpdateSQL(sql2);
		emcdb.closeStm();
		if(result == 0){%>		
			<%="false"%>
		<%}else{ %>
			
			<%="true"%>		
		<%} %>	