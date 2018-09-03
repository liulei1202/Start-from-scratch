package web;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//import net.sf.json.JSONObject;
//import org.json.*;
import WebServer.JNA.CLibrary;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.lang.String;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class test {
	public static int count;
	public static String[] paths;// = "Adapter_V3.0d.dll";
	public static int i;
	//public static String dllpath;
	public interface CLibrary extends Library {
		// msvcrt为dll名称,msvcrt目录的位置为:C:\Windows\System32下面,
		// int  QUERY(char *request, char* buff, int *len)
		//CLibrary Instance = (CLibrary) Native.loadLibrary(paths[0],CLibrary.class);
		//CLibrary Instance1 = (CLibrary) Native.loadLibrary(paths[1],CLibrary.class);
		String path = CLibrary.class.getResource("/").getPath() + "Adapter_V3.0d.dll";
		String dllpath = (CLibrary.class.getResource("/").getPath() + "Adapter_V3.0d.dll").substring(1);
		CLibrary[] Instance = {
				(CLibrary) Native.loadLibrary(paths[0],CLibrary.class),
				(CLibrary) Native.loadLibrary(paths[1],CLibrary.class),
				(CLibrary) Native.loadLibrary(paths[2],CLibrary.class)
				};
		
		//CLibrary[] Instance = new CLibrary[count];
		
		//Instance[0] = (CLibrary) Native.loadLibrary(paths[i],CLibrary.class);
		/*
		for(i=0;i<count;i++){
		
			Instance[i] = (CLibrary) Native.loadLibrary(paths[i],CLibrary.class);
		}
		*/
		//String dllpath = (CLibrary.class.getResource("/").getPath()+"Adapter_V3.0d.dll").substring(1);
		//CLibrary Instance = (CLibrary) Native.loadLibrary(dllpath,CLibrary.class);
		int QUERY(String request, Pointer buff, IntByReference len);
		
		//CLibrary Instance = (CLibrary) Native.loadLibrary("DLL1",CLibrary.class);
		//int add(int a,int b);
	}
	public static void main(String[] args) {
		
		/*
		String fileName = "NC_LINK_BH.840D_01.txt";
		//FileReader fr=new FileReader(fileName);
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line="";
        int i=0;
        int from = 5;
        int count = 10;
        int j=0;
        try {
        	
			while (true) {
				j++;
				System.out.println(j);
				if((line=br.readLine())!=null){
				i++;
				
				//	line = "{\"firstBlock\":1,\"maxBlock\":100,\"Block\":"+i+","+line.substring(1);
					
				System.out.println(line);
				}
				else{
					
					Thread.sleep(500);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//获取json配置文件
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
		
    	JSONArray sampleProbe = machines.getJSONObject(0).getJSONArray("setValues");
    	System.out.println(sampleProbe.length() > 0);
    	System.out.println(sampleProbe);
		/*
		paths = new String[3];
		paths[0] = "Adapter_V3.0d.dll";
		paths[1] = "opc_840d.dll";
		int index = Arrays.binarySearch(paths, "opc_840d.dll");
		System.out.println(index);
		*/
		/*
		CLibrary t = test.CLibrary.Instance[2];
		int index = Arrays.binarySearch(paths,"321");
		System.out.println(index);
		
		String[] request = new String[2];
		request[0] = "{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_EXAMPLE/Machine_01\" }";
		request[1] = "{ \"type\": \"QUERY\",\"path\":\"/NC_LINK_BH/840D_01\" }";
		
		for(int i=0;i<2;i++){
			IntByReference len = new IntByReference(1024);
			Pointer buff = new Memory(1024);
			
			int result = t.QUERY(request[i],buff,len);	//test.CLibrary.Instance
			System.out.println("result:"+result);
			System.out.println("request:"+request[i]);
			int realLen = len.getValue();
			
			//方法2
			byte[] byteArray = buff.getByteArray(0, realLen);
			String s;
			try {
				s = new String(byteArray,"GB2312");
			} catch (UnsupportedEncodingException e) {
				s = "wrong";
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("s:"+s);
			System.out.println(len.getValue());
		}
		
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}

/*
public class DllTest {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)Native.loadLibrary("CDLL", CLibrary.class);

        int add(int a, int b);
    }

    public static void main(String[] args) {
        int sum = CLibrary.INSTANCE.add(3, 6);

        System.out.println(sum);
    }
}*/
/*
String fileName = "/NC_LINK_EXAMPLE/Machine_01.txt";	///NC_LINK_EXAMPLE/Machine_01
fileName = fileName.replaceAll("/", "");
File file = new File(fileName);
BufferedWriter writer = null;
try {
	if(!file.exists()){
		file.createNewFile();
	}
	writer = new BufferedWriter(new FileWriter(file,true));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	System.out.println("createNewFile failed.");
}
for(int i=0;i<3;i++){
	try {
		writer.write(new Date().toString());
		System.out.println(i);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		System.out.println("IOException.");
	}
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("InterruptedException.");
	}
}
try {
	writer.flush();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
try {
	writer.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
*/