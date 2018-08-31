<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DBConnection.EMCDB" %>
<%
	String targetColor = java.net.URLDecoder.decode(request.getParameter("targetColor"),"UTF-8");
	targetColor = "#"+targetColor;
	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
    Double threshold = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8"));
    Double minLevels = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("minLevels"),"UTF-8"));
    Double maxLevels = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("maxLevels"),"UTF-8"));
    Double minFrequences = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("minFrequences"),"UTF-8"))*1000000;
    Double maxFrequences = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("maxFrequences"),"UTF-8"))*1000000;
    
    String sql= "update emcwzfqjc set color='"+targetColor+"',threshold='"+threshold+"',minLevels='"+minLevels
    +"',maxLevels='"+maxLevels+"',startfrequence='"+minFrequences+"',endfrequence='"+maxFrequences+"' where bh='"+bh+"'";
   // System.out.println(sql);
   EMCDB emcdb = new EMCDB();
   emcdb.dynaStm = emcdb.newStatement();
   int result = emcdb.UpdateSQL(sql);
   emcdb.closeStm();
   if(result==0){%>
   存储失败。
   <%}else{%>
 存储成功。
 	<%}%>