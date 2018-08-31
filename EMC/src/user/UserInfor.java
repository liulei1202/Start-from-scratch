package user;

import java.sql.SQLException;
import java.util.LinkedList;

import DBConnection.EMCDB;

/**
 * �����û���Ϣ����
 * @author lenovo
 *
 */
public class UserInfor {//extends ConnDB{
	
	//* �û�ID,�û����û�����û���ɫ��˵��,�����û�ID, �û���ɫ�Լ�̨վ����Ϊ�ؼ��֡�
	 public String userID;
	 public String userRole;//��Ϊadmin, ����Ա��������Ա
	 public String stationType;
	 
	 public String userName;
	 public String userPassword;
	 
	 public String unitName;
	 public String phoneNo;
	 public int confirm;
	 
	 public String unitType;
	 public String comments;
	 


	 /**
		 * 通过id实例化一个UserInfor
		 * @param UserID 用户名
		 */
		public UserInfor( String  userID){
		
			this.userID = userID;
		}
		
		
		/**
		 * 用户登录时，通过id和password创建UserInfor实例
		 * @param User 登录用户名
		 * @param UserPassowrd 登录密码
		 */
		public UserInfor( String  userID, String userPassword ){
		
			this.userID = userID;
			this.userPassword = userPassword;
		}

		
		
