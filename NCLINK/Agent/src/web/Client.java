package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        try {
            //����һ���ͻ���socket
            Socket socket = new Socket("localhost",8000);
            //��������˴�����Ϣ
            OutputStream ots = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(ots);
            pw.write("{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_BH/840D_01\" }");
            pw.flush();
            //�ر������
            socket.shutdownOutput();
            //��ȡ�������˴��ݵ�����
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String info = null;
            while((info=br.readLine())!=null){
                System.out.println("���ǿͻ��ˣ�������˵��"+info);
            }
            //�ر���Դ
            br.close();
            isr.close();
            is.close();
            pw.close();
            ots.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
