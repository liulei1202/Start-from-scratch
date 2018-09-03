package Adapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import WebServer.JNA;
import WebServer.MachineInfo;

public class CollectThread implements Runnable{

	public JSONObject equipment;
	public String name;
	public int interval;
	public CollectThread(JSONObject machine){
		this.equipment = machine;
		this.name = machine.getString("name");		//�豸����	"NC_LINK_EXAMPLE/Machine_01"
		this.interval = machine.getInt("interval");
	}
	
	@Override
	public void run() {
		//��ȡ�豸���ƺʹ洢�ļ���
		//String path = equipment.getString("path");	
		String fileName = name + ".txt";;	
		fileName = fileName.replaceAll("/", ".");	//�洢�ļ���
		//׼��д���ݵ��ļ�
		File file = new File(fileName);
		BufferedWriter writer = null;
		try {
			if(file.exists()){
				file.delete();
			}
			//System.out.println("file.exists():"+file.exists());
			if(!file.exists()){
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file,true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("createNewFile failed.");
			return;
		}
		//���豸��Ӧ��CLibrary.Instance���±�
		//int index = Arrays.binarySearch((String[])MechineInfo.name,name);	//��ȡname��MechineInfo.name�е��±꣬Ҳ����Instance���±�
		int index = Calculation.getIndex(MachineInfo.name, name);
		if(index < 0){
			System.out.println(name+" : can't find the index in Instance.");
			return;
		}
		String request = "{ \"type\": \"SAMPLES\",\"path\":\"" + name + "\"}";
		long starTime=0;
		while(true){
			starTime=System.currentTimeMillis();
			
			IntByReference len = new IntByReference(MachineInfo.buffLen);
			Pointer buff = new Memory(MachineInfo.buffLen);
			int result = JNA.CLibrary.Instance[index].SAMPLES(request, buff, len);
			
			int realLen = len.getValue();
			byte[] byteArray = buff.getByteArray(0, realLen);
			String s;
			try {
				s = new String(byteArray,"GB2312");
			} catch (UnsupportedEncodingException e) {
				System.out.println(name + " parse response error.");
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			//���ȡ���ݳ�����д���ļ�
			if(result == 0){
				//������д���ĵ�
				try {
					writer.write(s+ "\r\n");
					writer.flush();
				} catch (IOException e) {
					System.out.println(name+" write data to file error.");
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				MachineInfo.maxBlock[index]++;
			}else{
				System.out.println(s);
			}
			while(System.currentTimeMillis() - starTime < interval){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println(name+" Thread.sleep error.");
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		}
		/*
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	
}
/*
JSONArray sampleProbes = mechines.getJSONObject(i).getJSONArray("sampleProbes");
int count = sampleProbes.length();
for(int j=0;j<count;j++){
	JSONObject equipment = sampleProbes.getJSONObject(j);
	System.out.println("Initial " + equipment.getString("path") + "...");
	System.out.println(equipment);
	
}
*/