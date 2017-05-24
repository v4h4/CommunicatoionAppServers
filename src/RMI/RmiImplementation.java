package RMI;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;  
import java.rmi.server.UnicastRemoteObject;  
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
  
public class RmiImplementation extends UnicastRemoteObject implements RmiInterface {  
	private static final long serialVersionUID = 1L;

	public RmiImplementation() throws RemoteException {  
        super();  
    }  
	
    @Override  
    public int getSumOfList(int[] sumList){
    	System.out.println("Client calculated the Sum of List");
    	int sumOfList=0;
		try{
			System.out.println("\n\nsumList.size() == "+sumList.length+"\n\n");
			for(int i=1;i<sumList.length;i++){
				sumOfList=sumOfList+sumList[i];
			}
			return sumOfList;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return -43211234;
    }
    
    @Override  
    public int getMaxOfList(int[] maxList){
    	System.out.println("Client calculated the Maxium of List");
    	int maxOfList=0;
		try{
			for(int i=1;i<maxList.length;i++){
				if(maxOfList<maxList[i]){
					maxOfList=maxList[i];
				}
			}
			return maxOfList;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 43211234;
    }
    @Override  
    public double celsiusToFahrenheit(double celcius){
    	System.out.println("Client converted Celsius to Fahrenheit "); 
    	return ((((celcius)*9)/5)+32);
    }
    @Override  
    public double fahrenheitToCelsius(double fahrenheit){
    	System.out.println("Client converted Fahrenheit to Celsius ");
    	return ((((fahrenheit)-32)*5)/9);
    }
    @Override  
    public String getCurrentDate(){
    	return getCurrentGMTDate();
    }
    @Override  
    public String getCurrentTime(){
    	return getCurrentGMTTime();
    }
    @SuppressWarnings("resource")
	@Override 
    public byte[] downloadFile(String fileName){
    	System.out.println("Client requested to download file "+fileName);
    	byte[] byteFile=null; 
		FileChannel fileChannel;
		try {
			fileChannel = new FileInputStream("Blobs\\"+fileName).getChannel();
		    ByteBuffer buff = ByteBuffer.allocate((int)fileChannel.size());
		    fileChannel.read(buff);
		    byteFile=buff.array();
		    fileChannel.close();
		}catch(Exception ex){
            ex.printStackTrace();
        }
		return byteFile;
	}
    
    public String getCurrentGMTDate(){
    	System.out.println("Client checked the current Date ");
		try{
			DateFormat dt =new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			return dt.format(date);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "error";
	}
	
	public String getCurrentGMTTime(){
		System.out.println("Client checked the current Time");
		try{
			DateFormat dt =new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			return dt.format(date);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "error";
	}
	
	public String[] getAllFilesFromBlob(){
		try{
			ArrayList<String> results = new ArrayList<String>();
			File[] files = new File("Blobs").listFiles();
			for (File file : files) {
			    if (file.isFile()) {
			        results.add(file.getName());
			    }
			}
			return  results.toArray(new String[results.size()]);		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}  