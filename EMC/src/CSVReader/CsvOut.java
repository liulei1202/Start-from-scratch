package CSVReader;

import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvWriter;

public class CsvOut {
	public static void main(String[] args){
		
		CsvWriter csvWriter = new CsvWriter("D:\\a.csv",',',Charset.forName("GBK"));
		
		String[] headers = {"编号","姓名","年龄"};
		System.out.println(headers[0]);
		String[] content = {"123456","张山","34"};
		try {
			csvWriter.writeRecord(headers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		try {
			csvWriter.writeRecord(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		csvWriter.close();
		
		
		
	}
}
