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
            //创建一个客户端socket
            Socket socket = new Socket("localhost",8000);
            //向服务器端传递信息
            OutputStream ots = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(ots);
            pw.write("{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_BH/840D_01\" }");
            pw.flush();
            //关闭输出流
            socket.shutdownOutput();
            //获取服务器端传递的数据
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String info = null;
            while((info=br.readLine())!=null){
                System.out.println("我是客户端，服务器说："+info);
            }
            //关闭资源
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
