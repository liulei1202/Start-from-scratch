package WebServer;

import org.json.JSONArray;
import org.json.JSONObject;

public class MachineInfo {
	
	public static JSONArray config;
	public static int buffLen = 102400;	//dll调用的buff大小
	public static int MaxMachineCount = 5;//最大可连接机床数
	public static int actMachineCount;//实际连接机床数
	
	public static String[] name;  	//机床名称
	static String[] dllFileName;	//机床对应dll的名称
	public static String[] ip;
	public static int[] port;
	public static int[] maxBlock;	//每个机床文件存储的数据量
	
	public MachineInfo(JSONArray config,JSONObject mechineInfo){
	}
}
