<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.text.DecimalFormat" %>
 <%
  	String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
  	String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
  	String frequence = java.net.URLDecoder.decode(request.getParameter("frequence"),"UTF-8");
  	ArrayList<Double> al = Handle.PDSMFrequencefilter(bh, type,frequence, 0.0);
  
   DecimalFormat df = new DecimalFormat("#0.00");
   String min = String.valueOf(al.get(0));
   String max = String.valueOf(al.get(1));
   String aver = df.format(al.get(2));
 %>
<%=min %>,<%=max %>,<%=aver %>

