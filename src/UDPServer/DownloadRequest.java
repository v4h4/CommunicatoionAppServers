package UDPServer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class DownloadRequest {
	private String fileName=null;
	private String fileType=null;
	private String operationID=null;
	private int idCounter=10210;
	private byte[] originalByteArray=null;
	private Random rand=null;
	public DownloadRequest(String fileName,int idCounter){
		this.rand = new Random();
		seperateFileNameAndFileType(fileName);
		this.idCounter=idCounter;
		this.operationID=createOperationID(fileName,fileType);
		//this.originalByteArray=convertFileToByteArray_NIO(fileName);
		this.originalByteArray=convertFileToByteArray_NIO(fileName);
		System.out.println("originalByteArray.length == "+originalByteArray.length);
	}
	
	public String getOperationID(){
		return this.operationID;
	}
	
	public byte[] getOriginalByteArray(){
		return this.originalByteArray;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public String getFileType(){
		return this.fileType;
	}
	private String createOperationID(String fileName,String fileType){
		String operationID=null;
		try{
			int randNbr=rand.nextInt(10000000)+100000;
			operationID=idCounter+++fileName+randNbr+fileType+idCounter+++randNbr+fileName+idCounter++;
			idCounter++;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return operationID;
	}
	
	private void seperateFileNameAndFileType(String fileName){
		int length=0;
		for(int i=fileName.length()-1;i>=0;i--){
			if(fileName.charAt(i)!='.'){
				length++;
			}else{
				length++;
				break;
			}
		}
		char[] string = new char[length];  
		int index=(length-1);
		for(int i=(fileName.length()-1);i>=0 && index>=0;i--){
				string[(index)]=fileName.charAt(i);
				index--;
		}
		this.fileType= new String(string);
		this.fileName=fileName.substring(0,(fileName.length()-length));
	}
	
	@SuppressWarnings("resource")
	private byte[] convertFileToByteArray_NIO(String fileName){
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
	
	/*private byte[] convertFileToByteArray_IO(String fileName){
		FileInputStream fileInputStream=null;
        File file = new File("Blobs\\"+fileName);
        byte[] byteFile = new byte[(int) file.length()];
		try {
			fileInputStream = new FileInputStream(file);
		    fileInputStream.read(byteFile);
		    fileInputStream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
		return byteFile;
	}*/
}
