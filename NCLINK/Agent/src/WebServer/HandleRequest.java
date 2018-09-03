package WebServer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.json.JSONObject;

import Adapter.Calculation;
import Adapter.RemoteClient;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class HandleRequest {

	public String request;
	//public String response;
	private PrintStream out;
	
	public HandleRequest(String request,PrintStream out){
		this.request = request;
		//this.response = null;
		this.out = out;
	}
	//������Ӧ,������һ���Է���
    private void sendResponse(String response){
    	sendResponseHeader();
		out.println(response);
		out.flush();
    }
  //��Ӧͷ
    private void sendResponseHeader(){
    	out.println("HTTP/1.0 200 OK"); 
		out.println("MIME_version:1.0");
		out.println("Content_Type:application/json");//text/html
		out.println("");//����ͷ����Ϣ֮��Ҫ��һ��
    }
	//������Ӧ�壬�����ڷֿ��η���
    private void sendResponseBody(String response){
		out.println(response);
		out.flush();
    }
	
	//������������
	public void getResponse(){
    	
    	//requestתjson
    	JSONObject jsonRequest;
    	try{
    		jsonRequest = new JSONObject(request);
    	}
    	catch(Exception e){
    		e.printStackTrace();	//***
    		sendResponse("{\"code\":\"faided\";\"description\":\"json parse wrong\"}");
    		return;
    	}
		System.out.println(jsonRequest);
		//��ȡtype
		String requestType=jsonRequest.get("type").toString().toUpperCase();
		//��ȡname
		String name = jsonRequest.get("path").toString();
		name = name.substring(0,1).equals("/") ? name.substring(1) : name; //ȥ����ͷ��"/"
		//���·����û��ֵ��ֻ����PROBE���󣬷������л�����probe�ļ�
		if(name.equals("")){
			if(requestType.equals("PROBE")){
				sendResponseHeader();
				for(int i=0;i<MachineInfo.actMachineCount;i++){
					String s;
					//dll��������
					if(Calculation.getType(i).equals("dll")){
						IntByReference len = new IntByReference(MachineInfo.buffLen);
						Pointer buff = new Memory(MachineInfo.buffLen);
						JNA.CLibrary.Instance[i].PROBE(request,buff,len);
						
						int realLen = len.getValue();
						byte[] byteArray = buff.getByteArray(0, realLen);
						
						try {
							s = new String(byteArray,"GB2312");
						} catch (UnsupportedEncodingException e) {
							s = "wrong";
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						s = new RemoteClient().getData(MachineInfo.ip[i], MachineInfo.port[i], request);
						
					}
					sendResponseBody(s);
				}
				return;
	    	}else{
	    		sendResponse("{\"code\":\"faided\";\"description\":\"wrong request type.\"}");
	    		return;
	    	}
		}
		//��ȡindex,�����������Ӧ��dlld�±�
		int index = Calculation.getIndex(MachineInfo.name, name);
		if(index < 0){
			System.out.println(request+" : can't find the index in Instance.");
			sendResponse("{\"code\":\"faided\";\"description\":\"do not find the mechine.\"}");
			return;
		}
		if((requestType.equals("SAMPLES"))||(requestType.equals("CURRENT"))){
			//��txt�ļ�
			String fileName = name.replaceAll("/", ".") + ".txt";
    		BufferedReader br = null;
    		try {
    			br = new BufferedReader(new FileReader(fileName));
    		} catch (FileNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			System.out.println("can't find the file");
    			sendResponse("{\"code\":\"faided\";\"description\":\"can't find the data.\"}");
    			return;
    		}
    		//�Ƿ����interval���Ƿ���Ҫһֱ�ɼ�
			int interval=-1;
			boolean flag = true;
			try{
				interval = jsonRequest.getInt("interval");
			}catch(Exception e){
				flag = false;
			}
			//����interval��������������
			if(flag){
				if(interval < 0){
					System.out.println("interval parameter wrong");
	    			sendResponse("{\"code\":\"faided\";\"description\":\"interval parameter < 0.\"}");
	    			return;
				}
				int maxBlock = MachineInfo.maxBlock[index];
				if(maxBlock == 0){
	    			System.out.println("maxBlock == 0");
	    			sendResponse("{\"code\":\"faided\";\"description\":\"the machine has no data.\"}");
	    			return;
	    		}
				//�������interval��current����
				if(requestType.equals("CURRENT")){
					//long startTime;
					String line;
					int i=0;
					/*
					 * ��ǰ�Ƿ����µ�����д���ĵ�����������ʱ���ſ���readLine;ȷ��ͬ�����⡣
					 * �����maxBlock = MachineInfo.maxBlock[index];ʱ��û�������ݣ�����readLine()ʱ�������������ݣ�����ֵ�ǰ��������
					 */
					boolean havaData = true;
					int currentBlock;
					sendResponseHeader();
					try {
						/*
						 * �����߼���
						 * 1.��ȡ�������maxBlock
						 * 2.��maxBlock������
						 * 3.��Ϣinterval����
						 */
						while(true){
							if(havaData){
								while((line=br.readLine())!=null){
									i++;
									if(i==maxBlock){
										line = "{\"Block\":"+i+","+"\"firstBlock\":1,\"maxBlock\":"+maxBlock+","+line.substring(1)+"\r\n";
										sendResponseBody(line);
										break;
									}
								}
							}
							//startTime=System.currentTimeMillis();
							//while(System.currentTimeMillis()-startTime < interval){}
							Thread.sleep(interval);
							currentBlock = maxBlock;
							maxBlock = MachineInfo.maxBlock[index];
							if(maxBlock == currentBlock){
								havaData = false;
							}
							else{
								havaData = true;
							}
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("readLine() error.");
		    			sendResponse("{\"code\":\"faided\";\"description\":\"data exception.\"}");
		    			return;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Thread.sleep error.");
		    			sendResponse("{\"code\":\"faided\";\"description\":\"interval exception.\"}");
		    			return;
					}
				}
				//�������interval��samples����
				else{
					int from = 0,count=0,end=0;
					String line="";
		            int i=0;
					//�������󣬻�ȡfrom,count
					try{
						from = jsonRequest.getInt("from");
					}catch(Exception e){
						from = 1;
					}
					try{
						count = jsonRequest.getInt("count");
					}catch(Exception e){
						count = 10;
					}
					if((from<1)||(from>maxBlock)){
						System.out.println("from parameter wrong");
		    			sendResponse("{\"code\":\"faided\";\"description\":\"from parameter < 1 or too large.\"}");
		    			return;
					}
					if(count<1){
						System.out.println("count parameter wrong");
		    			sendResponse("{\"code\":\"faided\";\"description\":\"cout parameter < 1.\"}");
		    			return;
					}
					end = from+count-1;//������ֹ��
					end = (end>maxBlock) ? maxBlock : end;	//ȡ��С��
					/*
					 * ��ǰ�Ƿ����µ�����д���ĵ�����������ʱ���ſ���readLine;ȷ��ͬ�����⡣
					 * �����maxBlock = MachineInfo.maxBlock[index];ʱ��û�������ݣ�����readLine()ʱ�������������ݣ�����ֵ�ǰ��������
					 */
					boolean havaData = true;
					int currentBlock;
		            sendResponseHeader();
	    			try {
	    				while(true){
	    					if(havaData){
								while ((line=br.readLine())!=null) {
									i++;
									if((i>=from)&&(i<=end)){
										line = "{\"Block\":"+i+","+"\"firstBlock\":1,\"maxBlock\":"+maxBlock+","+line.substring(1)+"\r\n";
										sendResponseBody(line);
										if(i==end){//ȡ�����һ�����ݣ��˳�
											break;
										}
									}
								}
	    					}
							Thread.sleep(interval);
							maxBlock = MachineInfo.maxBlock[index];
							from = end+1;
							end = from+count-1;//������ֹ��
							end = (end>maxBlock) ? maxBlock : end;	//ȡ��С��
							if(end<from){//û���µ�ֵ
								from--;	//from����ԭֵ
								havaData = false;
							}else{
								havaData = true;
							}
	    				}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//��interval
    		int maxBlock = MachineInfo.maxBlock[index];
    		if(maxBlock == 0){
    			System.out.println("maxBlock == 0");
    			sendResponse("{\"code\":\"faided\";\"description\":\"the machine has no data.\"}");
    			return;
    		}
    		int from = 0,count=0,end=0,at=0;
    		if(requestType.equals("SAMPLES")){
				//�������󣬻�ȡfrom,count
				try{
					from = jsonRequest.getInt("from");
				}catch(Exception e){
					from = 1;
				}
				try{
					count = jsonRequest.getInt("count");
				}catch(Exception e){
					count = 10;
				}
				if((from<1)||(from>maxBlock)){
					System.out.println("from parameter wrong");
	    			sendResponse("{\"code\":\"faided\";\"description\":\"from parameter < 1 or too large.\"}");
	    			return;
				}
				if(count<1){
					System.out.println("count parameter wrong");
	    			sendResponse("{\"code\":\"faided\";\"description\":\"cout parameter < 1.\"}");
	    			return;
				}
				end = from+count-1;//������ֹ��
				if(end > maxBlock){
					end = maxBlock;
				}
    		}
    		else{
    			//�������󣬻�ȡat
    			try{
					at = jsonRequest.getInt("at");
				}catch(Exception e){
					at = maxBlock;
				}
    			if((at<1)||(at>maxBlock)){
					System.out.println("at parameter wrong");
	    			sendResponse("{\"code\":\"faided\";\"description\":\"at parameter parameter < 1 or too large.\"}");
	    			return;
				}
    		}
            String line="";
            int i=0;
            sendResponseHeader();
            try {
            	if(requestType.equals("SAMPLES")){
            		
	    			while ((line=br.readLine())!=null) {
	    				i++;
	    				if((i>=from)&&(i<=end)){
	    					line = "{\"Block\":"+i+","+"\"firstBlock\":1,\"maxBlock\":"+maxBlock+","+line.substring(1)+"\r\n";
	    					sendResponseBody(line);
	    					if(i==end){//ȡ�����һ�����ݣ��˳�
								break;
							}
	    				}
	    			}
            	}
            	else{
            		while ((line=br.readLine())!=null) {
	    				i++;
	    				if(i==at){
	    					line = "{\"Block\":"+i+","+"\"firstBlock\":1,\"maxBlock\":"+maxBlock+","+line.substring(1)+"\r\n";
	    					sendResponseBody(line);
	    					break;
	    				}
	    			}
            	}
            	br.close();
    			return;
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			System.out.println("readLine() error.");
    			sendResponse("{\"code\":\"faided\";\"description\":\"data exception.\"}");
    			return;
    		}
    	}
		//����QUERY��PROBE��SET����
		String s;
		//dll��������
		if(Calculation.getType(index).equals("dll")){
			IntByReference len = new IntByReference(MachineInfo.buffLen);
			Pointer buff = new Memory(MachineInfo.buffLen);
			int result=-100;
	    	if(requestType.equals("QUERY")){
				result = JNA.CLibrary.Instance[index].QUERY(request,buff,len);//����dll
	    	}
	    	else if(requestType.equals("PROBE")){
				result = JNA.CLibrary.Instance[index].PROBE(request,buff,len);//����dll
	    	}
	    	else if(requestType.equals("SET")){
				result = JNA.CLibrary.Instance[index].SET(request,buff,len);//����dll
	    	}
	    	else{  
	    		sendResponse("{\"code\":\"faided\";\"description\":\"wrong request type.\"}");
	    		return;
	    	}
	    	int realLen = len.getValue();
			//System.out.println("result:"+result);
			//System.out.println(realLen);
			byte[] byteArray = buff.getByteArray(0, realLen);
			try {
				s = new String(byteArray,"GB2312");
			} catch (UnsupportedEncodingException e) {
				s = "wrong";
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			s = new RemoteClient().getData(MachineInfo.ip[index], MachineInfo.port[index], request);
			
		}
		//System.out.println("s:"+s);
		sendResponse(s);
		return;
    }
}