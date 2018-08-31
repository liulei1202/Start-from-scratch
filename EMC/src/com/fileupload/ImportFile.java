package com.fileupload;

import BaseData.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;  
import java.nio.MappedByteBuffer;  
import java.nio.channels.FileChannel;  
import java.security.MessageDigest;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.*;


import javax.swing.JOptionPane;

import com.csvreader.CsvReader;

import CSVReader.*;
import DataQuery.*;
//import com.sun.org.apache.commons.collections.*;

import oracle.sql.BLOB;
import DBConnection.*;
import ExcelUtil.ExcelUtil;

@SuppressWarnings("unused")
public class ImportFile {


	public CSVInput  csvInput;
	//private String fileTypeList[];

	/**
	 * 默认的构造函数。
	 */
	public ImportFile(){
		/*
		fileTypeList = new String[12];
		fileTypeList[0]="DBDPJCSignalParameterReg";
		fileTypeList[1] = "DBDPJCOriginalMonitorData";
		fileTypeList[2] = "DBDPJCGraphicData";
		fileTypeList[3] = "DBDPJCAudioData";
		fileTypeList[4] = "DBDPJCVideoData";
		fileTypeList[5] = "DBPDSMOriginalMonitorData";
		fileTypeList[6] = "DBPDSMGraphicData";
		fileTypeList[7] = "DBPDSMVideoData";
		fileTypeList[8] = "DBXHCXSignalParameterReg";
		fileTypeList[9] = "";
		fileTypeList[10] = "";
		fileTypeList[11] = "";
		*/
		//System.out.println("import file.java:import file start.");
		
	}
	
