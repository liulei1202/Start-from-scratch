<%@page import="java.net.URLDecoder"%>
<%@ page import="user.*" %>
<%@ page import="DBConnection.*" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

  <%
    String userId = URLDecoder.decode(request.getParameter("userId"), "UTF-8");
    String confirm = URLDecoder.decode(request.getParameter("confirm"), "UTF-8");
    int temp = 0;
    userId = userId.trim();
    //System.out.println(confirm);
    if(confirm.trim().equals("通过")){
    	temp = 1;
    }else if(confirm.trim().equals("拒绝")){
    	temp = -1;
    }else {
    	temp = 0;
    }
    
    String sql = "update emcUserinfo set confirm= " + temp + " where userID='" + userId + "'";
    EMCDB emcdb = new EMCDB();
	emcdb.dynaStm = emcdb.newStatement();
	int result = emcdb.UpdateSQL(sql);
	emcdb.closeStm();
    if(result>0){%>
    	true
    <%}else{%>
    	false
	<%}%>
