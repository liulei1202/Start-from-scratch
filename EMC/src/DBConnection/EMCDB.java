package DBConnection;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DBConnection.AntZipUtil;


public class EMCDB {
	//static public ConnDB conn;
	//static public boolean connected;
	static public Connection connection;
	static public Statement stm;
	static public ResultSet rs;
	
	static public String driver;
	static public String url;
	static public String user;
	static public String pwd;
	
	public Connection dynaConn;
	public PreparedStatement pstm;
	public Statement dynaStm;
	public ResultSet dynaRs;
	/**
	 * Ĭ�ϻ����Ĺ��캯��ʹ�øù��캯������ʵ����Ҫ���г�ʼ����
	 */
	public EMCDB(){
		
		
		/*静态变量不能写进构造函数，否则每次实例化一个类，都会改写静态变量*/
		//System.out.println("EMCDB constructor");
		dynaConn = null;
		pstm = null;
		dynaStm = null;
		dynaRs = null;
	}
	
	/**
	 * ͨ�������ļ�����ʵ��
	 * @param configFile �����ļ�·������� 
	 */
	public EMCDB(String configFile){
		/*
		conn = new ConnDB();
		conn.init(configFile);
		connected = true;
		stm = conn.stm;
		rs = conn.rs;
		*/
	}
	
	/**
	 * ͨ�������ļ���ʵ����г�ʼ����
	 * @param configFile �����ļ���·�����ļ���
	 * @return ����ʼ���ɹ�������true,���򷵻�false
	 */
	public boolean init(String configFile){
		/*
		boolean ret;
		conn = new ConnDB();
		connected = true;
		ret = conn.init(configFile);
		stm = conn.stm;
		rs = conn.rs;*/
		return true;
		
	}
	
