<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.fileupload.*" %>
<%@ page import="java.io.File" %>
<%@ page import="DataQuery.Handle" %>
<%@ page import="DBConnection.*" %>
<% //System.out.println("DeleteFile.jsp");
   String userId = session.getAttribute("userID").toString();
   //System.out.println(userId);
   String base =  session.getAttribute("tempWorkDir").toString();
   //System.out.println(base);
   ImportFile infile=new ImportFile();
   String basepath=infile.getBasePath(base);/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\  */
   basepath = basepath +  userId + File.separator;/* E:\EMC\.metadata\.me_tcat\webapps\rsui\tempDir\a\ */
   //System.out.println(basepath);
   Handle.clearFiles(basepath);

   //EMCDB.quit();
%>