package DataQuery;

import java.util.Arrays;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import DBConnection.*;
import BaseData.*;
public class DataQuery {
	
	public DataQuery(){
		
		
	}
	
	public static LinkedList<DPJCHead> DPJCQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		LinkedList<DPJCHead> list=new LinkedList<DPJCHead>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			sql = "select frequence,businessType,businessName,starttime,endtime,Longitude,Latitude,Bandwidth,RadioFreattenuation,midFreattenuation," +
				"DeModen,Detector,Station,Monitor,monitorlocation,bh from emcdpjc" + sql +" order by frequence,starttime";
			
		}else if(type.equals("CDBDPJCQuery")){
			sql = "select frequence,businessType,businessName,starttime,endtime,Longitude,Latitude,Bandwidth,RadioFreattenuation,midFreattenuation," +
					"DeModen,Detector,Station,Monitor,monitorlocation,bh from emcudpjc" + sql +" order by frequence,starttime";
				
		}/*else if(type.equals("DBDPJCRepeat")){
			sql = select * from  emcdpjc where md5 in (select md5 from emcdpjc group bymd5 having count(md5) > 1)
				
		}else if(type.equals("CDBDPJCRepeat")){
			sql = select * from  emcudpjc where md5 in (select md5 from emcudpjc group bymd5 having count(md5) > 1)
			
		}*/
		else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					DPJCHead dpjc = new DPJCHead();
					dpjc.Frequence = Double.parseDouble(emcdb.dynaRs.getString("frequence"));
					dpjc.StartTime = emcdb.dynaRs.getString("starttime");
					dpjc.EndTime = emcdb.dynaRs.getString("endtime");
					dpjc.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
					dpjc.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
					dpjc.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
					dpjc.RadioFreattenuation = emcdb.dynaRs.getString("RadioFreattenuation");
					dpjc.MidFreattenuation = emcdb.dynaRs.getString("midFreattenuation");
					dpjc.DeModen = emcdb.dynaRs.getString("DeModen");
					dpjc.Detector = emcdb.dynaRs.getString("Detector");
					dpjc.Station = emcdb.dynaRs.getString("Station");
					dpjc.Monitor = emcdb.dynaRs.getString("Monitor");
					dpjc.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					dpjc.BH = emcdb.dynaRs.getString("bh");
					dpjc.businessType = emcdb.dynaRs.getString("businessType");
					dpjc.businessName = emcdb.dynaRs.getString("businessName");
					list.add(dpjc);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}

    
	public static LinkedList<PDSMHead> PDSMQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		LinkedList<PDSMHead> list=new LinkedList<PDSMHead>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and StartFrequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and EndFrequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBPDSMQuery")){
			sql = "select businessType,businessName,startfrequence,endfrequence,starttime,endtime,Longitude,Latitude,no,type,step,Bandwidth,RadioFreattenuation,midFreattenuation," +
				"Amplifier,DeModen,Detector,Station,Monitor,monitorlocation,bh from emcpdsm" + sql +" order by starttime,startfrequence";
			
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select businessType,businessName,startfrequence,endfrequence,starttime,endtime,Longitude,Latitude,no,type,step,Bandwidth,RadioFreattenuation,midFreattenuation," +
				"Amplifier,DeModen,Detector,Station,Monitor,monitorlocation,bh from emcupdsm" + sql +" order by starttime,startfrequence";
					
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					PDSMHead pdsm = new PDSMHead();
					pdsm.No = emcdb.dynaRs.getInt("no");
					pdsm.Type = emcdb.dynaRs.getString("type");
					pdsm.StartFrequence = Double.parseDouble(emcdb.dynaRs.getString("StartFrequence"));
					pdsm.EndFrequence = Double.parseDouble(emcdb.dynaRs.getString("EndFrequence"));
					pdsm.Step = emcdb.dynaRs.getString("step");
					pdsm.StartTime = emcdb.dynaRs.getString("starttime");
					pdsm.EndTime = emcdb.dynaRs.getString("endtime");
					pdsm.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
					pdsm.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
					pdsm.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
					pdsm.RadioFreattenuation = emcdb.dynaRs.getString("RadioFreattenuation");
					pdsm.MidFreattenuation = emcdb.dynaRs.getString("midFreattenuation");
					pdsm.Amplifier = emcdb.dynaRs.getString("Amplifier");
					pdsm.DeModen = emcdb.dynaRs.getString("DeModen");
					pdsm.Detector = emcdb.dynaRs.getString("Detector");
					pdsm.Station = emcdb.dynaRs.getString("Station");
					pdsm.Monitor = emcdb.dynaRs.getString("Monitor");
					pdsm.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					pdsm.BH = emcdb.dynaRs.getString("bh");
					pdsm.businessType = emcdb.dynaRs.getString("businessType");
					pdsm.businessName = emcdb.dynaRs.getString("businessName");
					list.add(pdsm);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	public static LinkedList<DPCXHead> DPCXQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		LinkedList<DPCXHead> list=new LinkedList<DPCXHead>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBXHCXQuery")){
			sql = "select businessType,businessName,frequence,starttime,endtime,Longitude,Latitude,Bandwidth,RadioWidth," +
				"Decode,Schema,Station,Monitor,monitorlocation,bh from emcdpcx" + sql +" order by starttime,frequence";
			
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select businessType,businessName,frequence,starttime,endtime,Longitude,Latitude,Bandwidth,RadioWidth," +
					"Decode,Schema,Station,Monitor,monitorlocation,bh from emcudpcx" + sql +" order by starttime,frequence";
						
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					DPCXHead dpcx = new DPCXHead();
					dpcx.Frequence = Double.parseDouble(emcdb.dynaRs.getString("Frequence"));
					dpcx.StartTime = emcdb.dynaRs.getString("starttime");
					dpcx.EndTime = emcdb.dynaRs.getString("endtime");
					dpcx.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
					dpcx.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
					dpcx.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
					dpcx.Radiowidth = emcdb.dynaRs.getString("Radiowidth");
					dpcx.Decode = emcdb.dynaRs.getString("Decode");
					dpcx.schema = emcdb.dynaRs.getString("schema");
					dpcx.Station = emcdb.dynaRs.getString("Station");
					dpcx.Monitor = emcdb.dynaRs.getString("Monitor");
					dpcx.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					dpcx.BH = emcdb.dynaRs.getString("bh");
					dpcx.businessType = emcdb.dynaRs.getString("businessType");
					dpcx.businessName = emcdb.dynaRs.getString("businessName");
					list.add(dpcx);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	public static LinkedList<LHDWHead> LHDWQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		
		LinkedList<LHDWHead> list=new LinkedList<LHDWHead>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence1>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence1<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBLHDWQuery")){
			sql = "select businessType,businessName,starttime,endtime,Station1,Longitude1,Latitude1,frequence1,Station2,Longitude2,Latitude2,frequence2," +
					"Station3,Longitude3,Latitude3,frequence3,Station,Monitor,monitorlocation,bh from emclhdw" + sql +" order by starttime, frequence1";
				
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select businessType,businessName,starttime,endtime,Station1,Longitude1,Latitude1,frequence1,Station2,Longitude2,Latitude2,frequence2," +
				"Station3,Longitude3,Latitude3,frequence3,Station,Monitor,monitorlocation,bh from emculhdw" + sql +" order by starttime, frequence1";
							
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					LHDWHead lhdw = new LHDWHead();
					lhdw.StartTime = emcdb.dynaRs.getString("starttime");
					lhdw.EndTime = emcdb.dynaRs.getString("endtime");
					lhdw.Station1 = emcdb.dynaRs.getString("Station1");
					lhdw.Longitude1 = Double.parseDouble(emcdb.dynaRs.getString("Longitude1"));
					lhdw.Latitude1 = Double.parseDouble(emcdb.dynaRs.getString("Latitude1"));
					lhdw.Frequence1 = Double.parseDouble(emcdb.dynaRs.getString("Frequence1"));
					lhdw.Station2 = emcdb.dynaRs.getString("Station2");
					lhdw.Longitude2 = Double.parseDouble(emcdb.dynaRs.getString("Longitude2"));
					lhdw.Latitude2 = Double.parseDouble(emcdb.dynaRs.getString("Latitude2"));
					lhdw.Frequence2 = Double.parseDouble(emcdb.dynaRs.getString("Frequence2"));
					lhdw.Station3 = emcdb.dynaRs.getString("Station3");
					lhdw.Longitude3 = Double.parseDouble(emcdb.dynaRs.getString("Longitude3"));
					lhdw.Latitude3 = Double.parseDouble(emcdb.dynaRs.getString("Latitude3"));
					lhdw.Frequence3 = Double.parseDouble(emcdb.dynaRs.getString("Frequence3"));
					lhdw.Station = emcdb.dynaRs.getString("Station");
					lhdw.Monitor = emcdb.dynaRs.getString("Monitor");
					lhdw.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					lhdw.BH = emcdb.dynaRs.getString("bh");
					lhdw.businessType = emcdb.dynaRs.getString("businessType");
					lhdw.businessName = emcdb.dynaRs.getString("businessName");
					list.add(lhdw);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}


	
	/**
	 * 查询单频监测和信号测向的信号参数登记数据
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> ParameterQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){//monitorTime,frequence,BANDWIDTH,Longitude,Latitude,MODULATE,BUSINESSTYPE,LAUNCHTIME,LEVELS,TESTNUMBER,Station,bh
			
			sql = "select * from EMCDPJCParameter" + sql +" order by monitorTime, frequence";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select * from EMCUDPJCParameter" + sql +" order by monitorTime, frequence";
		}else if(type.equals("DBXHCXQuery")){
			
			sql = "select * from EMCDPCXParameter" + sql +" order by monitorTime, frequence";
		}else if(type.equals("CDBXHCXQuery")){
			
			sql = "select * from EMCUDPCXParameter" + sql +" order by monitorTime, frequence";
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					//System.out.println(emcdb.dynaRs.getString("monitorTime"));
					if(!isDate(emcdb.dynaRs.getString("monitorTime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else 
					if(fmt.parse(emcdb.dynaRs.getString("monitorTime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					//System.out.println(emcdb.dynaRs.getString("monitorTime"));
					if(!isDate(emcdb.dynaRs.getString("monitorTime"))){
						flag = true;
					}else 
					if(fmt.parse(emcdb.dynaRs.getString("monitorTime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				//System.out.println(flag);
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					
					al.add(String.valueOf(emcdb.dynaRs.getDouble("frequence")/1000000));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(String.valueOf(emcdb.dynaRs.getDouble("BANDWIDTH")/1000));
					al.add(emcdb.dynaRs.getString("MODULATE"));
					al.add(emcdb.dynaRs.getString("BUSINESSTYPE"));
					al.add(emcdb.dynaRs.getString("businessName"));
					
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("LAUNCHTIME"));
					al.add(emcdb.dynaRs.getString("LEVELS"));
					al.add(emcdb.dynaRs.getString("TESTNUMBER"));
					al.add(emcdb.dynaRs.getString("NOISE"));
					al.add(emcdb.dynaRs.getString("OCCUPANCY"));
					
					al.add(emcdb.dynaRs.getString("THRESHOLD"));
					al.add(emcdb.dynaRs.getString("DIRECTION"));
					al.add(emcdb.dynaRs.getString("Longitude"));
					al.add(emcdb.dynaRs.getString("Latitude"));
					al.add(emcdb.dynaRs.getString("city"));
					al.add(emcdb.dynaRs.getString("COMPARED"));
					
					al.add(emcdb.dynaRs.getString("monitorTime"));
					al.add(emcdb.dynaRs.getString("monitor"));
					al.add(emcdb.dynaRs.getString("monitorLocation"));
					al.add(emcdb.dynaRs.getString("COMMENTS"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("Original"));
					al.add(emcdb.dynaRs.getString("FREQGRAP"));
					al.add(emcdb.dynaRs.getString("DIREGRAP"));
					al.add(emcdb.dynaRs.getString("LOCAGRAP"));
					list.add(al);
					//System.out.println(list.size());
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			System.out.println("parseException el");
			e1.printStackTrace();
			return null;
		}
		//System.out.println(list.size());
		return list;
	}
	
	
	/**
	 * 单频监测和信号测向的图形数据查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的图形数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> GraphicQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime, frequence,Station,Monitor,monitorlocation,bh from EMCDPJCGraphic" + sql +" order by GraphicType, starttime, frequence";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime, frequence,Station,Monitor,monitorlocation,bh from EMCuDPJCGraphic" + sql +" order by GraphicType, starttime, frequence";
		}else if(type.equals("DBXHCXQuery")){
			
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime, frequence,Station,Monitor,monitorlocation,bh from EMCDPCXGraphic" + sql +" order by GraphicType, starttime, frequence";
		}else if(type.equals("CDBXHCXQuery")){
			
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime, frequence,Station,Monitor,monitorlocation,bh from EMCuDPCXGraphic" + sql +" order by GraphicType, starttime, frequence";		
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("GraphicType"));
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					//System.out.println("图形station："+emcdb.dynaRs.getString("Station"));
					//System.out.println("图形station.contaions('null')："+emcdb.dynaRs.getString("Station").contains("null"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 频段扫描的图形数据查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的图形数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> GraphicQuery_PDSM(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and startFrequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and EndFrequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBPDSMQuery")){
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime,startFrequence,endFrequence,Station,Monitor,monitorlocation,bh from EMCPDSMGraphic" + sql +" order by starttime, startFrequence";
			
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select GraphicType,businessType,businessName,signalType,longitude,latitude,starttime,endtime,startFrequence,endFrequence,Station,Monitor,monitorlocation,bh from EMCuPDSMGraphic" + sql +" order by starttime, startFrequence";
				
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("GraphicType"));
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("startFrequence"));
					al.add(emcdb.dynaRs.getString("EndFrequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
			
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 单频监测和信号测向音频数据查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的音频数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> AudioQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCDPJCAudio" + sql +" order by starttime, frequence";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCuDPJCAudio" + sql +" order by starttime, frequence";
		}else if(type.equals("DBXHCXQuery")){
			
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCDPCXAudio" + sql +" order by starttime, frequence";
		}else if(type.equals("CDBXHCXQuery")){
			
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCuDPCXAudio" + sql +" order by starttime, frequence";		
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 单频监测、频段扫描和信号测向的视频数据查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的视频数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> VideoQuery(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCDPJCVideo" + sql +" order by starttime, frequence";
			
		}else if(type.equals("CDBDPJCQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCUDPJCVideo" + sql +" order by starttime, frequence";
				
		}else if(type.equals("DBPDSMQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCPDSMVideo" + sql +" order by starttime, frequence";
			
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCUPDSMVideo" + sql +" order by starttime, frequence";
				
		}else if(type.equals("DBXHCXQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCDPCXVideo" + sql +" order by starttime, frequence";
			
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,startfrequence,endfrequence,Station,Monitor,monitorlocation,bh from EMCUDPCXVideo" + sql +" order by starttime, frequence";
				
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("STARTfrequence"));
					al.add(emcdb.dynaRs.getString("ENDfrequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 单频监测、频段扫描和信号测向的视频数据查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的视频数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> VideoQuery_LHDW(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBLHDWQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCLHDWVideo" + sql +" order by starttime, frequence";
			
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select starttime,endtime,businessType,businessName,signalType,longitude,latitude, frequence,Station,Monitor,monitorlocation,bh from EMCULHDWVideo" + sql +" order by starttime, frequence";
				
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 短波超短波监测报告查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param keyWords 任务关键词
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的视频数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> JCBGQuery(String startTime,String endTime,String startFrequence,String endFrequence,String keyWords,String station,String monitorlocation,String author,String type){
			
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and startFrequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and startFrequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		if(!author.equals("")){
			if(author.contains("null")){
				sql = sql + " and writer is null ";
			}else{
				sql = sql + " and writer='" + author +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBJCBGQuery")){
			sql = "select businessType,businessName,type, filename,starttime,endtime, STARTfrequence,keywords,Station,writer,monitorlocation,equipment,bh from EMCJCBG" + sql +" order by starttime, STARTfrequence";
			
		}else if(type.equals("CDBJCBGQuery")){
			sql = "select businessType,businessName,type, filename,starttime,endtime, STARTfrequence,keywords,Station,writer,monitorlocation,equipment,bh from EMCUJCBG" + sql +" order by starttime, STARTfrequence";
				
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(!keyWords.equals("")){
					if((!emcdb.dynaRs.getString("keyWords").contains(keyWords))&&(!emcdb.dynaRs.getString("filename").contains(keyWords))){/*不包含关键字，则不显示 */
						flag = false;
					}
				}
				
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("type"));
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("STARTfrequence"));
					al.add(emcdb.dynaRs.getString("keywords"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("equipment"));
					al.add(emcdb.dynaRs.getString("writer"));
					al.add(emcdb.dynaRs.getString("filename"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 卫星监测报告查询
	 * @param startTime	查询起始时间
	 * @param endTime 查询终止时间
	 * @param startFrequence	查询起始频率
	 * @param endFrequence	查询终止频率
	 * @param keyWords 任务关键词
	 * @param station 查询监测台站
	 * @param monitorlocation 查询监测地点
	 * @param type 查询哪一类的视频数据
	 * @return 返回查询到的ArrayList<ArrayList<String>>类型的数据
	 */
	public static ArrayList<ArrayList<String>> JCBGQuery_Sat(String reportType,String startTime,String endTime,String keyWords,String station,String monitorlocation,String author,String type){
			
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		/*if(!startFrequence.equals("")){
			sql = sql + " and startFrequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and startFrequence<=" + Integer.parseInt(endFrequence);
		}*/
		if(!reportType.equals("")){
			sql = sql + " and Type='" + reportType + "'";
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		if(!author.equals("")){
			if(author.contains("null")){
				sql = sql + " and writer is null ";
			}else{
				sql = sql + " and writer='" + author +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("WXJCBGQuery")){
			sql = "select businessType,businessName,type, starttime,endtime, filename,keywords,Station,writer,monitorlocation,equipment,bh from EMCWJCBG" + sql +" order by type,starttime";
				
		}
		else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(!keyWords.equals("")){
					if((!emcdb.dynaRs.getString("keyWords").contains(keyWords))&&(!emcdb.dynaRs.getString("filename").contains(keyWords))){/*不包含关键字，则不显示 */
						flag = false;
					}
				}
				
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("filename"));
					al.add(emcdb.dynaRs.getString("type"));
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("equipment"));
					al.add(emcdb.dynaRs.getString("writer"));
					al.add(emcdb.dynaRs.getString("keywords"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 卫星监测转发器监测数据查询
	 * @param startTime
	 * @param endTime
	 * @param startPosition
	 * @param endPosition
	 * @param country
	 * @param frequence
	 * @param station
	 * @param author
	 * @param dataType
	 * @param type
	 * @return 返回ArrayList<ArrayList<String>>类型的查询结果
	 */
	public static ArrayList<ArrayList<String>> ZFQJCQuery(String startTime,String endTime,String startPosition,String endPosition,
			String country,String frequence,String station,String author,String type){
			
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		
		if(!startPosition.equals("")){
			sql = sql + " and Position>=" + Double.parseDouble(startPosition);
		}
		if(!endPosition.equals("")){
			sql = sql + " and Position<=" + Double.parseDouble(endPosition);
		}
		if(!country.equals("")){
			if(country.contains("null")){
				sql = sql + " and country is null ";
			}else{
				sql = sql + " and country='" + country + "'";
			}
		}
		if(!frequence.equals("")){
			if(frequence.contains("null")){
				sql = sql + " and frequence is null ";
			}else{
				sql = sql + " and frequence='" + frequence + "'";
			}
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!author.equals("")){
			if(author.contains("null")){
				sql = sql + " and person is null ";
			}else{
			sql = sql + " and person='" + author + "'";
			}
		}
		//System.out.println(country+"/"+frequence+"/"+station+"/"+author);
		
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("WXZFQJCQuery")){
			sql = "select * from EMCWZFQJC" + sql +" order by position,satname,time";
				
		}
		else{
			return null;
		}
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
					flag = true;
					try {
					if(!startTime.equals("")){
						if(!isDate(emcdb.dynaRs.getString("time"))){
							//System.out.println("!isDate()");
							flag = true;
						} else if(fmt.parse(emcdb.dynaRs.getString("time")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
							flag = false;
						}
					}
					if(!endTime.equals("")){
						if(!isDate(emcdb.dynaRs.getString("time"))){
							//System.out.println("!isDate()");
							flag = true;
						}else if(fmt.parse(emcdb.dynaRs.getString("time")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
							flag = false;
						}
					}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(flag){
					ArrayList<String> al = new ArrayList<String>();
					//System.out.println(emcdb.dynaRs.getString("bh"));
					//System.out.println(emcdb.dynaRs.getBlob("data").length());
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("time"));
					al.add(emcdb.dynaRs.getString("position"));
					al.add(emcdb.dynaRs.getString("satName"));
					al.add(emcdb.dynaRs.getString("country"));
					
					al.add(emcdb.dynaRs.getString("zfq"));
					al.add(String.valueOf((emcdb.dynaRs.getDouble("startFrequence")/1000000)));
					al.add(String.valueOf((emcdb.dynaRs.getDouble("endFrequence")/1000000)));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("polar"));
					al.add(emcdb.dynaRs.getString("antenna"));
					
					al.add(String.valueOf((emcdb.dynaRs.getDouble("rbw")/1000)));
					al.add(String.valueOf((emcdb.dynaRs.getDouble("vbw")/1000)));
					al.add(emcdb.dynaRs.getString("weaken"));
					al.add(emcdb.dynaRs.getString("levels"));
					al.add(emcdb.dynaRs.getString("station"));
					al.add(emcdb.dynaRs.getString("person"));
					
					al.add(emcdb.dynaRs.getString("comments"));
					al.add(String.valueOf(emcdb.dynaRs.getBlob("data").length()));
					al.add(emcdb.dynaRs.getString("bh"));
					list.add(al);
					}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 判断时间范围内，四种检测报告的数量，不存在某种报告则数量为0
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static ArrayList<ArrayList<String>> JCBGAnalysis(String type,String startTime,String endTime){
		
		String sql = "";
		
		if(!startTime.equals("")){
			
			sql = sql + " and to_timestamp(startTime,'yyyy-MM-dd HH24:MI:ss') >= to_timestamp('"+startTime+"','yyyy-MM-dd HH24:MI:ss') ";
			
		}
		if(!endTime.equals("")){
			sql = sql + " and to_timestamp(endTime,'yyyy-MM-dd HH24:MI:ss') <= to_timestamp('"+endTime+"','yyyy-MM-dd HH24:MI:ss') ";
		}
		
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		
		if(type.equals("DBJCBGQuery")){
			sql = "select type,count(*) as num from emcjcbg" +sql+" group by type ";
		}else if(type.equals("CDBJCBGQuery")){
			sql = "select type,count(*) as num from emcujcbg" +sql+" group by type ";
		}else{
			return null;
		}
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		String[] reportType = {"","","",""};//new String[4];//如果某一种类型的报告数量为0，则结果集中不会存在这种报告。但是再界面上应将数量显示为0。此数组存储存在的报告类型，用来判别哪些类型不存在
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		int i=-1;
		try {
			while(emcdb.dynaRs.next()){
					i++;
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("type"));
					al.add(emcdb.dynaRs.getString("num"));
					all.add(al);
					reportType[i] = emcdb.dynaRs.getString("type").trim();
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(!Arrays.asList(reportType).contains("专项监测报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("专项监测报告");
			al.add("0");
			all.add(al);
		}
		if(!Arrays.asList(reportType).contains("干扰查处报告")){
					
					ArrayList<String> al = new ArrayList<String>();
					al.add("干扰查处报告");
					al.add("0");
					all.add(al);
		}
		if(!Arrays.asList(reportType).contains("电磁环境监测报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("电磁环境监测报告");
			al.add("0");
			all.add(al);
		}
		if(!Arrays.asList(reportType).contains("其他报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("其他报告");
			al.add("0");
			all.add(al);
		}
		
		
		return all;
	}
	
	/**
	 * 判断时间范围内，四种检测报告的数量，不存在某种报告则数量为0
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static ArrayList<ArrayList<String>> JCBGAnalysis_Sat(String type,String startTime,String endTime){
		
		String sql = "";
		
		if(!startTime.equals("")){
			sql = sql + " and to_timestamp(startTime,'yyyy-MM-dd HH24:MI:ss') >= to_timestamp('"+startTime+"','yyyy-MM-dd HH24:MI:ss') ";
		}
		if(!endTime.equals("")){
			sql = sql + " and to_timestamp(endTime,'yyyy-MM-dd HH24:MI:ss') <= to_timestamp('"+endTime+"','yyyy-MM-dd HH24:MI:ss') ";
		}
		
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		
		if(type.equals("WXJCBGQuery")){
			sql = "select type,count(*) as num from emcwjcbg" +sql+" group by type ";
		}else{
			return null;
		}
		
		//System.out.println(sql);
		//SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//boolean flag = true;
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		//ArrayList<String> reportType = new ArrayList<String>();
		String[] reportType = {"","","",""};//new String[4];//如果某一种类型的报告数量为0，则结果集中不会存在这种报告。但是再界面上应将数量显示为0。此数组存储存在的报告类型，用来判别哪些类型不存在
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		int i=-1;
		try {
			while(emcdb.dynaRs.next()){
					i++;
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("type"));
					al.add(emcdb.dynaRs.getString("num"));
					all.add(al);
					reportType[i] = emcdb.dynaRs.getString("type").trim();
					//System.out.println("0"+reportType[i]+"0");
					//System.out.print(emcdb.dynaRs.getString("type")+"/");
	     			//System.out.println(emcdb.dynaRs.getString("num"));
				
			}
			emcdb.closeRs();
			emcdb.closeStm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(!Arrays.asList(reportType).contains("专项监测报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("专项监测报告");
			al.add("0");
			all.add(al);
		}
		if(!Arrays.asList(reportType).contains("卫星干扰定位报告")){
					
					ArrayList<String> al = new ArrayList<String>();
					al.add("卫星干扰定位报告");
					al.add("0");
					all.add(al);
		}
		if(!Arrays.asList(reportType).contains("卫星普查监测报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("卫星普查监测报告");
			al.add("0");
			all.add(al);
		}
		if(!Arrays.asList(reportType).contains("其他报告")){
			
			ArrayList<String> al = new ArrayList<String>();
			al.add("其他报告");
			al.add("0");
			all.add(al);
		}
		
		
		return all;
	}
	
	/**
	 * 查询数据库中所有的监测台站
	 * @param type 要查询的类型
	 * @return 返回所有监测台站，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryStation(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("DBDPJCQuery")){
			sql = "select distinct Station from emcdpjc union select  Station from emcdpjcgraphic union select Station from emcdpjcparameter union select  Station from emcdpjcaudio union select  Station from emcdpjcvideo";
		}else if(type.equals("DBPDSMQuery")){
			sql = "select distinct Station from emcpdsm union select  Station from emcpdsmgraphic union select Station from emcpdsmvideo";
		}else if(type.equals("DBXHCXQuery")){
			sql = "select distinct Station from emcdpcx union select  Station from emcdpcxgraphic union select Station from emcdpcxparameter union select  Station from emcdpcxaudio union select  Station from emcdpcxvideo";
		}else if(type.equals("DBLHDWQuery")){
			sql = "select distinct Station from emclhdw union select  Station from emclhdwvideo";
		}else if(type.equals("DBJCBGQuery")){
			sql = "select distinct Station from emcJCBG";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql = "select distinct Station from emcudpjc union select  Station from emcudpjcgraphic union select Station from emcudpjcparameter union select  Station from emcudpjcaudio union select  Station from emcudpjcvideo";
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select distinct Station from emcupdsm union select  Station from emcupdsmgraphic union select Station from emcupdsmvideo";
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select distinct Station from emcudpcx union select  Station from emcudpcxgraphic union select Station from emcudpcxparameter union select  Station from emcudpcxaudio union select  Station from emcudpcxvideo";
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select distinct Station from emculhdw union select  Station from emculhdwvideo";
		}else if(type.equals("CDBJCBGQuery")){
			sql = "select distinct Station from emcUJCBG";
		}else if(type.equals("WXJCBGQuery")){
			sql = "select distinct Station from emcWJCBG";
		}else if(type.equals("WXZFQJCQuery")){
			sql = "select distinct Station from emcWZFQJC";
		}else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("Station"));
			}

			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return DataQuery.removeDuplicate(list);
	}
	/**
	 * 查询数据库中所有的监测地点
	 * @param type 要查询的类型
	 * @return 返回所有监测地点，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> querymonitorlocation(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("DBDPJCQuery")){
			sql = "select distinct monitorlocation from emcdpjc union select  monitorlocation from emcdpjcgraphic union select monitorlocation from emcdpjcaudio union select  monitorlocation from emcdpjcvideo union select  monitorlocation from emcdpjcparameter";
		}else if(type.equals("DBPDSMQuery")){
			sql = "select distinct monitorlocation from emcpdsm union select  monitorlocation from emcpdsmgraphic union select monitorlocation from emcpdsmvideo";
		}else if(type.equals("DBXHCXQuery")){
			sql = "select distinct monitorlocation from emcdpcx union select  monitorlocation from emcdpcxgraphic union select monitorlocation from emcdpcxaudio union select  monitorlocation from emcdpcxvideo union select  monitorlocation from emcdpcxparameter";
		}else if(type.equals("DBLHDWQuery")){
			sql = "select distinct monitorlocation from emclhdw union select  monitorlocation from emclhdwvideo";
		}else if(type.equals("DBJCBGQuery")){
			sql = "select distinct monitorlocation from emcJCBG ";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql = "select distinct monitorlocation from emcudpjc union select  monitorlocation from emcudpjcgraphic union select  monitorlocation from emcudpjcaudio union select  monitorlocation from emcudpjcvideo union select  monitorlocation from emcudpjcparameter";
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select distinct monitorlocation from emcupdsm union select  monitorlocation from emcupdsmgraphic union select monitorlocation from emcupdsmvideo";
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select distinct monitorlocation from emcudpcx union select  monitorlocation from emcudpcxgraphic union select  monitorlocation from emcudpcxaudio union select  monitorlocation from emcudpcxvideo union select  monitorlocation from emcudpcxparameter";
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select distinct monitorlocation from emculhdw union select  monitorlocation from emculhdwvideo";
		}else if(type.equals("CDBJCBGQuery")){
			sql = "select distinct monitorlocation from emcUJCBG";
		}else if(type.equals("WXJCBGQuery")){
			sql = "select distinct monitorlocation from emcWJCBG";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("monitorlocation"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return DataQuery.removeDuplicate(list);
	}

	
	public static ArrayList<String> removeDuplicate(ArrayList<String> list){
		
		boolean hasnull = false;
		for(int i=list.size()-1;i>=0;i--){
			if((list.get(i) == null)||(list.get(i).trim().equals("null"))){
				hasnull = true;
				list.remove(i);
			}else{
				list.set(i, list.get(i).trim());
			}
		}
		for(int i=0;i<list.size()-1;i++){
			for(int j=list.size()-1;j>i;j--){
				if(list.get(j).equals(list.get(i))){
					list.remove(j);
				}
			}
			
		}
		if(hasnull){
			list.add(null);
		}
		return list; 
	}
	
	/**
	 * 查询数据库中所有的监测台站
	 * @param type 要查询的类型
	 * @return 返回所有监测台站，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryStationforAnalysis(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("DBDPJCQuery")){
			sql = "select distinct Station from emcdpjc";
		}else if(type.equals("DBPDSMQuery")){
			sql = "select distinct Station from emcpdsm";
		}else if(type.equals("DBXHCXQuery")){
			sql = "select distinct Station from emcdpcx";
		}else if(type.equals("DBLHDWQuery")){
			sql = "select distinct Station from emclhdw";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql = "select distinct Station from emcudpjc";
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select distinct Station from emcupdsm";
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select distinct Station from emcudpcx";
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select distinct Station from emculhdw";
		}else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("Station"));
			}

			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	/**
	 * 查询数据库中所有的监测地点
	 * @param type 要查询的类型
	 * @return 返回所有监测地点，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> querymonitorlocationforAnalysis(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("DBDPJCQuery")){
			sql = "select distinct monitorlocation from emcdpjc";
		}else if(type.equals("DBPDSMQuery")){
			sql = "select distinct monitorlocation from emcpdsm";
		}else if(type.equals("DBXHCXQuery")){
			sql = "select distinct monitorlocation from emcdpcx";
		}else if(type.equals("DBLHDWQuery")){
			sql = "select distinct monitorlocation from emclhdw";
		}
		else if(type.equals("CDBDPJCQuery")){
			sql = "select distinct monitorlocation from emcudpjc";
		}else if(type.equals("CDBPDSMQuery")){
			sql = "select distinct monitorlocation from emcupdsm";
		}else if(type.equals("CDBXHCXQuery")){
			sql = "select distinct monitorlocation from emcudpcx";
		}else if(type.equals("CDBLHDWQuery")){
			sql = "select distinct monitorlocation from emculhdw";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("monitorlocation"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 查询监测报告中所有的监测人
	 * @param type 要查询的类型
	 * @return 返回所有监测人，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryauthor(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("WXJCBGQuery")){
			sql = "select distinct writer from emcWJCBG";
		}else if(type.equals("DBJCBGQuery")){
			sql = "select distinct writer from emcJCBG";
		}else if(type.equals("CDBJCBGQuery")){
			sql = "select distinct writer from emcUJCBG";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("writer"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 查询卫星监测转发器监测中所有的监测人
	 * @param type 要查询的类型
	 * @return 返回所有监测人，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryPerson(String type){
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("WXZFQJCQuery")){
			sql = "select distinct person from emcWZFQJC";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("person"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 查询卫星监测转发器监测中所有的频段
	 * @param type 要查询的类型
	 * @return 返回所有频段，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryFrequence(String type){
		
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("WXZFQJCQuery")){
			sql = "select distinct frequence from emcWZFQJC";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("frequence"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 查询卫星监测转发器监测中所有的国家
	 * @param type 要查询的类型
	 * @return 返回所有国家，以ArrayList<String>类型返回
	 */
	public static ArrayList<String> queryCountry(String type){
		
		
		ArrayList<String> list=new ArrayList<String>();
		String sql="";
		if(type.equals("WXZFQJCQuery")){
			sql = "select distinct country from emcWZFQJC";
		}
		else{
			return null;
		}
		
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				list.add(emcdb.dynaRs.getString("country"));
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	public static LinkedList<DPJCHead> DPJCRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		LinkedList<DPJCHead> list=new LinkedList<DPJCHead>();
    	String sql="";
    	if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
    	if(type.equals("DBDPJCQuery")){
    		
    		sql = "select * from  emcdpjc where md5 in (select md5 from emcdpjc group by md5 having count(md5) > 1)" + sql +" order by md5,starttime,frequence";
    	}else if(type.equals("CDBDPJCQuery")){
    		
    		sql = "select * from  emcudpjc where md5 in (select md5 from emcudpjc group by md5 having count(md5) > 1)" + sql +" order by md5,starttime,frequence";
    	}	
    	else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					DPJCHead dpjc = new DPJCHead();
					dpjc.Frequence = Double.parseDouble(emcdb.dynaRs.getString("frequence"));
					dpjc.StartTime = emcdb.dynaRs.getString("starttime");
					dpjc.EndTime = emcdb.dynaRs.getString("endtime");
					dpjc.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
					dpjc.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
					dpjc.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
					dpjc.RadioFreattenuation = emcdb.dynaRs.getString("RadioFreattenuation");
					dpjc.MidFreattenuation = emcdb.dynaRs.getString("midFreattenuation");
					dpjc.DeModen = emcdb.dynaRs.getString("DeModen");
					dpjc.Detector = emcdb.dynaRs.getString("Detector");
					dpjc.Station = emcdb.dynaRs.getString("Station");
					dpjc.Monitor = emcdb.dynaRs.getString("Monitor");
					dpjc.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					dpjc.BH = emcdb.dynaRs.getString("bh");
					dpjc.MD5=emcdb.dynaRs.getString("md5");
					dpjc.businessType = emcdb.dynaRs.getString("businessType");
					dpjc.businessName = emcdb.dynaRs.getString("businessName");
					list.add(dpjc);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
public static LinkedList<PDSMHead> PDSMRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		LinkedList<PDSMHead> list=new LinkedList<PDSMHead>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and StartFrequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and EndFrequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBPDSMQuery")){
			
			sql = "select * from  emcpdsm where md5 in (select md5 from emcpdsm group by md5 having count(md5) > 1) " + sql +" order by md5,starttime,startfrequence";
		}else if(type.equals("CDBPDSMQuery")){
			
			sql = "select * from  emcupdsm where md5 in (select md5 from emcupdsm group by md5 having count(md5) > 1) " + sql +" order by md5,starttime,startfrequence";		
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					PDSMHead pdsm = new PDSMHead();
					pdsm.No = emcdb.dynaRs.getInt("no");
					pdsm.Type = emcdb.dynaRs.getString("type");
					pdsm.StartFrequence = Double.parseDouble(emcdb.dynaRs.getString("StartFrequence"));
					pdsm.EndFrequence = Double.parseDouble(emcdb.dynaRs.getString("EndFrequence"));
					pdsm.Step = emcdb.dynaRs.getString("step");
					pdsm.StartTime = emcdb.dynaRs.getString("starttime");
					pdsm.EndTime = emcdb.dynaRs.getString("endtime");
					pdsm.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
					pdsm.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
					pdsm.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
					pdsm.RadioFreattenuation = emcdb.dynaRs.getString("RadioFreattenuation");
					pdsm.MidFreattenuation = emcdb.dynaRs.getString("midFreattenuation");
					pdsm.Amplifier = emcdb.dynaRs.getString("Amplifier");
					pdsm.DeModen = emcdb.dynaRs.getString("DeModen");
					pdsm.Detector = emcdb.dynaRs.getString("Detector");
					pdsm.Station = emcdb.dynaRs.getString("Station");
					pdsm.Monitor = emcdb.dynaRs.getString("Monitor");
					pdsm.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
					pdsm.BH = emcdb.dynaRs.getString("bh");
					pdsm.MD5=emcdb.dynaRs.getString("md5");
					pdsm.businessType = emcdb.dynaRs.getString("businessType");
					pdsm.businessName = emcdb.dynaRs.getString("businessName");
					list.add(pdsm);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}

public static LinkedList<DPCXHead> DPCXRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
	
	LinkedList<DPCXHead> list=new LinkedList<DPCXHead>();
	String sql="";
	if(!startFrequence.equals("")){
		sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
	}
	if(!endFrequence.equals("")){
		sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
	}
	if(!station.equals("")){
		if(station.contains("null")){
			sql = sql + " and station is null ";
		}else{
		sql = sql + " and station='" + station + "'";
		}
	}
	if(!monitorlocation.equals("")){
		if(monitorlocation.contains("null")){
			sql = sql + " and monitorlocation is null ";
		}else{
		sql = sql + " and monitorlocation='" + monitorlocation +"'";
		}
	}
	sql = sql.replaceFirst("and","where" );//将第一个and替换为where
	if(type.equals("DBXHCXQuery")){
		
		sql = "select * from  emcdpcx where md5 in (select md5 from emcdpcx group by md5 having count(md5) > 1) "  + sql +" order by md5,starttime,frequence";
	}else if(type.equals("CDBXHCXQuery")){
		
		sql = "select * from  emcudpcx where md5 in (select md5 from emcudpcx group by md5 having count(md5) > 1) " + sql +" order by md5,starttime,frequence";			
	}else{
		return null;
	}
	
	SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 boolean flag = true;//标记时间是否满足要求 
	try{
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		while(emcdb.dynaRs.next()){
			flag = true;
			if(!startTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
					flag = false;
				}
			}
			if(!endTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("endtime"))){
					//System.out.println("!isDate()");
					flag = true;
				}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
					flag = false;
				}
			}
			if(flag){
				DPCXHead dpcx = new DPCXHead();
				dpcx.Frequence = Double.parseDouble(emcdb.dynaRs.getString("Frequence"));
				dpcx.StartTime = emcdb.dynaRs.getString("starttime");
				dpcx.EndTime = emcdb.dynaRs.getString("endtime");
				dpcx.Longitude = Double.parseDouble(emcdb.dynaRs.getString("Longitude"));
				dpcx.Latitude = Double.parseDouble(emcdb.dynaRs.getString("Latitude"));
				dpcx.Bandwidth = emcdb.dynaRs.getString("Bandwidth");
				dpcx.Radiowidth = emcdb.dynaRs.getString("Radiowidth");
				dpcx.Decode = emcdb.dynaRs.getString("Decode");
				dpcx.schema = emcdb.dynaRs.getString("schema");
				dpcx.Station = emcdb.dynaRs.getString("Station");
				dpcx.Monitor = emcdb.dynaRs.getString("Monitor");
				dpcx.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
				dpcx.MD5 = emcdb.dynaRs.getString("md5");
				dpcx.BH = emcdb.dynaRs.getString("bh");
				dpcx.businessType = emcdb.dynaRs.getString("businessType");
				dpcx.businessName = emcdb.dynaRs.getString("businessName");
				list.add(dpcx);
			}
		}
		emcdb.closeRs();
		emcdb.closeStm();
	}catch(SQLException e){
		e.printStackTrace();
		return null;
	}catch (ParseException e1) {
		e1.printStackTrace();
	}
	return list;
}
public static LinkedList<LHDWHead> LHDWRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
	
	//System.out.println("LHDWQuery()");
	LinkedList<LHDWHead> list=new LinkedList<LHDWHead>();
	String sql="";
	if(!startFrequence.equals("")){
		sql = sql + " and Frequence1>=" + Integer.parseInt(startFrequence);
	}
	if(!endFrequence.equals("")){
		sql = sql + " and Frequence1<=" + Integer.parseInt(endFrequence);
	}
	if(!station.equals("")){
		if(station.contains("null")){
			sql = sql + " and station is null ";
		}else{
		sql = sql + " and station='" + station + "'";
		}
	}
	if(!monitorlocation.equals("")){
		if(monitorlocation.contains("null")){
			sql = sql + " and monitorlocation is null ";
		}else{
		sql = sql + " and monitorlocation='" + monitorlocation +"'";
		}
	}
	sql = sql.replaceFirst("and","where" );//将第一个and替换为where
	if(type.equals("DBLHDWQuery")){
		
		sql = "select * from  emclhdw where md5 in (select md5 from emclhdw group by md5 having count(md5) > 1) " + sql +" order by md5,starttime, frequence1";	
	}else if(type.equals("CDBLHDWQuery")){
		
		sql = "select * from  emculhdw where md5 in (select md5 from emculhdw group by md5 having count(md5) > 1) " + sql +" order by md5,starttime, frequence1";				
	}else{
		return null;
	}
	
	SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 boolean flag = true;//标记时间是否满足要求 
	try{
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		while(emcdb.dynaRs.next()){
			flag = true;
			if(!startTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
					flag = false;
				}
			}
			if(!endTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("endtime"))){
					//System.out.println("!isDate()");
					flag = true;
				}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
					flag = false;
				}
			}
			if(flag){
				LHDWHead lhdw = new LHDWHead();
				lhdw.StartTime = emcdb.dynaRs.getString("starttime");
				lhdw.EndTime = emcdb.dynaRs.getString("endtime");
				lhdw.Station1 = emcdb.dynaRs.getString("Station1");
				lhdw.Longitude1 = Double.parseDouble(emcdb.dynaRs.getString("Longitude1"));
				lhdw.Latitude1 = Double.parseDouble(emcdb.dynaRs.getString("Latitude1"));
				lhdw.Frequence1 = Double.parseDouble(emcdb.dynaRs.getString("Frequence1"));
				lhdw.Station2 = emcdb.dynaRs.getString("Station2");
				lhdw.Longitude2 = Double.parseDouble(emcdb.dynaRs.getString("Longitude2"));
				lhdw.Latitude2 = Double.parseDouble(emcdb.dynaRs.getString("Latitude2"));
				lhdw.Frequence2 = Double.parseDouble(emcdb.dynaRs.getString("Frequence2"));
				lhdw.Station3 = emcdb.dynaRs.getString("Station3");
				lhdw.Longitude3 = Double.parseDouble(emcdb.dynaRs.getString("Longitude3"));
				lhdw.Latitude3 = Double.parseDouble(emcdb.dynaRs.getString("Latitude3"));
				lhdw.Frequence3 = Double.parseDouble(emcdb.dynaRs.getString("Frequence3"));
				lhdw.Station = emcdb.dynaRs.getString("Station");
				lhdw.Monitor = emcdb.dynaRs.getString("Monitor");
				lhdw.MonitorLocation = emcdb.dynaRs.getString("monitorlocation");
				lhdw.MD5=emcdb.dynaRs.getString("md5");
				lhdw.BH = emcdb.dynaRs.getString("bh");
				lhdw.businessType = emcdb.dynaRs.getString("businessType");
				lhdw.businessName = emcdb.dynaRs.getString("businessName");
				list.add(lhdw);
			}
		}
		emcdb.closeRs();
		emcdb.closeStm();
	}catch(SQLException e){
		e.printStackTrace();
		return null;
	}catch (ParseException e1) {
		e1.printStackTrace();
	}
	return list;
}


	public static ArrayList<ArrayList<String>> GraphicRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			
			sql = "select * from   EMCDPJCGraphic where md5 in (select md5 from EMCDPJCGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,GraphicType, starttime, frequence";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select * from  EMCuDPJCGraphic where md5 in (select md5 from EMCuDPJCGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,GraphicType, starttime, frequence";		
		}else if(type.equals("DBXHCXQuery")){
			
			sql = "select * from   EMCDPCXGraphic where md5 in (select md5 from EMCDPCXGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,GraphicType, starttime, frequence";
		}else if(type.equals("CDBXHCXQuery")){
			
			sql = "select * from   EMCuDPCXGraphic where md5 in (select md5 from EMCuDPCXGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,GraphicType, starttime, frequence";
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("GraphicType"));
					al.add(emcdb.dynaRs.getString("starttime"));
					//al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("md5"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					//System.out.println(al);
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	

public static ArrayList<ArrayList<String>> GraphicRepeat_PDSM(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
	
	ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
	String sql="";
	if(!startFrequence.equals("")){
		sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
	}
	if(!endFrequence.equals("")){
		sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
	}
	if(!station.equals("")){
		if(station.contains("null")){
			sql = sql + " and station is null ";
		}else{
		sql = sql + " and station='" + station + "'";
		}
	}
	if(!monitorlocation.equals("")){
		if(monitorlocation.contains("null")){
			sql = sql + " and monitorlocation is null ";
		}else{
		sql = sql + " and monitorlocation='" + monitorlocation +"'";
		}
	}
	sql = sql.replaceFirst("and","where" );//将第一个and替换为where
	if(type.equals("DBPDSMQuery")){
		
		sql = "select * from  EMCPDSMGraphic where md5 in (select md5 from EMCPDSMGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, startFrequence";
	}else if(type.equals("CDBPDSMQuery")){
		
		sql = "select * from  EMCuPDSMGraphic where md5 in (select md5 from EMCuPDSMGraphic group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, startFrequence";	
	}else{
		return null;
	}
	
	SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 boolean flag = true;//标记时间是否满足要求 
	try{
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		while(emcdb.dynaRs.next()){
			flag = true;
			if(!startTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
					flag = false;
				}
			}
			if(!endTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
					flag = false;
				}
			}
			if(flag){
				ArrayList<String> al = new ArrayList<String>();
				al.add(emcdb.dynaRs.getString("GraphicType"));
				al.add(emcdb.dynaRs.getString("starttime"));
				al.add(emcdb.dynaRs.getString("endtime"));
				al.add(emcdb.dynaRs.getString("startFrequence"));
				al.add(emcdb.dynaRs.getString("EndFrequence"));
				al.add(emcdb.dynaRs.getString("Station"));
				al.add(emcdb.dynaRs.getString("Monitor"));
				al.add(emcdb.dynaRs.getString("monitorlocation"));
				al.add(emcdb.dynaRs.getString("md5"));
				al.add(emcdb.dynaRs.getString("bh"));
				al.add(emcdb.dynaRs.getString("businessType"));
				al.add(emcdb.dynaRs.getString("businessName"));
				al.add(emcdb.dynaRs.getString("signalType"));
				al.add(emcdb.dynaRs.getString("longitude"));
				al.add(emcdb.dynaRs.getString("latitude"));
				list.add(al);
		
			}
		}
		emcdb.closeRs();
		emcdb.closeStm();
	}catch(SQLException e){
		e.printStackTrace();
		return null;
	}catch (ParseException e1) {
		e1.printStackTrace();
	}
	return list;
}


	
public static ArrayList<ArrayList<String>> AudioRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
		
		ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
		String sql="";
		if(!startFrequence.equals("")){
			sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
		}
		if(!endFrequence.equals("")){
			sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
		}
		if(!station.equals("")){
			if(station.contains("null")){
				sql = sql + " and station is null ";
			}else{
			sql = sql + " and station='" + station + "'";
			}
		}
		if(!monitorlocation.equals("")){
			if(monitorlocation.contains("null")){
				sql = sql + " and monitorlocation is null ";
			}else{
			sql = sql + " and monitorlocation='" + monitorlocation +"'";
			}
		}
		sql = sql.replaceFirst("and","where" );//将第一个and替换为where
		if(type.equals("DBDPJCQuery")){
			
			sql = "select * from   EMCDPJCAudio where md5 in (select md5 from EMCDPJCAudio group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";
		}else if(type.equals("CDBDPJCQuery")){
			
			sql = "select * from  EMCuDPJCAudio where md5 in (select md5 from EMCuDPJCAudio group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";
		}else if(type.equals("DBXHCXQuery")){
			
			sql = "select * from   EMCDPCXAudio where md5 in (select md5 from EMCDPCXAudio group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";
		}else if(type.equals("CDBXHCXQuery")){
			
			sql = "select * from   EMCuDPCXAudio where md5 in (select md5 from EMCuDPCXAudio group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";
		}else{
			return null;
		}
		
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 boolean flag = true;//标记时间是否满足要求 
		try{
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			emcdb.dynaRs = emcdb.QuerySQL(sql);
			while(emcdb.dynaRs.next()){
				flag = true;
				if(!startTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("starttime"))){
						//System.out.println("!isDate()");
						flag = true;
					} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
						flag = false;
					}
				}
				if(!endTime.equals("")){
					if(!isDate(emcdb.dynaRs.getString("endtime"))){
						//System.out.println("!isDate()");
						flag = true;
					}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
						flag = false;
					}
				}
				if(flag){
					ArrayList<String> al = new ArrayList<String>();
					al.add(emcdb.dynaRs.getString("starttime"));
					al.add(emcdb.dynaRs.getString("endtime"));
					al.add(emcdb.dynaRs.getString("frequence"));
					al.add(emcdb.dynaRs.getString("Station"));
					al.add(emcdb.dynaRs.getString("Monitor"));
					al.add(emcdb.dynaRs.getString("monitorlocation"));
					al.add(emcdb.dynaRs.getString("md5"));
					al.add(emcdb.dynaRs.getString("bh"));
					al.add(emcdb.dynaRs.getString("businessType"));
					al.add(emcdb.dynaRs.getString("businessName"));
					al.add(emcdb.dynaRs.getString("signalType"));
					al.add(emcdb.dynaRs.getString("longitude"));
					al.add(emcdb.dynaRs.getString("latitude"));
					list.add(al);
				}
			}
			emcdb.closeRs();
			emcdb.closeStm();
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}

