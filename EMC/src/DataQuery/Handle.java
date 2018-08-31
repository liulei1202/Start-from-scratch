package DataQuery;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import com.fileupload.*;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import net.sf.json.JSONArray;

import DBConnection.*;
import ExcelUtil.ExcelUtil;
import user.UserInfor;

public class Handle {
	
	public String time;
	public Double levels;
	public int size;//数据数量
	
	public Handle(){
		
		time = null;
		levels = 0.0;
		size = 0;
	}

	public static boolean deleteOriginalData(String bh,String type){
		
		boolean result = false;
		String sql1 = null;
		String sql2 = null;
		String table = "";
		if(type.equals("DBDPJCQuery")){
			sql1 = "delete from emcdpjc where bh='" + bh + "'";
			sql2 = "delete from emcdpjcdata where bh='" + bh + "'";
			table = "emcdpjcparameter";
			
		}else if(type.equals("DBPDSMQuery")){
			sql1 = "delete from emcpdsm where bh='" + bh + "'";
			sql2 = "delete from emcpdsmdata where bh='" + bh + "'";
		}else if(type.equals("DBXHCXQuery")){
			sql1 = "delete from emcdpcx where bh='" + bh + "'";
			sql2 = "delete from emcdpcxdata where bh='" + bh + "'";
			table = "emcdpcxparameter";
			
		}else if(type.equals("DBLHDWQuery")){
			sql1 = "delete from emclhdw where bh='" + bh + "'";
			sql2 = "delete from emclhdwdata where bh='" + bh + "'";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql1 = "delete from emcudpjc where bh='" + bh + "'";
			sql2 = "delete from emcudpjcdata where bh='" + bh + "'";
			table = "emcudpjcparameter";
			
		}else if(type.equals("CDBPDSMQuery")){
			sql1 = "delete from emcupdsm where bh='" + bh + "'";
			sql2 = "delete from emcupdsmdata where bh='" + bh + "'";
		}else if(type.equals("CDBXHCXQuery")){
			sql1 = "delete from emcudpcx where bh='" + bh + "'";
			sql2 = "delete from emcudpcxdata where bh='" + bh + "'";
			table = "emcudpcxparameter";
			
		}else if(type.equals("CDBLHDWQuery")){
			sql1 = "delete from emculhdw where bh='" + bh + "'";
			sql2 = "delete from emculhdwdata where bh='" + bh + "'";
		}/**/

		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int flag = 0;
		int flag1 = 0;
		int flag2 = 0;
		if(!table.equals("")){
			String sql3 = "update "+table+" set Original='0' where bh='"+bh+"'";
			flag= emcdb.UpdateSQL(sql3);
			
		}
		 flag1 = emcdb.UpdateSQL(sql1);
		 flag2= emcdb.UpdateSQL(sql2);
		
		emcdb.closeStm();
		if((flag1>0)&&(flag2>0)){
			
			result = true;
		}
		return result;
	}
	
