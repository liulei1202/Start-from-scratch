package web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFile {

	
	public String ReadFiletoString(String Path){  
        BufferedReader reader = null;  
        String laststr = "";  
        try{  
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(Path), "UTF-8"));  
            String tempString = null;  
            while((tempString = reader.readLine()) != null){  
                laststr += tempString;  
            }  
            reader.close();  
        }catch(IOException e){  
            e.printStackTrace();
            return null;
        }finally{  
            if(reader != null){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    return null;
                }  
            }  
        }  
        return laststr;  
    }  
}
