package ExcelUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	//默认单元格内容为数字时格式
	private static DecimalFormat df = new DecimalFormat("0");
	// 默认单元格格式化日期字符串 
	private static SimpleDateFormat sdf = new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss"); 
	// 格式化数字
	private static DecimalFormat nf = new DecimalFormat("0.00");  
	
	public static LinkedList<LinkedList<String>> readExcel(File file){
		if(file == null){
			return null;
		}
		if(file.getName().endsWith("xlsx")){
			//处理ecxel2007
			return readExcel2007(file);
		}else{
			//处理ecxel2003
			return readExcel2003(file);
		}
	}
	/*
	 * @return 将返回结果存储在LinkedList内，存储结构与二位数组类似
	 */
	public static LinkedList<LinkedList<String>> readExcel2003(File file){
		try{
			LinkedList<LinkedList<String>> rowList = new LinkedList<LinkedList<String>>();
			LinkedList<String> colList;
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet;
			HSSFRow row;
			HSSFCell cell;
			String value;
			int firstCellNum;
			int lastCellNum;
			int sheetSize = wb.getNumberOfSheets();
			boolean hasData = false;//每一行是否有数据。
			System.out.println("sheetSize"+sheetSize);
			for(int k=0;k<sheetSize;k++){
				System.out.println("sheet:"+ k);
				sheet = wb.getSheetAt(k);
				//if(sheet == null){//空sheet,不能这么写，计算量十分巨大?
					//continue;
				//}
				//System.out.println("getSheetAt"+k);
				if(sheet.getPhysicalNumberOfRows() == 0){
					System.out.println("空sheet");
					continue;
				}
				firstCellNum = sheet.getRow(sheet.getFirstRowNum()).getFirstCellNum();//第一个有用行的第一个单元的列数
				lastCellNum  = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();//第一个有用行的最后一个单元的列数
				System.out.println("当前sheet数据量"+sheet.getPhysicalNumberOfRows());
				for(int i = sheet.getFirstRowNum()+1 , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows()-1 ; i++ ){//第一行的标题不写入，所以最后有用行数少1行
					
					row = sheet.getRow(i);
					if(row == null){//行为空行
						continue;
					}else if(row.getFirstCellNum() == row.getLastCellNum()){//行只有一个值，则省略。做一个剔除工作
						rowCount++;
						continue;
					}
					else{
						rowCount++;
					}
					hasData = false;
					colList = new LinkedList<String>();
					for( int j = firstCellNum; j <= lastCellNum;j++){
						String merged = getMergedRegionValueHSSF(sheet,i,j);
						if(merged == null){//不是合并单元格
							cell = row.getCell(j);
							if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){//是不是空格
								colList.add("");
								continue;
							}else{
								hasData = true;
								value = cell.toString();
							}
						}else{//是合并单元格
							hasData = true;
							value = merged;
						}
						colList.add(value);
					}//end for j
					if(hasData){
						//System.out.println(colList); 
						rowList.add(colList);
					}
				}//end for i
			}//end for k
			//file.delete();
			return rowList;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("readExcel2003 exception");
			return null;
		}
	}
	
	public static LinkedList<LinkedList<String>> readExcel2007(File file){
		try{
			LinkedList<LinkedList<String>> rowList = new LinkedList<LinkedList<String>>();
			LinkedList<String> colList;
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet;
			XSSFRow row;
			XSSFCell cell;
			String value;
			int firstCellNum;
			int lastCellNum;
			int sheetSize = wb.getNumberOfSheets();
			boolean hasData = false;//每一行是否有数据。
			System.out.println("sheetSize:"+sheetSize);
			for(int k=0;k<sheetSize;k++){
				System.out.println("sheet:"+ k);
				sheet = wb.getSheetAt(k);
				//if(sheet == null){//空sheet,不能这么写，计算量十分巨大?
					//continue;
				//}
				if(sheet.getPhysicalNumberOfRows() == 0){
					System.out.println("空sheet");
					continue;
				}
				firstCellNum = sheet.getRow(sheet.getFirstRowNum()).getFirstCellNum();//第一个有用行的第一个单元的列数
				lastCellNum  = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();//第一个有用行的最后一个单元的列数
				System.out.println("PhysicalNumberOfRows:"+sheet.getPhysicalNumberOfRows());
				for(int i = sheet.getFirstRowNum()+1 , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows()-1 ; i++ ){//第一行的标题不写入，所以最后有用行数少1行
					
					row = sheet.getRow(i);
					if(row == null){//行为空行
						continue;
					}else if(row.getFirstCellNum() == row.getLastCellNum()){//行只有一个值，则省略。做一个剔除工作
						rowCount++;
						continue;
					}
					else{
						rowCount++;
					}
					hasData = false;
					colList = new LinkedList<String>();
					for( int j = firstCellNum; j <= lastCellNum;j++){
						String merged = getMergedRegionValueXSSF(sheet,i,j);
						if(merged == null){//不是合并单元格
							cell = row.getCell(j);
							if(cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK){//是不是空格
								colList.add("");
								continue;
							}else{
								hasData = true;
								value = cell.toString();
							}
						}else{//是合并单元格
							hasData = true;
							value = merged;
						}
						colList.add(value);
					}//end for j
					if(hasData){
						//System.out.println(colList); 
						rowList.add(colList);
					}
				}//end for i
			}//end for k
			//file.delete();
			return rowList;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("readExcel2003 exception");
			return null;
		}
	}
	
	
	public static LinkedList<LinkedList<String>> readExcel2007Old(File file){
		try{
			LinkedList<LinkedList<String>> rowList = new LinkedList<LinkedList<String>>();
			LinkedList<String> colList;
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;
			String value;
			int firstCellNum = sheet.getRow(sheet.getFirstRowNum()).getFirstCellNum();//第一个有用行的第一个单元的列数
			int lastCellNum  = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();//第一个有用行的最后一个单元的列数
			for(int i = sheet.getFirstRowNum() , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
				row = sheet.getRow(i);
				colList = new LinkedList<String>();
				if(row == null){
					//当读取行为空时
					/*
					if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
						rowList.add(colList);
					}*/
					continue;
				}else{
					rowCount++;
				}
				for( int j = firstCellNum; j <= lastCellNum;j++){
					cell = row.getCell(j);
					
					if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
						//当该单元格为空
						/*
						if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
							colList.add("");
						}*/
						colList.add("");
						continue;
					}
					value = cell.toString();
					/*
					switch(cell.getCellType()){
					 case XSSFCell.CELL_TYPE_STRING:  
		                    //System.out.println(i + "行" + j + " 列 is String type");  
		                    value = cell.getStringCellValue();  
		                    break;  
		                case XSSFCell.CELL_TYPE_NUMERIC:  
		                    if ("@".equals(cell.getCellStyle().getDataFormatString())) {  
		                        value = df.format(cell.getNumericCellValue());  
		                    } else if ("General".equals(cell.getCellStyle()  
		                            .getDataFormatString())) {  
		                        value = nf.format(cell.getNumericCellValue());  
		                    } else {  
		                        value = sdf.format(HSSFDateUtil.getJavaDate(cell  
		                                .getNumericCellValue()));  
		                    }  
		                    //System.out.println(i + "行" + j + " 列 is Number type ; DateFormt:" + value.toString()); 
		                    break;  
		                case XSSFCell.CELL_TYPE_BOOLEAN:  
		                    //System.out.println(i + "行" + j + " 列 is Boolean type");  
		                    value = Boolean.valueOf(cell.getBooleanCellValue());
		                    break;  
		                case XSSFCell.CELL_TYPE_BLANK:  
		                   //System.out.println(i + "行" + j + " 列 is Blank type");  
		                    value = "";  
		                    break;  
		                default:  
		                    //System.out.println(i + "行" + j + " 列 is default type");  
		                    value = cell.toString();  
					}// end switch*/
					colList.add(value);
				}//end for j
				rowList.add(colList);
			}//end for i
			//file.delete();
			return rowList;
		}catch(Exception e){
			System.out.println("readExcel2007 exception");
			return null;
		}
	}
	
	public static boolean writeExcel(LinkedList<LinkedList<String>> result,String path){
		if(result == null){
			return false;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		for(int i = 0 ;i < result.size() ; i++){
			 HSSFRow row = sheet.createRow(i);
			if(result.get(i) != null){
				for(int j = 0; j < result.get(i).size() ; j ++){
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(result.get(i).get(j));
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            wb.write(os);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        byte[] content = os.toByteArray();
        File file = new File(path);//Excel文件生成后存储的位置。
        OutputStream fos  = null;
        try
        {
            fos = new FileOutputStream(file);
            fos.write(content);
            os.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }         
        return true;
	}
	/**
     * 获取合并单元格的值，2003版xls
     * 
     * @param sheet
     * @param row
     * @param column
     * @return 不是合并单元格，返回null，是返回合并单元格的值
     */
    public static String getMergedRegionValueHSSF(HSSFSheet sheet, int row, int column) {
    	
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    HSSFRow fRow = sheet.getRow(firstRow);
                    HSSFCell fCell = fRow.getCell(firstColumn);
                    if(fCell == null || fCell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                    	return "";
                    }
                    else{
                    	return fCell.toString();
                    }
                }
            }
        }

        return null;
    }
	
	/**
     * 获取合并单元格的值，2007版xlsx
     * 
     * @param sheet
     * @param row
     * @param column
     * @return 不是合并单元格，返回null，是返回合并单元格的值
     */
    public static String getMergedRegionValueXSSF(XSSFSheet sheet, int row, int column) {
    	
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    XSSFRow fRow = sheet.getRow(firstRow);
                    XSSFCell fCell = fRow.getCell(firstColumn);
                    if(fCell == null || fCell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                    	return "";
                    }
                    else{
                    	return fCell.toString();
                    }
                }
            }
        }

        return null;
    }
	
	public static DecimalFormat getDf() {
		return df;
	}
	public static void setDf(DecimalFormat df) {
		ExcelUtil.df = df;
	}
	public static SimpleDateFormat getSdf() {
		return sdf;
	}
	public static void setSdf(SimpleDateFormat sdf) {
		ExcelUtil.sdf = sdf;
	}
	public static DecimalFormat getNf() {
		return nf;
	}
	public static void setNf(DecimalFormat nf) {
		ExcelUtil.nf = nf;
	}
	
	/*
	switch(cell.getCellType()){
	 case XSSFCell.CELL_TYPE_STRING:  
            //System.out.println(i + "行" + j + " 列 is String type");  
            value = cell.getStringCellValue();  
            break;  
        case XSSFCell.CELL_TYPE_NUMERIC:  
            if ("@".equals(cell.getCellStyle().getDataFormatString())) {  
                value = df.format(cell.getNumericCellValue());  
            } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {  
                value = nf.format(cell.getNumericCellValue());  
            } else {  
                value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));  
            }  
            //System.out.println(i + "行" + j  + " 列 is Number type ; DateFormt:" + value.toString()); 
            break;  
        case XSSFCell.CELL_TYPE_BOOLEAN:  
            //System.out.println(i + "行" + j + " 列 is Boolean type");  
            value = Boolean.valueOf(cell.getBooleanCellValue());
            break;  
        case XSSFCell.CELL_TYPE_BLANK:  
            //System.out.println(i + "行" + j + " 列 is Blank type");  
            value = "";  
            break;  
        default:  
            //System.out.println(i + "行" + j + " 列 is default type");  
            value = cell.toString();  
	}// end switch*/	
	
}
