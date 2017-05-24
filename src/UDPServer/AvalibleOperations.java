package UDPServer;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.luan.LUANObject;

public class AvalibleOperations {
		
	public String getCurrentDate(){
		try{
			DateFormat dt =new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			return dt.format(date);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "error";
	}
	
	public String getCurrentTime(){
		try{
			DateFormat dt =new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			return dt.format(date);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "error";
	}
	
	public int getSumOfList(int[] sumArray){
		int sumOfList=0;
		try{
			for(int i=1;i<sumArray.length;i++){
				sumOfList=sumOfList+sumArray[i];
			}
			System.out.println("\n\nreturn sumOfList; == ("+sumOfList+")");
			return sumOfList;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return -1010;
	}
	
	public int getMaxOfList(int[] maxArray){
		int maxOfList=0;
		try{
			for(int i=1;i<maxArray.length;i++){
				if(maxOfList<maxArray[i]){
					maxOfList=maxArray[i];
				}
			}
			return maxOfList;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return -1010;
	}
	
	public double getC2F(double celsius_temperature){//°C to °F	Multiply by 9, then divide by 5, then add 32
		return (((celsius_temperature*9)/5)+32);
	}
		
	public double getF2C(double fahrenheit_temperature){//°F to °C	Deduct 32, then multiply by 5, then divide by 9
		return (((fahrenheit_temperature-32)*5)/9);
	}
	
	public File getFileFromServerStorage(String fileName){
		File file=null;
		try{
			file=new File(fileName);
		}catch(Exception ex){
			
		}
		return file;
	}
	
	@SuppressWarnings("resource")
	public byte[] getFileFromNioBlobStorage(String fileName){
		byte[] byteFile=null; 
		FileChannel fileChannel;
		try {
			fileChannel = new FileInputStream(fileName).getChannel();
		    ByteBuffer buff = ByteBuffer.allocate((int)fileChannel.size());
		    fileChannel.read(buff);
		    byteFile=buff.array();
		    fileChannel.close();
		}catch(Exception ex){
			byteFile=null; 
			ex.printStackTrace();
        }
		return byteFile;
	}
	
	public LUANObject startUpGetAllFilesFromBlob(){
		LUANObject luan = new LUANObject();
		try{
			ArrayList<String> results = new ArrayList<String>();
			File[] files = new File("Blobs").listFiles();
			for (File file : files) {
			    if (file.isFile()) {
			        results.add(file.getName());
			    }
			}
			luan.put("headCmd", "Start-up Avalible Files");
			luan.put("Avalible Files", results.toArray(new String[results.size()]));			
		}catch(Exception ex){
			ex.printStackTrace();
			luan=null;
		}
		return luan;
	}
}
