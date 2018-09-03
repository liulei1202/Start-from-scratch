package web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.net.Socket;
 
 
//ÿ��һ�����ӽ���ʱ���������ֳ�һ��ͨ�ŵ��߳�
public class CommunicateThread extends Thread{
	//��ͻ���ͨ�ŵ��׽���
	Socket client;
	
	public CommunicateThread(Socket s) {
		client = s;
	}
	
	//��ȡ�����������Դ��·��
	public String getResourcePath(String s){
		// һ���HTTP�����ĵĵ�һ���ǡ�GET /index.html HTTP/1.1��
		// ����Ҫ��ȡ�ľ����м��"/indext.apsx"
		System.out.println(s);
		//��ȡ��Դ��λ��
		String s1 = s.substring(s.indexOf(' ')+1);
		s1 = s1.substring(1,s1.indexOf(' '));
		
		//Ĭ����ԴΪindex.html
		if(s1.equals(""))
			s1 = "index.html";
		
		return s1;
	}
 
	public void sendFile(PrintStream out,File file){
		try{
			DataInputStream in  = new DataInputStream(new FileInputStream(file));
			int len = (int)file.length();
			byte buf[] = new byte[len];
			in.readFully(buf);//��ȡ�����ݵ�buf������
			out.write(buf,0,len);
			out.flush();
			in.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
			}
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		try{
			//��ȡ�û���IP��ַ�Ͷ˿ں�
			String clientIP = client.getInetAddress().toString();
			int clientPort = client.getPort();
			// ����Request�������Խ���
            
			//�������������
			PrintStream out = new PrintStream(client.getOutputStream());
			//��������������
			DataInputStream in = new DataInputStream(client.getInputStream());
			//��ȡ������ύ������
			String content = in.readLine();
			System.out.println(content);
			String msg = content;
			while((content = in.readLine())!=null){
				
				if(content.length() == 0){
					break;
				}
				System.out.println(content);
			}
			
			//��ȡ�ļ�·��
			String fileName = getResourcePath(msg);
			System.out.println("The user asked for resource: "+fileName);
			File file = new File(fileName);
			
			if(file.exists()){//
				//������Ӧ���ĸ�ʽ����
				System.out.println(fileName+" start send");
				
				out.println("HTTP/1.0 200 OK"); 
				out.println("MIME_version:1.0");
				out.println("Content_Type:text/html");
				int len = (int) file.length();
				out.println("Content_Length:"+len);
				out.println("");//����ͷ����Ϣ֮��Ҫ��һ��
				
				//�����ļ�
				sendFile(out,file);
				System.out.println(out);
				out.flush();
			}
			else{
				System.out.println("can't find File");
				
					out.println("HTTP/1.0 200 OK"); 
					out.println("MIME_version:1.0");
					out.println("Content_Type:text/html");
					
					String response = "this is 0 " + new Date();
					
					int len = response.length();
					out.println("Content_Length:"+len);
					out.println(response);
					
					out.println("");//����ͷ����Ϣ֮��Ҫ��һ��
				for(int i=1;i<5;i++){	
					response = "this is "+ i + " " + new Date();
					out.println(response);
				}				
				out.flush();
			}
			client.close();		
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}
	
 
 
}
