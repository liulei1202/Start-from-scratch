package web;

import java.net.ServerSocket;
import java.net.Socket;
import web.CommunicateThread;
public class WebServer {
	
	public static void main(String[] args) {
		int Port = 1231;//�˿ںţ����������ǲ��ԣ����Բ�Ҫʹ�ó��ö˿�
		//���������׽���
		ServerSocket server = null;
		Socket client = null;
		try{
			server = new ServerSocket(Port);
			//��������ʼ����
			System.out.println("The WebServer is listening on port "+server.getLocalPort());
			while(true){
				client = server.accept();
				//���߳�����
				new CommunicateThread(client).start();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
