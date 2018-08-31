<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.ReadColor" %>
<%
    //var param = "ImageRGB/GETRGBProcess.jsp?ColorChange=" + ColorChange + "&threshold=" + threshold + "&path=" + path;
    String color = java.net.URLDecoder.decode(request.getParameter("color"),"UTF-8");
    String[] tempColor = color.split(",");
    int[] targetColor = new int[3];
    targetColor[0] = Integer.parseInt(tempColor[0]);
    targetColor[1] = Integer.parseInt(tempColor[1]);
    targetColor[2] = Integer.parseInt(tempColor[2]);
    Double threshold = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("threshold"),"UTF-8"));
    String source = java.net.URLDecoder.decode(request.getParameter("path"),"UTF-8");
    Double minLevels = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("minLevels"),"UTF-8"));
    Double maxLevels = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("maxLevels"),"UTF-8"));
    Double minFrequences = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("minFrequences"),"UTF-8"));
    Double maxFrequences = Double.parseDouble(java.net.URLDecoder.decode(request.getParameter("maxFrequences"),"UTF-8"));
    //+ "&minLevels=" + minLevels + "&maxLevels=" + maxLevels + "&minFrequences=" + minFrequences + "&maxFrequences=" + maxFrequences;
    LinkedList<ArrayList<Double>> ll = ReadColor.getImagePixel(source, targetColor, threshold,minLevels,maxLevels,minFrequences,maxFrequences);
%>
<table id="infoTable" style="display:none">
<% 	int size = ll.size();
	for(int i=0;i<size;i++){%>
	<tr><td><%=ll.get(i).get(0) %></td><td><%=ll.get(i).get(1) %></td></tr>
 <%}%>
</table>