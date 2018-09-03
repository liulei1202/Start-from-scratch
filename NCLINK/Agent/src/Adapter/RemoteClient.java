package Adapter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import WebServer.MachineInfo;

public class RemoteClient {

    public String getData(String ip,int port,String request) {
    	String message;
        try {
            //创建一个客户端socket
        	Socket socket = new Socket(ip,port);
            
            //向服务器端传递信息
        	OutputStream outs = socket.getOutputStream();
        	PrintWriter pw = new PrintWriter(outs);
            /*
            String a = "{ \"type\": \"PROBE\",\"path\":\"/NC_LINK_BH/840D_01\" }";
            String b = "{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_BH/840D_01\" }";
            String c = "{ \"type\": \"PROBE\",\"path\":\"/NC_LINK_EXAMPLE/Machine_01\" }";
            String d = "{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_EXAMPLE/Machine_01\" }";
            */
            pw.write(request);
            pw.flush();
            //关闭输出流
            socket.shutdownOutput();
            //获取服务器端传递的数据
            InputStream ins = socket.getInputStream();
            DataInputStream in = new DataInputStream(ins);
            byte[] buffer = new byte[MachineInfo.buffLen];  //缓冲区的大小
            in.read(buffer);               //处理接收到的报文，转换成字符串
            /**
             * C++传递过来的中文字，需要转化一下。C++默认使用GBK。
             * GB2312是GBK的子集，只有简体中文。因为数据库用GB2312，所以这里直接转为GB2312
             */
	        message = new String(buffer,"GB2312").trim();
	        //System.out.println(message);
	        in.close();
	        ins.close();
            pw.close();
            outs.close();
            socket.close();
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "{\"code\":\"faided\";\"description\":\"RemoteClient:Unsupported Encoding Exception.\"}";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "{\"code\":\"faided\";\"description\":\"RemoteClient Exception.\"}";
		}
        return message;
        /*
        JSONObject json;
        try{
        	json = new JSONObject(message);
        }catch(JSONException e){
        	json = new JSONObject("{\"code\":\"faided\";\"description\":\"RemoteClient json parse wrong.\"}");
        }
        return json;
        */
    }

    public RemoteClient(){
    	
    }
}

