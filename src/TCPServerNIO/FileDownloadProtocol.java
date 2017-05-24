package TCPServerNIO;
import java.util.ArrayList;

import org.luan.LUANObject;



public class FileDownloadProtocol implements Runnable{
	private CommunicationProtocolTcpNIO com = null;
	private static ArrayList <DownloadRequest> downloadingRequestList = new ArrayList <DownloadRequest>();

	
	public FileDownloadProtocol(CommunicationProtocolTcpNIO com) {
		this.com=com;
	}

	
	@SuppressWarnings("static-access")
	public void run(){
		System.out.println("FileDownloadProtocol has STARTED!");
		while(true){
			try{
				Thread.currentThread().sleep(200);
				checkIfNewDownloadRequestHasBeenAdded();
			}catch(Exception ex){
				ex.printStackTrace();
				//System.exit(0);
			}
		}
	}
	
	/********************************************************************************************/
	/*************************************FileDownloadProtocol Routine***************************/
	/********************************************************************************************/
	
	private void checkIfNewDownloadRequestHasBeenAdded(){
		if(downloadingRequestList.size()>0){
			DownloadRequest downloadRequest=downloadingRequestList.get(0);
			byte[] original=downloadRequest.getOriginalByteArray();
			//ArrayList <byte[]> byteArrays=dynamicDivideFileByteArrayToOptimalSizes(original);
			ArrayList <byte[]> byteArrays=standard500kbSegmentation(original);
			sendDownloadRequestFromClient(byteArrays, downloadRequest);
		}	
	}
	
	private void sendDownloadRequestFromClient(ArrayList <byte[]> byteArrList,DownloadRequest downloadRequest ){
		try{
			com.sendLUANObject(getStartDownloadJsonInfo(byteArrList.size(), downloadRequest));		
			Thread.sleep(10);
			for(int i=0;i<byteArrList.size();i++){
				com.sendFile(byteArrList.get(i),downloadRequest.getSocketChannel());
				Thread.sleep(5);
			}
			Thread.sleep(10);
			com.sendLUANObject(getEndDownloadJsonInfo(byteArrList.size(), downloadRequest));
			byteArrList.remove(0);
			downloadingRequestList.remove(0);	
			System.out.println("Sending From Server - Completed Successfully");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
	

	/********************************************************************************************/
	/*************************************ByteArray Operation************************************/
	/********************************************************************************************/
	
	
	private ArrayList <byte[]> standard500kbSegmentation(byte[] original){
		ArrayList <byte[]> byteArrays = new ArrayList <byte[]>();
		if(original.length>500000){
			try{
				int orginalSize=original.length;
				int index=0;
				int transportSize=500000;
				//int i=0;
				byte[] transportByte=null;
				while(index<original.length){
					if(orginalSize>transportSize){
						transportByte=new byte[transportSize];
						orginalSize=orginalSize-transportSize;
					}else{
						transportByte=new byte[orginalSize];
					}
					for(int q=0;q<transportByte.length && index<original.length;q++){
						transportByte[q]=original[index];
						index++;
					}
					byteArrays.add(transportByte);
					//System.out.println("byteArrays.add("+i+","+transportByte.length+");");
					//i++;
				}
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
		}else{
			byteArrays.add(original);
		}
		return byteArrays;
	}
	
	private LUANObject getStartDownloadJsonInfo(int byteArrSize,DownloadRequest dReq){
		LUANObject luan = new LUANObject();
		try{
			luan.put("headCmd","File Download START");
			luan.put("operationID",dReq.getOperationID());
			luan.put("totaltAmountOfPackages",byteArrSize);
			luan.put("segmentTransferSize",500000 );
			luan.put("originalLength",dReq.getOriginalByteArray().length);
			luan.put("fileName",dReq.getFileName() );
			luan.put("fileType", dReq.getFileType());
			System.out.println("\n\n\n-----------------------------------------------------");
			System.out.println("headCmd == "+luan.getString("headCmd"));
			System.out.println("originalLength == "+dReq.getOriginalByteArray());
			System.out.println("byteArrays.size() == "+byteArrSize);
			System.out.println("segmentTransferSize == "+500000);
			System.out.println("fileName == "+dReq.getFileName());
			System.out.println("fileType == "+dReq.getFileType());
			System.out.println("-----------------------------------------------------\n\n\n");
			return luan;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	private LUANObject getEndDownloadJsonInfo(int byteArrSize,DownloadRequest dReq){
		LUANObject luan = new LUANObject();
		try{
			luan.put("headCmd","File Download END");
			luan.put("operationID",dReq.getOperationID());
			luan.put("totaltAmountOfPackages",byteArrSize);
			luan.put("segmentTransferSize",500000 );
			luan.put("originalLength",dReq.getOriginalByteArray().length);
			luan.put("fileName",dReq.getFileName());
			luan.put("fileType", dReq.getFileType());
			System.out.println("\n\n\n-----------------------------------------------------");
			System.out.println("headCmd == "+luan.getString("headCmd"));
			System.out.println("originalLength == "+dReq.getOriginalByteArray().length);
			System.out.println("byteArrays.size() == "+byteArrSize);
			System.out.println("segmentTransferSize == "+500000);
			System.out.println("fileName == "+dReq.getFileName());
			System.out.println("fileType == "+dReq.getFileType());
			System.out.println("-----------------------------------------------------\n\n\n");
			return luan;
		}catch(Exception ex){
			ex.printStackTrace();
		}return null;
	}
	
	/********************************************************************************************/
	/**********************************Outside Thread Operation**********************************/
	/********************************************************************************************/
	
	public void addFileTransferToQueue (DownloadRequest downloadRequest){		
		downloadingRequestList.add(downloadRequest);
	}
}
