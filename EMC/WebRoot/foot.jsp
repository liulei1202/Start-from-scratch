<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div style="display:none;position:absolute;bottom:0;height:48px;width:100%;background-color:#ffffff;border-top:1px solid">
	<div id="a" style="float:right;margin-right:48px;height:100%;font-size:17px;line-height:48px;color:rgb(102,102,102);">
	</div>
</div>
<script type ="text/JavaScript" >
setInterval("a.innerHTML='版本1.0 版权所有 2017年.  '+new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());", 1000); 
</script>
<%-- 
<font size="1">版本1.0 版权所有 2015年10月1日. 今天是：<%=new Date() %></font>
--%>