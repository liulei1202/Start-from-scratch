package DBConnection;
 
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
  
public class AntZipUtil {  
    /**
     * 将源文件压缩成zip文件
     * @param zipFileName zip文件路径名+文件名
     * @param inputFileName 原文件路径名+文件名
     * @throws Exception
     */
    public static void zipFile(String zipFileName, String inputFileName)  
            throws Exception {  
        org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(  
                new FileOutputStream(zipFileName));  
        out.setEncoding("GBK");  
        File inputFile = new File(inputFileName);  
        zipIt(out, inputFile, "", true);  
        out.close();  
    }  
      
    /* 
     * 能支持中文的压缩 。参数base 开始为"" first 开始为true 
     */  
    public static void zipIt(org.apache.tools.zip.ZipOutputStream out, File f,String base, boolean first) throws Exception {  
        if (f.isDirectory()) {  
            File[] fl = f.listFiles();  
            if (first) {  
                first = false;  
            } else {  
                base = base + "/";  
            }  
            for (int i = 0; i < fl.length; i++) {  
                zipIt(out, fl[i], base + fl[i].getName(), first);  
            }  
        } else {  
            if (first) {  
                base = f.getName();  
            }  
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));  
            FileInputStream in = new FileInputStream(f);  
            
            int length=-1;
            int i=0;
            byte[] buf = new byte[1024*64];/*一次压缩64k*/
            while ((length = in.read(buf)) != -1) {  
                out.write(buf,0,length);
            }  
            out.flush();
            in.close();
        }  
    }  
    /**
     * 将zip文件解压到unzipPath路径下
     * @param unZipFileName zip文件的文件路径+文件名
     * @param unZipPath 解压路径
     * @throws Exception
     */
    public static String unZipFile(String unZipFileName, String unZipPath)  
            throws Exception {  
        org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(  
                unZipFileName);
        String fileName = null;
        fileName = unZipFileByOpache(zipFile, unZipPath);  
        zipFile.close();
        return fileName;
    }  
      
    /* 
     * 解压文件 unZip为解压路径 
     */  
    public static String unZipFileByOpache(org.apache.tools.zip.ZipFile zipFile,  
            String unZipRoot) throws Exception, IOException {  
        java.util.Enumeration e = zipFile.getEntries();  
        org.apache.tools.zip.ZipEntry zipEntry;
        String fileName = null;
        while (e.hasMoreElements()) {  
            zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();  
            InputStream fis = zipFile.getInputStream(zipEntry);  
            if (zipEntry.isDirectory()) {  
            } else {
            	fileName = zipEntry.getName();
            	//System.out.println(fileName);
                File file = new File(unZipRoot //+ File.separator  
                        + zipEntry.getName());  
                File parentFile = file.getParentFile();  
                parentFile.mkdirs();  
                FileOutputStream fos = new FileOutputStream(file);  
                byte[] b = new byte[1024];  
                int len;  
                while ((len = fis.read(b, 0, b.length)) != -1) {  
                    fos.write(b, 0, len);  
                }  
                fos.close();  
                fis.close();  
            }  
        }
        return fileName;
    }  
  
  
}  