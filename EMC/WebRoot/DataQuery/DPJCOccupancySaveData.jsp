<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.*"%>
<%
	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
	Double occupancy = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("occupancy"),"UTF-8"));
	
    String result = Handle.occupancySave(bh,type,occupancy);
%>
<%=result%>