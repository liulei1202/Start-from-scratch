package WebServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.*;
import java.net.*;

import Adapter.CollectThread;
import Adapter.RemoteClient;
import Adapter.RemoteCollectThread;
import WebServer.MachineInfo;

public class Agent {

    private int port=8000;
    private String ip="192.168.0.108";
    private ServerSocket serverSocket;

    public Agent() throws IOException {
        
    }

    public void adapter() {
    	//��ȡjson�����ļ�
    	JSONArray machines = null;
    	try {
    		machines = new JSONArray(new JSONTokener(new FileReader(new File("Agent.cnf"))));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("JSONException");
			return;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("FileNotFoundException");
			return;
		}
    	//System.out.println(machines);
    	int machineCount = machines.length();
    	MachineInfo.actMachineCount = machineCount;
    	//ϵͳ֧��MaxmachineCount����������
    	if(machineCount > MachineInfo.MaxMachineCount){
    		System.out.println("machineCount > " + MachineInfo.MaxMachineCount);
    		return;
    	}
    	//���name��dllpath�ַ�����������ʹ��jna����dllǰ���
    	MachineInfo.name = new String[MachineInfo.MaxMachineCount];
    	MachineInfo.dllFileName = new String[MachineInfo.MaxMachineCount];
    	MachineInfo.ip = new String[MachineInfo.MaxMachineCount];
    	MachineInfo.port = new int[MachineInfo.MaxMachineCount];
    	MachineInfo.maxBlock = new int[MachineInfo.MaxMachineCount];
    	for(int i=0;i<machineCount;i++){
    		//mechineInfo.put(mechines.getJSONObject(i).getString("name"), mechines.getJSONObject(i).getString("dlpath"));
    		MachineInfo.name[i] = machines.getJSONObject(i).getString("name");
    		try{
    			MachineInfo.dllFileName[i] = machines.getJSONObject(i).getString("dllpath");
    			MachineInfo.ip[i] = "";
    			MachineInfo.port[i] = 0;
    		}catch(Exception e){
    			MachineInfo.ip[i] = machines.getJSONObject(i).getString("ip");
    			MachineInfo.port[i] = machines.getJSONObject(i).getInt("port");
    			MachineInfo.dllFileName[i] = null;
    		}
    		MachineInfo.maxBlock[i]=0;
    		//System.out.println("����˳��");
    		//System.out.println(MechineInfo.name[i]);
    	}
    	//��ȫ����String[]
    	for(int i=machineCount;i<MachineInfo.MaxMachineCount;i++){
    		MachineInfo.name[i] = "";
    		MachineInfo.dllFileName[i] = null;
    		MachineInfo.ip[i] = "";
    		MachineInfo.port[i] = 0;
    		MachineInfo.maxBlock[i]=0;
    	}
    	//�ڳ�ʼ�������õ�dll�󣬼���CLibrary interface
    	JNA.CLibrary jna = JNA.CLibrary.Instance[0];
    	//��ʼ��ʼ���豸
    	for(int i=0;i<machineCount;i++){
    		JSONObject equipment = machines.getJSONObject(i);
    		System.out.println("Initial " + equipment.getString("name") + "...\r\n");
    		System.out.println(equipment);
    		//����dll�ͳ�ʼ��
			if(MachineInfo.dllFileName[i] != null){
				if(equipment.getJSONArray("setValues").length() > 0){
					IntByReference len = new IntByReference(MachineInfo.buffLen);
					Pointer buff = new Memory(MachineInfo.buffLen);
					//String req = "{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_EXAMPLE/Machine_01\",\"ids\": [{ \"id\": \"version\" }, { \"id\": \"isoclassid\"}] }";
					int result = JNA.CLibrary.Instance[i].SET(equipment.toString(),buff,len);//����dll *** SetSampleProbeδת��ΪSet
					
					int realLen = len.getValue();
					byte[] byteArray = buff.getByteArray(0, realLen);
					String s;
					try {
						s = new String(byteArray,"GB2312");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("SetSampleProbe failed: response parse wrong.");
						continue;
					}
					System.out.println(s + "\r\n");
					if(result == 0){
						System.out.println("Initial " + equipment.getString("name") + " succeed!\r\n");
		    			
					}else{
						System.out.println("Initial " + equipment.getString("name") + " failed!\r\n");
					}
				}else{
					System.out.println("setValues is null!\r\n");
				}
				Thread collectThread = new Thread(new CollectThread(equipment));
				collectThread.start();
			}
			//Զ��ip,port�ͳ�ʼ��
			else{
				if(equipment.getJSONArray("setValues").length() > 0){
					JSONObject json = new JSONObject(new RemoteClient().getData(MachineInfo.ip[i], MachineInfo.port[i], equipment.toString()));
					System.out.println(json + "\r\n");
					if(json.getString("code").toUpperCase().equals("SUCCESS")){
						System.out.println("Initial " + equipment.getString("name") + " succeed!\r\n");
					}else{
						System.out.println("Initial " + equipment.getString("name") + " failed!\r\n");
					}
				}else{
					System.out.println("setValues is null!\r\n");
				}
				Thread collectThread = new Thread(new RemoteCollectThread(equipment));
				collectThread.start();
			}
    	}	
    }
    
	public void service() {
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("new ServerSocket() failed.");
			return;
		}    
		// ����������
        System.out.println("serverSocket is listening on port: " + port);
        while (!serverSocket.isClosed()) {
            Socket socket=null;
            try {
                socket = serverSocket.accept();      // ���տͻ�����  
                
                Thread workThread=new Thread(new SocketThread(socket));   // ����һ�������̡߳��ѽ��ܵ���socket���봦��
                workThread.start();        // ���������߳�
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[])throws IOException {
    	
        Agent agent = new Agent();
        agent.adapter();
        agent.service();
    }

}
