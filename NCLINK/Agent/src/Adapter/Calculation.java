package Adapter;

import WebServer.MachineInfo;

public class Calculation {

	public static int getIndex(String[] array,String value){
		for(int i=0;i<array.length;i++){
			if(array[i].equals(value)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * ���������ţ���ȡ��������Զ���ͣ�����dll��
	 * @param index �豸�±���������Ҫ��getIndex����ȡ��
	 * @return remote or dll
	 */
	public static String getType(int index){
		if(!MachineInfo.ip[index].equals("")){
			return "remote";
		}else{
			return "dll";
		}
	}
}
