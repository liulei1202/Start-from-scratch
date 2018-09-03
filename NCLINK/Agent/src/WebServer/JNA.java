package WebServer;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//import net.sf.json.JSONObject;
//import org.json.*;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.lang.String;
public class JNA {
	
	
	
	public interface CLibrary extends Library {
		
		// int  QUERY(char *request, char* buff, int *len)
		CLibrary[] Instance = {
				(CLibrary) Native.loadLibrary(MachineInfo.dllFileName[0],CLibrary.class),
				(CLibrary) Native.loadLibrary(MachineInfo.dllFileName[1],CLibrary.class),
				(CLibrary) Native.loadLibrary(MachineInfo.dllFileName[2],CLibrary.class),
				(CLibrary) Native.loadLibrary(MachineInfo.dllFileName[3],CLibrary.class),
				(CLibrary) Native.loadLibrary(MachineInfo.dllFileName[4],CLibrary.class)
				};
		int PROBE(String request, Pointer buff, IntByReference len);
		int QUERY(String request, Pointer buff, IntByReference len);
		int SET(String request, Pointer buff, IntByReference len);
		int SAMPLES(String request, Pointer buff, IntByReference len);
	}
	
	public JNA(){
		
	}
}