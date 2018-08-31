
package CSVReader;

import java.io.BufferedReader;
import java.io.IOException;




public class PIPE implements Runnable {	
		
		private ShareSpace  space;      //用于存贮读入的数据//
		private BufferedReader inFile;
		private String lineBuffer;
		
		
		public boolean dataOver;
		
		
		public PIPE(ShareSpace  space,  BufferedReader inFile){
			
			this.space = space;
			this.inFile = inFile;
			dataOver = false;
		}
		
		
		public void run() {
			
			int i;	
			while ( !dataOver ) {
				
				if(space.isFull()){
					Thread.yield();
				}
				else if(CSVInput.m_FileOpenFlag==false){
					try {
						if(inFile!=null)
						    inFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}else {
					int len = space.getEmptyLength();
					System.out.println("queue is loading " + len + " items.");
					try {
						for(i = 0; i < len && inFile.ready(); i++){
							lineBuffer = inFile.readLine();
							space.inSpace(lineBuffer);
						}
						if(i < len){
							dataOver = true;
							space.dataOver = true;
							inFile.close();
						}
					} catch( IOException e) {
						System.out.println("CSV Data load failed!");
					} 
				}
			}
			
		}

	}