		/**
		 * 通过id和password判断是否存在该用户，并创建UserInfor实例
		 * @return 返回1表示审核通过，0表示未审核，-1表示拒绝，-2表示未注册。
		 */
		public int getRole(){
			
			String sql = "select userRole,stationType,userName,unitName,phoneno,confirm from emcUserinfo where userID='" + userID + "' and userpassword='" + userPassword + "'";
			int result = -2;
			//System.out.println(sql);
			try {
				EMCDB emcdb = new EMCDB();
				emcdb.dynaStm = emcdb.newStatement();
				emcdb.dynaRs = emcdb.QuerySQL(sql);
				
				while(emcdb.dynaRs.next()){
					userRole = emcdb.dynaRs.getString("userRole");
					stationType= emcdb.dynaRs.getString("stationType");
					userName= emcdb.dynaRs.getString("userName");
					unitName = emcdb.dynaRs.getString("unitName");
					phoneNo = emcdb.dynaRs.getString("phoneno");
					confirm = emcdb.dynaRs.getInt("confirm");
					result = confirm;
					//System.out.println(confirm);
				};
				emcdb.closeRs();
				emcdb.closeStm();
				//System.out.println(result);
				return result;
				
			  }catch (SQLException sqlex){		  
				sqlex.printStackTrace();
				//System.out.println(false);
				return -2;
			  }
		}
		/**
		 * 通过用户id获取用户信息，在系统管理员进行用户审核时使用
		 * @param id 用户id
		 * @return 获得信息返回true，未获得信息返回false
		 */
		public boolean getInformation(){
			
			boolean result = false;
			String sql = "select userRole,stationType,userName,unitName,phoneNo,confirm from emcUserinfo where userID='" + userID +"'";
			try {
				EMCDB emcdb = new EMCDB();
				emcdb.dynaStm = emcdb.newStatement();
				emcdb.dynaRs = emcdb.QuerySQL(sql);
				
				while(emcdb.dynaRs.next()){
					result = true;
					userRole = emcdb.dynaRs.getString("userRole");
					stationType= emcdb.dynaRs.getString("stationType");
					userName= emcdb.dynaRs.getString("userName");
					unitName = emcdb.dynaRs.getString("unitName");
					phoneNo = emcdb.dynaRs.getString("phoneNo");
					confirm = emcdb.dynaRs.getInt("confirm");
				};
				emcdb.closeRs();
				emcdb.closeStm();
			  }catch (SQLException e){		  
				e.printStackTrace();
				return false;
			  }
			return result;
		}
		/**
		 * ����ݿ�������һ�����û�.
		 * �����������û���Ϊ"admin"���û������û��ض�Ϊ����Ա�û���
		 * @param UserName �û���
		 * @param Grade �û�����
		 * @param UserPassowrd �û�����
		 */
		public  static int addUser( String  userID, String userRole,String stationtype ){
			
			String sql;
			
			if (userID.equals("admin"))
				return 0;
			sql = "insert into emcUserinfo (userid, userRole,stationtype,userpassword) values('" + userID + "','" + userRole + "','"+stationtype+"',666666)";
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			int result = emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return result;
			
		}
		/**
		 * ����ݿ�������һ�����û�.
		 * �����������û���Ϊ"admin"���û������û��ض�Ϊ����Ա�û���
		 * @param UserName �û���
		 * @param Grade �û�����
		 * @param UserPassowrd �û�����
		 */	 
		public static int addUserFromReg( String  userID, String userRole, String stationType, String userPassword, String userName, String unitName, String telephone ,int confirm){
			
			String sql=null;
			int total = 0;
			if (userID.equals("admin")){
				return -1;
			}
			sql = "select count(*) as num from emcuserinfo where userid='"+userID+"'";
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				while(emcdb.dynaRs.next()){
					total = emcdb.dynaRs.getInt("num");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			emcdb.closeRs();
			if(total!=0){/*用户名已存在*/
				return 0;
			}
			
			
			sql = "insert into emcUserinfo (userid, userRole,stationtype, userpassword,username,unitname,phoneno,confirm) values('" + userID + "','" + userRole + "','" + stationType + "','"+userPassword+"','" + userName + "','" + unitName + "','" + telephone + "'," + confirm + ")";
			System.out.println(sql);
			total =  emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return total;
			
		}
		
		/**
		 * ����ݿ���ɾ��ָ�����û�.
		 * ������ɾ���û���Ϊ"admin"���û������û��ض�Ϊ����Ա�û���
		 * @param String UserName Ҫɾ���û��ĵ�¼��
		 */
		public static int  deleteUser( String  userID){
			
			if (userID.equals("admin"))
				return 0;
			String sql;
			sql = "delete from emcUserinfo where userID='" + userID + "'";
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			int result = emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return result;
		}
		
		
		/**
		 * ����ݿ��иı�ָ���û�������.
		 * �����Ըı��û���Ϊ"admin"�����͡����û��ض�Ϊ����Ա�û���
		 * @param String  UserName Ҫ�ı����͵��û���
		 * @param String Grade�û���������
		 * 
		 */
		public static int  changeUserRole( String  userID, String userRole){
			
			if (userID.equals("admin"))
				return 0 ;
			
			String sql;
			sql = "update emcUserinfo set userRole='" + userRole + "' where userID='" + userID + "'";
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			int result = emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return result;
		}
		
		
		/**
		 * ����ݿ��иı�ָ���û��Ŀ���.
		 * @param String  UserName Ҫ�ı����͵��û���
		 * @param String  Password �û����¿���
		 * 
		 */
		public static int  changeUserPassword( String  userID, String userPassword){

			String sql = "update emcUserinfo set userpassword='" + userPassword + "' where userID='" + userID + "'";
			//System.out.println("user pass modify:" + sql);
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			int result = emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return result;
		}
		
		/**
		 * ����ݿ�������ָ���û��Ŀ���Ϊ666666.
		 * @param String  UserName Ҫ�ı����͵��û���
		 */
		public static int resetUserPassword( String  userID){
			
			String sql = "update emcUserinfo set userPassword='666666' where userID='" + userID + "'";
			//System.out.println("User-reset User Password: " + sql);
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			int result = emcdb.UpdateSQL(sql);
			emcdb.closeStm();
			return result;
		}
		/**
		 * ��ȡ�����û����û��������
		 * @return UserNode���͵�LinkedList�ṹ�����а����������û����û��������
		 */
		public  static LinkedList<UserInfor> userList(){
			
			LinkedList<UserInfor> ll;
			ll = new LinkedList<UserInfor>();
			
			String sql = "select userID, userRole, stationType, userName,unitName,phoneno,confirm from emcUserinfo order by confirm desc,userRole,stationType,decode(userRole,'admin','1','系统管理','2','数据管理','3','数据需求','4','台站','5',userRole),decode(stationtype,'短波','1','超短波','2','卫星','3',stationType)";
			try {
				EMCDB emcdb = new EMCDB();
				emcdb.dynaStm = emcdb.newStatement();
				emcdb.dynaRs = emcdb.QuerySQL(sql);
				while(emcdb.dynaRs.next()){
					UserInfor us = new UserInfor(emcdb.dynaRs.getString("userID"));
					us.userRole = emcdb.dynaRs.getString("userRole");
					us.stationType = emcdb.dynaRs.getString("stationType");
					us.userName = emcdb.dynaRs.getString("userName");
					us.unitName = emcdb.dynaRs.getString("unitName");
					us.phoneNo = emcdb.dynaRs.getString("phoneno");
					us.confirm = emcdb.dynaRs.getInt("confirm");
					ll.add(us);
				}
				emcdb.closeRs();
				emcdb.closeStm();
			}catch(SQLException sqlex){		  
				sqlex.printStackTrace();
				return null;
			}
			return ll;
		}
		
		public static LinkedList<String> getUserName(){
			
			LinkedList<String> userName = new LinkedList<String>();
			String sql = "select distinct userName from emcuserinfo";
			
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				while(emcdb.dynaRs.next()){
					userName.add(emcdb.dynaRs.getString("username"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return userName;
		}
		
		
		/**
		 * ��/Ϊ�ָ���ǰ����ת��Ϊ�ַ�
		 */
		public String toString(){
			
			return userID +"/" + userRole + "/" +userPassword;
		}
		/**
		 * 数据修改时，用来判断数据是否为空的，本不应放在这个类，为了方便而为之
		 * @param str
		 * @return
		 */
		public static String forNull(String str){
			//System.out.println(str);
			if(str == null){
				return "";
			}
			else if(str.contains("null")){
				return "";
			}else{
				return str.trim();
			}
				
		}
}