public static ArrayList<ArrayList<String>> VideoRepeat(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
	
	ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
	String sql="";
	if(!startFrequence.equals("")){
		sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
	}
	if(!endFrequence.equals("")){
		sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
	}
	if(!station.equals("")){
		if(station.contains("null")){
			sql = sql + " and station is null ";
		}else{
		sql = sql + " and station='" + station + "'";
		}
	}
	if(!monitorlocation.equals("")){
		if(monitorlocation.contains("null")){
			sql = sql + " and monitorlocation is null ";
		}else{
		sql = sql + " and monitorlocation='" + monitorlocation +"'";
		}
	}
	sql = sql.replaceFirst("and","where" );//将第一个and替换为where
	if(type.equals("DBDPJCQuery")){
		
		sql = "select * from EMCDPJCVideo where md5 in (select md5 from EMCDPJCVideo group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";;
	}else if(type.equals("CDBDPJCQuery")){
		
		sql = "select * from   EMCUDPJCVideo where md5 in (select md5 from EMCUDPJCVideo group by md5 having count(md5) > 1)" + sql +" order by  md5,starttime, frequence";;	
	}else if(type.equals("DBPDSMQuery")){
		
		sql = "select * from   EMCPDSMVideo where md5 in (select md5 from EMCPDSMVideo group by md5 having count(md5) > 1)" + sql +" order by  md5,starttime, frequence";;
	}else if(type.equals("CDBPDSMQuery")){
		
		sql = "select * from   EMCUPDSMVideo where md5 in (select md5 from EMCUPDSMVideo group by md5 having count(md5) > 1)" + sql +" order by  md5,starttime, frequence";;	
	}else if(type.equals("DBXHCXQuery")){
		
		sql = "select * from   EMCDPCXVideo where md5 in (select md5 from EMCDPCXVideo group by md5 having count(md5) > 1)" + sql +" order by  md5,starttime, frequence";;
	}else if(type.equals("CDBXHCXQuery")){
		
		sql = "select * from  EMCUDPCXVideo where md5 in (select md5 from EMCUDPCXVideo group by md5 having count(md5) > 1)" + sql +" order by  md5,starttime, frequence";;	
	}else{
		return null;
	}
	
	SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 boolean flag = true;//标记时间是否满足要求 
	try{
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		while(emcdb.dynaRs.next()){
			flag = true;
			if(!startTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
					flag = false;
				}
			}
			if(!endTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("endtime"))){
					//System.out.println("!isDate()");
					flag = true;
				}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
					flag = false;
				}
			}
			if(flag){
				ArrayList<String> al = new ArrayList<String>();
				al.add(emcdb.dynaRs.getString("starttime"));
				al.add(emcdb.dynaRs.getString("endtime"));
				al.add(emcdb.dynaRs.getString("frequence"));
				al.add(emcdb.dynaRs.getString("STARTfrequence"));
				al.add(emcdb.dynaRs.getString("ENDfrequence"));
				al.add(emcdb.dynaRs.getString("Station"));
				al.add(emcdb.dynaRs.getString("Monitor"));
				al.add(emcdb.dynaRs.getString("monitorlocation"));
				al.add(emcdb.dynaRs.getString("MD5"));
				al.add(emcdb.dynaRs.getString("bh"));
				al.add(emcdb.dynaRs.getString("businessType"));
				al.add(emcdb.dynaRs.getString("businessName"));
				al.add(emcdb.dynaRs.getString("signalType"));
				al.add(emcdb.dynaRs.getString("longitude"));
				al.add(emcdb.dynaRs.getString("latitude"));
				list.add(al);
			}
		}
		emcdb.closeRs();
		emcdb.closeStm();
	}catch(SQLException e){
		e.printStackTrace();
		return null;
	}catch (ParseException e1) {
		e1.printStackTrace();
	}
	return list;
}
public static ArrayList<ArrayList<String>> VideoRepeat_LHDW(String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitorlocation,String type){
	
	ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
	String sql="";
	if(!startFrequence.equals("")){
		sql = sql + " and Frequence>=" + Integer.parseInt(startFrequence);
	}
	if(!endFrequence.equals("")){
		sql = sql + " and Frequence<=" + Integer.parseInt(endFrequence);
	}
	if(!station.equals("")){
		if(station.contains("null")){
			sql = sql + " and station is null ";
		}else{
		sql = sql + " and station='" + station + "'";
		}
	}
	if(!monitorlocation.equals("")){
		if(monitorlocation.contains("null")){
			sql = sql + " and monitorlocation is null ";
		}else{
		sql = sql + " and monitorlocation='" + monitorlocation +"'";
		}
	}
	sql = sql.replaceFirst("and","where" );//将第一个and替换为where
	if(type.equals("DBLHDWQuery")){
		
		sql = "select * from   EMCLHDWVideo where md5 in (select md5 from EMCLHDWVideo group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";	
	}else if(type.equals("CDBLHDWQuery")){
		
		sql = "select * from   EMCULHDWVideo where md5 in (select md5 from EMCULHDWVideo group by md5 having count(md5) > 1)" + sql +" order by md5,starttime, frequence";	
	}else{
		return null;
	}
	
	SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 boolean flag = true;//标记时间是否满足要求 
	try{
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		while(emcdb.dynaRs.next()){
			flag = true;
			if(!startTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("starttime"))){
					//System.out.println("!isDate()");
					flag = true;
				} else if(fmt.parse(emcdb.dynaRs.getString("starttime")).getTime() < fmt.parse(startTime).getTime()){/*监测数据时间小于要查的起始时间，不显示该数据 */
					flag = false;
				}
			}
			if(!endTime.equals("")){
				if(!isDate(emcdb.dynaRs.getString("endtime"))){
					//System.out.println("!isDate()");
					flag = true;
				}else if(fmt.parse(emcdb.dynaRs.getString("endtime")).getTime() > fmt.parse(endTime).getTime()){/*监测数据的时间大于要查的终止时间*/
					flag = false;
				}
			}
			if(flag){
				ArrayList<String> al = new ArrayList<String>();
				al.add(emcdb.dynaRs.getString("starttime"));
				al.add(emcdb.dynaRs.getString("endtime"));
				al.add(emcdb.dynaRs.getString("frequence"));
				al.add(emcdb.dynaRs.getString("Station"));
				al.add(emcdb.dynaRs.getString("Monitor"));
				al.add(emcdb.dynaRs.getString("monitorlocation"));
				al.add(emcdb.dynaRs.getString("md5"));
				al.add(emcdb.dynaRs.getString("bh"));
				al.add(emcdb.dynaRs.getString("businessType"));
				al.add(emcdb.dynaRs.getString("businessName"));
				al.add(emcdb.dynaRs.getString("signalType"));
				al.add(emcdb.dynaRs.getString("longitude"));
				al.add(emcdb.dynaRs.getString("latitude"));
			
				list.add(al);
			}
		}
		emcdb.closeRs();
		emcdb.closeStm();
	}catch(SQLException e){
		e.printStackTrace();
		return null;
	}catch (ParseException e1) {
		e1.printStackTrace();
	}
	return list;
}

public static boolean isDate(String str_input){
	
	if(!isNull(str_input)){
		SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fmt.setLenient(false);
		try {
			fmt.format(fmt.parse(str_input));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}else{
		return false;
	}
}
public static boolean isNull(String str){
	if(str==null)
		return true;
	else
		return false;
}


}
	

