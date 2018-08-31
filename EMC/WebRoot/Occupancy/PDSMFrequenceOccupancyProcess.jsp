<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.text.DecimalFormat" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
String minThreshold = java.net.URLDecoder.decode(request.getParameter("minThreshold"),"UTF-8");
String threshold = java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8");

Double Occupancy = Handle.PDSMFrequenceCalculate(bh, type,Double.parseDouble(frequence), Double.parseDouble(minThreshold), Double.parseDouble(threshold));
Occupancy = Occupancy*100;
DecimalFormat df = new DecimalFormat("#0.00");
String Occup = df.format(Occupancy);/*修改double样式*/
%>
占用度：<%=Occup %>%
