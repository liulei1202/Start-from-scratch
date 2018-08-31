package DBConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnDB {
	
	public Connection conn;
	public Statement stm;
	public ResultSet rs;

	//String url = "jdbc:odbc:myDataSource";
	private	String url;// = "jdbc:oracle:thin:@192.168.0.100:1521/orcl";
	private String username;// = "SYSTEM";//"" for access
	private String password;// = "orcl"; //"" for access
	
	/**
	 * ���ı��ļ���ʽ����������ļ���ʼ��
	 * @param initFile �����ļ����ļ���
	 * @return ����ʼ���ɹ�������true,���򷵻�false
	 */
	public boolean init(String initFile){
	
		BufferedReader configFile;
		try {
			configFile = new BufferedReader( new FileReader(initFile));
			String lineBuffer;
			int beginIndex;
			while(configFile.ready()){
				lineBuffer = configFile.readLine();
				lineBuffer = lineBuffer.toUpperCase();
				beginIndex = lineBuffer.indexOf("[IP]");
				if (beginIndex >= 0 ){
					url = "jdbc:oracle:thin:@" + lineBuffer.substring(beginIndex + 4);
					continue;
				}
				beginIndex = lineBuffer.indexOf("[USERNAME]");
				if (beginIndex >= 0 ){
					username = lineBuffer.substring(beginIndex + 10);
					continue;
				}
				beginIndex = lineBuffer.indexOf("[PASSWORD]");
				if (beginIndex >= 0 ){
					password = lineBuffer.substring(beginIndex + 10);
					continue;
				}
			}
			configFile.close();
			
		} catch( IOException e) {
			System.out.println("DataBase connection configuration file error:" + initFile);
			return false;
		} 
		//System.out.println(url + username + password);
	    try	    {
	    	//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	    	Class.forName("oracle.jdbc.driver.OracleDriver");
	    	//Class.forName("dm.jdbc.driver.DmDriver");
	    	
	    	conn = DriverManager.getConnection(url,username,password);
	    }  catch(ClassNotFoundException snfex)    {
	      System.err.println("�޷�װ��Jdbc-Odbc����");
	      snfex.printStackTrace();
	      return false;
	      //System.exit(1);
	    }  catch(SQLException sqlex)    {
	      System.err.println("�޷�������ݿ�");
	      sqlex.printStackTrace();
	      //System.exit(1);
	      return false;
	    }
	    
	    try   {
	      stm = conn.createStatement();
	    }  catch(SQLException sqlex)  {
	      sqlex.printStackTrace();
	      return false;
	    }
		
		return true;
	}
	
	/**
	 * ���ڸ����ĳ�ʼ�����ú���
	 * @param driver ��������
	 * @param url ָ����ݿ��������URL
	 * @param username ������ݿ������û���
	 * @param password ������ݿ�����Ӧ�û��Ŀ���
	 * @return
	 */
	public boolean init(String driver, String url, String username, String password){
	
		//driver = "";
		//url = "jdbc:dm://" + url ;
		//url = "jdbc:oracle:thin:@" + url;//useUnicode=true&characterEncoding=UTF-8
		//url = url + "?useUnicode=true;characterEncoding=UTF8";
		//System.out.println("conndb:" + driver +"-"+ url+"-" + username+"-" + password);
		
		try	    {
	    	//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//Class.forName("oracle.jdbc.OracleDriver");	
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.100/orcl","System","orcl");
			Class.forName(driver);		
			conn = DriverManager.getConnection(url,username,password);
	    }  catch(ClassNotFoundException snfex)    {
	      System.err.println("找不到Jdbc-Odbc驱动");
	      snfex.printStackTrace();
	      return false;
	      //System.exit(1);
	    }  catch(SQLException sqlex)    {
	      System.err.println("连接Oracle失败");
	      sqlex.printStackTrace();
	      //System.exit(1);
	      return false;
	    }
	    
	    try   {
	      stm = conn.createStatement();
	    }  catch(SQLException sqlex)  {
	      sqlex.printStackTrace();
	      return false;
	    }
		
	    rs = null;
		return true;
		
	}
	
	/**
	 * Ĭ�ϵĹ��캯��
	 * ʹ�øù��캯��֮�󣬽����ʵ��������г�ʼ����������ܳ�������ݿ�����Ӳ�ʧ�ܡ�
	 */
	public ConnDB() {

		url = "jdbc:oracle:thin:@192.168.0.100:1521/orcl";
		username = "SYSTEM";//"" for access
		password = "orcl"; //"" for access

	}
	/**
	 * �ر���ݿ������
	 */
	public void CloseDB()	  {
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(stm!=null)
			try {
				stm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(conn!=null)
			try{
		      conn.close();
		    }   catch(SQLException sqlex)   {
		      System.err.println("Unable close connection");
		      sqlex.printStackTrace();
		      //System.exit(1);
		    }
	  }
	/**
	 * ��ʹ��ݿ�ִ��һ��sql����
	 * @param sql ��Ҫִ�е�sql����
	 * @return ��ݿ�ִ��sql����������¼�ĸ���
	 */
	public  int  exeSQL(String sql){
		
		try {
			return stm.executeUpdate(sql);
		 }catch (SQLException sqlex){		  
			sqlex.printStackTrace();
			return 0;
		 }
	}
	
	/**
	 * ��ʹ��ݿ�ִ��һ��select ���
	 * @param sql ��ִ�е�sql���
	 * @return sql ��ѯ�����Ľ��
	 */
	public  ResultSet   selectSQL(String sql){
		
		try {
			return stm.executeQuery(sql);
		}catch (SQLException sqlex){		  
			sqlex.printStackTrace();
			
		}
		return rs;// �Ƿ�Ӧ�÷���null
	}
	

	
}
