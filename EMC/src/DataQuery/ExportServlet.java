package DataQuery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DBConnection.*;
import oracle.sql.BLOB;
import BaseData.*;

public class ExportServlet extends HttpServlet{
	 
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	             throws ServletException, IOException {
		  
		//request.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html;charset=utf-8");
		//response.setHeader("content-type", "text/html;charset=UTF-8");
        //response.setCharacterEncoding("UTF-8");
		  boolean flag = false;
		  String bh = java.net.URLDecoder.decode(request.getParameter("bh"),"UTF-8");
		  bh = bh.trim();
		  String type  = java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8");
		  String dataType  = java.net.URLDecoder.decode(request.getParameter("dataType"),"UTF-8");
	
		  String sql=Handle.getExportFileSql(bh,type,dataType);
		  System.out.println("sql:"+sql);
		  
		  OutputStream out=null;
		  BLOB blob = null;
		  try {
			EMCDB db = new EMCDB();
			db.connDB("");
			db.dynaRs = db.dynaStm.executeQuery(sql);
			if(db.dynaRs.next()){
				blob = (BLOB) db.dynaRs.getBlob("data");
				 //处理文件名
				 bh = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
		         String realname =  bh +".zip";
		        //设置响应头，控制浏览器下载该文件
		     	 response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
		       
				InputStream in = blob.getBinaryStream();
				byte[] buffer = new byte[1024];
				int length = -1;
				 //创建输出流
		        out = response.getOutputStream();
				while ((length = in.read(buffer, 0, 1024)) != -1) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.flush();
				out.close();
				flag = true;
			}
			db.dynaConn.commit();
			db.closeDB();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         doGet(request, response);
    }
	     
}
