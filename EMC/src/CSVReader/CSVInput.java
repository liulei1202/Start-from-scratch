package CSVReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CSVInput {
	
	/**
	 * 操作xml源文件的变量
	 */
	private BufferedReader inFile;
	private ShareSpace space;
	public static Boolean m_FileOpenFlag;	//标识文件是否打开，如果打开文件成功，则取值为TRUE,否则聚会为FALSE. 所有的操作在该变量为FALSE情况下均被忽略
	public boolean dataOver;/*标志是否将文件中的内容读入到内存。如果读取完毕，其值为true,否则为false. */
//	private Thread ReaderThread;
	public String lineBuffer;
	
	/**
	 * 默认的构造函数，使用该构造函数，必须通过调用init(String FilePathandName)给出XML数据文件，进行初始化
	 */
	public CSVInput(){
		System.out.println("CSVInput Constructor");
		inFile = null;
		space = new ShareSpace();
		m_FileOpenFlag = false;
		dataOver = false;
		lineBuffer = null;
	}
	


	public boolean init(String csvFile){

		//打开文件
		//System.out.println("csvinput.java: filename:" + csvFile);
		try {

			inFile = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"GBK"));//UTF-8出现乱码
		} catch( IOException e) {
			System.out.println("EMC CSV Parser.java : Open  file " + csvFile + " failed!");
			return false;
		}
		m_FileOpenFlag = true;
		readCSV();	
		
		return true;
	}
	
	public boolean readCSV(){
		if(dataOver||space.isFull()){
			return true;
		}
		int i;
		int len = space.getEmptyLength();
		
		
		try {
			for(i = 0; i < len && inFile.ready(); i++){
				lineBuffer = inFile.readLine();
				space.inSpace(lineBuffer);
			}
			if(i < len){
				dataOver = true;
				space.dataOver = true;
				inFile.close();
			} 
			//System.out.println("queue is loading " + i + " items.");
			return true;
		} catch (IOException e) {
			System.out.println("CSV Data load failed!");
			e.printStackTrace();
			return false;
		}
	}
	
	//关闭对监测文件的操作。中止所有线程的运行。
	public void close(){
		try {
			if(inFile!=null){
				inFile.close();
			}
			m_FileOpenFlag=false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("inFile.close().");
	}
	


	
	public String getNextLine(){

		while(space.isEmpty() && !dataOver){
			readCSV();
		}
		if (space.dataOver && space.isEmpty())
			return null;
		else{
			lineBuffer = space.outSpace();
			return lineBuffer;
		}
	}
	/**
	 * 统一对类中的变量初始化为默认值。
	
	private void initState(){

		//System.out.println("csvinput.java:initState()");
		
		
	}
	*/
}




/*
package CSVReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class CSVInput {
	

	private BufferedReader inFile;
	private ShareSpace space;

	public static Boolean m_FileOpenFlag;	//标识文件是否打开，如果打开文件成功，则取值为TRUE,否则聚会为FALSE. 所有的操作在该变量为FALSE情况下均被忽略


	private Thread ReaderThread;

	public String lineBuffer;
	

	private void initState(){

		System.out.println("csvinput.java:initState()");
		inFile = null;
		m_FileOpenFlag = false;
		
		space = new ShareSpace();
		
	}
	

	public boolean init(String csvFile){

		//打开文件
		System.out.println("csvinput.java: filename:" + csvFile);
		try {

			inFile = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"GBK"));//UTF-8出现乱码
		} catch( IOException e) {
			System.out.println("EMC CSV Parser.java : Open  file " + csvFile + " failed!");
			return false;
		}
		
		m_FileOpenFlag = true;
		ReaderThread = new Thread(new PIPE(space,inFile));
		ReaderThread.setPriority(4);
		ReaderThread.start();	
		
		return true;
	}
	
	//关闭对监测文件的操作。中止所有线程的运行。
	public void close(){
		try {
			if(inFile!=null){
				inFile.close();
			}
			m_FileOpenFlag=false;
			ReaderThread.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("EMC csv Parser.java : " + "thread is stopped.");
	}
	

	public CSVInput(){
		System.out.println("CSVInput Constructor");
		initState();
	}

	
	public String getNextLine(){

		while(space.isEmpty() && !space.dataOver)
			Thread.yield();
		
		if (space.dataOver && space.isEmpty())
			return null;
		else{
			lineBuffer = space.outSpace();
			return lineBuffer;
		}
	}

}
*/