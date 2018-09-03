package WebServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Date;

import org.json.JSONObject;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


//import net.sf.json.JSONObject;

	// 线程处理器类，负责与单个客户的通信。
class SocketThread implements Runnable{       
	//private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private PrintStream out;
    private String firstLine;
    private String requestType;
    private String url;
    
    public SocketThread(Socket socket){
        this.socket=socket;
        in = null;
        out = null;
        firstLine = null;
        requestType = null;
        url = null;
    }
    
    public SocketThread(ServerSocket serverSocket,Socket socket){
    	//this.serverSocket=serverSocket;
        this.socket=socket;
        firstLine = null;
    }
    private String getRequestType(){
    	int index = firstLine.indexOf(' ');
    	return firstLine.substring(0,index).toUpperCase();
    }
    private String getURL(){
    	int index = firstLine.indexOf(' ');
    	int end = firstLine.lastIndexOf(' ');
    	return firstLine.substring(index+2,end);
    	
    }
    private String handleException(){
    	
    	if(!requestType.equals("GET")){
        	return "{\"code\":\"faided\";\"description\":\"NOT A GET request.\"}";
        }
    	if(url.length() == 0){
        	return "{\"code\":\"faided\";\"description\":\"no request content.\"}";
    	}
        if(url.equals("favicon.ico")){
        	return "favicon.ico";
        }
        try {
			url = java.net.URLDecoder.decode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"code\":\"faided\";\"description\":\"decode request failed.\"}";
		}
        
        return null;
    }
    //输出请求的全部内容
    private void getContent(){
    	String msg = null;
		//取请求内容
        try {
			while ((msg = in.readLine()) != null) {    // 接收和发送数据，直到通信结束。
				System.out.print(msg.length()+": ");
			    System.out.println(msg);
			    if(msg.length() == 0){
			    	break;
			    }
			}
		} catch (IOException e) {
			System.out.println("readLine() failed.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //发送响应
    private void sendResponse(String response){
    	out.println("HTTP/1.0 200 OK"); 
		out.println("MIME_version:1.0");
		out.println("Content_Type:application/json");//text/html
		
		//String response = "this is 0 :" + new Date();
		int len = response.length();
		//out.println("Content_Length:"+len);
		out.println("");//报文头和信息之间要空一行
		out.println(response);
		out.flush();
		/*
		while(true){
			out.println(response);
			out.flush();
		}
		*/
    }
    
    public void run(){
        try {
            
            in = new DataInputStream(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            firstLine = in.readLine();
            
            requestType = getRequestType();
            
            url = getURL();
            
            //getContent();
            
            String result = handleException();
            if(result != null){
            	sendResponse(result);
            }
            else{
            	new HandleRequest(url,out).getResponse();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(socket!=null) {
                	socket.close();       // 断开连接
                	//System.out.println("socket.close()");
                }
                
            }
            catch (IOException e){
            	e.printStackTrace();
            }
        }
    }
}