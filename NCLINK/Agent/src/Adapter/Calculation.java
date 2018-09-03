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
	 * 根据索引号，获取适配器是远程型，还是dll型
	 * @param index 设备下标索引，需要由getIndex函数取得
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
