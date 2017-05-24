package TCPServerIO;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.luan.LUANObject;



public class FileDownloadProtocol implements Runnable{
	private CommunicationProtocolTcpIO com = null;
	private static ConcurrentHashMap<String, ArrayList <LUANObject>> luanListMap = new ConcurrentHashMap<String, ArrayList <LUANObject>>();
	private static ConcurrentHashMap<String,DownloadRequest> downloadingRequestLMap = new ConcurrentHashMap<String,DownloadRequest>();
	
	public FileDownloadProtocol(CommunicationProtocolTcpIO com) {
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
	
	
	/*private void printLuanObjects(LUANObject luan){
		try{
			System.out.println("\n\n\n----------------------------------------------------------------------------------------------------");
			System.out.println("luan.getString(\"headCmd\") == "+luan.getString("headCmd"));
			System.out.println("luan.getString(\"operationID\") == "+luan.getString("operationID"));
			System.out.println("luan.getInteger(\"originalLength\") == "+luan.getInteger("originalLength"));
			System.out.println("luan.getInteger(\"totaltAmountOfPackages\") == "+luan.getInteger("totaltAmountOfPackages"));
			System.out.println("luan.getInteger(\"segmentTransferSize\") == "+luan.getInteger("segmentTransferSize"));
			System.out.println("luan.getString(\"fileName\") == "+luan.getString("fileName"));
			System.out.println("luan.getString(\"fileType\") == "+luan.getString("fileType"));
			System.out.println("luan.getInteger(\"packageNbr\")) == "+luan.getInteger("packageNbr"));
			//System.out.println("luan.getByteArray(\"byteArray\") == "+luan.getByteArray("byteArray"));
			//System.out.println("luan.getByteArray(\"byteArray\").length == "+luan.getByteArray("byteArray").length);
			System.out.println("----------------------------------------------------------------------------------------------------\n\n\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}*/
	
	
	private void sendPackagesIfTheyExist(){
		try{
			for(String key:luanListMap.keySet()){
				//System.out.println("inne i första if-satsen");
				//System.out.println("downloadingRequestLMap.remove("+key+") == "+downloadingRequestLMap.remove(key));
				if(luanListMap.size()>0 && luanListMap.get(key).size()>0 && downloadingRequestLMap.size()>0){
					//System.out.println("inne i andra if-satsen");
					ArrayList <LUANObject> luanList =luanListMap.get(key);
					if(luanList.size()>luanList.get(0).getInteger("totaltAmountOfPackages")){
						//System.out.println("inne i tredje if-satsen");
						for(int i=0;i<luanList.size();i++){
							if(luanList.get(i).getInteger("packageNbr")==i){
								//System.out.println("inne i fjärde if-satsen");
								//printLuanObjects(luanList.get(i));
								com.sendLUANObject(luanList.get(i));
								Thread.sleep(50);
							
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
		ArrayList <byte[]> byteArrays=standard500kbSegmentation(down.getOriginalByteArray());
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
				/*System.out.println("\n\n\n-----------------------------------------------------");
				System.out.println("operationsID == "+down.getOperationID());
				System.out.println("originalLength == "+down.getOriginalByteArray().length);
				System.out.println("byteArrays.size() == "+byteArrays.size());
				System.out.println("segmentTransferSize == "+byteArrays.get(i).length);
				//System.out.println("byteArrays.get("+i+") == "+byteArrays.get(i));
				System.out.println("-----------------------------------------------------\n\n\n");
				*/luan.put("headCmd", headCmd);
				luan.put("operationID", down.getOperationID());
				luan.put("originalLength", down.getOriginalByteArray().length);
				luan.put("totaltAmountOfPackages",byteArrays.size());
				luan.put("segmentTransferSize", byteArrays.get(i).length/*segmentTransferSize*/);
				luan.put("fileName",down.getFileName());
				luan.put("fileType",down.getFileType());
				luan.put("byteArray", byteArrays.get(i));
				//System.out.println("luan.getByteArray(\"byteArray\").length == "+luan.getByteArray("byteArray").length);
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
	
	/********************************************************************************************/
	/**********************************Outside Thread Operation**********************************/
	/********************************************************************************************/
	
	public void addFileTransferToQueue (DownloadRequest operationID){		
		downloadingRequestLMap.put(operationID.getOperationID(), operationID);
	}
}
