package EMCTools;

import java.io.*;
import java.util.*;  
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;  
import org.apache.commons.fileupload.disk.*;
/**
 * 上传文件的通用类。
 * 使用方法：
 * 1. 申明一个本类的变量，EMCFileUpload fu;
 * 2. 调用构造函数对变量进行实例化： fu = new EMCFileUpload();
 * 3. 读取客户端上传的文件： fu.fetchFiles(request, user); 其中request就是jsp环境中的内置变量request,user是打开页面的用户名称。
 * 说明：这里的用户名是为了区分不同用户上传的文件名。
 * 该函数返回为true时表示上传文件没有出现问题，否则会有ServletException,IOException异常，需要用户处理
 * 
 * 说明：
 * 本类处理的文件数量不能大于100个，否则 会出现问题，出现的问题不详
 * @author lenovo
 *
 */
public  class EMCFileUpload {
	
	private String base;
		
	/**
	 * 该类的构造函数。
	 * @param base String 类型的值，说明文件传过程中临时存贮文件的目录，以及上传上文件存放的目录。
	 */
	public EMCFileUpload(String base){

		this.base = base;
	}
	
	/**
	 * 实现上传文件的函数。在服务器端调用该函数可以实现客户端指定文件的上传。
	 * 
	 * @param request 上传文件页面传回的变量
	 * @param user 表示上传文件的用户，该函数将用户名+序号作为上传文件的文件名，对上传文件的存贮
	 * @return 如果上传成功返回true,否则如果上传类型不正确，返回假。上传过程中出现错误，抛出异常。
	 * @throws ServletException
	 * @throws IOException
	 */
	
public boolean fetchFiles(HttpServletRequest request, String user) throws ServletException,IOException{ 
		
		//System.out.println("EMCfileupload.java: fetchFiles:" + user);
		
		int maxFileSize = 10000 * 1024;
		int maxMemSize = 10 * 1024;
		
		request.setCharacterEncoding("UTF-8");
		
		// 駼下传类型的内容
		String contentType = request.getContentType();
		//System.out.println(contentType);
		
		//if(contentType == null || "".equals(contentType ) || contentType.indexOf("multipart/form-data") < 0)
		//	return false;
		//设置文件存贮的环境
		DiskFileItemFactory factory = new DiskFileItemFactory();
	    // 设置内存中存储文件的最大值
		factory.setSizeThreshold(maxMemSize);
		// 本地存储的数据大于 maxMemSize.
		File file = new File(base);
		if(!file.exists())
			file.mkdirs();
		factory.setRepository(file);
	    
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
		
	    List<FileItem> items;  
	    //System.out.println(".java: ready to fetch");
	    try {  
	        items = upload.parseRequest(request); 
	        //获取文件中的内容
		    //System.out.println("EMCFileUload.java: number of items uploaded " + items.size());
		    for (Object val : items) {  
		    	//System.out.println(".java : i is " + i);
		        FileItem item = (FileItem) val;  
		        if (item.isFormField()) {   // 不是文件类型
		        	//System.out.println("EMCFileUload.java: file upload.java(not file): " + item.getFieldName() + ":" + item.getString("UTF-8"));
		        	continue;
		        } else {  //文件类型
		        	String fileName = item.getName();
		        	int slashPos = fileName.lastIndexOf('\\');	
		        	fileName = user + fileName.substring(slashPos + 1);
		        	//fileName = fileName.substring(slashPos + 1);
		        	//System.out.println("EMCFileUload.java: file upload.java:" + fileName);//该条语句用于测试，观察上传的文件名
		        	File f = new File(base + "\\" +fileName);
		        	if (f.exists())
		        		f.delete();
		           item.write(f);              
		        }
		    }
	        
	    } catch (FileUploadException e) {  
	        throw new ServletException(e);
	    }  catch (IOException e) {  
            throw e;  
        } catch (Exception e) {  
            throw new ServletException(e);  
        }   
	    
	    return true;
	} 
}