	/**
	 * 导入和修改图形数据
	 * @param path
	 * @param fileType 要导入或修改的图形类型
	 * @param businessType
	 * @param businessName
	 * @param signalType
	 * @param longitude
	 * @param latitude
	 * @param graphicType
	 * @param startTime
	 * @param endTime
	 * @param frequence
	 * @param station
	 * @param monitor
	 * @param monitorLocation
	 * @param bh 图形bh，导入时为0
	 * @param grapType 原图形类型，导入时为0
	 * @return
	 */
	public String importGraphicData_DPJCXHCX(String path,String fileType,String businessType,String businessName,String signalType,String longitude,String latitude,String graphicType,String startTime,String endTime,String frequence,String station,String monitor,String monitorLocation,String bh,String grapType) {
		
		String table = null;
		String table2 = null;
		String temp = null;
		if((fileType.equals("DBDPJCGraphicData"))||fileType.equals("DBDPJCQuery")){
			
			table = "EMCDPJCGraphic";
			table2 = "EMCDPJCParameter";
			temp = "DPJC-";
		}else if((fileType.equals("DBXHCXGraphicData"))||fileType.equals("DBXHCXQuery")){
			table = "EMCDPCXGraphic";
			table2 = "EMCDPCXParameter";
			temp = "XHCX-";
		}else if((fileType.equals("CDBDPJCGraphicData"))||fileType.equals("CDBDPJCQuery")){
			
			table = "EMCUDPJCGraphic";
			table2 = "EMCUDPJCParameter";
			temp = "CDPJC-";
		}else if((fileType.equals("CDBXHCXGraphicData"))||fileType.equals("CDBXHCXQuery")){
			
			table = "EMCUDPCXGraphic";
			table2 = "EMCUDPCXParameter";
			temp = "CXHCX-";
		}else{
			return "页面类型错误";
		}
		
		if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
		    
		    String sql3 = "select count(*) as num from "+table2+" where bh='"+bh+"'";
		    emcdb.dynaRs = emcdb.QuerySQL(sql3);
		    try {
				if(emcdb.dynaRs.next()){
					if(emcdb.dynaRs.getInt("num")!=0){
						if(!grapType.trim().equals(graphicType.trim()))
						return "这是信号参数登记中的数据，禁止修改图形类型。";
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			//删除原始类型图片
			String sql = "delete from "+table+" where bh='"+bh+"' and graphictype='"+grapType+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			//删除修改过的类型的图片
			//如果要把同bh下的图修改成其他类型，需要先把其他类型的图删除
			String sql2 = "delete from "+table+" where bh='"+bh+"' and graphictype='"+graphicType+"'";
			emcdb.UpdateSQL(sql2);
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    //System.out.println("starttime"+startTime);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + graphicType+ "-" + startTime.substring(0, startTime.indexOf(' ')) + "-" + df.format(Double.parseDouble(frequence)/1000000) + "-" + md5 + "-" + currentTime;
		}
		
		String sql = null;
		String sql2 = null;
		
		if((fileType.equals("DBDPJCGraphicData"))||(fileType.trim().equals("DBDPJCQuery"))){
			
			
			sql="insert into EMCDPJCGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPJCGRAPHIC WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("DBXHCXGraphicData"))||(fileType.trim().equals("DBXHCXQuery"))){
			
			
			sql="insert into EMCDPCXGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPCXGRAPHIC WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBDPJCGraphicData"))||(fileType.trim().equals("CDBDPJCQuery"))){
			
			
			sql="insert into EMCUDPJCGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPJCGRAPHIC WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBXHCXGraphicData"))||(fileType.trim().equals("CDBXHCXQuery"))){
			
			
			sql="insert into EMCUDPCXGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPCXGRAPHIC WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}
	
	/**
	 * 导入频段扫描的图形数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importGraphicData_PDSM(String path,String fileType,String businessType,String businessName,String signalType,String longitude,String latitude,String graphicType,String startTime,String endTime,String startFrequence,String endFrequence,String station,String monitor,String monitorLocation,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("DBPDSMGraphicData"))||fileType.equals("DBPDSMQuery")){
			
			table = "EMCPDSMGraphic";
			temp = "PDSM-";
		}else if((fileType.equals("CDBPDSMGraphicData"))||fileType.equals("CDBPDSMQuery")){
			
			table = "EMCUPDSMGraphic";
			temp = "CPDSM-";
		}else{
			return "页面类型错误";
		}
		
		if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			//删除原始类型图片
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			
			emcdb.closeStm();
	    }
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.0");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + graphicType+ "-" + startTime.substring(0, startTime.indexOf(' ')) + "-" + Double.parseDouble(startFrequence)/1000000 + "-" + md5 + "-" + currentTime;
		}
		String sql = null;
		String sql2 = null;
		
		if((fileType.equals("DBPDSMGraphicData"))||(fileType.trim().equals("DBPDSMQuery"))){
			
			
			sql="insert into EMCPDSMGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,startfrequence,endfrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCPDSMGRAPHIC WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBPDSMGraphicData"))||(fileType.trim().equals("DBPDSMQuery"))){
			
			
			sql="insert into EMCUPDSMGRAPHIC(bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime,endTime,startfrequence,endfrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+graphicType+"','"+startTime+"','"+endTime+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUPDSMGRAPHIC WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		 
		/*解压文件放到固定位置*/
		else
			return "上传失败";
	}
	
	/**
	 * 导入音频数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importAudioData(String path,String fileType,String businessType,String businessName,String signalType,String longitude,String latitude,String startTime,String endTime,String frequence,String station,String monitor,String monitorLocation,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("DBDPJCAudioData"))||fileType.equals("DBDPJCQuery")){
			
			table = "EMCDPJCAudio";
			temp = "DPJC-";
		}else if((fileType.equals("DBXHCXAudioData"))||fileType.equals("DBXHCXQuery")){
			table = "EMCDPCXAudio";
			temp = "XHCX-";
		}else if((fileType.equals("CDBDPJCAudioData"))||fileType.equals("CDBDPJCQuery")){
			
			table = "EMCUDPJCAudio";
			temp = "CDPJC-";
		}else if((fileType.equals("CDBXHCXAudioData"))||fileType.equals("CDBXHCXQuery")){
			
			table = "EMCUDPCXAudio";
			temp = "CXHCX-";
		}else{
			return "页面类型错误";
		}
		
	    if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		//System.out.println(bh);
		if(bh.trim().equals("0")){
			bh = temp + startTime.substring(0, startTime.indexOf(' ')) + "-" + df.format(Double.parseDouble(frequence)/1000000) + "-" + md5 + "-" + currentTime;
		}
		//System.out.println(bh);
		String sql = null;
		String sql2 = null;
		if((fileType.equals("DBDPJCAudioData"))||fileType.equals("DBDPJCQuery")){
			
			
			sql="insert into EMCDPJCAudio(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"',"+Double.parseDouble(frequence)+",'"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPJCAudio WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("DBXHCXAudioData"))||fileType.equals("DBXHCXQuery")){
			
			
			sql="insert into EMCDPCXAudio(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"',"+Double.parseDouble(frequence)+",'"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPCXAudio WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBDPJCAudioData"))||fileType.equals("CDBDPJCQuery")){
			
			
			sql="insert into EMCUDPJCAudio(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"',"+Double.parseDouble(frequence)+",'"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPJCAudio WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBXHCXAudioData"))||fileType.equals("CDBXHCXQuery")){
			
			
			sql="insert into EMCUDPCXAudio(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"',"+Double.parseDouble(frequence)+",'"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPCXAudio WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}
	
	/**
	 * 导入视频数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importVideoData(String path, String fileType,String businessType,String businessName,String signalType,String longitude,String latitude,String startTime,String endTime,String frequence,String startFrequence,String endFrequence,String station,String monitor,String monitorLocation,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("DBDPJCVideoData"))||fileType.equals("DBDPJCQuery")){
			
			table = "EMCDPJCVideo";
			temp = "DPJC-";
		}else if((fileType.equals("DBPDSMVideoData"))||fileType.equals("DBPDSMQuery")){
			table = "EMCPDSMVideo";
			temp = "PDSM-";
		}else if((fileType.equals("DBXHCXVideoData"))||fileType.equals("DBXHCXQuery")){
			table = "EMCDPCXVideo";
			temp = "XHCX-";
		}else if((fileType.equals("CDBDPJCVideoData"))||fileType.equals("CDBDPJCQuery")){
			
			table = "EMCUDPJCVideo";
			temp = "CDPJC-";
		}else if((fileType.equals("CDBPDSMVideoData"))||fileType.equals("CDBPDSMQuery")){
			table = "EMCPDSMVideo";
			temp = "PDSM-";
		}else if((fileType.equals("CDBXHCXVideoData"))||fileType.equals("CDBXHCXQuery")){
			
			table = "EMCUDPCXVideo";
			temp = "CXHCX-";
		}else{
			return "页面类型错误";
		}
		
	    if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + startTime.substring(0, startTime.indexOf(' ')) + "-" + df.format(Double.parseDouble(frequence)/1000000) + "-" + md5 + "-" + currentTime;
		}
		//System.out.println(bh);
		String sql = null;
		String sql2 = null;
		if((fileType.equals("DBDPJCVideoData"))||fileType.equals("DBDPJCQuery")){
			
			
			sql="insert into EMCDPJCVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPJCVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("DBPDSMVideoData"))||fileType.equals("DBPDSMQuery")){
			
			
			sql="insert into EMCPDSMVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCPDSMVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("DBXHCXVideoData"))||fileType.equals("DBXHCXQuery")){
			
			
			sql="insert into EMCDPCXVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCDPCXVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBDPJCVideoData"))||fileType.equals("CDBDPJCQuery")){
			
			
			sql="insert into EMCUDPJCVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPJCVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBPDSMVideoData"))||fileType.equals("CDBPDSMQuery")){
			
			
			sql="insert into EMCUPDSMVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUPDSMVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBXHCXVideoData"))||fileType.equals("CDBXHCXQuery")){
			
			
			sql="insert into EMCUDPCXVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,startFrequence,endFrequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+startFrequence+"','"+endFrequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCUDPCXVideo WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}
	
	
	/**
	 * 导入视频数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importVideoData_LHDW(String path, String fileType,String businessType,String businessName,String signalType,String longitude,String latitude,String startTime,String endTime,String frequence,String station,String monitor,String monitorLocation,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("DBLHDWVideoData"))||fileType.equals("DBLHDWQuery")){
			
			table = "EMCLHDWVideo";
			temp = "LHDW-";
		}else if((fileType.equals("CDBLHDWVideoData"))||fileType.equals("CDBLHDWQuery")){
			table = "EMCULHDWVideo";
			temp = "CLHDW-";
		}else{
			return "页面类型错误";
		}
		
	    if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + startTime.substring(0, startTime.indexOf(' ')) + "-" + df.format(Double.parseDouble(frequence)/1000000) + "-" + md5 + "-" + currentTime;
		}
		//System.out.println(bh);
		String sql = null;
		String sql2 = null;
		
		if((fileType.equals("DBLHDWVideoData"))||fileType.equals("DBLHDWQuery")){
			
			
			sql="insert into EMCLHDWVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCLHDWVideo WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBLHDWVideoData"))||fileType.equals("CDBLHDWQuery")){
			
			
			sql="insert into EMCULHDWVideo(bh,businessType,businessName,signalType,longitude,latitude,startTime,endTime,frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+signalType+"','"+longitude+"','"+latitude+"','"+startTime+"','"+endTime+"','"+frequence+"','"+station+"','"+monitor+"','"+monitorLocation+"','md5value',empty_blob())";
			sql2="select data FROM EMCULHDWVideo WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}
	
	/**
	 * 信号参数登记的excel表导入
	 * @param path 文件路径+文件名
	 * @param type 导入类型
	 * @return 返回是否导入成功
	 */
	public String importSignalParameterReg(String path,String type){
		
		boolean n =true;//是否全部导入成功
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		File file = new File(path);  
		LinkedList<LinkedList<String>> result = ExcelUtil.readExcel(file);
		System.out.println("行数:"+result.size()+"/"+"每行数据："+result.get(0).size());
        for(int i = 0 ;i < result.size() ;i++){ 
            for(int j = 0;j<result.get(i).size(); j++){  
               System.out.print(result.get(i).get(j) + ",");
            }
            System.out.println("");
        
            
        	String frequence = result.get(i).get(0);	
        	String Station = result.get(i).get(1);
    		String bandWidth = result.get(i).get(2);
    		String modulate = result.get(i).get(3);
    		String businessType = result.get(i).get(4);
    		String businessName = result.get(i).get(5);
    		
    		String signalType = result.get(i).get(6);
    		String launchTime = result.get(i).get(7);
         	String levels = result.get(i).get(8);
    		String testNumber = result.get(i).get(9);
    		String noise = result.get(i).get(10);
    		String occupancy = result.get(i).get(11);
    		
    		String threshold = result.get(i).get(12);
    		String direction = result.get(i).get(13);
    		String longitude = result.get(i).get(14);
    		String latitude = result.get(i).get(15);		
    		String city = result.get(i).get(16);
    		String compared = result.get(i).get(17);
    		
    		String monitorTime = result.get(i).get(18);
    		if(!DataQuery.isDate(monitorTime)){
    			i++;
    			//System.out.println("第"+i+"条数据时间格式不正确。");
    			return "第"+i+"条数据时间格式不正确。请修改。";
    		}
    		String monitor = result.get(i).get(19);
    		String monitorLocation = result.get(i).get(20);
    		String comments = result.get(i).get(21);
    		
    		Double Frequence = Double.parseDouble(frequence)*1000000;//输入的频率是MHz
    		Double BandWidth = Double.parseDouble(bandWidth)*1000;//输入的频率是kHz
    		Date dt = new Date(); 
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    String currentTime = sdf.format(dt);
    		DecimalFormat df = new DecimalFormat("#.000");
    		//经纬度样式应该为116°25'56.0E或者直接为数字
    		//Double Longitude = ImportFile.GetLongiLatiDouble(longitude.trim()); 
    		//Double Latitude = ImportFile.GetLongiLatiDouble(latitude.trim());
    		String sql;
    		String bh = monitorTime.contains(" ") ? monitorTime.substring(0,monitorTime.indexOf(" ")).trim() : monitorTime;
    		bh = bh + "-" + Frequence/1000000 + "-" + longitude + "-" + latitude + "-" +Station+ "-" +noise+ "-" + currentTime;
    		
    		if(type.equals("DBDPJCSignalParameterReg")){	//短波-单频检测-信号参数登记
    			
    			bh = "DPJC" + "-" + bh;
    			sql = "insert into EMCDPJCPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station + "','" + noise + "','" + compared + "','" + comments + "')";
    		}else if(type.equals("DBXHCXSignalParameterReg")){	//短波-信号测向-信号参数登记
    			
    			bh = "XHCX" + "-" + bh;
    			sql = "insert into EMCDPCXPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station + "','" + noise + "','" + compared + "','" + comments + "')";
    		}else if(type.equals("CDBDPJCSignalParameterReg")){	//超短波-单频检测-信号参数登记
    			
    			bh = "CDPJC" + "-" + bh;
    			sql = "insert into EMCUDPJCPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station + "','" + noise + "','" + compared + "','" + comments + "')";
    		}else if(type.equals("CDBXHCXSignalParameterReg")){	//超短波-信号测向-信号参数登记
    			
    			bh = "CXHCX" + "-" + bh;
    			sql = "insert into EMCUDPCXPARAMETER (bh,Original,FREQGRAP,DIREGRAP,LOCAGRAP,businessName,signalType,monitor,monitorLocation,city, frequence, bandWidth, modulate, Longitude, Latitude, monitorTime, businessType, launchTime, levels, testNumber, occupancy, threshold, direction, Station,  noise, compared, comments) values('" + bh +"','0','0','0','0','"+businessName+"','"+signalType+"','"+monitor+"','"+monitorLocation+"','"+city + "'," + Frequence + ",'" + BandWidth + "','"+ modulate +"','" + longitude + "','" + latitude + "','" + monitorTime + "','" + businessType + "','" + launchTime + "','" + levels + "','" + testNumber + "','" + occupancy + "','" + threshold + "','" + direction + "','" + Station + "','" + noise + "','" + compared + "','" + comments + "')";
    		}else{
    			
    			return "未知的数据类型，上传失败。";
    		}
    	    //System.out.println(sql);
    		if(emcdb.UpdateSQL(sql) < 1){
    			i++;
				return "第"+i+"条数据上传失败，停止上传。";
    		}
        }
        emcdb.closeStm();
		return "导入成功";
		
	}
	
	/**
	 * 转发器监测的手动数据导入
	 * @param path 文件路径+文件名
	 * @param type 导入类型
	 * @return 返回是否导入成功
	 */
	public String importZFQJCManual(String path,String bh,String businessType,String businessName,String zfq,String antenna,String weaken,String comments,String type,Double position,String satName,String country,
			Double rbw,Double vbw,String levels,String frequence,String polar,
			Double startFrequence,Double endFrequence,String station,String time,String person){
		//System.out.println("rbw:"+rbw+"/vbw:"+vbw);
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.00");
	    String currentTime = sdf.format(dt);
	    //System.out.println("starttime"+startTime);
		String sql = null;
		String sql2 = null;
		if(!bh.equals("0")){
			EMCDB emcdb = new EMCDB();
			emcdb.dynaStm = emcdb.newStatement();
			String sql3 = "delete from emcwzfqjc where bh='"+bh+"'";
			emcdb.UpdateSQL(sql3);
		}
		path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩后文件的md5
		bh = time.contains(" ") ? time.substring(0,time.lastIndexOf(" ")).trim() : time;
		bh = bh + "-" + df.format(position)  + "-" + startFrequence + "-" + polar+ "-" + md5 + "-" + currentTime;
			
		bh = "ZFQJC" + "-" + bh;
		sql = "insert into EMCWZFQJC (bh,businessType,businessName,zfq,antenna,weaken,comments, position, satName, country, rbw,vbw,levels,frequence,polar,startFrequence,endFrequence,station,time,person,MD5,data) "+
		" values('" + bh +"','"+businessType+"','"+businessName+"','"+zfq+"','"+antenna+"','"+weaken+"','"+comments + "'," + position + ",'" + satName + "','"+ country +"'," + rbw + "," + vbw + ",'" + levels + "','" + frequence + "','" +
		polar + "'," + startFrequence + "," + endFrequence + ",'" + station + "','" + time + "','" + person + "','"+md5+"',empty_blob())";
	
		sql2="select data FROM EMCWZFQJC WHERE  bh = '"+bh+"'";
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}	
	
	/**
	 * 转发器监测的excel表导入
	 * @param filePath Excel文件路径+文件名
	 * @param type 导入类型
	 * @return 返回是否导入成功
	 */
	public String importZFQJCExcel(String filePath,String type){
		
		int flag1 =0;//是否全部导入成功
		
		File file = new File(filePath);  
		LinkedList<LinkedList<String>> result = ExcelUtil.readExcel(file);
		//System.out.println(result.size());
		System.out.println("行数:"+result.size()+"/"+"每行数据："+result.get(0).size());
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
        for(int i = 0 ;i < result.size() ;i++){
        for(int j = 0;j<result.get(i).size(); j++){
            System.out.print(result.get(i).get(j) + ",");
        }
        System.out.println("");
        
        String businessType = result.get(i).get(0);
        String businessName = result.get(i).get(1);
        String time = result.get(i).get(2);
        if(!DataQuery.isDate(time)){
        	i++;
        	return "第"+i+"条数据时间格式不正确。请修改。";
        }
    	Double position = ImportFile.GetLongiLatiDouble(result.get(i).get(3).trim());	
		String satName = result.get(i).get(4);
		String country = result.get(i).get(5);
		
		String zfq = result.get(i).get(6);
		Double startFrequence = Double.parseDouble(result.get(i).get(7).trim())*1000000;
		Double endFrequence = Double.parseDouble(result.get(i).get(8).trim())*1000000;
		String frequence = result.get(i).get(9);
		if(!frequence.equals("")){
			if(frequence.contains("(")){
				frequence = frequence.substring(0,frequence.indexOf("("));
			}else if(frequence.contains("（")){
				frequence = frequence.substring(0,frequence.indexOf("（"));
			}
		}
		String polar = result.get(i).get(10);
		String antenna = result.get(i).get(11);
		if(!antenna.equals("")){
			String regEX = "[\\u4e00-\\u9fa5]";
		    Pattern p = Pattern.compile(regEX);
		    Matcher m = p.matcher(antenna);
			if(m.find()){//说明存在汉字
				antenna="X";
			}else if(antenna.contains("#")){
				antenna = antenna.substring(0, antenna.indexOf("#"));
			}
		}
		antenna = antenna.contains(".0")?antenna.replace(".0", "") : antenna;
		
		Double rbw = Double.parseDouble(result.get(i).get(12).trim())*1000;
		Double vbw = Double.parseDouble(result.get(i).get(13).trim())*1000;		
		String weaken = result.get(i).get(14);
		String levels = result.get(i).get(15);	
		String station = result.get(i).get(16);
		String person = result.get(i).get(17);
		String comments = result.get(i).get(18);
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime = sdf.format(dt);
		
		String sql = null;
		String sql2 = null;
		
		
		String md5 = null;
		String bh = time.contains(" ") ? time.substring(0,time.lastIndexOf(" ")).trim() : time;
		bh = bh + "-" + position  + "-" + startFrequence + "-" + polar+ "-" + md5 + "-" + currentTime;
		
		if(type.equals("WXZFQJCDataExcel")){
			
			bh = "ZFQJC" + "-" + bh;
			sql = "insert into EMCWZFQJC (bh,businessType,businessName,zfq,antenna,weaken,comments, position, satName, country, rbw,vbw,levels,frequence,polar,startFrequence,endFrequence,station,time,person,MD5,data) "+
			" values('" + bh +"','"+businessType+"','"+businessName+"','"+zfq+"','"+antenna+"','"+weaken+"','"+comments + "'," + position + ",'" + satName + "','"+ country +"'," + rbw + "," + vbw + ",'" + levels + "','" + frequence + "','" +
			polar + "'," + startFrequence + "," + endFrequence + ",'" + station + "','" + time + "','" + person + "','"+md5+"',empty_blob())";
			//System.out.println(sql);
			
			if(emcdb.UpdateSQL(sql)<1){
				i++;
				return "第"+i+"条数据上传失败，停止上传。";
			}
		}else{
			return "未知的数据类型，上传失败。";
		}
        }
        emcdb.closeStm();
		//System.out.println(sql);
		//flag1=EMCDB.InsertFile(sql,sql2,,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		return "上传成功";
	}
	
	
	//单频监测原始数据导入
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @param bh bh为空说明从原始监测数据导入页面导入，非空说明从信号参数登记导入
	 * @return
	 */
	public boolean importDBDPJCOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		//System.out.println(fileName);
		csvInput = new CSVInput();
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		DPJCHead dpjcHead;
		dpjcHead = new DPJCHead();
		dpjcHead.BH = "DPJC";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime = sdf.format(dt);	    		
	    
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短

		
		String temp[];
		//temp = new String[20]; //假设DPJC文件中每行最多20个数据项，每个数据项是以逗号，为分隔符
		
		LinkedList<DPJCOriginal> dpjcOriginal;
		dpjcOriginal = new LinkedList<DPJCOriginal>();//假设DPJC文件中最多有20行文件头信息
		
		String sql;
		
		//1. 将文件头读入数组dpjcFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			DPJCOriginal dpjc = new DPJCOriginal();
			dpjc.Time = temp[0].trim();
			dpjc.Frequence = Double.parseDouble(temp[1].trim());
			dpjc.Levels = Double.parseDouble(temp[2].trim());
			dpjc.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			dpjc.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			dpjcOriginal.add(dpjc);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组dpjcFileHead[]中人数据项		
		columns = dpjcOriginal.size();
		for(i = 0; i < columns; i++){
			if (i == 0) {
				dpjcHead.StartTime = dpjcOriginal.get(i).Time;
				dpjcHead.Frequence = dpjcOriginal.get(i).Frequence;
				dpjcHead.BH = dpjcHead.BH + "-" + dpjcHead.StartTime.substring(0, dpjcHead.StartTime.indexOf(' ')) + "-" + dpjcHead.Frequence/1000000;
			}
			if(dpjcOriginal.get(i).Name.contains("经度") ){
				dpjcHead.Longitude = GetLongiLatiDouble(dpjcOriginal.get(i).Value.trim());
				dpjcHead.BH = dpjcHead.BH + "-" + df.format(dpjcHead.Longitude);//df.format(d)
			}
			if(dpjcOriginal.get(i).Name.contains("纬度")){
				dpjcHead.Latitude = GetLongiLatiDouble(dpjcOriginal.get(i).Value.trim());
				dpjcHead.BH = dpjcHead.BH + "-" + df.format(dpjcHead.Latitude);
			}
			if(dpjcOriginal.get(i).Name.contains("中频带宽")){
				int t=1;
				if (dpjcOriginal.get(i).Value.contains("k")||dpjcOriginal.get(i).Value.contains("K")){
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("k", " ");
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("K", " ");
					t = 1000;
				}
				if (dpjcOriginal.get(i).Value.contains("M")||dpjcOriginal.get(i).Value.contains("m")){
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("m", " ");
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("M", " ");
					t = 1000000;
				}
				if(dpjcOriginal.get(i).Value.contains("Hz")){
					dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.substring(0, dpjcOriginal.get(i).Value.indexOf("Hz"));
				}//System.out.println(dpjcOriginal.get(i).Value);
				dpjcHead.Bandwidth = String.valueOf(Double.parseDouble(dpjcOriginal.get(i).Value.trim()) * t);
				//dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				//dpjcHead.Bandwidth = dpjcOriginal.get(i).Value;
				
			}
			if(dpjcOriginal.get(i).Name.contains("射频衰减")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.RadioFreattenuation = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("中频衰减")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.MidFreattenuation = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("解调")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.DeModen = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("检波")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.Detector = dpjcOriginal.get(i).Value;
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		dpjcHead.Station = station;
		dpjcHead.Monitor = monitor;
		dpjcHead.MonitorLocation = monitorLocation;
		dpjcHead.Comments = null;
		dpjcHead.MD5 = null;  
		
		dpjcHead.BH = dpjcHead.BH + "-" + currentTime;
		if(!bh.equals("0")){
			dpjcHead.BH = bh;
		}
		columns = dpjcOriginal.size();
		
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			
			sql = "insert into EMCdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "','"+dpjcOriginal.get(i).Time+"', " + dpjcOriginal.get(i).Frequence + "," + dpjcOriginal.get(i).Levels + ")";
			//sql = "insert into EMCdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "', to_timestamp('"+dpjcOriginal.get(i).Time+"','yyyy-mm-dd hh24:mi:ss.ff') , " + dpjcOriginal.get(i).Frequence + "," + dpjcOriginal.get(i).Levels + ")";
			//System.out.println(sql);
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		
		i=0;
		sql = "insert into EMCdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "',?,?,?)";
		//sql = sql + temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				dpjcHead.EndTime = temp[0].trim();
				//System.out.println(temp[0].trim());
				//System.out.println(Timestamp.valueOf(temp[0].trim()));
				
				db.pstm.setString(1, temp[0].trim());     
				db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
				db.pstm.setDouble(3, Double.parseDouble(temp[2].trim()));
				db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    	db.pstm.executeBatch();      
				    db.dynaConn.commit();      
				    db.pstm.clearBatch();   
				    //System.out.println(i/10000);   
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();     
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
/*		
 		//测试conn为静态变量得情况。
 		EMCDB db = new EMCDB();
		//db.connDB(sql);
		try {
			EMCDB.conn.conn.setAutoCommit(false);
			db.pstm = EMCDB.conn.conn.prepareStatement(sql);
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				dpjcHead.EndTime = temp[0].trim();
				
				db.pstm.setString(1, temp[0]);      
				db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
				db.pstm.setDouble(3, Double.parseDouble(temp[2].trim()));
				db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%2000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    	db.pstm.executeBatch();      
			    EMCDB.conn.conn.commit();      
			    db.pstm.clearBatch();      
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			EMCDB.conn.conn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		   //db.closeDB();
		   // EMCDB.conn.conn.setAutoCommit(true);
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
*/  
		csvInput.close();
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCDPJC(bh,businessType,businessName,filename,frequence,startTime,endTime,Longitude,Latitude,Bandwidth,RadioFreattenuation,midFreattenuation,DeModen,Detector,station,monitor,monitorLocation,MD5,data)values" +
				"('"+dpjcHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ dpjcHead.Frequence+",'"+dpjcHead.StartTime+"','"+dpjcHead.EndTime+"',"+dpjcHead.Longitude+","+dpjcHead.Latitude +",'"
				+ dpjcHead.Bandwidth+"','"+dpjcHead.RadioFreattenuation+"','"+dpjcHead.MidFreattenuation+"','"+dpjcHead.DeModen+"','"+dpjcHead.Detector+"','"
				+dpjcHead.Station+"','"+dpjcHead.Monitor+"','"+dpjcHead.MonitorLocation+"','md5value',empty_blob())";
		String sql2="select data FROM EMCDPJC WHERE  bh = '"+dpjcHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return
	 */
	public boolean importDBPDSMOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		Long beginTime = System.currentTimeMillis();
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		PDSMHead pdsmHead;
		pdsmHead = new PDSMHead();
		pdsmHead.BH = "PDSM";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		//temp = new String[20]; //假设pdsm文件中每行最多20个数据项，每个数据项是以逗号，为分隔符
		
		LinkedList<PDSMOriginal> pdsmOriginal;
		pdsmOriginal = new LinkedList<PDSMOriginal>();//假设pdsm文件中最多有20行文件头信息
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			PDSMOriginal pdsm = new PDSMOriginal();
			pdsm.Time = temp[0];
			pdsm.Frequence = Double.parseDouble(temp[1].trim());
			pdsm.Levels = Double.parseDouble(temp[2].trim());
			pdsm.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			pdsm.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			pdsmOriginal.add(pdsm);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组pdsmFileHead[]中人数据项
		pdsmHead.StartTime = pdsmOriginal.get(0).Time;
		columns = pdsmOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(pdsmOriginal.get(i).Name.contains("经度") ){
				pdsmHead.Longitude = GetLongiLatiDouble(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("纬度")){
				pdsmHead.Latitude = GetLongiLatiDouble(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("监测数目") ){
				pdsmHead.No = Integer.parseInt(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("监测类型") ){
				pdsmHead.Type = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("最低频率") ){
				pdsmHead.StartFrequence = Double.parseDouble(pdsmOriginal.get(i).Value.substring(0,pdsmOriginal.get(i).Value.length()-3).trim());//去掉KHZ三个字
				int t = 1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("m")||pdsmOriginal.get(i).Value.contains("M")){
					t = 1000000;
				}
				if (pdsmOriginal.get(i).Value.contains("g")||pdsmOriginal.get(i).Value.contains("G")){
					t = 1000000000;
				}
				pdsmHead.StartFrequence = pdsmHead.StartFrequence*t;
			}
			if(pdsmOriginal.get(i).Name.contains("最高频率") ){
				pdsmHead.EndFrequence = Double.parseDouble(pdsmOriginal.get(i).Value.substring(0,pdsmOriginal.get(i).Value.length()-3).trim());//去掉KHZ三个字
				int t = 1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("m")||pdsmOriginal.get(i).Value.contains("M")){
					t = 1000000;
				}
				if (pdsmOriginal.get(i).Value.contains("g")||pdsmOriginal.get(i).Value.contains("G")){
					t = 1000000000;
				}
				pdsmHead.EndFrequence = pdsmHead.EndFrequence*t;
			}
			if(pdsmOriginal.get(i).Name.contains("步长") ){
				pdsmHead.Step = pdsmOriginal.get(i).Value.trim();
			}/**
			*有的文件存在这四项数据，有的不存在
			*
			if(pdsmOriginal.get(i).Name.contains("起始日期") ){
				pdsmHead.StartTime = pdsmOriginal.get(i).Value.trim() + " ";
			}
			if(pdsmOriginal.get(i).Name.contains("起始时间") ){
				pdsmHead.StartTime = pdsmHead.StartTime + pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("结束日期") ){
				pdsmHead.EndTime = pdsmOriginal.get(i).Value.trim() + " ";
			}
			if(pdsmOriginal.get(i).Name.contains("结束时间") ){
				pdsmHead.EndTime = pdsmHead.EndTime + pdsmOriginal.get(i).Value.trim();
			}*/
			if(pdsmOriginal.get(i).Name.contains("中频带宽")){
				int t=1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("k", " ");
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("K", " ");
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("M")||pdsmOriginal.get(i).Value.contains("m")){
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("m", " ");
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("M", " ");
					t = 1000000;
				}
				if(pdsmOriginal.get(i).Value.contains("Hz")){
					pdsmOriginal.get(i).Value = pdsmOriginal.get(i).Value.substring(0, pdsmOriginal.get(i).Value.indexOf("Hz"));
				}//System.out.println(pdsmOriginal.get(i).Value);
				pdsmHead.Bandwidth = String.valueOf(Double.parseDouble(pdsmOriginal.get(i).Value.trim()) * t);
				//pdsmHead.Bandwidth = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("射频衰减")){
				pdsmHead.RadioFreattenuation = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("中频衰减")){
				pdsmHead.MidFreattenuation = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("前置放大器") ){
				pdsmHead.Amplifier = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("解调")){
				pdsmHead.DeModen = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("检波")){
				pdsmHead.Detector = pdsmOriginal.get(i).Value.trim();
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		pdsmHead.Station = station;
		pdsmHead.Monitor = monitor;
		pdsmHead.MonitorLocation = monitorLocation;
		pdsmHead.Comments = null;
		pdsmHead.MD5 = null;  
		//substring(0, dpjcHead.StartTime.indexOf(' '))
		pdsmHead.BH = pdsmHead.BH + "-" + pdsmHead.StartTime.substring(0, pdsmHead.StartTime.indexOf(' ')) + "-" 
				+ df.format(pdsmHead.StartFrequence/1000000) + "-" + df.format(pdsmHead.Longitude) + "-" 
				+ df.format(pdsmHead.Latitude) + "-" + currentTime;
		if(!bh.equals("0")){
			pdsmHead.BH = bh;
		}
		columns = pdsmOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCpdsmdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "','" + pdsmOriginal.get(i).Time + "'," + pdsmOriginal.get(i).Frequence + "," + pdsmOriginal.get(i).Levels + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			pdsmHead.EndTime = temp[0].trim();
			sql = "insert into EMCpdsmdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "','"
					+ temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMCPDSMdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "',?,?,?)";
		//sql = sql + temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				pdsmHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setDouble(3, Double.parseDouble(temp[2].trim()));
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();    
			    //System.out.println(i/10000);  
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}		
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCpdsm(bh,businessType,businessName,fileName,Longitude,Latitude,no,type,startfrequence,endfrequence,step,startTime,endTime,Bandwidth,RadioFreattenuation,midFreattenuation,Amplifier,DeModen,Detector,station,monitor,monitorLocation,MD5,data)values" +
				"('"+pdsmHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ pdsmHead.Longitude+","+ pdsmHead.Latitude+","+ pdsmHead.No+",'"+pdsmHead.Type+"',"+pdsmHead.StartFrequence+","+pdsmHead.EndFrequence+",'"+pdsmHead.Step+"','"+pdsmHead.StartTime+"','"+pdsmHead.EndTime+"','"
				+ pdsmHead.Bandwidth+"','"+pdsmHead.RadioFreattenuation+"','"+pdsmHead.MidFreattenuation+"','"+pdsmHead.Amplifier+"','"+pdsmHead.DeModen+"','"+pdsmHead.Detector+"','"
				+pdsmHead.Station+"','"+pdsmHead.Monitor+"','"+pdsmHead.MonitorLocation+"','md5value',empty_blob())";
		String sql2="select data FROM EMCpdsm WHERE  bh = '"+pdsmHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return 
	 */
	public boolean importDBXHCXOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		DPCXHead dpcxHead;
		dpcxHead = new DPCXHead();
		dpcxHead.BH = "XHCX";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		
		LinkedList<DPCXOriginal> dpcxOriginal;
		dpcxOriginal = new LinkedList<DPCXOriginal>();
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			DPCXOriginal dpcx = new DPCXOriginal();
			dpcx.Time = temp[0];
			dpcx.Frequence = Double.parseDouble(temp[1].trim());
			dpcx.Bearing = temp[2].trim();
			dpcx.Quality = temp[3].trim();
			dpcx.Levels = Double.parseDouble(temp[4].trim());
			dpcx.longitude = Double.parseDouble(temp[5].trim());
			dpcx.Latitude = Double.parseDouble(temp[6].trim());
			
			dpcx.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			dpcx.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			dpcxOriginal.add(dpcx);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组dpcxFileHead[]中人数据项		
		dpcxHead.StartTime = dpcxOriginal.get(0).Time;
		dpcxHead.Frequence = dpcxOriginal.get(0).Frequence;
		columns = dpcxOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(dpcxOriginal.get(i).Name.contains("经度") ){
				dpcxHead.Longitude = GetLongiLatiDouble(dpcxOriginal.get(i).Value.trim());
			}
			if(dpcxOriginal.get(i).Name.contains("纬度")){
				dpcxHead.Latitude = GetLongiLatiDouble(dpcxOriginal.get(i).Value.trim());
			}
			if(dpcxOriginal.get(i).Name.contains("测向带宽") ){
				dpcxHead.Bandwidth = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("音频带宽") ){
				dpcxHead.Radiowidth = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("解调")){
				dpcxHead.Decode = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("模式") ){
				dpcxHead.schema = dpcxOriginal.get(i).Value.trim();
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		dpcxHead.Station = station;
		dpcxHead.Monitor = monitor;
		dpcxHead.MonitorLocation = monitorLocation;
		dpcxHead.Comments = null;
		dpcxHead.MD5 = null;  
		
		dpcxHead.BH = dpcxHead.BH + "-" + dpcxHead.StartTime.substring(0, dpcxHead.StartTime.indexOf(' ')) + "-" 
				+ df.format(dpcxHead.Frequence/1000000) + "-" + df.format(dpcxHead.Longitude) + "-" 
				+ df.format(dpcxHead.Latitude) + "-" + currentTime;
		if(!bh.equals("0")){
			dpcxHead.BH = bh;
		}
		columns = dpcxOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCdpcxdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" 
				+ dpcxHead.BH + "','" + dpcxOriginal.get(i).Time + "'," + df.format(dpcxOriginal.get(i).Frequence) + ",'" + dpcxOriginal.get(i).Bearing + "','" 
				+ dpcxOriginal.get(i).Quality + "'," + dpcxOriginal.get(i).Levels + "," + dpcxOriginal.get(i).longitude + "," + dpcxOriginal.get(i).Latitude + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			dpcxHead.EndTime = temp[0].trim();
			sql = "insert into EMCdpcxdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" 
			+ dpcxHead.BH + "','" + temp[0] + "'," + df.format(Double.parseDouble(temp[1])) + ",'" + temp[2].trim() + "','" 
			+ temp[3].trim() + "'," + Double.parseDouble(temp[4].trim()) + "," + temp[5].trim() + "," + temp[6].trim() + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMCDPCXdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" + dpcxHead.BH + "',?,?,?,?,?,?,?)";
		
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				dpcxHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setString(3, temp[2].trim());
			    db.pstm.setString(4, temp[3].trim());
			    db.pstm.setDouble(5, Double.parseDouble(temp[4].trim()));
			    db.pstm.setDouble(6, GetLongiLatiDouble(temp[5].trim()));
			    db.pstm.setDouble(7, GetLongiLatiDouble(temp[6].trim()));
			    
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();     
			    //System.out.println(i/10000); 
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}				
		
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCdpcx(bh,businessType,businessName,fileName,Longitude,Latitude,bandwidth,radiowidth,decode,schema,starttime,endtime,frequence,station,monitor,monitorLocation,MD5,data)values" +
				"('"+dpcxHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ dpcxHead.Longitude +","+ dpcxHead.Latitude +",'"+ dpcxHead.Bandwidth +"','"+ dpcxHead.Radiowidth +"','"+ dpcxHead.Decode +"','"+ dpcxHead.schema +"','"+
				dpcxHead.StartTime +"','"+ dpcxHead.EndTime +"',"+ df.format(dpcxHead.Frequence) +",'"+ dpcxHead.Station +"','"+ dpcxHead.Monitor +"','"+ dpcxHead.MonitorLocation +"','md5value',empty_blob())";
		String sql2="select data FROM EMCdpcx WHERE  bh = '"+dpcxHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return 上传成功返回true,否则返回false
	 */
	public boolean importDBLHDWOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		LHDWHead lhdwHead;
		lhdwHead = new LHDWHead();
		lhdwHead.BH = "LHDW";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		
		LinkedList<LHDWOriginal> lhdwOriginal;
		lhdwOriginal = new LinkedList<LHDWOriginal>();
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			LHDWOriginal lhdw = new LHDWOriginal();
			lhdw.Time = temp[0];
			lhdw.Frequence = Double.parseDouble(temp[1].trim());
			lhdw.Station = temp[2].trim();
			
			lhdw.Bearing = temp[4].trim();
			lhdw.Quality = temp[5].trim();
			lhdw.Levels = Double.parseDouble(temp[6].trim());
			lhdw.longitude = Double.parseDouble((temp[7].trim()));
			lhdw.Latitude = Double.parseDouble((temp[8].trim()));
			
			lhdw.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			lhdw.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			lhdwOriginal.add(lhdw);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组lhdwFileHead[]中人数据项		
		lhdwHead.StartTime = lhdwOriginal.get(0).Time;
		lhdwHead.Frequence1 = lhdwOriginal.get(0).Frequence;//频率一样
		lhdwHead.Frequence2 = lhdwOriginal.get(0).Frequence;
		lhdwHead.Frequence3 = lhdwOriginal.get(0).Frequence;
		
		int occur=0;//记录文件信息中重复的内容是第几次出现
		columns = lhdwOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(lhdwOriginal.get(i).Name.contains("监测单元") ){
				occur++;
				if(occur==1)
					lhdwHead.Station1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Station2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Station3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("测向带宽") ){
				if(occur==1)
					lhdwHead.Bandwidth1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Bandwidth2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Bandwidth3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("音频带宽") ){
				if(occur==1)
					lhdwHead.Radiowidth1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Radiowidth2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Radiowidth3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("解调")){
				if(occur==1)
					lhdwHead.Decode1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Decode2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Decode3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("模式") ){
				if(occur==1)
					lhdwHead.schema1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.schema2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.schema3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("经度") ){
				if(occur==1)
					lhdwHead.Longitude1 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==2)
					lhdwHead.Longitude2 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==3)
					lhdwHead.Longitude3 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
			}
			if(lhdwOriginal.get(i).Name.contains("纬度")){
				if(occur==1)
					lhdwHead.Latitude1 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==2)
					lhdwHead.Latitude2 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==3)
					lhdwHead.Latitude3 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
			}
			
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		lhdwHead.Station = station;
		lhdwHead.Monitor = monitor;
		lhdwHead.MonitorLocation = monitorLocation;
		lhdwHead.Comments = null;
		lhdwHead.MD5 = null;  
		
		lhdwHead.BH = lhdwHead.BH + "-" + lhdwHead.StartTime.substring(0, lhdwHead.StartTime.indexOf(' ')) + "-" 
				+ df.format(lhdwHead.Frequence1/1000000) + "-" + df.format(lhdwHead.Longitude1) + "-" 
				+ df.format(lhdwHead.Latitude1) + "-" + currentTime;
		if(!bh.equals("0")){
			lhdwHead.BH = bh;
		}
		columns = lhdwOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMClhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" 
				+ lhdwHead.BH + "','" + lhdwOriginal.get(i).Time + "'," + df.format(lhdwOriginal.get(i).Frequence) + ",'" + lhdwOriginal.get(i).Station + "','" + lhdwOriginal.get(i).Bearing + "','" 
				+ lhdwOriginal.get(i).Quality + "'," + lhdwOriginal.get(i).Levels + "," + lhdwOriginal.get(i).longitude + "," + lhdwOriginal.get(i).Latitude + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			lhdwHead.EndTime = temp[0].trim();
			sql = "insert into EMClhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" 
			+ lhdwHead.BH + "','" + temp[0] + "'," + df.format(Double.parseDouble(temp[1])) + ",'" + temp[2].trim() + "','" + temp[4].trim() + "','" 
			+ temp[5].trim() + "'," + temp[6].trim() + "," + temp[7].trim() + "," + temp[8].trim() + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMClhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" + lhdwHead.BH + "',?,?,?,?,?,?,?,?)";
		
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				lhdwHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setString(3, temp[2].trim());
			    db.pstm.setString(4, temp[4].trim());
			    db.pstm.setString(5, temp[5].trim());
			    db.pstm.setDouble(6, Double.parseDouble(temp[6].trim()));
			    db.pstm.setDouble(7, GetLongiLatiDouble(temp[7].trim()));
			    db.pstm.setDouble(8, GetLongiLatiDouble(temp[8].trim()));
			    
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();  
			    //System.out.println(i/10000);    
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}						
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMClhdw(bh,businessType,businessName,fileName,station1,bandwidth1,radiowidth1,decode1,schema1,frequence1,Longitude1,Latitude1,station2,bandwidth2,radiowidth2,decode2,schema2,frequence2,Longitude2,Latitude2,station3,bandwidth3,radiowidth3,decode3,schema3,frequence3,Longitude3,Latitude3,starttime,endtime,station,monitor,monitorLocation,MD5,data)values" +
				"('"+lhdwHead.BH+"','"+businessType+"','"+businessName+"','"+name+"','" + lhdwHead.Station1 +"','" + lhdwHead.Bandwidth1 +"','"+ lhdwHead.Radiowidth1 +"','"+ lhdwHead.Decode1 +"','"+ lhdwHead.schema1 +"',"+ df.format(lhdwHead.Frequence1) +"," + lhdwHead.Longitude1 +","+ lhdwHead.Latitude1 +","
				+"'" + lhdwHead.Station2 +"','"+ lhdwHead.Bandwidth2 +"','"+ lhdwHead.Radiowidth2 +"','"+ lhdwHead.Decode2 +"','"+ lhdwHead.schema2 +"',"+ df.format(lhdwHead.Frequence2) +"," + lhdwHead.Longitude2 +","+ lhdwHead.Latitude2 +","
				+"'" + lhdwHead.Station3 +"','"+ lhdwHead.Bandwidth3 +"','"+ lhdwHead.Radiowidth3 +"','"+ lhdwHead.Decode3 +"','"+ lhdwHead.schema3 +"',"+ df.format(lhdwHead.Frequence3) +"," + lhdwHead.Longitude3 +","+ lhdwHead.Latitude3 +","
				+"'"+lhdwHead.StartTime +"','"+ lhdwHead.EndTime +"','"+ lhdwHead.Station +"','"+ lhdwHead.Monitor +"','"+ lhdwHead.MonitorLocation +"','md5value',empty_blob())";
		String sql2="select data FROM EMClhdw WHERE  bh = '"+lhdwHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	
	//单频监测原始数据导入
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return
	 */
	public boolean importCDBDPJCOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		DPJCHead dpjcHead;
		dpjcHead = new DPJCHead();
		dpjcHead.BH = "DPJC";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime = sdf.format(dt);	    		
	    
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短

		
		String temp[];
		//temp = new String[20]; //假设DPJC文件中每行最多20个数据项，每个数据项是以逗号，为分隔符
		
		LinkedList<DPJCOriginal> dpjcOriginal;
		dpjcOriginal = new LinkedList<DPJCOriginal>();//假设DPJC文件中最多有20行文件头信息
		
		String sql;
		
		//1. 将文件头读入数组dpjcFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			DPJCOriginal dpjc = new DPJCOriginal();
			dpjc.Time = temp[0];
			dpjc.Frequence = Double.parseDouble(temp[1].trim());
			dpjc.Levels = Double.parseDouble(temp[2].trim());
			dpjc.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			dpjc.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			dpjcOriginal.add(dpjc);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组dpjcFileHead[]中人数据项		
		columns = dpjcOriginal.size();
		for(i = 0; i < columns; i++){
			if (i == 0) {
				dpjcHead.StartTime = dpjcOriginal.get(i).Time;
				dpjcHead.Frequence = dpjcOriginal.get(i).Frequence;
				dpjcHead.BH = dpjcHead.BH + "-" + dpjcHead.StartTime.substring(0, dpjcHead.StartTime.indexOf(' ')) + "-" + dpjcHead.Frequence/1000000;
			}
			if(dpjcOriginal.get(i).Name.contains("经度") ){
				dpjcHead.Longitude = GetLongiLatiDouble(dpjcOriginal.get(i).Value.trim());
				dpjcHead.BH = dpjcHead.BH + "-" + df.format(dpjcHead.Longitude);//df.format(d)
			}
			if(dpjcOriginal.get(i).Name.contains("纬度")){
				dpjcHead.Latitude = GetLongiLatiDouble(dpjcOriginal.get(i).Value.trim());
				dpjcHead.BH = dpjcHead.BH + "-" + df.format(dpjcHead.Latitude);
			}
			if(dpjcOriginal.get(i).Name.contains("中频带宽")){
				int t = 1;
				if (dpjcOriginal.get(i).Value.contains("k")||dpjcOriginal.get(i).Value.contains("K")){
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("k", " ");
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("K", " ");
					t = 1000;
				}
				if (dpjcOriginal.get(i).Value.contains("M")||dpjcOriginal.get(i).Value.contains("m")){
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("m", " ");
					dpjcOriginal.get(i).Value=dpjcOriginal.get(i).Value.replace("M", " ");
					t = 1000000;
				}
				if(dpjcOriginal.get(i).Value.contains("Hz")){
					dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.substring(0, dpjcOriginal.get(i).Value.indexOf("Hz"));
				}//System.out.println(dpjcOriginal.get(i).Value);
				dpjcHead.Bandwidth = String.valueOf(Double.parseDouble(dpjcOriginal.get(i).Value.trim()) * t);
				//dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				//dpjcHead.Bandwidth = dpjcOriginal.get(i).Value;
				
			}
			if(dpjcOriginal.get(i).Name.contains("射频衰减")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.RadioFreattenuation = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("中频衰减")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.MidFreattenuation = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("解调")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.DeModen = dpjcOriginal.get(i).Value;
			}
			if(dpjcOriginal.get(i).Name.contains("检波")){
				dpjcOriginal.get(i).Value = dpjcOriginal.get(i).Value.trim();
				dpjcHead.Detector = dpjcOriginal.get(i).Value;
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		dpjcHead.Station = station;
		dpjcHead.Monitor = monitor;
		dpjcHead.MonitorLocation = monitorLocation;
		dpjcHead.Comments = null;
		dpjcHead.MD5 = null;  
		
		dpjcHead.BH = dpjcHead.BH + "-" + currentTime;
		if(!bh.equals("0")){
			dpjcHead.BH = bh;
		}
		columns = dpjcOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCUdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "','" + dpjcOriginal.get(i).Time + "'," + dpjcOriginal.get(i).Frequence + "," + dpjcOriginal.get(i).Levels + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			dpjcHead.EndTime = temp[0].trim();
			
			sql = "insert into EMCUdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "','";
			sql = sql + temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMCUdpjcdata(bh,time,frequence,levels) values('" + dpjcHead.BH + "',?,?,?)";
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				dpjcHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setDouble(3, Double.parseDouble(temp[2].trim()));
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();      
			    //System.out.println(i/10000);
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}		
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCUDPJC(bh,businessType,businessName,filename,frequence,startTime,endTime,Longitude,Latitude,Bandwidth,RadioFreattenuation,midFreattenuation,DeModen,Detector,station,monitor,monitorLocation,MD5,data)values" +
				"('"+dpjcHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ dpjcHead.Frequence+",'"+dpjcHead.StartTime+"','"+dpjcHead.EndTime+"',"+dpjcHead.Longitude+","+dpjcHead.Latitude +",'"
				+ dpjcHead.Bandwidth+"','"+dpjcHead.RadioFreattenuation+"','"+dpjcHead.MidFreattenuation+"','"+dpjcHead.DeModen+"','"+dpjcHead.Detector+"','"
				+dpjcHead.Station+"','"+dpjcHead.Monitor+"','"+dpjcHead.MonitorLocation+"','md5value',empty_blob())";
		String sql2="select data FROM EMCUDPJC WHERE  bh = '"+dpjcHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return
	 */
	public boolean importCDBPDSMOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){

		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		PDSMHead pdsmHead;
		pdsmHead = new PDSMHead();
		pdsmHead.BH = "PDSM";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		//temp = new String[20]; //假设pdsm文件中每行最多20个数据项，每个数据项是以逗号，为分隔符
		
		LinkedList<PDSMOriginal> pdsmOriginal;
		pdsmOriginal = new LinkedList<PDSMOriginal>();//假设pdsm文件中最多有20行文件头信息
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			PDSMOriginal pdsm = new PDSMOriginal();
			pdsm.Time = temp[0];
			pdsm.Frequence = Double.parseDouble(temp[1].trim());
			pdsm.Levels = Double.parseDouble(temp[2].trim());
			pdsm.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			pdsm.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			pdsmOriginal.add(pdsm);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组pdsmFileHead[]中人数据项
		pdsmHead.StartTime = pdsmOriginal.get(0).Time;
		columns = pdsmOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(pdsmOriginal.get(i).Name.contains("经度") ){
				pdsmHead.Longitude = GetLongiLatiDouble(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("纬度")){
				pdsmHead.Latitude = GetLongiLatiDouble(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("监测数目") ){
				pdsmHead.No = Integer.parseInt(pdsmOriginal.get(i).Value.trim());
			}
			if(pdsmOriginal.get(i).Name.contains("监测类型") ){
				pdsmHead.Type = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("最低频率") ){
				pdsmHead.StartFrequence = Double.parseDouble(pdsmOriginal.get(i).Value.substring(0,pdsmOriginal.get(i).Value.length()-3).trim());//去掉KHZ三个字
				int t = 1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("m")||pdsmOriginal.get(i).Value.contains("M")){
					t = 1000000;
				}
				if (pdsmOriginal.get(i).Value.contains("g")||pdsmOriginal.get(i).Value.contains("G")){
					t = 1000000000;
				}
				pdsmHead.StartFrequence = pdsmHead.StartFrequence*t;
			}
			if(pdsmOriginal.get(i).Name.contains("最高频率") ){
				pdsmHead.EndFrequence = Double.parseDouble(pdsmOriginal.get(i).Value.substring(0,pdsmOriginal.get(i).Value.length()-3).trim());//去掉KHZ三个字
				int t = 1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("m")||pdsmOriginal.get(i).Value.contains("M")){
					t = 1000000;
				}
				if (pdsmOriginal.get(i).Value.contains("g")||pdsmOriginal.get(i).Value.contains("G")){
					t = 1000000000;
				}
				pdsmHead.EndFrequence = pdsmHead.EndFrequence*t;
			}
			if(pdsmOriginal.get(i).Name.contains("步长") ){
				pdsmHead.Step = pdsmOriginal.get(i).Value.trim();
			}/**
			*有的文件存在这四项数据，有的不存在
			*
			if(pdsmOriginal.get(i).Name.contains("起始日期") ){
				pdsmHead.StartTime = pdsmOriginal.get(i).Value.trim() + " ";
			}
			if(pdsmOriginal.get(i).Name.contains("起始时间") ){
				pdsmHead.StartTime = pdsmHead.StartTime + pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("结束日期") ){
				pdsmHead.EndTime = pdsmOriginal.get(i).Value.trim() + " ";
			}
			if(pdsmOriginal.get(i).Name.contains("结束时间") ){
				pdsmHead.EndTime = pdsmHead.EndTime + pdsmOriginal.get(i).Value.trim();
			}*/
			if(pdsmOriginal.get(i).Name.contains("中频带宽")){
				int t=1;
				if (pdsmOriginal.get(i).Value.contains("k")||pdsmOriginal.get(i).Value.contains("K")){
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("k", " ");
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("K", " ");
					t = 1000;
				}
				if (pdsmOriginal.get(i).Value.contains("M")||pdsmOriginal.get(i).Value.contains("m")){
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("m", " ");
					pdsmOriginal.get(i).Value=pdsmOriginal.get(i).Value.replace("M", " ");
					t = 1000000;
				}
				if(pdsmOriginal.get(i).Value.contains("Hz")){
					pdsmOriginal.get(i).Value = pdsmOriginal.get(i).Value.substring(0, pdsmOriginal.get(i).Value.indexOf("Hz"));
				}//System.out.println(pdsmOriginal.get(i).Value);
				pdsmHead.Bandwidth = String.valueOf(Double.parseDouble(pdsmOriginal.get(i).Value.trim()) * t);
			}
			if(pdsmOriginal.get(i).Name.contains("射频衰减")){
				pdsmHead.RadioFreattenuation = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("中频衰减")){
				pdsmHead.MidFreattenuation = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("前置放大器") ){
				pdsmHead.Amplifier = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("解调")){
				pdsmHead.DeModen = pdsmOriginal.get(i).Value.trim();
			}
			if(pdsmOriginal.get(i).Name.contains("检波")){
				pdsmHead.Detector = pdsmOriginal.get(i).Value.trim();
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		pdsmHead.Station = station;
		pdsmHead.Monitor = monitor;
		pdsmHead.MonitorLocation = monitorLocation;
		pdsmHead.Comments = null;
		pdsmHead.MD5 = null;  
		//substring(0, dpjcHead.StartTime.indexOf(' '))
		pdsmHead.BH = pdsmHead.BH + "-" + pdsmHead.StartTime.substring(0, pdsmHead.StartTime.indexOf(' ')) + "-" 
				+ pdsmHead.StartFrequence/1000000 + "-" + df.format(pdsmHead.Longitude) + "-" 
				+ df.format(pdsmHead.Latitude) + "-" + currentTime;
		if(!bh.equals("0")){
			pdsmHead.BH = bh;
		}
		columns = pdsmOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCupdsmdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "','" + pdsmOriginal.get(i).Time + "'," + pdsmOriginal.get(i).Frequence + "," + pdsmOriginal.get(i).Levels + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			pdsmHead.EndTime = temp[0].trim();
			sql = "insert into EMCupdsmdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "','"
					+ temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		
		i=0;
		sql = "insert into EMCUPDSMdata(bh,time,frequence,levels) values('" + pdsmHead.BH + "',?,?,?)";
		//sql = sql + temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				pdsmHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setDouble(3, Double.parseDouble(temp[2].trim()));
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();
			    //System.out.println(i/10000);
			    }
			    
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}				
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCupdsm(bh,businessType,businessName,filename,Longitude,Latitude,no,type,startfrequence,endfrequence,step,startTime,endTime,Bandwidth,RadioFreattenuation,midFreattenuation,Amplifier,DeModen,Detector,station,monitor,monitorLocation,MD5,data)values" +
				"('"+pdsmHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ pdsmHead.Longitude+","+ pdsmHead.Latitude+","+ pdsmHead.No+",'"+pdsmHead.Type+"',"+pdsmHead.StartFrequence+","+pdsmHead.EndFrequence+",'"+pdsmHead.Step+"','"+pdsmHead.StartTime+"','"+pdsmHead.EndTime+"','"
				+ pdsmHead.Bandwidth+"','"+pdsmHead.RadioFreattenuation+"','"+pdsmHead.MidFreattenuation+"','"+pdsmHead.Amplifier+"','"+pdsmHead.DeModen+"','"+pdsmHead.Detector+"','"
				+pdsmHead.Station+"','"+pdsmHead.Monitor+"','"+pdsmHead.MonitorLocation+"','md5value',empty_blob())";
		String sql2="select data FROM EMCupdsm WHERE  bh = '"+pdsmHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return 
	 */
	public boolean importCDBXHCXOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		DPCXHead dpcxHead;
		dpcxHead = new DPCXHead();
		dpcxHead.BH = "XHCX";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		
		LinkedList<DPCXOriginal> dpcxOriginal;
		dpcxOriginal = new LinkedList<DPCXOriginal>();
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			DPCXOriginal dpcx = new DPCXOriginal();
			dpcx.Time = temp[0];
			dpcx.Frequence = Double.parseDouble(temp[1].trim());
			dpcx.Bearing = temp[2].trim();
			dpcx.Quality = temp[3].trim();
			dpcx.Levels = Double.parseDouble(temp[4].trim());
			dpcx.longitude = Double.parseDouble(temp[5].trim());
			dpcx.Latitude = Double.parseDouble(temp[6].trim());
			
			dpcx.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			dpcx.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			dpcxOriginal.add(dpcx);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组dpcxFileHead[]中人数据项		
		dpcxHead.StartTime = dpcxOriginal.get(0).Time;
		dpcxHead.Frequence = dpcxOriginal.get(0).Frequence;
		columns = dpcxOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(dpcxOriginal.get(i).Name.contains("经度") ){
				dpcxHead.Longitude = GetLongiLatiDouble(dpcxOriginal.get(i).Value.trim());
			}
			if(dpcxOriginal.get(i).Name.contains("纬度")){
				dpcxHead.Latitude = GetLongiLatiDouble(dpcxOriginal.get(i).Value.trim());
			}
			if(dpcxOriginal.get(i).Name.contains("测向带宽") ){
				dpcxHead.Bandwidth = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("音频带宽") ){
				dpcxHead.Radiowidth = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("解调")){
				dpcxHead.Decode = dpcxOriginal.get(i).Value.trim();
			}
			if(dpcxOriginal.get(i).Name.contains("模式") ){
				dpcxHead.schema = dpcxOriginal.get(i).Value.trim();
			}
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		dpcxHead.Station = station;
		dpcxHead.Monitor = monitor;
		dpcxHead.MonitorLocation = monitorLocation;
		dpcxHead.Comments = null;
		dpcxHead.MD5 = null;  
		
		dpcxHead.BH = dpcxHead.BH + "-" + dpcxHead.StartTime.substring(0, dpcxHead.StartTime.indexOf(' ')) + "-" 
				+ df.format(dpcxHead.Frequence/1000000) + "-" + df.format(dpcxHead.Longitude) + "-" 
				+ df.format(dpcxHead.Latitude) + "-" + currentTime;
		if(!bh.equals("0")){
			dpcxHead.BH = bh;
		}
		columns = dpcxOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCUdpcxdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" 
				+ dpcxHead.BH + "','" + dpcxOriginal.get(i).Time + "'," + df.format(dpcxOriginal.get(i).Frequence) + ",'" + dpcxOriginal.get(i).Bearing + "','" 
				+ dpcxOriginal.get(i).Quality + "'," + dpcxOriginal.get(i).Levels + "," + dpcxOriginal.get(i).longitude + "," + dpcxOriginal.get(i).Latitude + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			dpcxHead.EndTime = temp[0].trim();
			sql = "insert into EMCUdpcxdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" 
			+ dpcxHead.BH + "','" + temp[0] + "'," + df.format(Double.parseDouble(temp[1])) + ",'" + temp[2].trim() + "','" 
			+ temp[3].trim() + "'," + Double.parseDouble(temp[4].trim()) + "," + temp[5].trim() + "," + temp[6].trim() + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMCUDPCXdata(bh,time,frequence,Bearing,Quality,levels,longitude,latitude) values('" + dpcxHead.BH + "',?,?,?,?,?,?,?)";
		//sql = sql + temp[0] + "'," + Double.parseDouble(temp[1].trim()) + "," + Double.parseDouble(temp[2].trim()) + ")";
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				dpcxHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setString(3, temp[2].trim());
			    db.pstm.setString(4, temp[3].trim());
			    db.pstm.setDouble(5, Double.parseDouble(temp[4].trim()));
			    db.pstm.setDouble(6, GetLongiLatiDouble(temp[5].trim()));
			    db.pstm.setDouble(7, GetLongiLatiDouble(temp[6].trim()));
			    
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();   
			    //System.out.println(i/10000);   
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}						
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCUdpcx(bh,businessType,businessName,filename,Longitude,Latitude,bandwidth,radiowidth,decode,schema,starttime,endtime,frequence,station,monitor,monitorLocation,MD5,data)values" +
				"('"+dpcxHead.BH+"','"+businessType+"','"+businessName+"','"+name+"',"+ dpcxHead.Longitude +","+ dpcxHead.Latitude +",'"+ dpcxHead.Bandwidth +"','"+ dpcxHead.Radiowidth +"','"+ dpcxHead.Decode +"','"+ dpcxHead.schema +"','"+
				dpcxHead.StartTime +"','"+ dpcxHead.EndTime +"',"+ df.format(dpcxHead.Frequence) +",'"+ dpcxHead.Station +"','"+ dpcxHead.Monitor +"','"+ dpcxHead.MonitorLocation +"','md5value',empty_blob())";
		String sql2="select data FROM EMCUdpcx WHERE  bh = '"+dpcxHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 
	 * @param fileName 文件路径+上传者+文件名 
	 * @param station 从页面获得的检测人
	 * @param monitor 从页面获得的检测站
	 * @param monitorLocation 从页面获得胡检测地
	 * @return 上传成功返回true,否则返回false
	 */
	public boolean importCDBLHDWOriginalMonitorData(String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		csvInput = new CSVInput();
		
		csvInput.init(fileName);
		String line = csvInput.getNextLine();//跳过头行
		
		LHDWHead lhdwHead;
		lhdwHead = new LHDWHead();
		lhdwHead.BH = "LHDW";
		
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
	    String currentTime = sdf.format(dt);	    		
	    DecimalFormat df = new DecimalFormat("#.000");//double类型样式，如df.format(d),用户将bh中的double类型的经纬度缩短
		
		String temp[];
		
		LinkedList<LHDWOriginal> lhdwOriginal;
		lhdwOriginal = new LinkedList<LHDWOriginal>();
		
		String sql;
		
		//1. 将文件头读入数组pdsmFileHead[]中
		int i,columns;

		line = csvInput.getNextLine();
		while(line != null){
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			columns = temp.length - 1;
			if(!temp[columns].contains(":"))
				break;
			
			LHDWOriginal lhdw = new LHDWOriginal();
			lhdw.Time = temp[0];
			lhdw.Frequence = Double.parseDouble(temp[1].trim());
			lhdw.Station = temp[2].trim();
			
			lhdw.Bearing = temp[4].trim();
			lhdw.Quality = temp[5].trim();
			lhdw.Levels = Double.parseDouble(temp[6].trim());
			lhdw.longitude = Double.parseDouble((temp[7].trim()));
			lhdw.Latitude = Double.parseDouble((temp[8].trim()));
			
			lhdw.Name = temp[columns].substring(0,temp[columns].indexOf(":"));
			lhdw.Value = temp[columns].substring(temp[columns].indexOf(":") + 1);
			lhdwOriginal.add(lhdw);
			
			line = csvInput.getNextLine();
		}
		
		//2. 分析数组lhdwFileHead[]中人数据项		
		lhdwHead.StartTime = lhdwOriginal.get(0).Time;
		lhdwHead.Frequence1 = lhdwOriginal.get(0).Frequence;//频率一样
		lhdwHead.Frequence2 = lhdwOriginal.get(0).Frequence;
		lhdwHead.Frequence3 = lhdwOriginal.get(0).Frequence;
		
		int occur=0;//记录文件信息中重复的内容是第几次出现
		columns = lhdwOriginal.size();
		for(i = 0; i < columns; i++){
			
			if(lhdwOriginal.get(i).Name.contains("监测单元") ){
				occur++;
				if(occur==1)
					lhdwHead.Station1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Station2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Station3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("测向带宽") ){
				if(occur==1)
					lhdwHead.Bandwidth1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Bandwidth2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Bandwidth3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("音频带宽") ){
				if(occur==1)
					lhdwHead.Radiowidth1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Radiowidth2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Radiowidth3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("解调")){
				if(occur==1)
					lhdwHead.Decode1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.Decode2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.Decode3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("模式") ){
				if(occur==1)
					lhdwHead.schema1 = lhdwOriginal.get(i).Value.trim();
				else if(occur==2)
					lhdwHead.schema2 = lhdwOriginal.get(i).Value.trim();
				else if(occur==3)
					lhdwHead.schema3 = lhdwOriginal.get(i).Value.trim();
			}
			if(lhdwOriginal.get(i).Name.contains("经度") ){
				if(occur==1)
					lhdwHead.Longitude1 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==2)
					lhdwHead.Longitude2 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==3)
					lhdwHead.Longitude3 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
			}
			if(lhdwOriginal.get(i).Name.contains("纬度")){
				if(occur==1)
					lhdwHead.Latitude1 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==2)
					lhdwHead.Latitude2 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
				else if(occur==3)
					lhdwHead.Latitude3 = GetLongiLatiDouble(lhdwOriginal.get(i).Value.trim());
			}
			
		}
		
		//3. 在单频监测数据表中插入数据， 先播放入文件头的数据，再插入其余数据
		lhdwHead.Station = station;
		lhdwHead.Monitor = monitor;
		lhdwHead.MonitorLocation = monitorLocation;
		lhdwHead.Comments = null;
		lhdwHead.MD5 = null;  
		
		lhdwHead.BH = lhdwHead.BH + "-" + lhdwHead.StartTime.substring(0, lhdwHead.StartTime.indexOf(' ')) + "-" 
				+ df.format(lhdwHead.Frequence1/1000000) + "-" + df.format(lhdwHead.Longitude1) + "-" 
				+ df.format(lhdwHead.Latitude1) + "-" + currentTime;

		if(!bh.equals("0")){
			lhdwHead.BH = bh;
		}
		columns = lhdwOriginal.size();
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		for(i = 0; i < columns; i++){
			sql = "insert into EMCUlhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" 
				+ lhdwHead.BH + "','" + lhdwOriginal.get(i).Time + "'," + df.format(lhdwOriginal.get(i).Frequence) + ",'" + lhdwOriginal.get(i).Station + "','" + lhdwOriginal.get(i).Bearing + "','" 
				+ lhdwOriginal.get(i).Quality + "'," + lhdwOriginal.get(i).Levels + "," + lhdwOriginal.get(i).longitude + "," + lhdwOriginal.get(i).Latitude + ")";
			emcdb.UpdateSQL(sql);
		}
		emcdb.closeStm();
		/*
		while(line != null){
			
			line = line.replace("\"","");//去除双引号
			//System.out.println(line);
			temp = line.split(",");//时间，频率，水平,文件信息
			
			lhdwHead.EndTime = temp[0].trim();
			sql = "insert into EMCUlhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" 
			+ lhdwHead.BH + "','" + temp[0] + "'," + df.format(Double.parseDouble(temp[1])) + ",'" + temp[2].trim() + "','" + temp[4].trim() + "','" 
			+ temp[5].trim() + "'," + temp[6].trim() + "," + temp[7].trim() + "," + temp[8].trim() + ")";
			EMCDB.exeSQL(sql);
			line = csvInput.getNextLine();
		}*/
		i=0;
		sql = "insert into EMCUlhdwdata(bh,time,frequence,station,Bearing,Quality,levels,longitude,latitude) values('" + lhdwHead.BH + "',?,?,?,?,?,?,?,?)";
		
		EMCDB db = new EMCDB();
		db.connDB(sql);
		try {
			Long beginTime = System.currentTimeMillis();
			while(line != null){
				
				line = line.replace("\"","");//去除双引号
				//System.out.println(line);
				temp = line.split(",");//时间，频率，水平,文件信息
				lhdwHead.EndTime = temp[0].trim();
				
			    db.pstm.setString(1, temp[0]);      
			    db.pstm.setDouble(2, Double.parseDouble(temp[1].trim()));
			    db.pstm.setString(3, temp[2].trim());
			    db.pstm.setString(4, temp[4].trim());
			    db.pstm.setString(5, temp[5].trim());
			    db.pstm.setDouble(6, Double.parseDouble(temp[6].trim()));
			    db.pstm.setDouble(7, GetLongiLatiDouble(temp[7].trim()));
			    db.pstm.setDouble(8, GetLongiLatiDouble(temp[8].trim()));
			    
			    db.pstm.addBatch();      
			    //每1000次提交一次     
			    if((++i)%50000==0){//可以设置不同的大小；如50，100，500，1000等等      
			    db.pstm.executeBatch();      
			    db.dynaConn.commit();      
			    db.pstm.clearBatch();   
			    //System.out.println(i/10000);   
			    }
			    line = csvInput.getNextLine();
			}
			//清空池子
			db.pstm.executeBatch();      
			db.dynaConn.commit();      
			db.pstm.clearBatch();
		    
			Long endTime = System.currentTimeMillis();      
		    System.out.println("写入每行数据用时："+(endTime-beginTime)/1000+"秒");      
		    db.closeDB();
		    
			} catch (SQLException e) {
				
				e.printStackTrace();
			}								
		
		csvInput.close();
		
		String name = fileName.contains("\\") ? fileName.substring(fileName.lastIndexOf("\\")+1) : fileName;
		sql="insert into EMCUlhdw(bh,businessType,businessName,filename,station1,bandwidth1,radiowidth1,decode1,schema1,frequence1,Longitude1,Latitude1,station2,bandwidth2,radiowidth2,decode2,schema2,frequence2,Longitude2,Latitude2,station3,bandwidth3,radiowidth3,decode3,schema3,frequence3,Longitude3,Latitude3,starttime,endtime,station,monitor,monitorLocation,MD5,data)values" +
				"('"+lhdwHead.BH+"','"+businessType+"','"+businessName+"','"+name+"','" + lhdwHead.Station1 +"','" + lhdwHead.Bandwidth1 +"','"+ lhdwHead.Radiowidth1 +"','"+ lhdwHead.Decode1 +"','"+ lhdwHead.schema1 +"',"+ df.format(lhdwHead.Frequence1) +"," + lhdwHead.Longitude1 +","+ lhdwHead.Latitude1 +","
				+"'" + lhdwHead.Station2 +"','"+ lhdwHead.Bandwidth2 +"','"+ lhdwHead.Radiowidth2 +"','"+ lhdwHead.Decode2 +"','"+ lhdwHead.schema2 +"',"+ df.format(lhdwHead.Frequence2) +"," + lhdwHead.Longitude2 +","+ lhdwHead.Latitude2 +","
				+"'" + lhdwHead.Station3 +"','"+ lhdwHead.Bandwidth3 +"','"+ lhdwHead.Radiowidth3 +"','"+ lhdwHead.Decode3 +"','"+ lhdwHead.schema3 +"',"+ df.format(lhdwHead.Frequence3) +"," + lhdwHead.Longitude3 +","+ lhdwHead.Latitude3 +","
				+"'"+lhdwHead.StartTime +"','"+ lhdwHead.EndTime +"','"+ lhdwHead.Station +"','"+ lhdwHead.Monitor +"','"+ lhdwHead.MonitorLocation +"','md5value',empty_blob())";
		String sql2="select data FROM EMCUlhdw WHERE  bh = '"+lhdwHead.BH+"'";
		
		boolean flag1=EMCDB.InsertFile(sql,sql2,fileName,true);
		//boolean flag2=EMCDB.ExportFile(sql2,fileName);//把数据库中的文件读到E盘根目录下，证明文件确实成功插入数据库。
		
		return flag1;
	}
	
	/**
	 * 导入监测报告数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importJCBGData(String path,String fileType,String reportName,String reportType,String startTime,String endTime,String startFrequence,String endFrequence,String keyWords,String station,String author,String monitorLocation,String equipment,String businessType,String businessName,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("DBJCBGData"))||fileType.equals("DBJCBGQuery")){
			
			table = "EMCJCBG";
			temp = "JCBG-";
		}else if((fileType.equals("CDBJCBGData"))||fileType.equals("CDBJCBGQuery")){
			table = "EMCUJCBG";
			temp = "CJCBG-";
		}else{
			return "页面类型错误";
		}
		
	    if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + reportType+"-"+startTime.substring(0, startTime.indexOf(' ')) + "-" + df.format(Double.parseDouble(startFrequence)/1000000) + "-" + md5 + "-" + currentTime;
		}
		String sql = null;
		String sql2 = null;
		if((fileType.equals("DBJCBGData"))||fileType.equals("DBJCBGQuery")){
			
			sql="insert into EMCJCBG(bh,businessType,businessName,filename,type,startTime,endTime,startFrequence,endFrequence,keyWords,station,writer,monitorLocation,equipment,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+reportName+"','"+reportType+"','"+startTime+"','"+endTime+"',"+startFrequence+","+endFrequence+",'"+keyWords+"','"+station+"','"+author+"','"+monitorLocation+"','"+equipment+"','md5value',empty_blob())";
			sql2="select data FROM EMCJCBG WHERE  bh = '"+bh+"'";
		}else if((fileType.equals("CDBJCBGData"))||fileType.equals("CDBJCBGQuery")){
			
			sql="insert into EMCUJCBG(bh,businessType,businessName,filename,type,startTime,endTime,startFrequence,endFrequence,keyWords,station,writer,monitorLocation,equipment,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+reportName+"','"+reportType+"','"+startTime+"','"+endTime+"',"+startFrequence+","+endFrequence+",'"+keyWords+"','"+station+"','"+author+"','"+monitorLocation+"','"+equipment+"','md5value',empty_blob())";
			sql2="select data FROM EMCUJCBG WHERE  bh = '"+bh+"'";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}	
	
	/**
	 * 导入卫星监测报告数据
	 * @param path	文件路径+文件名
	 * @return  成功返回 “上传成功”， 不成功返回 “上传失败”
	 */
	public String importJCBGData_Sat(String path,String fileType,String reportName,String reportType,String startTime,String endTime,String keyWords,String station,String author,String monitorLocation,String equipment,String businessType,String businessName,String bh) {
		
		String table = null;
		String temp = null;
		if((fileType.equals("WXJCBGData"))||fileType.equals("WXJCBGQuery")){
			
			table = "EMCWJCBG";
			temp = "WJCBG-";
		}else{
			return "页面类型错误";
		}
		
	    if(!bh.trim().equals("0")){
	    	
	    	EMCDB emcdb = new EMCDB();
		    emcdb.dynaStm = emcdb.newStatement();
			String sql = "delete from "+table+" where bh='"+bh+"'";
			if(emcdb.UpdateSQL(sql)<1){
				return "删除原文件时失败。";
			}
			emcdb.closeStm();
	    }
		
		boolean flag1 = false;
		boolean flag2 = false;
		Date dt = new Date(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DecimalFormat df = new DecimalFormat("#.000");
	    String currentTime = sdf.format(dt);
	    path = path.replaceAll("%20", " ");
		String md5 = EMCDB.getMd5ByFile(path);//获取压缩前文件的md5
		if(bh.trim().equals("0")){
			bh = temp + reportType+"-"+startTime.substring(0, startTime.indexOf(' ')) + "-" + md5 + "-" + currentTime;
		}
		
		String sql = null;
		String sql2 = null;
		if((fileType.equals("WXJCBGData"))||fileType.equals("WXJCBGQuery")){
			
			sql="insert into EMCWJCBG(bh,businessType,businessName,filename,type,startTime,endTime,keyWords,station,writer,monitorLocation,equipment,MD5,data)values" +
					"('"+bh+"','"+businessType+"','"+businessName+"','"+reportName+"','"+reportType+"','"+startTime+"','"+endTime+"','"+keyWords+"','"+station+"','"+author+"','"+monitorLocation+"','"+equipment+"','md5value',empty_blob())";
			sql2="select data FROM EMCWJCBG WHERE  bh = '"+bh+"'";
		}
		
		else{
			
			return "未知的数据类型，上传失败。";
		}
		
		flag1=EMCDB.InsertFile(sql,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1)
			return "上传成功";
		else
			return "上传失败";
	}	
	
	
	/*
	public boolean importDBDPJCGraphicData(String fileName){
		
		return true;
	}
	public boolean importDBDPJCAudioData(String fileName){
		
		return true;
	}
	public boolean importDBDPJCVideoData(String fileName){
		
		return true;
	}
	public boolean importDBdpcxOriginalMonitorData(String fileName){
		
		return true;
	}
	public boolean importDBPDSMGraphicData(String fileName){
		
		return true;
	}
	public boolean importDBPDSMVideoData(String fileName){
		
		return true;
	}
	public boolean importDBXHCXSignalParameterReg(String fileName){
		
		return true;
	}
	
	public boolean importDBXHCXGraphicData(String fileName){
		
		return true;
	}
	public boolean importDBXHCXAudioData(String fileName){
		
		return true;
	}
	public boolean importDBXHCXVideoData(String fileName){
		
		return true;
	}
	
	public boolean importDBLHDWVideoData(String fileName){
		
		return true;
	}
	public boolean importDBJCBGData(String fileName){
		
		return true;   
	}
	*/
	/**
	 * 4种原始监测的上传和修改
	 * @param fileName 文件名+上传者+路径名
	 * @param fileType 数据类型
	 * @param station 检测人
	 * @param monitor 检测站
	 * @param monitorLocation 检测地
	 * @return
	 */
	public String importCSV(String fileName, String fileType, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		

		//System.out.println("importCSV(). fileName=" + fileName + "; fileType=" + fileType);
		String table = null;
	    if(!bh.trim().equals("0")){	//说明是数据修改
			if(fileType.trim().equals("DBXHCXQuery")){
				
				table = "EMCDPCX";
			}else if(fileType.trim().equals("CDBXHCXQuery")){
				
				table = "EMCUDPCX";
			}else if(fileType.trim().equals("DBLHDWQuery")){
				
				table = "EMCLHDW";
			}else if(fileType.trim().equals("CDBLHDWQuery")){
				
				table = "EMCULHDW";
			}
			if(table != null){//删除这4种数据，剩下的4种在dpjcOrpdsm中作出是否可以导入的判断后再删除
				EMCDB emcdb = new EMCDB();
			    emcdb.dynaStm = emcdb.newStatement();
				String sql = "delete from "+table+" where bh='"+bh+"'";
				if(emcdb.UpdateSQL(sql)<1){
					return "删除原文件时失败。";
				}
				String sql2 = "delete from "+table+"data where bh='"+bh+"'";
				emcdb.UpdateSQL(sql2);
				emcdb.closeStm();
			}
	    }
		boolean result =false;
		if((fileType.equals("DBXHCXOriginalMonitorData") )||(fileType.equals("DBXHCXQuery")))
			result = importDBXHCXOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
		
		else if((fileType.equals("DBLHDWOriginalMonitorData") )||(fileType.equals("DBLHDWQuery")))
			result = importDBLHDWOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
		else if((fileType.equals("CDBXHCXOriginalMonitorData") )||(fileType.equals("CDBXHCXQuery")))
			result = importCDBXHCXOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
		
		else if((fileType.equals("CDBLHDWOriginalMonitorData") )||(fileType.equals("CDBLHDWQuery")))
			result = importCDBLHDWOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
		else
			return dpjcOrpdsm(fileName,fileType, station, monitor, monitorLocation,businessType,businessName,bh);
		
		if(result)
			return "文件上传成功";
		else
			return "文件上传失败";
		/*
		if(fileType.equals("DBDPJCSignalParameterReg") )
			importSignalParameterReg(fileName);
		else if(fileType.equals("DBDPJCOriginalMonitorData") )
			importDBDPJCOriginalMonitorData(fileName, station, monitor, monitorLocation);
		else if(fileType.equals("DBDPJCGraphicData") )
			importDBDPJCGraphicData(fileName);
		else if(fileType.equals("DBDPJCAudioData") )
			importDBDPJCAudioData(fileName);
		else if(fileType.equals("DBDPJCVideoData") )
			importDBDPJCVideoData(fileName);
		else if(fileType.equals("DBPDSMOriginalMonitorData") )
			importDBPDSMOriginalMonitorData(fileName);
		else if(fileType.equals("DBPDSMGraphicData") )
			importDBPDSMGraphicData(fileName);
		else if(fileType.equals("DBPDSMVideoData") )
			importDBPDSMVideoData(fileName);
		else if(fileType.equals("DBXHCXSignalParameterReg") )
			importDBXHCXSignalParameterReg(fileName);
		else if(fileType.equals("DBXHCXOriginalMonitorData") )
			importDBXHCXOriginalMonitorData(fileName);
		else if(fileType.equals("DBXHCXGraphicData") )
			importDBXHCXGraphicData(fileName);
		else if(fileType.equals("DBXHCXAudioData") )
			importDBXHCXAudioData(fileName);
		else if(fileType.equals("DBXHCXVideoData") )
			importDBXHCXVideoData(fileName);
		else if(fileType.equals("DBLHDWOriginalMonitorData") )
			importDBLHDWOriginalMonitorData(fileName);
		else if(fileType.equals("DBLHDWVideoData") )
			importDBLHDWVideoData(fileName);
		else if(fileType.equals("DBJCBGData") )
			importDBJCBGData(fileName);
		*/	
		
	}
	public String dpjcOrpdsm(String fileName,String fileType, String station,String monitor,String monitorLocation,String businessType,String businessName,String bh){
		
		boolean result = false;
		String line = null;
		String temp[];
		String[] frequence = new String[2];
		
		CsvReader csvReader;
		try {
			csvReader = new CsvReader(fileName);
			csvReader.readHeaders();
			
			csvReader.readRecord();
			line = csvReader.getRawRecord();
			line = line.replaceAll("\"","");//去除双引号
			temp = line.split(",");//时间，频率，水平,文件信息
			frequence[0] = temp[1].trim();
			
			csvReader.readRecord();
			line = csvReader.getRawRecord();
			line = line.replaceAll("\"","");//去除双引号
			temp = line.split(",");//时间，频率，水平,文件信息
			frequence[1] = temp[1].trim();
			
			csvReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "找不到文件。";
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "读取csv失败。";
		}		
		if((!frequence[0].equals(frequence[1]))&&((fileType.equals("DBDPJCQuery"))||(fileType.equals("CDBDPJCQuery")))){//dpjc修改导入了pdsm数据，不允许
			
			return "该文件不是单频监测原始监测数据，请导入单频监测原始监测数据。";
		}else if((frequence[0].equals(frequence[1]))&&((fileType.equals("DBPDSMQuery"))||(fileType.equals("CDBPDSMQuery")))){//pdsm修改导入了dpjc数据，不允许
			
			return "该文件不是频段扫描原始监测数据，请导入频段扫描原始监测数据。";
		}
		else{
			String table = null;
			if(fileType.trim().equals("DBDPJCQuery")){
				
				table = "EMCDPJC";
			}else if(fileType.trim().equals("CDBDPJCQuery")){
				
				table = "EMCUDPJC";
			}else if(fileType.trim().equals("DBPDSMQuery")){
					
					table = "EMCPDSM";
			}else if(fileType.trim().equals("CDBPDSMQuery")){
				
				table = "EMCUPDSM";
			}
			if(table != null){//删除这4种数据，剩下的4种在dpjcOrpdsm中作出是否可以导入的判断后再删除
				EMCDB emcdb = new EMCDB();
			    emcdb.dynaStm = emcdb.newStatement();
				String sql = "delete from "+table+" where bh='"+bh+"'";
				if(emcdb.UpdateSQL(sql)<1){
					return "删除原文件时失败。";
				}
				String sql2 = "delete from "+table+"data where bh='"+bh+"'";
				emcdb.UpdateSQL(sql2);
				emcdb.closeStm();
			}
			
			if(frequence[0].equals(frequence[1])){
				if(fileType.equals("DBDPJCOriginalMonitorData")||fileType.equals("DBPDSMOriginalMonitorData")||fileType.equals("DBDPJCQuery")){
					
					result = importDBDPJCOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
				}
				if(fileType.equals("CDBDPJCOriginalMonitorData")||fileType.equals("CDBPDSMOriginalMonitorData")||fileType.equals("CDBDPJCQuery")){
					
					result = importCDBDPJCOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
				}
			}else{
				if(fileType.equals("DBDPJCOriginalMonitorData")||fileType.equals("DBPDSMOriginalMonitorData")||fileType.equals("DBPDSMQuery")){
					
					result = importDBPDSMOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
				}
				if(fileType.equals("CDBDPJCOriginalMonitorData")||fileType.equals("CDBPDSMOriginalMonitorData")||fileType.equals("CDBPDSMQuery")){
					
					result = importCDBPDSMOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
				}
			}
		}
		if(result)
			return "文件上传成功";
		else
			return "文件上传失败";
	}
	/**
	 * 从信号参数登记中导入图形数据,同时可以用来在信号参数登记中修改图形数据
	 * @param path
	 * @param bh
	 * @param fileType
	 * @param grapType
	 * @return
	 */
	public String importGrapfromSignal(String path,String bh,String fileType,String grapType){
		
		boolean flag1 = false;
		String sql = null;
		String sql1 = null;
		String sql2 = null;
		String table = null;//取数据的信号参数登记表
		String table2 = null;//要写入的图形表
		if((fileType.equals("DBDPJCSignalParameterReg"))||(fileType.equals("DBDPJCQuery"))){
			
			 table = "emcdpjcparameter";
			 table2 = "emcdpjcgraphic";
		}else if((fileType.equals("DBXHCXSignalParameterReg"))||(fileType.equals("DBXHCXQuery"))){
			
			 table = "emcdpcxparameter";
			 table2 = "emcdpcxgraphic";
		}else if((fileType.equals("CDBDPJCSignalParameterReg"))||(fileType.equals("CDBDPJCQuery"))){
			
			 table = "emcudpjcparameter";
			 table2 = "emcudpjcgraphic";
		}else if((fileType.equals("CDBXHCXSignalParameterReg"))||(fileType.equals("CDBXHCXQuery"))){
			
			 table = "emcudpcxparameter";
			 table2 = "emcudpcxgraphic";
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		sql = "select * from "+table+" where bh='"+bh+"'";
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			if(emcdb.dynaRs.next()){
				
			sql1="insert into "+table2+" (bh,businessType,businessName,signalType,longitude,latitude,graphicType,startTime," +
					"frequence,station,monitor,monitorLocation,MD5,data)values" +
					"('"+bh+"','"+emcdb.dynaRs.getString("businessType")+"','"+emcdb.dynaRs.getString("businessName")
					+"','"+emcdb.dynaRs.getString("signalType")+"','"+emcdb.dynaRs.getString("longitude")
					+"','"+emcdb.dynaRs.getString("latitude")+"','"+grapType
					+"','"+emcdb.dynaRs.getString("MONITORTIME")+"','"+emcdb.dynaRs.getString("frequence")
					+"','"+emcdb.dynaRs.getString("station")+"','"+emcdb.dynaRs.getString("monitor")
					+"','"+emcdb.dynaRs.getString("monitorLocation")+"','md5value',empty_blob())";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql2="select data FROM "+table2+" WHERE  bh = '"+bh+"' and graphictype='"+grapType+"'";
		flag1=EMCDB.InsertFile(sql1,sql2,path,true);
		//flag2=EMCDB.ExportFile(sql2,path);
		if(flag1){
			String column = null;
			if(grapType.trim().equals("中频图")){
				column = "FREQGRAP";
			}else if(grapType.trim().equals("示向图")){
				column = "DIREGRAP";
			}else if(grapType.trim().equals("定位图")){
				column = "LOCAGRAP";
			}else{
				return "上传失败，图形类型错误";
			}

			String sql3 = "update "+table+" set "+column+"='1' where bh='"+bh+"'";
			int flag = emcdb.UpdateSQL(sql3);
			//System.out.println("信号登记受影响行数："+flag);
			emcdb.closeRs();
			emcdb.closeStm();
			return "上传成功";
			/*
			if(emcdb.UpdateSQL(sql3)>0){
				return "上传成功";
			}else{
				return "更新信号参数登记表失败";
			}
			*/
		}
			
		else{
			emcdb.closeRs();
			emcdb.closeStm();
			return "上传失败";
		}
	}
	/**
	 * 从信号参数登记中导入和修改原始数据
	 * @param fileName
	 * @param bh
	 * @param fileType
	 * @return
	 */
	public String importOriginalfromSignal(String fileName,String bh,String fileType){
		
		//String fileName, String station,String monitor,String monitorLocation,String businessType,String businessName
		String sql = null;
		String table = null;//取数据的信号参数登记表
		if((fileType.equals("DBDPJCSignalParameterReg"))||(fileType.equals("DBDPJCQuery"))){
			
			 table = "emcdpjcparameter";
			 
		}else if((fileType.equals("DBXHCXSignalParameterReg"))||(fileType.equals("DBXHCXQuery"))){
			
			 table = "emcdpcxparameter";
			 
		}else if((fileType.equals("CDBDPJCSignalParameterReg"))||(fileType.equals("CDBDPJCQuery"))){
			
			 table = "emcudpjcparameter";
			 
		}else if((fileType.equals("CDBXHCXSignalParameterReg"))||(fileType.equals("CDBXHCXQuery"))){
			
			 table = "emcudpcxparameter";
			 
		}else{
			
			return "未知的数据类型，上传失败。";
		}
		String station = null;
		String monitor = null;
		String monitorLocation = null;
		String businessType = null;
		String businessName = null;
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		sql = "select * from "+table+" where bh='"+bh+"'";
		emcdb.dynaRs = emcdb.QuerySQL(sql);
		try {
			if(emcdb.dynaRs.next()){
				station = emcdb.dynaRs.getString("station");
				monitor = emcdb.dynaRs.getString("monitor");
				monitorLocation = emcdb.dynaRs.getString("monitorLocation");
				businessType = emcdb.dynaRs.getString("businessType");
				businessName = emcdb.dynaRs.getString("businessName");
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "获取信号参数登记信息失败。";
		}
		//System.out.println(station+"/"+businessType);
		boolean result = false;
		if((fileType.equals("DBDPJCSignalParameterReg"))||(fileType.equals("DBDPJCQuery"))){
			
			result = importDBDPJCOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
			
		}else if((fileType.equals("DBXHCXSignalParameterReg"))||(fileType.equals("DBXHCXQuery"))){
			
			result = importDBXHCXOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
			
		}else if((fileType.equals("CDBDPJCSignalParameterReg"))||(fileType.equals("CDBDPJCQuery"))){
			
			result = importCDBDPJCOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
			
		}else {
			
			result = importCDBXHCXOriginalMonitorData(fileName, station, monitor, monitorLocation,businessType,businessName,bh);
			
		}
		if(result){//修改与之对应的信号参数登记表的信息
			String sql3 = "update "+table+" set Original='1' where bh='"+bh+"'";
			int flag = emcdb.UpdateSQL(sql3);
			//System.out.println("信号登记受影响行数："+flag);

			emcdb.closeRs();
			emcdb.closeStm();
			return "上传成功。";
		}
		else{
			emcdb.closeRs();
			emcdb.closeStm();
			return "文件上传失败。";
		}
		
	}
	
	/**
	 * 修改信号参数登记的关联文件
	 * @param path 文件所在路径
	 * @param type 信号参数登记的类型，短波、超短波，单频、测向
	 * @param bh 信号参数登记的bh
	 * @param fileType 文件类型，一种原始监测数据和三种图形数据
	 * @return
	 */
	public String modifyFileFromSignal(String path,String type,String bh,String fileType){
		
		String table = null;
		if(type.equals("DBDPJCQuery")){
			
			table = "EMCDPJC";
		}else if(type.equals("CDBDPJCQuery")){
			
			table = "EMCUDPJC";
		}else if(type.equals("DBXHCXQuery")){
			
			table = "EMCDPCX";
		}else if(type.equals("CDBXHCXQuery")){
			
			table = "EMCUDPCX";
		}else{
			return null;
		}
		String sql = null;
		String sql2 = null;
		EMCDB emcdb = new EMCDB();
		emcdb.dynaStm = emcdb.newStatement();
		if(!fileType.trim().equals("原始数据")){
			table = table+"GRAPHIC";
			sql = "delete from "+table+" where bh='"+bh+"' and graphictype='"+fileType+"'";
		}else{
			sql = "delete from "+table+" where bh='"+bh+"'";
			sql2 = "delete from "+table+"DATA where bh='"+bh+"'";
			emcdb.UpdateSQL(sql2);
		}
		
		emcdb.UpdateSQL(sql);
		emcdb.closeStm();
		if(!fileType.trim().equals("原始数据")){
			return importGrapfromSignal(path,bh,type,fileType);
		}else{
			return importOriginalfromSignal(path,bh,type);
		}
	}
	
	public  String getBasePath(String base)    {  
	       
		String path1=Thread.currentThread().getContextClassLoader().getResource("").toString();
		//file:/E:/EMC/.metadata/.me_tcat/webapps/rsui/WEB-INF/classes/
		path1=path1.replace('/', '\\'); // 将/换成\  
		path1=path1.replace("file:", ""); //去掉file:  
		path1=path1.replace("classes\\", ""); //去掉class\  
		path1=path1.replace("WEB-INF\\", ""); //去掉class\  
		path1=path1.substring(1); //去掉第一个\,如 \D:\JavaWeb...  
		path1+=base;  
      
        return path1;  
    }
	//获取经纬度的值。将116°25'56.0E形式的经纬度转换为double类型的一个值。
		//注意经纬度中没有双引号
		//20161205：如果格式就是double形式的经纬度，只进行类型的换。
		public static Double GetLongiLatiDouble(String str) {
			
			String d = "0.0";
			String s = "0.0";
			String m = "0.0";
			double jwd=0.0;
			
			if(str==""||str==null)
				return jwd;
			
			//double jd=0.0;
			if(str.contains("°")){
				String[] temp=str.split("°");
				d=temp[0];
				
				if(temp[1].contains("'")){
					String[] temp2=temp[1].split("'");
					m=temp2[0];
					
					s=temp2[1];
					s=s.substring(0,s.length()-1);
				}
				jwd=java.lang.Double.parseDouble(d.trim())+java.lang.Double.parseDouble(m.trim()) / 60 + java.lang.Double.parseDouble(s.trim()) / 3600;
				
			}else{
				jwd = java.lang.Double.parseDouble(str);
			}
			return 	jwd;
				

		}
	
	/**
	 * 判断监测数据文件的类型，如单频监测，单频测向，频段扫描，频率列表，宽带监测。
	 * 将监测数据文件入库集成。
	 * @param path 待处理的XML监测文件说明，包括路径和文件名
	 * @return 以字符串表示的处理结果。
	 */
	/*
	  public String  ImportXML(String path, String schemaFile) {
	 

		boolean  flag=false;
		String bh = null;
		System.out.println("import file.java: " + path +" \nschema file: " + schemaFile);
		myEmcParser = new EMCXMLParser(path,schemaFile);
		myEmcParser.close();
		
		String jd=myEmcParser.getMonitoringHeadValueByName("经度");
		System.out.println("ImportFile.java-jd:"+jd);
		fileMsg.jd=myEmcParser.GetLongiLatiDouble(myEmcParser.getMonitoringHeadValueByName("经度"));
		System.out.println("ImportFile.java- fileMsg.jd:"+fileMsg.jd);
		fileMsg.wd=myEmcParser.GetLongiLatiDouble(myEmcParser.getMonitoringHeadValueByName("纬度"));		
		fileMsg.jczbh=myEmcParser.getMonitoringHeadValueByName("监测单元");
		fileMsg.jsjmc=myEmcParser.getJsjmc();
		fileMsg.txmc=myEmcParser.getTxmc();
		fileMsg.unitType=myEmcParser.getUt();
		
		ArrayList<String> str=new ArrayList<String>();
		str=myEmcParser.getEndtime(path);
		myEmcParser.endTime=str.get(1);
		myEmcParser.end_pl=Double.parseDouble(str.get(0));
		fileMsg.startTime = myEmcParser.startTime;
		fileMsg.endTime =myEmcParser.endTime;

		listMsg.isSucceed=GetJCSBLJ(fileMsg.jsjmc, fileMsg.txmc, fileMsg.jczbh);
		int n=myEmcParser.i_FileType;
		System.out.println("ImportFile.java:-ImportXML  parsing ok." );
		
		
		
		System.out.println("ImportFile.java-listMsg.isSucceed:" +listMsg.isSucceed);
		System.out.println("ImportFile.java-n:" +n);

		if(listMsg.isSucceed){
			switch(n){
			case 1:
				bh=ImportDpjcFile(path);
				break;
			case 2 :
				bh=ImportDpcxFile(path);
				break;
			case 3:
				bh=ImportPdsmFile(path);
				break;
			case 4:
				bh=ImportPllbFile(path);
				break;
			case 5:
				bh=ImportKdcxFile(path);
				break;
			case 0:
				return "不是标准文件";
			}
			if(bh!="")			{
				if(FillOtherTable(bh))
					return "匹配成功";        //成功插入数据 以及相关信息
				else 
					return "插入相关信息失败！ 请重新插入";      //未能插入相关信心
			}
			return "数据文件已存在，无需重新插入";   // 数据表存在相关信息 ，不需从新插入

		}else 
			return "匹配失败";        //匹配不成功
	
	}


	public String  ImportXML(String path ,String schemapath, String jczbh,String jsjmc,String txmc) {

		boolean  flag=false;
		String bh = null;
		myEmcParser = new EMCXMLParser(path,schemapath);
		myEmcParser.close();
		
		fileMsg.jd=myEmcParser.GetLongiLatiDouble(myEmcParser.getMonitoringHeadValueByName("经度"));
		System.out.println("fileMsg.jd:"+fileMsg.jd);
		fileMsg.wd=myEmcParser.GetLongiLatiDouble(myEmcParser.getMonitoringHeadValueByName("纬度"));		
		//JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE); 
		fileMsg.jczbh=jczbh;
		fileMsg.jsjmc=jsjmc;
		fileMsg.txmc=txmc;
		fileMsg.unitType=myEmcParser.getUt();

		ArrayList<String> str=new ArrayList<String>();
		str=myEmcParser.getEndtime(path);
		myEmcParser.endTime=str.get(1);
		myEmcParser.end_pl=Double.parseDouble(str.get(0));
		fileMsg.startTime = myEmcParser.startTime;
		fileMsg.endTime =myEmcParser.endTime;
		System.out.println(fileMsg.startTime );
		listMsg.isSucceed=GetJCSBLJ(fileMsg.jsjmc, fileMsg.txmc, fileMsg.jczbh);
		int n=myEmcParser.i_FileType;
		System.out.println("ImportXML  ok" );

		System.out.println("listMsg.isSucceed:" +listMsg.isSucceed);
		System.out.println("n:" +n);
		
		if(listMsg.isSucceed){

			switch(n){
			case 1:
				bh=ImportDpjcFile(path);
				break;
			case 2 :
				bh=ImportDpcxFile(path);
				break;
			case 3:
				bh=ImportPdsmFile(path);
				break;
			case 4:
				bh=ImportPllbFile(path);
				break;
			case 5:
				bh=ImportKdcxFile(path);
				break;
			case 0:
				return "不是标准文件";
			}
			
			if(bh!="")	{
				if(FillOtherTable(bh))
					return "匹配成功";        //成功插入数据 以及相关信息
				else 
					return "插入相关信息失败！ 请重新插入";      //未能插入相关信心
			}
			return "数据文件已存在，无需重新插入";   // 数据表存在相关信息 ，不需从新插入
		}	else 
			return "匹配失败";        //匹配不成功
	}

	private boolean FillOtherTable(String bh) {
		// TODO Auto-generated method stub
		String yssjbh, rwbh, sql;
		yssjbh="YS"+bh;
		rwbh="RW"+bh;
		sql="insert into jc_jcrw(JCRWBH) values('"+rwbh+"')";
		System.out.println(sql);
		EMCDB.exeSQL(sql);
		sql="insert into jc_jcrw_zrw (jcrwbh, jczrwbh, jcsblj)values('"+rwbh+"', '"+rwbh+"','"+fileMsg.jcsblj+"')";
		EMCDB.exeSQL(sql);
		System.out.println(sql);
		sql="insert into jc_jcxtzxzrw (jczbh, jcxtbh, jcrwbh, jczrwbh) values ('"+fileMsg.jczbh+"', '"+fileMsg.jcxtbh+"', '"+rwbh+"', '"+rwbh+"')";
		EMCDB.exeSQL(sql);
		System.out.println(sql);
		sql="insert into jc_yssj (yssjbh, jcrwbh, jczrwbh, jd_jcz, wd_jcz, jcsj_qs, jcsj_js, dporcq) values ('"+yssjbh+"', '"+rwbh+"', '"+rwbh+"', "+fileMsg.jd+", "+fileMsg.wd+", to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), '"+fileMsg.unitType+"')";		
		System.out.println(sql);

		EMCDB.exeSQL(sql);
		return true;
	}


	private  String ImportKdcxFile(String path) {

		boolean flag = false;
		String md5="待处理";
		String bh;
		double qspl=myEmcParser.UnitToMHz(myEmcParser.start_pl,myEmcParser.getDataHeadValueByName("频率"));
		double zzpl=myEmcParser.UnitToMHz(myEmcParser.end_pl,myEmcParser.getDataHeadValueByName("频率"));
		int n=path.lastIndexOf('\\');
		bh=myEmcParser.GetNumber(5);
		String yssjbh="YS"+bh;
		String sql;
		sql="insert into jc_yssj_kdjccx(yssjbh, kdjccxbh, plsmsj_qs, plsmsj_zz, smpl_qs, smpl_zz, jd_jcdd, wd_jcdd,MD5, dpcq)values('"+yssjbh+"', '"+bh+"',to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), "+qspl+", "+zzpl+", "+fileMsg.jd+", "+fileMsg.wd+", '"+md5+"',empty_blob())";
		String sql2;
		sql2="select dpcq from jc_yssj_kdjccx where kdjccxbh='"+bh+"'";
		flag=EMCDB.InsertBlob(sql, sql2, path,true);
		if(flag)
			return bh;
		return null;	
	}


	private  String ImportPllbFile(String path) {

		boolean flag = false;
		String md5="待处理";
		String bh;
		//double jcpl=myEmcParser.UnitToMHz(myEmcParser.pl1,myEmcParser.getDataHeadValueByName("频率"));
		int n=path.lastIndexOf('\\');
		//String filename=path.substring(n+1, path.length()-4);
		bh=myEmcParser.GetNumber(4);
		String yssjbh="YS"+bh;
		double smpl_qs=myEmcParser.UnitToMHz(myEmcParser.start_pl,myEmcParser.getDataHeadValueByName("频率"));
		double smpl_zz=myEmcParser.UnitToMHz(myEmcParser.end_pl,myEmcParser.getDataHeadValueByName("频率"));
		String pllb="暂未处理";
		String sql;

		sql="insert into jc_yssj_pllbsm(yssjbh, pllbsmbh, smpl_qs, smpl_zz, plsmsj_qs, plsmsj_zz, smpl_lb, jd_jcdd, wd_jcdd, MD5,dpcq)values('"+yssjbh+"', '"+bh+"', "+smpl_qs+", "+smpl_zz+", to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), '"+pllb+"', "+fileMsg.jd+", "+fileMsg.wd+",'"+md5+"', empty_blob())";

		String sql2 = "select dpcq from jc_yssj_pllbsm where pllbsmbh='"+bh+"'";
		flag=EMCDB.InsertBlob(sql, sql2, path,true);
		if(flag)
			return bh;
		return null;
	}


	private String ImportPdsmFile(String path) {

		boolean flag = false;
		String md5="待处理";
		String bh;
		//double jcpl=myEmcParser.UnitToMHz(myEmcParser.pl1,myEmcParser.getDataHeadValueByName("频率"));
		double qspl=myEmcParser.getFrequece(myEmcParser.getMonitoringHeadValueByName("最低频率"));
		double zzpl=myEmcParser.getFrequece(myEmcParser.getMonitoringHeadValueByName("最高频率"));
		
		
		
		int n=path.lastIndexOf('\\');
		//String filename=path.substring(n+1, path.length()-4);
		bh=myEmcParser.GetNumber(3);
		String yssjbh="YS"+bh;
		String sql ; 
		sql="insert into jc_yssj_pdsm(yssjbh, pdsmbh, plsmsj_qs, plsmsj_zz, jd_jcdd, wd_jcdd, smpl_qs, smpl_zz, MD5,dpcq)values('"+yssjbh+"', '"+bh+"',to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), "+fileMsg.jd+", "+fileMsg.wd+", "+qspl+", "+zzpl+",'"+md5+"', empty_blob())";

		String sql2 = "select dpcq from jc_yssj_pdsm where pdsmbh='"+bh+"'";
		flag=EMCDB.InsertBlob(sql, sql2, path,true);
		if(flag)
			return bh;
		return null;
	}


	private String ImportDpcxFile(String path) {
		boolean flag = false;
		String md5="待处理";
		String bh;
		double jcpl=myEmcParser.UnitToMHz(myEmcParser.pl1,myEmcParser.getDataHeadValueByName("频率"));
		
		int n=path.lastIndexOf('\\');
		String filename=path.substring(n+1, path.length()-4);
		bh=myEmcParser.GetNumber(2);
		String yssjbh="YS"+bh;
		String sql="insert into jc_yssj_dpcx(yssjbh, dpcxbh, cxsj_qs, cxsj_zz, jd_jcdd, wd_jcdd, cxpl, dpcq)values('"+yssjbh+"', '"+bh+"', to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'), "+fileMsg.jd+", "+fileMsg.wd+", "+jcpl+",'"+md5+"', empty_blob())";

		String sql2="select dpcq from jc_yssj_dpcx where dpcxbh='"+ bh+"'";

		flag=EMCDB.InsertBlob(sql, sql2, path,true);
		if(flag)
			return bh;
		return null;
	}
*/
	/**
	 * 导入单频监测数据
	 * @param path    文件路径
	 * @return  成功返回  dpjcbh   不成功返回 null
	 * @throws SQLException 
	 */
	/*
	private  String ImportDpjcFile(String path) {

		boolean flag = false;
		String md5="待处理";
		String bh;
		double jcpl=myEmcParser.UnitToMHz(myEmcParser.pl1,myEmcParser.getDataHeadValueByName("频率"));
		int n=path.lastIndexOf('\\');
		String filename=path.substring(n+1, path.length()-4);
		bh=myEmcParser.GetNumber(1);
		String yssjbh="YS"+bh;
		String sql="insert into jc_yssj_dpjc(yssjbh, dpjcbh, jd_jcdd, wd_jcdd, smsj_qs, smsj_zz, jcpl,MD5, dpcq)values('"+yssjbh+"','"+bh+"',"+fileMsg.jd+","+fileMsg.wd+",to_timestamp('"+fileMsg.startTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'),to_timestamp('"+fileMsg.endTime+"', 'yyyy-mm-dd HH24:MI:SS.ff'),"+jcpl+",'"+md5+"',empty_blob())";
		String sql2="select  dpcq FROM jc_yssj_dpjc WHERE  yssjbh ='"+yssjbh+"'";
		flag=EMCDB.InsertBlob(sql, sql2, path,true);
		if(flag)
			return bh;
		return null;

	}

	//根据接收机名称，天线名称，监测台站编号判断
	//1. 根据接收机名称，监测台站编号找接收机编号,如果没有找到接收机，直接返回false
	//2. 在找到接收机编号 的前提下，找天线编号 ，如果没有找到天线，直接返回false
	//3. 在找到接收机，天线的前提下， 找监测系统。
	Boolean GetJCSBLJ(String jsjmc, String txmc, String jczbh)	{
		
		String sql;
		sql="select jsjbh from jc_jcz_jcxt_jsjzxx where jsjmc='"+jsjmc+"' and jczbh='"+jczbh+"'";//jc_jcz_jcxt_jsjzxx接收机主机信息
		ArrayList<String> jsjbh=new ArrayList<String>();
		System.out.println("get jsjbh(receiver no) from jsjzxx according to jsjmc and jczbh :" + sql);
		try {
			EMCDB.rs = EMCDB.stm.executeQuery(sql);

			while(EMCDB.rs.next()){
				String  strjsjbh=EMCDB.rs.getString(1);
				jsjbh.add(strjsjbh);
			}
			if(jsjbh.size()==0){
				EMCDB.rs.close();
				EMCDB.conn.conn.commit();
				return false;
			}
			EMCDB.rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int i=0;
		ArrayList<String> txbh=new ArrayList<String>();
		try {
			sql="select txbh from jc_jcz_jcxt_txzxx where txmc='"+txmc+"' and jczbh='"+jczbh+"'";
			System.out.println("get txbh（antenna ) from txzxx according to txmc, jczbh: "+sql);
			EMCDB.rs = EMCDB.stm.executeQuery(sql);

			while(EMCDB.rs.next())			{
				String  strtxbh=EMCDB.rs.getString(1);
				txbh.add(strtxbh);
			}
			if(txbh.size()==0){
				EMCDB.rs.close();	
				return false;
			}

			EMCDB.rs.close();

			String jcsblj;
			for (i=0;i<jsjbh.size();i++){
				for(int j=0;j<txbh.size();j++){
					jcsblj=jsjbh.get(i)+"-"+txbh.get(j);
					sql="select jcxtbh from jc_jcz_jcxt_sblj where jcsblj='"+jcsblj+"' and jczbh='"+jczbh+"'";
					System.out.println("get jcxtbh（system no ) from sblj according to jcsblj, jczbh: "+sql);
					EMCDB.rs = EMCDB.stm.executeQuery(sql);
					while(EMCDB.rs.next()){
						String strjcxtbh=EMCDB.rs.getString(1);
						fileMsg.jcxtbh=strjcxtbh;
						fileMsg.jcsblj=jcsblj;
						i=jsjbh.size()+1;
						j=txbh.size()+1;
					}
					EMCDB.rs.close();
				}
			}
			EMCDB.conn.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (i<=jsjbh.size()){
			return false;
		}
		return true;
	}
	*/
	
	/**
	 * 获取web-inf下指定文件的路径和文件名
	 * @param fileName web-inf下的文件名名称
	 * @return web-inf目录下fileName文件的完整路径和文件名
	 */
	/*
	 public  String getXmlPath(String fileName)    {  
	 
        //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/   
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString();  
        path=path.replace('/', '\\'); // 将/换成\  
        path=path.replace("file:", ""); //去掉file:  
        path=path.replace("classes\\", ""); //去掉class\  
        path=path.substring(1); //去掉第一个\,如 \D:\JavaWeb...  
        path+=fileName;  
        System.out.println("import file.java：file path = " +path);  
        return path;  
    }  
	*/
	
	
	
}
