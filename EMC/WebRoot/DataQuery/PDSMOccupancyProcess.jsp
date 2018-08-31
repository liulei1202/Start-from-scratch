<!-- 已弃用，现在直接在占用度页面进行计算 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.text.DecimalFormat" %>
<%
String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
String threshold = java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8");
//System.out.println(threshold);
Double Occupancy = Handle.PDSMcalculate(bh,type,Double.parseDouble(threshold));
Occupancy = Occupancy*100;
DecimalFormat df = new DecimalFormat("#0.00");
String Occup = df.format(Occupancy);/*修改double样式*/
%>
占用度：<%=Occup %>%
