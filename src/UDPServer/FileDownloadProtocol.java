package UDPServer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.luan.LUANObject;



public class FileDownloadProtocol implements Runnable{
	private UdpCommunicationProtcol com = null;
	private static ConcurrentHashMap<String, ArrayList <LUANObject>> luanListMap = new ConcurrentHashMap<String, ArrayList <LUANObject>>();
	private static ConcurrentHashMap<String,DownloadRequest> downloadingRequestLMap = new ConcurrentHashMap<String,DownloadRequest>();
	
	public FileDownloadProtocol(UdpCommunicationProtcol com) {
		this.com=com;
	}

	
	@SuppressWarnings("static-access")
	public void run(){
		System.out.println("FileDownloadProtocol has STARTED!");
		while(true){
			try{
				sendPackagesIfTheyExist();
				Thread.currentThread().sleep(2000);
				checkIfNewFileHasBeenAdded();
			}catch(Exception ex){
				ex.printStackTrace();
				//System.exit(0);
			}
		}
	}
	
	/********************************************************************************************/
	/*************************************FileDownloadProtocol Routine***************************/
	/********************************************************************************************/
	
	private void sendPackagesIfTheyExist(){
		try{
			for(String key:luanListMap.keySet()){
				if(luanListMap.size()>0 && luanListMap.get(key).size()>0 && downloadingRequestLMap.size()>0){
					ArrayList <LUANObject> luanList =luanListMap.get(key);
					if(luanList.size()>luanList.get(0).getInteger("totaltAmountOfPackages")){
						for(int i=0;i<luanList.size();i++){
							if(luanList.get(i).getInteger("packageNbr")==i){
								com.sendDatagramLUANObject(luanList.get(i));
								Thread.sleep(100);
							
							}
						}
						luanListMap.remove(key);
						downloadingRequestLMap.remove(key);
				//		System.out.println("luanListMap.remove("+key+") == "+luanListMap.remove(key));
				//		System.out.println("downloadingRequestLMap.remove("+key+") == "+downloadingRequestLMap.remove(key));
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
	private void checkIfNewFileHasBeenAdded(){
		for(String key: downloadingRequestLMap.keySet()){
			divideFileBytesAndStoreThemInFilePackages(downloadingRequestLMap.get(key));
		}		
	}
	
	
	private void divideFileBytesAndStoreThemInFilePackages(DownloadRequest down){
		//byte[] original=down.getOriginalByteArray();
		//ArrayList <byte[]> byteArrays=dynamicDivideFileByteArrayToOptimalSizes(original);
		ArrayList <byte[]> byteArrays=standardUDP60kbSegmentation(down.getOriginalByteArray());
		createLUANObjectPackage(down,byteArrays);
	}
	
	private void createLUANObjectPackage(DownloadRequest down,ArrayList <byte[]> byteArrays){
		String headCmd="File Download";
		try{
			for(int i=0;i<byteArrays.size();i++){
				if(i==0){
					headCmd="File Download START";
				}
				else if(i==(byteArrays.size()-1)){
					headCmd="File Download END";
				}else{
					headCmd="File Download";
				}
				LUANObject luan = new LUANObject();
				luan.put("headCmd", headCmd);
				luan.put("operationID", down.getOperationID());
				luan.put("originalLength", down.getOriginalByteArray().length);
				luan.put("totaltAmountOfPackages",byteArrays.size());
				luan.put("segmentTransferSize", byteArrays.get(i).length/*segmentTransferSize*/);
				luan.put("fileName",down.getFileName());
				luan.put("fileType",down.getFileType());
				luan.put("byteArray", byteArrays.get(i));
				luan.put("packageNbr", i);
				addToLuanListMap(luan);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	//	System.exit(0);
	}
	
	private void addToLuanListMap(LUANObject luan){
		try{
			if(luanListMap.containsKey(luan.getString("operationID"))){
				luanListMap.get(luan.getString("operationID")).add(luan);
				/*ArrayList <LUANObject> luanList = luanListMap.get(luan.getString("operationID"));
				for(int i=0;i<luanList.size();i++){
					printLuanObjects(luanList.get(i));
				}*/
			}else{
				ArrayList <LUANObject> luanList = new ArrayList<LUANObject>();
				luanList.add(luan);
				luanListMap.put(luan.getString("operationID"), luanList);
				/*luanList = luanListMap.get(luan.getString("operationID"));
				for(int i=0;i<luanList.size();i++){
					printLuanObjects(luanList.get(i));
				}*/
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/********************************************************************************************/
	/*************************************ByteArray Operation************************************/
	/********************************************************************************************/
	
	private ArrayList <byte[]> standardUDP60kbSegmentation(byte[] original){
		ArrayList <byte[]> byteArrays = new ArrayList <byte[]>();
		if(original.length>50000){
			try{
				int orginalSize=original.length;
				int index=0;
				int transportSize=50000;
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
	
	/********************************************************************************************/
	/**********************************Outside Thread Operation**********************************/
	/********************************************************************************************/
	
	public void addFileTransferToQueue (DownloadRequest operationID){		
		downloadingRequestLMap.put(operationID.getOperationID(), operationID);
	}
}