	public static boolean deleteOtherData(String bh,String type,String dataType){
		
		boolean result = false;
		String sql1 = null;
		if(type.equals("DBDPJCQuery")){
			sql1 = "delete from emcdpjc"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("DBPDSMQuery")){
			sql1 = "delete from emcpdsm"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("DBXHCXQuery")){
			sql1 = "delete from emcdpcx"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("DBLHDWQuery")){
			sql1 = "delete from emclhdw"+ dataType +" where bh='" + bh + "'";
			
		}
		else if(type.equals("CDBDPJCQuery")){
			sql1 = "delete from emcudpjc"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("CDBPDSMQuery")){
			sql1 = "delete from emcupdsm"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("CDBXHCXQuery")){
			sql1 = "delete from emcudpcx"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("CDBLHDWQuery")){
			sql1 = "delete from emculhdw"+ dataType +" where bh='" + bh + "'";
			
		}else if(type.equals("DBJCBGQuery")){
			sql1 = "delete from emcJCBG where bh='" + bh + "'";
		}else if(type.equals("CDBJCBGQuery")){
			sql1 = "delete from emcUJCBG where bh='" + bh + "'";
		}else if(type.equals("WXJCBGQuery")){
			sql1 = "delete from emcWJCBG where bh='" + bh + "'";
		}else if(type.equals("WXZFQJCQuery")){
			sql1 = "delete from emcWZFQJC where bh='" + bh + "'";
		}else{
			return false;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int flag = emcdb.UpdateSQL(sql1);
		/*
		String sql2 = "update emcdpjcparameter set station='try' where bh='a'";
		int flag2 = emcdb.UpdateSQL(sql2);
		System.out.println("flag2:"+flag2);
		*/
		emcdb.closeStm();
		if(flag>0){
			result = true;
		}
		return result;
	}
	/**
	 * 删除DPJC和DPCX中的图形，因为这两种的图形bh会重复，要用bh和图形类型一起删除
	 * @param bh
	 * @param type
	 * @param dataType 图形类型
	 * @return
	 */
	public static boolean deleteParaGrap(String bh,String type,String dataType){
		
		boolean result = false;
		String sql1 = null;
		String table = null;
		if(type.equals("DBDPJCQuery")){
			sql1 = "delete from emcdpjcgraphic where bh='" + bh + "' and GRAPHICTYPE = '" + dataType+"'";
			table = "emcdpjcparameter";
		}else if(type.equals("DBXHCXQuery")){
			sql1 = "delete from emcdpcxgraphic where bh='" + bh + "' and GRAPHICTYPE = '" + dataType+"'";
			table = "emcdpcxparameter";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql1 = "delete from emcudpjcgraphic where bh='" + bh + "' and GRAPHICTYPE = '" + dataType+"'";
			table = "emcudpjcparameter";
		}
		else if(type.equals("CDBXHCXQuery")){
			sql1 = "delete from emcudpcxgraphic where bh='" + bh + "' and GRAPHICTYPE = '" + dataType+"'";
			table = "emcudpcxparameter";
		}
		else{
			return false;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		int flag = emcdb.UpdateSQL(sql1);
		if(flag>0){
			result = true;
		}
		String column = null;
		if(dataType.trim().equals("中频图")){
			column = "FREQGRAP";
		}else if(dataType.trim().equals("示向图")){
			column = "DIREGRAP";
		}else if(dataType.trim().equals("定位图")){
			column = "LOCAGRAP";
		}else{
			emcdb.closeStm();
			return result;
		}
		
			
		String sql3 = "update "+table+" set "+column+"='0' where bh='"+bh+"'";
		emcdb.UpdateSQL(sql3);
		
		
		emcdb.closeStm();
		return result;
	}	
	
	
	/*
	public static boolean deleteJCBGData(String bh,String type){
		
		boolean result = false;
		String sql1 = null;
		if(type.equals("DBJCBGQuery")){
			sql1 = "delete from emcJCBG where bh='" + bh + "'";
		}else if(type.equals("CDBJCBGQuery")){
			sql1 = "delete from emcUJCBG where bh='" + bh + "'";
		}else if(type.equals("WXJCBGQuery")){
			sql1 = "delete from emcWJCBG where bh='" + bh + "'";
		}else{
			return false;
		}
		
		int flag1 = EMCDB.exeSQL(sql1);
		
		if(flag1>0){
			result = true;
		}
		return result;
	}
	*/
	/**
	 * 给定页面隐藏类型值，数据类型，编号，返回导出文件的sql语句
	 * @param bh
	 * @param type
	 * @param dataType
	 * @return 
	 */
	public static String getExportFileSql(String bh,String type,String dataType){
		
	   String sql = null;
	   if(type.equals("DBDPJCQuery")){
	   		sql = "EMCDPJC";
	   }else if(type.equals("DBPDSMQuery")){
	   		sql = "EMCPDSM";
	   }else if(type.equals("DBXHCXQuery")){
	   		sql = "EMCDPCX";
	   }else if(type.equals("DBLHDWQuery")){
	   		sql = "EMCLHDW";
	   }else if(type.equals("DBJCBGQuery")){/*查询监测报告数据时，dataType为空*/
	   		sql = "EMCJCBG";
	   }
	   else if(type.equals("CDBDPJCQuery")){
	   		sql = "EMCUDPJC";
	   }else if(type.equals("CDBPDSMQuery")){
	   		sql = "EMCUPDSM";
	   }else if(type.equals("CDBXHCXQuery")){
	   		sql = "EMCUDPCX";
	   }else if(type.equals("CDBLHDWQuery")){
	   		sql = "EMCULHDW";
	   }else if(type.equals("CDBJCBGQuery")){
	   		sql = "EMCUJCBG";
	   }else if(type.equals("WXJCBGQuery")){
	   		sql = "EMCWJCBG";
	   }else if(type.equals("WXZFQJCQuery")){
	   		sql = "EMCWZFQJC";
	   }else{
		   return null;
	   }
	   String regEX = "[\\u4e00-\\u9fa5]";
	   Pattern p = Pattern.compile(regEX);
	   Matcher m = p.matcher(dataType);
	   
	   if(m.find()){//说明存在汉字，为图形数据文件。汉字是图形类型。
		   sql = "select data from " + sql + "Graphic where bh='"+bh+"' and graphictype='"+ dataType+"'"; 
	   }else{ 
		   sql = "select data FROM " + sql + dataType +" WHERE  bh = '"+ bh +"'";
	   }
		return sql;
	}
	
	/**
	 * 获取单频检测的文件名和监测频率
	 * @param bh
	 * @param type
	 * @return 返回ArrayList<String>数组，第一个值为文件名，第二个值为监测频率
	 */
	public static ArrayList<String> DPJCinfo(String bh,String type){
		
		String sql = null;
		ArrayList<String> al = new ArrayList<String>();
		if(type.equals("DBDPJCQuery")){
			
			sql = "select filename,frequence from emcdpjc where bh='" + bh + "'";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select filename,frequence from emcudpjc where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			if(emcdb.dynaRs.next()){
				
				al.add(emcdb.dynaRs.getString("filename"));
				al.add(String.valueOf(emcdb.dynaRs.getDouble("frequence")/1000000));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return al;
	}
	
	
	/**
	 * 对单频点进行过滤，计算最大值，最小值，均值
	 * @param bh 数据编号
	 * @param type 数据类型
	 * @param minlevel 最小值，用于过滤数据,默认为0
	 * @return 返回Double类型数组，数组中的数据分别是：场强最小值，最大值，平均值
	 */
	public static ArrayList<Double> DPJCfilter(String bh,String type,Double minlevel){
		
		ArrayList<Double> al= new ArrayList<Double>();
		Double temp=0.0;
		Double max=0.0;//定义初始值
		Double min=100.0;//定义初始值
		Double aver=0.0;
		int count = 0;
		Double sum = 0.0;
		String sql=null;
		
		if(type.equals("DBDPJCQuery")){
			
			sql = "select levels from emcdpjcdata where bh='" + bh + "'";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select levels from emcudpjcdata where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					count++;
					if(max<temp){
						max = temp;
					}
					if(min>temp){
						min = temp;
					}
					sum = sum + temp;
					aver = sum/count;
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println("minlevel="+minlevel);
		//System.out.println("有效数据数量count="+count);
		if(count == 0){/*该文件中场强均为负值，文件失效。*/
			min=0.0;
			max=0.0;
			aver=0.0;
		}
		al.add(min);
		al.add(max);
		al.add(aver);
		
		return al;
	}
	
	/**
	 * 计算单频监测占用度
	 * @param bh 数据编号
	 * @param type 数据类型
	 * @param minlevel 最小值，用于过滤数据
	 * @param threshold 阈值，用于计算占用度
	 * @return 返回Double类型数据，表示占用度，计算失败返回null。
	 */
	public static Double DPJCcalculate(String bh,String type,Double minlevel,Double threshold){
		
		Double temp=0.0;
		Double total = 0.0;
		Double useful = 0.0;
		Double Occupancy = 0.0;
		String sql=null;
		if(type.equals("DBDPJCQuery")){
			
			sql = "select levels from emcdpjcdata where bh='" + bh + "'";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select levels from emcudpjcdata where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					total++;
					if(temp>=threshold){
						useful++;
					}
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println("共有大于最小值"+minlevel+"的数据："+total);
		//System.out.println("大于阈值"+threshold+"的数据："+useful);
		Occupancy = useful/total;
		return Occupancy;
	}
	

	/**
	 * 获取PDSM数据的文件名和频率范围
	 * @param bh
	 * @param type
	 * @return  返回ArrayList<String>数组，第一个String是文件名，第二个是频率范围
	 */
	public static ArrayList<String> PDSMinfo(String bh,String type){
		
		ArrayList<String> al = new ArrayList<String>();
		String sql = null;
		
		if(type.equals("DBPDSMQuery")){
			
			sql = "select fileName, startfrequence,endfrequence from emcpdsm where bh='" + bh + "'";
		}else if(type.equals("CDBPDSMQuery")){
			
			sql = "select fileName, startfrequence,endfrequence frequence from emcupdsm where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			if(emcdb.dynaRs.next()){
				
				al.add(emcdb.dynaRs.getString("filename"));
				String temp = emcdb.dynaRs.getDouble("startfrequence")/1000000 +"MHz 至 " + emcdb.dynaRs.getDouble("endfrequence")/1000000;
				al.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println("al.size()"+al.size());
		return al;
	}
	
	/**
	 * 已弃用
	 * 对频段扫描进行过滤，计算每个单频点的最大场强，返回这些最大场强中的最小值，最大值，平均值
	 * @param bh 数据编号
	 * @param type 数据类型
	 * @param minlevel 最小值，用于过滤数据，默认为0.0
	 * @return 返回Double类型数组，数组中前三个数是各频点的最大值组成的数组中的：最小值，最大值，平均值
	 */
	public static ArrayList<Double> PDSMfilter(String bh,String type,Double minlevel){
		
		String sql = null;
		String table = null;
		if(type.equals("DBPDSMQuery")){
			table = "emcpdsmdata";
		}else if(type.equals("CDBPDSMQuery")){
			table = "emcupdsmdata";
		}else{
			return null;
		}
		sql = "select distinct frequence from "+table+" where bh='" + bh + "' order by frequence";
		ArrayList<Double> frequence = new ArrayList<Double>();//frequence保存所有频点
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				frequence.add(emcdb.dynaRs.getDouble("frequence"));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		emcdb.closeRs();
		//emcdb.closeStm();
		int size = frequence.size();
		System.out.println("共有频点："+size);
		//levels存储每个频点下的最大场强值
		ArrayList<Double> levels = new ArrayList<Double>();
		for(int i=0;i<size;i++){
			sql = "select max(levels) as num from "+table+" where bh='"+bh+"' and frequence="+frequence.get(i);
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				if(emcdb.dynaRs.next()){
					//System.out.println(emcdb.dynaRs.getDouble("num"));
					levels.add(emcdb.dynaRs.getDouble("num"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			emcdb.closeRs();
		}
		emcdb.closeStm();
		//计算levels中的最大值，最小值，平均值
		ArrayList<Double> al= new ArrayList<Double>();
		Double max=levels.get(0);
		Double min=levels.get(0);
		Double aver=0.0;
		Double sum = 0.0;
		for(int j =0;j<size;j++){
			//al.add(levels.get(j));
			if(levels.get(j)>max)
				max = levels.get(j);
			if(levels.get(j)<min)
				min = levels.get(j);
			sum = sum + levels.get(j);
		}
		aver = sum/size;
		al.add(min);
		al.add(max);
		al.add(aver);
		return al;
	}
	
	/**
	 * 计算频段的占用度。已弃用
	 * @return 返回Double类型数据，表示占用度，计算失败返回null。
	 */
	public static Double PDSMcalculate(String bh,String type,Double threshold){
		
		Double useful = 0.0;
		Double Occupancy = 0.0;
		String sql = null;
		String table = null;
		if(type.equals("DBPDSMQuery")){
			table = "emcpdsmdata";
		}else if(type.equals("CDBPDSMQuery")){
			table = "emcupdsmdata";
		}else{
			return null;
		}
		sql = "select distinct frequence from "+table+" where bh='" + bh + "' order by frequence";
		ArrayList<Double> frequence = new ArrayList<Double>();//frequence保存所有频点
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				frequence.add(emcdb.dynaRs.getDouble("frequence"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		emcdb.closeRs();
		//emcdb.closeStm();
		int size = frequence.size();
		System.out.println("共有频点："+size);
		for(int i=0;i<size;i++){
			sql = "select max(levels) as num from "+table+" where bh='"+bh+"' and frequence="+frequence.get(i);
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				if(emcdb.dynaRs.next()){
					if(i%100 ==0){
						System.out.println("第i个:"+i);
					}
					//System.out.println(emcdb.dynaRs.getDouble("num"));
					if(emcdb.dynaRs.getDouble("num") >=threshold){
						useful++;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			emcdb.closeRs();
		}
		emcdb.closeStm();
		//System.out.println(useful);
		if(useful == 0.0){
			return 0.0;
		}else{
			Occupancy = useful/size;
		}
		return Occupancy;
	}
	
	/**
	 * 获取频段扫描数据的所有频点,从小到大排序
	 * @param bh 数据编号
	 * @param type 数据类型
	 * @return 返回所有频点组成的数组
	 */
	public static ArrayList<Double> PDSMFrequence(String bh, String type){
		
		ArrayList<Double> frequence = new ArrayList<Double>();
		String sql=null;
		//得到所有频点
		if(type.equals("DBPDSMQuery")){
			
			sql = "select distinct frequence from emcpdsmdata where bh='" + bh + "' order by frequence";
		}else if(type.equals("CDBPDSMQuery")){
			
			sql = "select distinct frequence from emcupdsmdata where bh='" + bh + "' order by frequence";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				frequence.add(emcdb.dynaRs.getDouble("frequence"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return frequence;
	}
	/**
	 * 计算频段扫描中某一频点的场强最小值，最大值和平均值
	 * @param bh 数据编号
	 * @param type 数据类型 
	 * @param frequence 频段扫描数据中的某一个频点
	 * @return 返回指定频点的最小值，最大值，平均值
	 */
	public static ArrayList<Double> PDSMFrequencefilter(String bh,String type,String frequence,Double minlevel){
		
		ArrayList<Double> al= new ArrayList<Double>();
		Double temp=0.0;
		Double max=0.0;//定义一个不可能这么小的初始值
		Double min=100.0;//定义一个不可能这么大的初始值
		Double aver=0.0;
		int count = 0;//大于minlevel的数据的数量
		Double sum = 0.0;
		String sql = null;
		if(type.equals("DBPDSMQuery")){
			
			sql = "select levels from emcpdsmdata where bh='" + bh + "' and frequence = "+frequence + "order by frequence";
		}else if(type.equals("CDBPDSMQuery")){
			
			sql = "select levles from emcupdsmdata where bh='" + bh + "' and frequence = "+frequence + "order by frequence";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					count++;
					if(max<temp){
						max = temp;
					}
					if(min>temp){
						min = temp;
					}
					sum = sum + temp;
					aver = sum/count;
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if(count == 0){/*该文件中场强均为负值，文件失效。*/
			min=0.0;
			max=0.0;
			aver=0.0;
		}
		//System.out.println("minlevel="+minlevel);
		//System.out.println("有效数据数量count="+count);
		al.add(min);
		al.add(max);
		al.add(aver);
		return al;
	}
	
	public static Double PDSMFrequenceCalculate(String bh,String type,Double frequence,Double minlevel,Double threshold){
		
		Double temp=0.0;
		Double total = 0.0;
		Double useful = 0.0;
		Double Occupancy = 0.0;
		String sql=null;
		if(type.equals("DBPDSMQuery")){
			
			sql = "select levels from emcpdsmdata where bh='" + bh + "' and frequence = "+frequence + "order by frequence";
		}else if(type.equals("CDBPDSMQuery")){
			
			sql = "select levles from emcupdsmdata where bh='" + bh + "' and frequence = "+frequence + "order by frequence";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					total++;
					if(temp>=threshold){
						useful++;
					}
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println("共有大于最小值"+minlevel+"的数据："+total);
		//System.out.println("大于阈值"+threshold+"的数据："+useful);
		Occupancy = useful/total;
		return Occupancy;
	}

	
	/**
	 * LHDW分析不是这么用的
	 * @param bh
	 * @param type
	 * @param minlevel
	 * @return
	 */
	
	public static ArrayList<Double> filterLHDW(String bh,String type,Double minlevel){
		
		ArrayList<Double> al= new ArrayList<Double>();
		Double temp=0.0;
		Double max=0.0;//定义一个不可能这么小的初始值
		Double min=100.0;//定义一个不可能这么大的初始值
		Double aver=0.0;
		int count = 0;
		Double sum = 0.0;
		String sql=null;
		
		if(type.equals("DBLHDWQuery")){
			
			sql = "select levels from emcLHDWdata where bh='" + bh + "'";
		}else if(type.equals("CDBLHDWQuery")){
			
			sql = "select levels from emcuLHDWdata where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					count++;
					if(max<temp){
						max = temp;
					}
					if(min>temp){
						min = temp;
					}
					sum = sum + temp;
					aver = sum/count;
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println("minlevel="+minlevel);
		//System.out.println("有效数据数量count="+count);
		al.add(min);
		al.add(max);
		al.add(aver);
		return al;
	}
	//LHDW不怎么分析
	public static Double calculateLHDW(String bh,String type,Double minlevel,Double threshold){
		
		Double temp=0.0;
		Double total = 0.0;
		Double useful = 0.0;
		Double Occupancy = 0.0;
		String sql=null;
		if(type.equals("DBLHDWQuery")){
			
			sql = "select levels from emcLHDWdata where bh='" + bh + "'";
		}else if(type.equals("CDBLHDWQuery")){
			
			sql = "select levels from emcuLHDWdata where bh='" + bh + "'";
		}else{
			return null;
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getDouble("levels");
				if(temp>=minlevel){
					total++;
					if(temp>=threshold){
						useful++;
					}
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println("共有大于最小值"+minlevel+"的数据："+total);
		//System.out.println("大于阈值"+threshold+"的数据："+useful);
		Occupancy = useful/total;
		return Occupancy;
	}
	

	
	/**
	 * 单频监测的原始监测数据播放,已弃用
	 * @param bh
	 * @param type
	 * @return 返回时间组成的字符串和场强组成的字符串
	 */
	public ArrayList<String> DPJCPlay(String bh,String type){
		
		//System.out.println("DPJCPlay");
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> time = new ArrayList<String>();
		ArrayList<Double> levels = new ArrayList<Double>();
		
		String temp;
		String sql = "select time,levels from emcdpjcdata where bh='" + bh + "' order by time";
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			while(emcdb.dynaRs.next()){
				temp = emcdb.dynaRs.getString("time").trim();
				temp = temp.contains(" ") ? temp.substring(temp.lastIndexOf(" ")).trim() : temp;
				//temp = "'"+temp+"'";
				time.add(temp);
				levels.add(emcdb.dynaRs.getDouble("levels"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		size = time.size();
		String timeJson = JSONArray.fromObject(time).toString();
		String levelsJson = JSONArray.fromObject(levels).toString();
		
		//System.out.println(timeJson);
		//System.out.println(levelsJson);
		System.out.println(size);
		
		result.add(timeJson);
		result.add(levelsJson);
		return result;
	}
	/**
	 * 查看dpjc的信号参数登记中是否有该bh，存在返回true。
	 * @param bh 要查询的bh
	 * @param type 页面类型。判断方式：type包含DB或者CDB
	 * @return
	 */
	public static boolean isRepeat(String bh,String type){
		
		String table = null;
		if(type.contains("CDB")){
			
			table="EMCUDPJCparameter";
		}else{
			
			table = "EMCDPJCParameter";
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		String sql = "select count(*) as num from "+table+" where bh='"+bh+"'";
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		int count = 0;
		try {
			if(emcdb.dynaRs.next()){
				
				count = emcdb.dynaRs.getInt("num");
				System.out.println(count);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		emcdb.closeRs();
		emcdb.closeStm();
		if(count > 0 ){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * pdsm获取单频点的csv文件。从pdsm中导出csv文件，从csv文件里获取信息。而不是从pdsmdata表
	 * @param bh
	 * @param type
	 * @param frequence 频点
	 * @param path 放置在服务器的路径名+文件名
	 * @param fileName 导出的文件名和新写入信号参数登记及原始监测数据的bh
	 * @param operater 操作方式。1表示导出文件，2表示导入到数据库 
	 * @return
	 */
	public static String writeCSVfromCSV(String bh,String type,String frequence,String path,String fileName,HttpServletResponse response,String operate){//,HttpServletResponse response
		
		System.out.println("频段扫描导出频点："+frequence);
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		String table = null;//原始表
		String table3 = null;//要写入的parameter
		String newType = null;//要写入的原始数据
		if(type.equals("DBPDSMQuery")){
			table = "EMCPDSM";
			table3="EMCDPJCparameter";
			newType = "DBDPJCQuery";
			
		}else if(type.equals("CDBPDSMQuery")){
			table = "EMCUPDSM";
			table3 = "EMCUDPJCParameter";
			newType = "CDBDPJCQuery";
		}else{
			//return;
			return "页面类型错误。";
		}
		String sql = "select * from "+table+" where bh='"+bh+"'";
		String sql2 = "select * from "+table+"Data where bh='"+bh+"' and frequence='"+frequence+"'  order by to_timestamp(time,'yyyy-MM-dd HH24:MI:ss.ff')";
		//System.out.println(sql2);
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		System.out.println(path);
		CsvWriter csvWriter = new CsvWriter(path,',',Charset.forName("GBK"));
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		/*
		String LONGITUDE=null;
		String LATITUDE=null;
		String STARTTIME=null;
		String ENDTIME=null;
		String BANDWIDTH=null;
		String RADIOFREATTENUATION=null;
		String MIDFREATTENUATION=null;
		String DEMODEN=null;
		String DETECTOR = null;*/
		String[] information = new String[7];
		String LONGITUDE =null;
		String LATITUDE =null; 
		String BANDWIDTH =null; 
		String DEMODEN =null;
		String STATION =null;
		String MONITOR =null;  
		String MONITORLOCATION =null; 
		String COMMENTS =null;
		String BUSINESSTYPE =null;
		String BUSINESSNAME =null;

		try {
			while(emcdb.dynaRs.next()){
				information[0] = "经度:"+emcdb.dynaRs.getString("LONGITUDE");
				information[1] = "纬度:"+emcdb.dynaRs.getString("LATITUDE");
				information[2] = "中频带宽(Hz):"+emcdb.dynaRs.getString("BANDWIDTH");
				information[3] = "射频衰减:"+emcdb.dynaRs.getString("RADIOFREATTENUATION");
				information[4] = "中频衰减:"+emcdb.dynaRs.getString("MIDFREATTENUATION");
				information[5] = "解调:"+emcdb.dynaRs.getString("DEMODEN");
				information[6] = "检波器:"+emcdb.dynaRs.getString("DETECTOR");
				 LONGITUDE =emcdb.dynaRs.getString("LONGITUDE");
				 LONGITUDE = (LONGITUDE == null) ? LONGITUDE : LONGITUDE.trim();
				 LATITUDE =emcdb.dynaRs.getString("LATITUDE"); 
				 LATITUDE = (LATITUDE == null) ? LATITUDE : LATITUDE.trim();
				 BANDWIDTH =emcdb.dynaRs.getString("BANDWIDTH"); 
				 BANDWIDTH = (BANDWIDTH == null) ? BANDWIDTH : BANDWIDTH.trim();
				 DEMODEN =emcdb.dynaRs.getString("DEMODEN");
				 DEMODEN = (DEMODEN == null) ? DEMODEN : DEMODEN.trim();
				 STATION =emcdb.dynaRs.getString("STATION");
				 STATION = (STATION == null) ? STATION : STATION.trim();
				 MONITOR =emcdb.dynaRs.getString("MONITOR");  
				 MONITOR = (MONITOR == null) ? MONITOR : MONITOR.trim();
				 MONITORLOCATION =emcdb.dynaRs.getString("MONITORLOCATION"); 
				 MONITORLOCATION = (MONITORLOCATION == null) ? MONITORLOCATION : MONITORLOCATION.trim();
				 //COMMENTS = emcdb.dynaRs.getString("COMMENTS");
				 BUSINESSTYPE =emcdb.dynaRs.getString("BUSINESSTYPE");
				 BUSINESSTYPE = (BUSINESSTYPE == null) ? BUSINESSTYPE : BUSINESSTYPE.trim();
				 BUSINESSNAME =emcdb.dynaRs.getString("BUSINESSNAME");
				 BUSINESSNAME = (BUSINESSNAME == null) ? BUSINESSNAME : BUSINESSNAME.trim();
				 //System.out.println(BUSINESSNAME);
				
				
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//return;
			return "获取不到数据。";
		}
		String[] headers = {"时间","频率 (Hz)","Level (dBμV/m)","文件信息"};
		String[] content;
		emcdb.closeRs();
		int i = -1;
		String oldfileName = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
		String oldfilepath = path.substring(0,path.lastIndexOf("\\") +1);// 文件路径
		String result = EMCDB.ExportFile(sql, oldfileName.trim(), oldfilepath);
		String oldPath = oldfilepath+result;
		String line = null;
		String temp[];
		try {
			csvWriter.writeRecord(headers);
			CsvReader csvReader = new CsvReader(oldPath);
			csvReader.readHeaders();
			while(csvReader.readRecord()){
				line = csvReader.getRawRecord();
				line = line.replaceAll("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				if(temp[1].trim().equals(frequence)){
					if((++i)<7){
						content = new String[]{temp[0],temp[1],temp[2],information[i]};
						csvWriter.writeRecord(content);
					}else{
						content = new String[]{temp[0],temp[1],temp[2]};
						csvWriter.writeRecord(content);
					}
					if(i%1000 == 0){
						System.out.println("写入csv第i行："+i);
					}
				}
				
			}
				
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		emcdb.dynaRs = emcdb.QuerySQL(sql2);
		try {
			csvWriter.writeRecord(headers);
			while(emcdb.dynaRs.next()){
				
				if((++i)<7){	
					
					content = new String[]{emcdb.dynaRs.getString("time"),emcdb.dynaRs.getString("frequence"),emcdb.dynaRs.getString("levels"),information[i]};
					csvWriter.writeRecord(content);
				}else{
					content = new String[]{emcdb.dynaRs.getString("time"),emcdb.dynaRs.getString("frequence"),emcdb.dynaRs.getString("levels")};
					csvWriter.writeRecord(content);
				}
				if(i%1000 == 0){
					System.out.println("写入csv第i行："+i);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return;
			return "IO异常";
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return;
			return "SQL异常";
		}
		*/
		//emcdb.closeRs();
		csvWriter.close();
		
		//importCDBDPJCOriginalMonitorData(path, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
			
		if(operate.equals("1")){
			try {
				download(path,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return;
				return "Servlet异常。";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return;
				return "IO异常。";
			}
			return "导出csv文件成功。";
		}else{
			if(i<9){
				return "本频点数据量少于10个，禁止写入到数据库。如果需要可以保存到本地。";
			}
			ImportFile infile = new ImportFile();
			String sql3 = "insert into "+table3+" (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,monitor,"+
					"monitorLocation, frequence, bandWidth, modulate, Longitude, Latitude,  businessType,Station) values('"+
					fileName +"','0','0','0','0','"+BUSINESSNAME+"','"+MONITOR+"','"+
					MONITORLOCATION + "'," + frequence + ",'" + BANDWIDTH+ "','"+ DEMODEN +"','" + LONGITUDE + "','" + LATITUDE +"','" + 
					BUSINESSTYPE + "','" + STATION + "')";
			System.out.println(sql3);
			if(emcdb.UpdateSQL(sql3)>0){
				return infile.importOriginalfromSignal(path,fileName,newType);
			}else{
				return "写入信号参数登记失败。";
			}
		}
		//return;
		//return "导出csv成功。";
		 
		 
	}
	
	
	/**
	 * PDSM数据 频点导出为csv
	 * @param bh
	 * @param type
	 * @param frequence 频点
	 * @param path 放置在服务器的路径名+文件名
	 * @param fileName 导出的文件名和新写入信号参数登记及原始监测数据的bh
	 * @param operater 操作方式。1表示导出文件，2表示导入到数据库 
	 * @return
	 */
	public static String writeCSV(String bh,String type,String frequence,String path,String fileName,HttpServletResponse response,String operate){//,HttpServletResponse response
		
		System.out.println("频段扫描导出频点："+frequence);
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		String table = null;//原始表
		String table3 = null;//要写入的parameter
		String newType = null;//要写入的原始数据
		if(type.equals("DBPDSMQuery")){
			table = "EMCPDSM";
			table3="EMCDPJCparameter";
			newType = "DBDPJCQuery";
			
		}else if(type.equals("CDBPDSMQuery")){
			table = "EMCUPDSM";
			table3 = "EMCUDPJCParameter";
			newType = "CDBDPJCQuery";
		}else{
			//return;
			return "页面类型错误。";
		}
		String sql = "select * from "+table+" where bh='"+bh+"'";
		String sql2 = "select * from "+table+"Data where bh='"+bh+"' and frequence='"+frequence+"'  order by to_timestamp(time,'yyyy-MM-dd HH24:MI:ss.ff')";
		//System.out.println(sql2);
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		System.out.println(path);
		CsvWriter csvWriter = new CsvWriter(path,',',Charset.forName("GBK"));
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		/*
		String LONGITUDE=null;
		String LATITUDE=null;
		String STARTTIME=null;
		String ENDTIME=null;
		String BANDWIDTH=null;
		String RADIOFREATTENUATION=null;
		String MIDFREATTENUATION=null;
		String DEMODEN=null;
		String DETECTOR = null;*/
		String[] information = new String[7];
		String LONGITUDE =null;
		String LATITUDE =null; 
		String BANDWIDTH =null; 
		String DEMODEN =null;
		String STATION =null;
		String MONITOR =null;  
		String MONITORLOCATION =null; 
		String COMMENTS =null;
		String BUSINESSTYPE =null;
		String BUSINESSNAME =null;

		try {
			while(emcdb.dynaRs.next()){
				information[0] = "经度:"+emcdb.dynaRs.getString("LONGITUDE");
				information[1] = "纬度:"+emcdb.dynaRs.getString("LATITUDE");
				information[2] = "中频带宽(Hz):"+emcdb.dynaRs.getString("BANDWIDTH");
				information[3] = "射频衰减:"+emcdb.dynaRs.getString("RADIOFREATTENUATION");
				information[4] = "中频衰减:"+emcdb.dynaRs.getString("MIDFREATTENUATION");
				information[5] = "解调:"+emcdb.dynaRs.getString("DEMODEN");
				information[6] = "检波器:"+emcdb.dynaRs.getString("DETECTOR");
				 LONGITUDE =emcdb.dynaRs.getString("LONGITUDE");
				 LONGITUDE = (LONGITUDE == null) ? LONGITUDE : LONGITUDE.trim();
				 LATITUDE =emcdb.dynaRs.getString("LATITUDE"); 
				 LATITUDE = (LATITUDE == null) ? LATITUDE : LATITUDE.trim();
				 BANDWIDTH =emcdb.dynaRs.getString("BANDWIDTH"); 
				 BANDWIDTH = (BANDWIDTH == null) ? BANDWIDTH : BANDWIDTH.trim();
				 DEMODEN =emcdb.dynaRs.getString("DEMODEN");
				 DEMODEN = (DEMODEN == null) ? DEMODEN : DEMODEN.trim();
				 STATION =emcdb.dynaRs.getString("STATION");
				 STATION = (STATION == null) ? STATION : STATION.trim();
				 MONITOR =emcdb.dynaRs.getString("MONITOR");  
				 MONITOR = (MONITOR == null) ? MONITOR : MONITOR.trim();
				 MONITORLOCATION =emcdb.dynaRs.getString("MONITORLOCATION"); 
				 MONITORLOCATION = (MONITORLOCATION == null) ? MONITORLOCATION : MONITORLOCATION.trim();
				 //COMMENTS = emcdb.dynaRs.getString("COMMENTS");
				 BUSINESSTYPE =emcdb.dynaRs.getString("BUSINESSTYPE");
				 BUSINESSTYPE = (BUSINESSTYPE == null) ? BUSINESSTYPE : BUSINESSTYPE.trim();
				 BUSINESSNAME =emcdb.dynaRs.getString("BUSINESSNAME");
				 BUSINESSNAME = (BUSINESSNAME == null) ? BUSINESSNAME : BUSINESSNAME.trim();
				
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//return;
			return "获取不到数据。";
		}
		String[] headers = {"时间","频率 (Hz)","Level (dBμV/m)","文件信息"};
		String[] content;
		emcdb.closeRs();
		emcdb.dynaRs = emcdb.QuerySQL(sql2);
		int i = -1;
		try {
			csvWriter.writeRecord(headers);
			while(emcdb.dynaRs.next()){
				
				if((++i)<7){	
					
					content = new String[]{emcdb.dynaRs.getString("time"),emcdb.dynaRs.getString("frequence"),emcdb.dynaRs.getString("levels"),information[i]};
					csvWriter.writeRecord(content);
				}else{
					content = new String[]{emcdb.dynaRs.getString("time"),emcdb.dynaRs.getString("frequence"),emcdb.dynaRs.getString("levels")};
					csvWriter.writeRecord(content);
				}
				if(i%1000 == 0){
					System.out.println("写入csv第i行："+i);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return;
			return "IO异常";
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return;
			return "SQL异常";
		}
		emcdb.closeRs();
		csvWriter.close();
		
		//importCDBDPJCOriginalMonitorData(path, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
			
		if(operate.equals("1")){
			try {
				download(path,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return;
				return "Servlet异常。";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return;
				return "IO异常。";
			}
			return "导出csv文件成功。";
		}else{
			if(i<9){
				return "本频点数据量少于10个，禁止写入到数据库。如果需要可以保存到本地。";
			}
			ImportFile infile = new ImportFile();
			String sql3 = "insert into "+table3+" (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,monitor,"+
					"monitorLocation, frequence, bandWidth, modulate, Longitude, Latitude,  businessType,Station) values('"+
					fileName +"','0','0','0','0','"+BUSINESSNAME+"','"+MONITOR+"','"+
					MONITORLOCATION + "'," + frequence + ",'" + BANDWIDTH+ "','"+ DEMODEN +"','" + LONGITUDE + "','" + LATITUDE +"','" + 
					BUSINESSTYPE + "','" + STATION + "')";
			System.out.println(sql3);
			if(emcdb.UpdateSQL(sql3)>0){
				return infile.importOriginalfromSignal(path,fileName,newType);
			}else{
				return "写入信号参数登记失败。";
			}
		}
		//return;
		//return "导出csv成功。";
		 
		 
	}
	
	/**
	 * 导出文件到客户端
	 * @param fileUrl 文件路径+文件名
	 * @param response response响应
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void download(String fileUrl, HttpServletResponse response) throws ServletException, IOException {
		
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("\\") +1, fileUrl.length());// 文件名称
		//System.out.println(fileName);
		// 清空response
		response.reset();
		/*
		URL url = new URL(fileUrl);
		URLConnection conn = url.openConnection();
		int filesize = conn.getContentLength(); // 取数据长度
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		
		// 文件名称转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
		response.addHeader("Content-Disposition",
		        "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso8859-1"));
		response.addHeader("Content-Length", "" + filesize);
		BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		// 从输入流中读入字节流，然后写到文件中
		byte[] buffer = new byte[1024];
		int nRead;
		while ((nRead = bis.read(buffer, 0, 1024)) > 0) { // bis为网络输入流
		os.write(buffer, 0, nRead);
		}
		bis.close();
		os.flush();
		os.close();
		*/
		//System.out.println(URLEncoder.encode(fileName, "UTF-8"));
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
	       
		FileInputStream is = new FileInputStream(fileUrl);
		byte[] buffer = new byte[1024];
		int length = -1;
		 //创建输出流
		OutputStream os = response.getOutputStream();
		while ((length = is.read(buffer, 0, 1024)) != -1) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.flush();
		os.close();
		}
	/**
	 * 计算dpjc原始数据的发射时段，并写入信号参数登记数据
	 * @param bh
	 * @param fileType
	 * @param threshold 阈值
	 * @param interval 时间间隔
	 * @return
	 */
	public static String launchTime(String bh,String fileType,Double threshold,Double interval){
		
		String table = null;
		String table2 = null;
		String table3= null;
		String newbh = null;
		if(fileType.equals("DBDPJCQuery")){
			
			table = "EMCDPJCDATA";
			table2 = "EMCDPJC";
			table3 = "EMCDPJCParameter";
			newbh = "DPJC-";
		}else if(fileType.equals("CDBDPJCQuery")){
			table = "EMCUDPJCDATA";
			table2 = "EMCUDPJC";
			table3 = "EMCUDPJCParameter";
			newbh = "CDPJC-";
		}else{
			table = table2 = table3 = "wrong";
			
		}
		String sql = "select * from "+table+ " where bh='"+bh+"' order by to_timestamp(time,'yyyy-MM-dd HH24:MI:ss.ff')";
		String sql2 = "select * from "+table2+" where bh='"+bh+"'";

		Double first = -100.0;
		Double second = -100.0;
		String firstTime = "0";
		String secondTime = "0";
		LinkedList<String> ll = new LinkedList<String>();
		String startTime = null;
		String endTime = null;
		Double startLevels = 0.0;
		Double endLevels = 0.0;

		String LONGITUDE =null;
		String LATITUDE =null; 
		String BANDWIDTH =null; 
		String DEMODEN =null;
		String STATION =null;
		String MONITOR =null;  
		String MONITORLOCATION =null; 
		String COMMENTS =null; 
		String FREQUENCE =null;  
		String BUSINESSTYPE =null;
		String BUSINESSNAME =null;

		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		//System.out.println("sql2:"+sql2);
		emcdb.dynaRs = emcdb.QuerySQL(sql2);
		try {
			if(emcdb.dynaRs.next()){
				startTime = emcdb.dynaRs.getString("starttime");
				startTime = (startTime == null) ? startTime : startTime.trim();
				endTime = emcdb.dynaRs.getString("endTime");
				endTime = (endTime == null) ? endTime : endTime.trim();
				 FREQUENCE =emcdb.dynaRs.getString("FREQUENCE");  
				 FREQUENCE = (FREQUENCE == null) ? FREQUENCE : FREQUENCE.trim();
				 
				 LONGITUDE =emcdb.dynaRs.getString("LONGITUDE");
				 LONGITUDE = (LONGITUDE == null) ? LONGITUDE : LONGITUDE.trim();
				 LATITUDE =emcdb.dynaRs.getString("LATITUDE"); 
				 LATITUDE = (LATITUDE == null) ? LATITUDE : LATITUDE.trim();
				 BANDWIDTH =emcdb.dynaRs.getString("BANDWIDTH"); 
				 BANDWIDTH = (BANDWIDTH == null) ? BANDWIDTH : BANDWIDTH.trim();
				 DEMODEN =emcdb.dynaRs.getString("DEMODEN");
				 DEMODEN = (DEMODEN == null) ? DEMODEN : DEMODEN.trim();
				 STATION =emcdb.dynaRs.getString("STATION");
				 STATION = (STATION == null) ? STATION : STATION.trim();
				 MONITOR =emcdb.dynaRs.getString("MONITOR");  
				 MONITOR = (MONITOR == null) ? MONITOR : MONITOR.trim();
				 MONITORLOCATION =emcdb.dynaRs.getString("MONITORLOCATION"); 
				 MONITORLOCATION = (MONITORLOCATION == null) ? MONITORLOCATION : MONITORLOCATION.trim();
				 //COMMENTS = emcdb.dynaRs.getString("COMMENTS");
				 BUSINESSTYPE =emcdb.dynaRs.getString("BUSINESSTYPE");
				 BUSINESSTYPE = (BUSINESSTYPE == null) ? BUSINESSTYPE : BUSINESSTYPE.trim();
				 BUSINESSNAME =emcdb.dynaRs.getString("BUSINESSNAME");
				 BUSINESSNAME = (BUSINESSNAME == null) ? BUSINESSNAME : BUSINESSNAME.trim();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "SQL异常1。";
		}
		Double maxlevels=-1000.0;
		Double minlevels=1000.0;
		Double noise =0.0;
		int count = 0;
		emcdb.closeRs();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		//单数为降到阈值以下时，双数为升到阈值以上时
		try {
			while(emcdb.dynaRs.next()){
				//获取发射时间点
				second = emcdb.dynaRs.getDouble("levels");
				secondTime = emcdb.dynaRs.getString("time");
				if(first != -100.0){	//second不是第一个数据
					if((first>=threshold)&&(second<threshold)){
						//firstTime = firstTime.contains(".") ? firstTime.substring(0,firstTime.lastIndexOf(".")).trim() : firstTime;//去掉毫秒
						ll.add(firstTime);
					}
					if((first<threshold)&&(second>=threshold)){
						
						//secondTime = secondTime.contains(".") ? secondTime.substring(0,secondTime.lastIndexOf(".")).trim() : secondTime;//去掉毫秒
						ll.add(secondTime);
					}
				}else{	//second是第一个数据
					startLevels = second;
					if(second < threshold){
						ll.add(startTime);
					}
				}
				first = emcdb.dynaRs.getDouble("levels");
				firstTime = emcdb.dynaRs.getString("time");
				endLevels = first;
				//获取场强最大值最小值和测试次数
				if(maxlevels<first){
					maxlevels = first;
				}
				if(minlevels>first){
					minlevels = first;
				}
				count++;
			}
			noise = minlevels+8;//背景噪声=场强最小值+8；
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "SQL异常2。";
		}
		emcdb.closeRs();
		int size = ll.size();
		//System.out.println("全部size1:"+size);
		if((size%2 == 1)){//发射时段为单数，说明结尾时场强低于阈值
			ll.add(endTime);
		}
		size = ll.size();//size肯定是双数
		//System.out.println("全部size2:"+size);
		LinkedList<String> newll = new LinkedList<String>();
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=0;i<size-1;i=i+2){
			//System.out.println(i);
			//System.out.println("下行"+ll.get(i));
			//System.out.println("上行"+ll.get(i+1));
			try {
				if(fmt.parse(ll.get(i+1)).getTime() - fmt.parse(ll.get(i)).getTime() > (interval*60*1000)){
						newll.add(ll.get(i));
						newll.add(ll.get(i+1));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "SimpleDateFormat异常。";
			}
		}
		//System.out.println("newll:"+newll);
		String result = "";
		int newSize = newll.size();
		//System.out.println("newSize:"+newSize);
		if(newSize == 0){
			//result = startTime+"-"+endTime;
			if(startLevels >= threshold){
				result = result + startTime+"-"+endTime+"; ";
			}else{
				result = result + ll.get(1)+"-"+endTime+"; ";
			}
		}else{
			//LinkedList<String> result = new LinkedList<String>();
			if(!newll.get(0).equals(startTime)){
				if(startLevels >= threshold){
					result = result + startTime+"-"+newll.get(0)+"; ";
				}else{
					result = result + ll.get(1)+"-"+newll.get(0)+"; ";
				}
			}
			for(int i=1;i<newSize-1;i=i+2){
				result = result + newll.get(i)+"-"+newll.get(i+1)+"; "; 
			}
			if(!newll.get(newSize-1).equals(endTime)){
				result = result + newll.get(newSize-1)+"-"+endTime+"; ";
			}
		}
		System.out.println("result:"+result);
		
		if(!Handle.isRepeat(bh, fileType)){	//信号参数登记中无此数据
		
			String sql3 = "insert into "+table3+" (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,monitor,"+
			"monitorLocation, frequence, bandWidth, modulate, Longitude, Latitude,  businessType, launchTime, "+
			"threshold,Station,levels,testnumber,noise) values('"+bh +"','1','0','0','0','"+BUSINESSNAME+"','"+MONITOR+"','"+
			MONITORLOCATION + "'," + FREQUENCE + ",'" + BANDWIDTH+ "','"+ DEMODEN +"','" + LONGITUDE + "','" + LATITUDE +"','" + 
			BUSINESSTYPE + "','" + result + "','" + threshold + "','" + STATION + "','" + maxlevels + "','" + count + "','" + noise + "')";
			//System.out.println(sql3);
			if(emcdb.UpdateSQL(sql3)>0){
				/*
			   ImportFile infile = new ImportFile();
			   sql = "select data FROM " + table2 +" WHERE  bh = '"+ bh +"'";
			   bh = bh.contains(" ") ? bh.substring(0,bh.indexOf(" ")).trim() : bh;
			   result=EMCDB.ExportFile(sql,bh.trim(),basepath);
			   infile.importOriginalfromSignal(basepath+result,newbh,fileType);
				*/
				emcdb.closeStm();
				return result +"已写入数据库。";
			}else{
				emcdb.closeStm();
				return "写入数据库失败。";
			}
		}else{	//信号参数登记中已经有数据
			String sql3 = "update "+table3+" set launchtime='"+result+"',threshold='"+threshold
					+"',levels='"+maxlevels+"',testnumber='"+count+"',noise='"+noise+"' where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql3)>0){
				emcdb.closeStm();
				return result +"已写入数据库。";
			}else{
				emcdb.closeStm();
				return "写入数据库失败。";
			}
		}
	}
	
	public static String occupancySave(String bh,String type,Double occupancy){
		
		String table2 = null;
		String table3= null;
		
		if(type.equals("DBDPJCQuery")){
			
			table2 = "EMCDPJC";
			table3 = "EMCDPJCParameter";
		}else if(type.equals("CDBDPJCQuery")){
			
			table2 = "EMCUDPJC";
			table3 = "EMCUDPJCParameter";
		}else{
			table2 = table3 = "wrong";
			
		}
		String sql2 = "select * from "+table2+" where bh='"+bh+"'";
		String LONGITUDE =null;
		String LATITUDE =null; 
		String BANDWIDTH =null; 
		String DEMODEN =null;
		String STATION =null;
		String MONITOR =null;  
		String MONITORLOCATION =null; 
		String COMMENTS =null; 
		String FREQUENCE =null;  
		String BUSINESSTYPE =null;
		String BUSINESSNAME =null;

		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		//System.out.println("sql2:"+sql2);
		emcdb.dynaRs = emcdb.QuerySQL(sql2);
		try {
			if(emcdb.dynaRs.next()){
				 FREQUENCE =emcdb.dynaRs.getString("FREQUENCE");  
				 FREQUENCE = (FREQUENCE == null) ? FREQUENCE : FREQUENCE.trim();
				 LONGITUDE =emcdb.dynaRs.getString("LONGITUDE");
				 LONGITUDE = (LONGITUDE == null) ? LONGITUDE : LONGITUDE.trim();
				 LATITUDE =emcdb.dynaRs.getString("LATITUDE"); 
				 LATITUDE = (LATITUDE == null) ? LATITUDE : LATITUDE.trim();
				 BANDWIDTH =emcdb.dynaRs.getString("BANDWIDTH"); 
				 BANDWIDTH = (BANDWIDTH == null) ? BANDWIDTH : BANDWIDTH.trim();
				 DEMODEN =emcdb.dynaRs.getString("DEMODEN");
				 DEMODEN = (DEMODEN == null) ? DEMODEN : DEMODEN.trim();
				 STATION =emcdb.dynaRs.getString("STATION");
				 STATION = (STATION == null) ? STATION : STATION.trim();
				 MONITOR =emcdb.dynaRs.getString("MONITOR");  
				 MONITOR = (MONITOR == null) ? MONITOR : MONITOR.trim();
				 MONITORLOCATION =emcdb.dynaRs.getString("MONITORLOCATION"); 
				 MONITORLOCATION = (MONITORLOCATION == null) ? MONITORLOCATION : MONITORLOCATION.trim();
				 //COMMENTS = emcdb.dynaRs.getString("COMMENTS");
				 BUSINESSTYPE =emcdb.dynaRs.getString("BUSINESSTYPE");
				 BUSINESSTYPE = (BUSINESSTYPE == null) ? BUSINESSTYPE : BUSINESSTYPE.trim();
				 BUSINESSNAME =emcdb.dynaRs.getString("BUSINESSNAME");
				 BUSINESSNAME = (BUSINESSNAME == null) ? BUSINESSNAME : BUSINESSNAME.trim();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "SQL异常1。";
		}
		emcdb.closeRs();
		
		if(!Handle.isRepeat(bh, type)){
		
			String sql3 = "insert into "+table3+" (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,monitor,"+
			"monitorLocation, frequence, bandWidth, modulate, Longitude, Latitude,  businessType,"+
			"Station,occupancy) values('"+bh +"','1','0','0','0','"+BUSINESSNAME+"','"+MONITOR+"','"+
			MONITORLOCATION + "'," + FREQUENCE + ",'" + BANDWIDTH+ "','"+ DEMODEN +"','" + LONGITUDE + "','" + LATITUDE +"','" + 
			BUSINESSTYPE + "','" + STATION + "','" + occupancy + "')";
			//System.out.println(sql3);
			if(emcdb.UpdateSQL(sql3)>0){
				emcdb.closeStm();
				return "存储成功。";
			}else{
				emcdb.closeStm();
				return "存储失败。";
			}
		}else{	//信号参数登记中已经有数据
			String sql3 = "update "+table3+" set occupancy='"+occupancy+"' where bh='"+bh+"'";
			System.out.println(sql3);
			if(emcdb.UpdateSQL(sql3)>0){
				emcdb.closeStm();
				return "填写成功。";
			}else{
				emcdb.closeStm();
				return "填写失败。";
			}
		}
	}
	/**
	 * 
	 * @param bh 所有要导出的数据的bh
	 * @param path 文件夹路径
	 * @param filepath Excel文件路径
	 * @param response
	 * @return
	 */
	public static String batchDownload(String[] bh,String path,String filepath,HttpServletResponse response){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		LinkedList<LinkedList<String>> ll = new LinkedList<LinkedList<String>>();
		//制表头
		LinkedList<String> list = new LinkedList<String>();
		list.add("任务类型");list.add("任务名称");list.add("日期");list.add("轨位（°E）");
		list.add("卫星名称");list.add("国家");list.add("转发器");list.add("起始频率（MHz）");
		list.add("终止频率（MHz）");list.add("频段");list.add("极化方式");list.add("天线类型");
		list.add("RBW（KHz）");list.add("VBW（KHz）");list.add("衰减（dB）");list.add("参考电平（dBm）");
		list.add("监测站");list.add("监测人");list.add("备注");list.add("频谱图");
		ll.add(list);
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		String sql = null;
		for(int i=0;i<bh.length;i++){
			sql = "select * from emcwzfqjc where bh='"+bh[i]+"'";
			String fileName = bh[i].contains(" ") ? bh[i].substring(0,bh[i].indexOf(" ")).trim() : bh[i];
			String result=EMCDB.ExportFile(sql,fileName,path+"频谱图\\");
			//System.out.println(result);
			//无data数据时，result返回0
			if(result.equals("0")){
				result = "无";
			}
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				while(emcdb.dynaRs.next()){
					list = new LinkedList<String>();
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("businessType")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("businessName")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("time")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("position")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("satName")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("country")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("zfq")));list.add(String.valueOf(emcdb.dynaRs.getDouble("startFrequence")/1000000));
					list.add(String.valueOf(emcdb.dynaRs.getDouble("endFrequence")/1000000));list.add(UserInfor.forNull(emcdb.dynaRs.getString("frequence")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("polar")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("antenna")));
					list.add(String.valueOf(emcdb.dynaRs.getDouble("rbw")/1000));list.add(String.valueOf(emcdb.dynaRs.getDouble("vbw")/1000));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("weaken")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("levels")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("station")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("person")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("comments")));list.add(UserInfor.forNull(result));
					ll.add(list);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "读取数据库异常";
			}
		}
		boolean result = ExcelUtil.writeExcel(ll, filepath);
		if(result){
			try {
				//压缩文件
				String zip = path.substring(0,path.lastIndexOf("\\"));//去掉\
				zip = zip + ".zip";//压缩文件路径
				AntZipUtil.zipFile(zip, path);//创建压缩文件
				clearFiles(path);//删除源文件
				//导出文件
				//System.out.println(zip);
				download(zip,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return"压缩文件失败。";
			}
		}else{
			return "写Excel文件异常";
		}
		return "导出成功。";
	}
	/**
	 * 
	 * @param bh 所有要导出的数据的bh
	 * @param type 查询页面类型
	 * @param path 文件夹路径，如D:\a\
	 * @param filepath Excel文件路径
	 * @param response
	 * @return
	 */
	public static String batchDownloadParameter(String[] bh,String type,String path,String filepath,HttpServletResponse response){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		LinkedList<LinkedList<String>> ll = new LinkedList<LinkedList<String>>();
		//制表头
		LinkedList<String> list = new LinkedList<String>();
		list.add("频率(MHz)");list.add("监测站");list.add("占用带宽(kHz)");list.add("调制方式");
		list.add("业务类型");list.add("业务名称");list.add("信号类型");list.add("发射时段");
		list.add("场强最大值(dBuV/m)");list.add("测试次数");list.add("背景噪声(dBuV/m.Hz)");list.add("占用度");
		list.add("统计门限(dBuV/m)");list.add("示向度/监测站/等级");list.add("经度");list.add("纬度");
		list.add("城市");list.add("以往对比");list.add("中频分析时间");list.add("监测人");
		list.add("监测地");list.add("备注");
		list.add("监测原始文件");list.add("中频图");list.add("示向图");list.add("定位图");
		ll.add(list);
		String table = null;
		String table1 = null;
		if(type.equals("DBDPJCQuery")){
			table = "EMCDPJCParameter";
			table1 = "EMCDPJCGraphic";
		}else if(type.equals("DBXHCXQuery")){
			table = "EMCDPCXParameter";
			table1 = "EMCDPCXGraphic";
		}else if(type.equals("CDBDPJCQuery")){
			table = "EMCUDPJCParameter";
			table1 = "EMCUDPJCGraphic";
		}else if(type.equals("CDBXHCXQuery")){
			table = "EMCUDPCXParameter";
			table1 = "EMCUDPCXGraphic";
		}else{
			return "页面类型错误";
		}
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		String sql = null;
		String sql1 = null;
		String sql2 = null;
		String sql3 = null;
		String FREQGRAP = null;
		String DIREGRAP = null;
		String LOCAGRAP = null;
		for(int i=0;i<bh.length;i++){
			sql = "select * from "+table+" where bh='"+bh[i]+"'";
			sql1 = "select data from "+table1+" where bh='"+bh[i]+"' and GRAPHICTYPE='中频图'";
			sql2 = "select data from "+table1+" where bh='"+bh[i]+"' and GRAPHICTYPE='示向图'";
			sql3 = "select data from "+table1+" where bh='"+bh[i]+"' and GRAPHICTYPE='定位图'";
			
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			try {
				while(emcdb.dynaRs.next()){
					String fileName = bh[i].contains(" ") ? bh[i].substring(0,bh[i].indexOf(" ")).trim() : bh[i];
					if(emcdb.dynaRs.getString("FREQGRAP").trim().equals("1")){
						FREQGRAP = EMCDB.ExportFile(sql1,fileName,path+"中频图\\");
					}else{
						FREQGRAP = "无";
					}
					if(emcdb.dynaRs.getString("DIREGRAP").trim().equals("1")){
						DIREGRAP = EMCDB.ExportFile(sql2,fileName,path+"示向图\\");
					}else{
						DIREGRAP = "无";
					}
					if(emcdb.dynaRs.getString("LOCAGRAP").trim().equals("1")){
						LOCAGRAP = EMCDB.ExportFile(sql3,fileName,path+"定位图\\");
					}else{
						LOCAGRAP = "无";
					}
					list = new LinkedList<String>();
					list.add(String.valueOf(emcdb.dynaRs.getDouble("frequence")/1000000));list.add(UserInfor.forNull(emcdb.dynaRs.getString("Station")));
					list.add(String.valueOf(emcdb.dynaRs.getDouble("bandWidth")/1000));list.add(UserInfor.forNull(emcdb.dynaRs.getString("modulate")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("businessType")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("businessName")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("signalType")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("launchTime")));
					
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("levels")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("testNumber")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("noise")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("occupancy")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("threshold")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("direction")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("Longitude")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("Latitude")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("city")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("compared")));
					
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("monitorTime")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("monitor")));
					list.add(UserInfor.forNull(emcdb.dynaRs.getString("monitorLocation")));list.add(UserInfor.forNull(emcdb.dynaRs.getString("comments")));
					list.add(UserInfor.forNull(""));list.add(UserInfor.forNull(FREQGRAP));list.add(UserInfor.forNull(DIREGRAP));list.add(UserInfor.forNull(LOCAGRAP));
					ll.add(list);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "读取数据库异常";
			}
		}
		boolean result = ExcelUtil.writeExcel(ll, filepath);
		if(result){
			try {
				//压缩文件
				String zip = path.substring(0,path.lastIndexOf("\\"));//去掉\
				zip = zip + ".zip";//压缩文件路径
				AntZipUtil.zipFile(zip, path);//创建压缩文件
				clearFiles(path);//删除源文件
				//导出文件
				//System.out.println(zip);
				download(zip,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return"压缩文件失败。";
			}
		}else{
			return "写Excel文件异常";
		}
		return "导出成功。";
	}
	/**  
     * 下载远程文件并保存到本地,这是反过来的。客户端是本地 ,无用 
     * @param remoteFilePath 远程文件路径   
     * @param localFilePath 本地文件路径  
     */
    public static void downloadFile(String remoteFilePath, String localFilePath)
    {	
    	System.out.println(remoteFilePath);
    	System.out.println(localFilePath);
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
	
	
	/**
	 * 删除指定路径下的文件的目录
	 * @param workspaceRootPath 要删除的路径
	 */
	public static void clearFiles(String workspaceRootPath){
	     File file = new File(workspaceRootPath);
	     if(file.exists()){
	          deleteFile(file);
	     }else{
	    	 //System.out.println("路径不存在");
	     }
	}
	public static void deleteFile(File file){
		
	     if(file.isDirectory()){
	          File[] files = file.listFiles();
	          for(int i=0; i<files.length; i++){
	               deleteFile(files[i]);
	          }
	     }
	     file.delete();
	}

}
