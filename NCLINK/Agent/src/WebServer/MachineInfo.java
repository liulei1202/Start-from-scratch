package WebServer;

import org.json.JSONArray;
import org.json.JSONObject;

public class MachineInfo {
	
	public static JSONArray config;
	public static int buffLen = 102400;	//dll���õ�buff��С
	public static int MaxMachineCount = 5;//�������ӻ�����
	public static int actMachineCount;//ʵ�����ӻ�����
	
	public static String[] name;  	//��������
	static String[] dllFileName;	//������Ӧdll������
	public static String[] ip;
	public static int[] port;
	public static int[] maxBlock;	//ÿ�������ļ��洢��������
	
	public MachineInfo(JSONArray config,JSONObject mechineInfo){
	}
}
