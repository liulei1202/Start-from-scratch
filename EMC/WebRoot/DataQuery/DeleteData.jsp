<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="DataQuery.Handle" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
 <%
   String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
   String type = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
   String dataType = java.net.URLDecoder.decode(request.getParameter("dataType"),"UTF-8");
   boolean result = false;
   
   String regEX = "[\\u4e00-\\u9fa5]";
   Pattern p = Pattern.compile(regEX);
   Matcher m = p.matcher(dataType);
   
   if(m.find()){//说明存在汉字，为图形数据文件。汉字是图形类型。
   		//System.out.println(dataType);
   		result = Handle.deleteParaGrap(bh,type,dataType);//图形数据的bh不唯一，删除时需要加上图形类型
   }
   else if(type.contains("JCBG")||type.contains("ZFQJC")){
   		result = Handle.deleteOtherData(bh,type,dataType);
   }
   else if(dataType.equals("")){/*说明删除的是原始监测数据*/
   		result = Handle.deleteOriginalData(bh,type);
   }else{
   		result = Handle.deleteOtherData(bh,type,dataType);
   }
  %>
<%=result%>