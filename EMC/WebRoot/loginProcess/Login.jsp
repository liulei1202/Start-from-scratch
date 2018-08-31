<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

  <%
  	int role = Integer.parseInt(session.getAttribute("role").toString());
    switch(role){
	
	case 1:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForAdmin.jsp";</script>
	<%	break;
	case 2:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForDataMan.jsp";</script>
	<%	break;
	case 3:%>
		<!-- <jsp:forward page="MainPageForDataReq.jsp" /> -->
		<script>window.location.href="/rsui/loginProcess/MainPageForDataReq.jsp";</script>
	<%	break;
	case 4:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForSysMan.jsp";</script>
	<%	break;
	case 5:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForSW.jsp";</script>
	<%	break;
	case 6:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForUSW.jsp";</script>
	<%	break;
	case 7:%>
		
		<script>window.location.href="/rsui/loginProcess/MainPageForSat.jsp";</script>
	<%	break;
	
		default:%>
		<jsp:forward page="/index.jsp" />
	<%}%>
 