	/**
	 * ͨ�����ò����ʵ����г�ʼ����
	 * @param driver ������ݿ���������
	 * @param url ��ݿ��������URL
	 * @param user ������ݿ���û�
	 * @param pwd ������ݿ��û���Ӧ�Ŀ���
	 * @return
	 */
	public boolean init(String Driver, String Url, String User, String Pwd){
		
		//System.out.println(driver+"/"+url+"/"+user+"/"+pwd);
		
		driver = Driver;
		url = Url;
		user = User;
		pwd = Pwd;
		/*
		boolean ret;
		conn = new ConnDB();
		connected = true;
		ret = conn.init(driver,url, user, pwd);
		stm = conn.stm;
		rs = conn.rs;
		return ret;
		*/
		//System.out.println("EMCDB.init()");
		try{
	    	Class.forName(driver);		
	    	connection = DriverManager.getConnection(url,user,pwd);
	    }  
		catch(ClassNotFoundException snfex)    {
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
	      stm = connection.createStatement();
	    }  catch(SQLException sqlex)  {
	      sqlex.printStackTrace();
	      return false;
	    }
		
	    rs = null;
	    
	    return true;
		
	}
	/**
	 * 返回动态创建的Statement语句
	 * @return 成功返回Statement，失败返回null
	 */
	public Statement newStatement(){
		//System.out.println("getStatement");
		try{
		      return connection.createStatement();
		}catch(SQLException sqlex){
		      sqlex.printStackTrace();
		      return null;
		}
	}
	/**
	 * 动态调用executeQuery(sql)命令
	 * @param sql sql语句
	 * @return 成功返回resultset,失败返回null
	 */
	public ResultSet QuerySQL(String sql){
		
		try {
			return dynaStm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 动态调用executeUpdat(sql)命令
	 * @param sql sql语句
	 * @return 返回修改行数，失败返回0
	 */
	public int UpdateSQL(String sql){
		
		try {
			return dynaStm.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 关闭dynaRs
	 */
	public void closeRs(){
		
		if(dynaRs!=null){
			try {
				dynaRs.close();
			} catch (SQLException e) {
				System.out.println("closeRs():dynaRs关闭失败");
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	}
	/**
	 * 关闭dynaStm
	 */
	public void closeStm(){
		
		if(dynaStm != null){
			try {
				dynaStm.close();
			} catch (SQLException e) {
				System.out.println("closeStm():dynaStm关闭失败");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 创建Oracle的动态连接,取消自动提交，sql非空时，创建prepareStatement(sql)，sql为空时，创建createStatement()
	 * @param url Oracle地址
	 * @param username 用户名
	 * @param password 密码
	 * @return 连接成功，返回true，否则，返回false
	 */
	public boolean connDB(String sql){
		
		try {
			dynaConn = DriverManager.getConnection(url,user,pwd);
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		try {
			dynaConn.setAutoCommit(false);
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
	    try {
	    	if(!sql.equals(""))
	    		pstm = dynaConn.prepareStatement(sql);
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	    try {
	    	if(sql.equals(""))
	    		dynaStm = dynaConn.createStatement();
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	    return true;
	    	
	}
	/**
	 * 关闭连接,专门为读取原始监测数据而写
	 * @param pstm
	 * @param conn
	 */
	public void closeDB(){
		
		try {
			dynaConn.setAutoCommit(true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(dynaRs!=null){
			try {
				dynaRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(dynaStm!=null){
			try {
				dynaStm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstm!=null){
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(dynaConn!=null){
			try {
				dynaConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("closeDB()");
	}
	/*
	public static void quit(){
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
			conn.CloseDB();
	}
	*/

	/**
	 * ��ʹ��ݿ�ִ��һ��sql����
	 * @param sql ��Ҫִ�е�sql����
	 * @return ��ݿ�ִ��sql����������¼�ĸ���
	 */
	public static int  exeSQL(String sql){
		//System.out.println(sql);
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
	public static ResultSet selectSQL(String sql){
		//System.out.println(sql);
		try {
			return stm.executeQuery(sql);
		}catch (SQLException sqlex){		  
			sqlex.printStackTrace();
			return null;
		}
		
	}
	


	/**
	 * 在数据库表中插入一个blob字段
	 * 
	 * @param sql 插入语句，同时对于blob类型的字段插入empty_blob();
	 * @param sql2 select ....for update语句
	 * @param path 需要插入到数据库中blob字段中的文件
	 * @param isZIP 标识插入文件是否需要压缩
	 * @return
	 */
	/*
	public  static boolean InsertBlob(String sql,String sql2, String path,boolean isZIP){
		
		String dirpath;
		if( isZIP ){
			AntZipUtil zip = new AntZipUtil(); 
			dirpath=path.replace(".xml", ".zip");
			try {
				AntZipUtil.zipFile(dirpath, path);
			} catch (Exception e2) {
				e2.printStackTrace();
				return false;
			}//压缩入口  
			File file = new File(path); 
			file.delete();
		}else
			dirpath = path;
		
		Blob blob = null;
		boolean flag = false;
		try {
			conn.conn.setAutoCommit(false);
			exeSQL(sql);
			rs = stm.executeQuery(sql2);
			
			while(EMCDB.rs.next()){
				blob = rs.getBlob(1);
				//System.out.println("Insert blob():");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
	
		InputStream in = null;
		try {
			in = new FileInputStream(dirpath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		OutputStream out = null;
		try {
			out = blob.setBinaryStream(1L);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		byte[] buffer = new byte[1024];
		int length = -1;
		try {
			while ((length = in.read(buffer)) != -1){
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			conn.conn.commit();
			conn.conn.setAutoCommit(true);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		File file1 = new File(dirpath); 
		file1.delete();
		return flag;
    }
	
	public static boolean ExportBlob(String sql,String path){
        //System.out.println("ExportBlob sql"+sql);
        System.out.println("ExportBlob path:"+path);
		Blob blob = null;
		boolean flag = false;
		try {
			conn.conn.setAutoCommit(false);
			rs = stm.executeQuery(sql);
			while(rs.next()){
				blob = rs.getBlob(1);
				//System.out.println("Export blob():");
			}
			if(blob==null){
				//System.out.println("blob is null");
				return false;
			}else {
				InputStream in = blob.getBinaryStream();
				byte[] buf = new byte[1024];
				int bytesIn = 0;
				FileOutputStream out = new FileOutputStream(path);
				while ((bytesIn = in.read(buf, 0, 1024)) != -1) {
					out.write(buf, 0, bytesIn);
				}
				in.close();
				out.close();
				flag = true;
			}	
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			conn.conn.commit();
			conn.conn.setAutoCommit(true);
			 flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	    return flag;

	}
	*/
	/**
	 * 将文件写入数据库的函数
	 * @param sql	insert into 语句
	 * @param sql2	select * from 语句
	 * @param path	文件路径+文件名。
	 * @param isZIP	是否需要压缩，默认需要
	 * @return	添加数据库成功，返回true，否则，返回false
	 */
	public static boolean InsertFile(String sql, String sql2, String path ,boolean isZIP){
		path = path.replaceAll("%20", " ");
		String md5 = getMd5ByFile(path);//获取压缩后文件的md5
		//System.out.println("压缩后文件的md5："+md5);
		sql = sql.replace("md5value", md5);//替换sql语句中的sql值
		String dirpath;
		//Date dt = new Date(); 
	    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss。SSS");
	    //System.out.println("开始压缩文件："+sdf.format(dt));	
		if( isZIP ){
			dirpath = path.contains(".") ? path.substring(0,path.lastIndexOf(".")).trim() : path;
			dirpath = dirpath + ".zip";	//将扩展名改为.zip
			//System.out.println(dirpath);
			try {//压缩入口  
				AntZipUtil.zipFile(dirpath, path);
			} catch (Exception e2) {
				e2.printStackTrace();
				return false;
			}
			File file = new File(path);
			file.delete();
		}else{
			dirpath = path;
		}
		//Date dd = new Date(); 
	    //System.out.println("文件压缩完成："+sdf.format(dd));
		System.out.println("EMCDB.InsertFile():开始插入文件。");
		System.out.println(sql);
		/*
		try {
            conn.conn.setAutoCommit(false);// 取消自动提交功能  
            OutputStream os = null;
            // 插入一个空对象empty_blob()  
            stm.executeUpdate(sql);  
            // 锁定数据行进行更新，注意"for update"语句  
            String sql3 = sql2 + " for update";
            rs = stm.executeQuery(sql3);  
            if (rs.next()) {
                // 得到java.sql.Blob对象后强制转换为oracle.sql.BLOB  
                oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob("data");
                // 通过getBinaryOutputStream()方法获得向数据库中插入图片的"管道"  
                os = blob.getBinaryOutputStream();
                // 读取想要存储的图片文件  
                InputStream is = new FileInputStream(dirpath);
                // 依次读取流字节,并输出到已定义好的数据库字段中.
                byte[] buffer = new byte[1024];
        		int length = -1;
    			while ((length = is.read(buffer)) != -1){
    				os.write(buffer, 0, length);
    			}
                is.close();
            }
            os.flush();  
            os.close();
            conn.conn.commit();  
            conn.conn.setAutoCommit(true);// 恢复现场  
            System.out.println("EMCDB.InsertFile():插入数据库成功。");
            File file2 = new File(dirpath);
            file2.delete();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }		
		*/
		
		try {
			EMCDB emcdb = new EMCDB();
			emcdb.dynaConn = DriverManager.getConnection(url,user,pwd);
			emcdb.dynaConn.setAutoCommit(false);// 取消自动提交功能
			emcdb.dynaStm = emcdb.dynaConn.createStatement();
			
            OutputStream os = null;
            // 插入一个空对象empty_blob()  
            emcdb.dynaStm.executeUpdate(sql);  
            // 锁定数据行进行更新，注意"for update"语句  
            String sql3 = sql2 + " for update";
            emcdb.dynaRs = emcdb.dynaStm.executeQuery(sql3);  
            if (emcdb.dynaRs.next()) {
                // 得到java.sql.Blob对象后强制转换为oracle.sql.BLOB  
                oracle.sql.BLOB blob = (oracle.sql.BLOB) emcdb.dynaRs.getBlob("data");
                // 通过getBinaryOutputStream()方法获得向数据库中插入图片的"管道"  
                os = blob.getBinaryOutputStream();
                // 读取想要存储的图片文件  
                InputStream is = new FileInputStream(dirpath);
                // 依次读取流字节,并输出到已定义好的数据库字段中.
                byte[] buffer = new byte[1024];
        		int length = -1;
    			while ((length = is.read(buffer)) != -1){
    				os.write(buffer, 0, length);
    			}
                is.close();
            }
            os.flush();  
            os.close();
            emcdb.dynaConn.commit();
            emcdb.closeDB();
            System.out.println("EMCDB.InsertFile():文件插入数据库成功。");
            File file2 = new File(dirpath);
            file2.delete();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
    /**
     * 从数据库里获取名称为data，类型为blob的数据（取出的数据是zip文件）。对zip文件解压，然后删除zip文件。
     * @param sql	"select data from table where bh= *"语句，查找数据库中的blob类型的文件
     * @param path	文件名.导出文件的文件名为path.zip
     * @param basepath 放置文件的路径
     * @return	返回压缩包内文件的文件名
     */

    public static String ExportFile(String sql,String path,String basepath){
    	/*
    	String FileName = path.substring(path.lastIndexOf("\\")+1);//去掉路径
    	FileName = FileName.substring(0,FileName.lastIndexOf("."));//去掉文件扩展名
    	*/
    	String FileName = path + ".zip";	//将扩展名改为.zip.(写入数据库的是*.zip文件，故导出的文件也必须是*。zip文件)
    	basepath = basepath.replaceAll("%20", " ");
    	File file = new File(basepath);	//创建文件夹
		if(!file.exists()){
			file.mkdirs();
		}
		String realname = null;
        try {
        	EMCDB emcdb = new EMCDB();
        	emcdb.dynaStm = emcdb.newStatement();
            emcdb.dynaRs = emcdb.dynaStm.executeQuery(sql);
            if (emcdb.dynaRs.next()) {
            	//System.out.println(emcdb.dynaRs.getBlob("data").length());
            	
            	if(emcdb.dynaRs.getBlob("data").length()==0){
            		//System.out.println("data.length()==0");
            		return "0";
            	}
                oracle.sql.BLOB b = (oracle.sql.BLOB) emcdb.dynaRs.getBlob("data");  
                InputStream is = b.getBinaryStream();  
                FileOutputStream fos = new FileOutputStream(basepath + FileName);  
                byte[] buffer = new byte[1024];
        		int length = -1;
    			while ((length = is.read(buffer)) != -1){
    				fos.write(buffer, 0, length);
    			}
                fos.flush();  
                fos.close();  
                is.close();
                //System.out.println("EMCDB.InsertFile():从数据库读取成功。");
                
				try {
					realname = AntZipUtil.unZipFile(basepath + FileName,basepath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}               
                //System.out.println("解压成功:" + realname);
                File file1 = new File(basepath + FileName);//删除压缩包
                file1.delete();
            }
            emcdb.closeRs();
            emcdb.closeStm();
            return realname;
        } 
        catch (SQLException e) {  
            e.printStackTrace();
            return null;
        } catch (IOException e) {  
            e.printStackTrace();
            return null;
        }
    }  
    
	/**
	 * 获取文件的md5
	 * @param path	文件路径
	 * @return	返回文件的md5
	 * @throws FileNotFoundException
	 */

public static String getMd5ByFile(String path) {
	
	try {
		
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = fis.read(buffer, 0, 1024)) != -1) {
            md.update(buffer, 0, length);
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        fis.close();
       
        return bigInt.toString(16);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        return null;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
}
/*public static boolean deleteFile(String fileName1) {
    File file = new File("E:\\"+fileName1);
    
    // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
    if (file.exists() && file.isFile()) {
        if (file.delete()) {
            System.out.println("删除单个文件" + fileName1 + "成功！");
            return true;
        } else {
            System.out.println("删除单个文件" + fileName1 + "失败！");
            return false;
        }
    } else {
        System.out.println("删除单个文件失败：" + fileName1 + "不存在！");
        return false;
    }
}
}
*/
	
    /** 
     * 关闭所有与数据库相关的连接 
     *  
     * @param conn 
     * @param stmt 
     * @param rs 
       
    public static void closeAll() {  
        if (rs != null) {  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }   
        if (stm != null) {  
            try {  
                stm.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }
        if (conn != null) { 
            try {  
                conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        System.out.println("数据库关闭。");
    }  */
    

	
	
	
	
	/*
	 public  static boolean InsertBlob(String sql,String sql2, String path,boolean isZIP)  {
	 		
		String dirpath;
		if( isZIP ){
			AntZipUtil zip = new AntZipUtil(); 
			dirpath=path.replace(".xml", ".zip");
			try {
				zip.zipFile(dirpath, path);
			} catch (Exception e2) {
				e2.printStackTrace();
				return false;
			}//ѹ�����  
			File file = new File(path); 
			file.delete();
		}else
			dirpath = path;
		 
		Blob blob = null;
		boolean flag = false;
		try {
			conn.conn.setAutoCommit(false);
			exeSQL(sql);
			rs = stm.executeQuery(sql2);
			
			while(EMCDB.rs.next()){
				blob = (Blob) rs.getBlob(1);
				//System.out.println("Insert blob():");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
	
		InputStream in = null;
		try {
			in = new FileInputStream(dirpath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		OutputStream out = null;
		try {
			out = blob.setBinaryStream(1L);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		byte[] buffer = new byte[1024];
		int length = -1;
		try {
			while ((length = in.read(buffer)) != -1){
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			conn.conn.commit();
			conn.conn.setAutoCommit(true);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		File file1 = new File(dirpath); 
		file1.delete();
		return flag;
    }
	
	public static boolean ExportBlob(String sql,String path){
        //System.out.println("ExportBlob sql"+sql);
        System.out.println("ExportBlob path:"+path);
		Blob blob = null;
		boolean flag = false;
		try {
			conn.conn.setAutoCommit(false);
			rs = stm.executeQuery(sql);
			while(rs.next()){
				blob = (Blob) rs.getBlob(1);
				//System.out.println("Export blob():");
			}
			if(blob==null){
				//System.out.println("blob is null");
				return false;
			}else {
				InputStream in = blob.getBinaryStream();
				byte[] buf = new byte[1024];
				int bytesIn = 0;
				FileOutputStream out = new FileOutputStream(path);
				while ((bytesIn = in.read(buf, 0, 1024)) != -1) {
					out.write(buf, 0, bytesIn);
				}
				in.close();
				out.close();
				flag = true;
			}	
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			conn.conn.commit();
			conn.conn.setAutoCommit(true);
			 flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	    return flag;

	}
	*/

