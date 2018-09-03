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
            //����һ���ͻ���socket
        	Socket socket = new Socket(ip,port);
            
            //��������˴�����Ϣ
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
            //�ر������
            socket.shutdownOutput();
            //��ȡ�������˴��ݵ�����
            InputStream ins = socket.getInputStream();
            DataInputStream in = new DataInputStream(ins);
            byte[] buffer = new byte[MachineInfo.buffLen];  //�������Ĵ�С
            in.read(buffer);               //������յ��ı��ģ�ת�����ַ���
            /**
             * C++���ݹ����������֣���Ҫת��һ�¡�C++Ĭ��ʹ��GBK��
             * GB2312��GBK���Ӽ���ֻ�м������ġ���Ϊ���ݿ���GB2312����������ֱ��תΪGB2312
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

