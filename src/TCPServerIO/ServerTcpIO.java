package TCPServerIO;
import org.luan.LUANObject;

import GuiServerSelection.MainFrameServerSelection;

public class ServerTcpIO implements Runnable{
	private AvalibleOperations aop;
	private CommunicationProtocolTcpIO com;
	private FileDownloadProtocol fdp = null;
	private int idCounter=10234;
	public ServerTcpIO(CommunicationProtocolTcpIO com,MainFrameServerSelection selectServer){
		this.com=com;
		this.aop=new AvalibleOperations();
		this.fdp = new FileDownloadProtocol(com);
		new Thread(fdp,"FileDownload Protocol").start();
		//this.selectServer=selectServer;
	}
	
	public void run(){
		while(true){
			LUANObject luan=com.reciveLUANObject();
			operationManager(luan);
		}
	}
	
	private void operationManager(LUANObject cmd){
		try{
			if(cmd!=null){
				if(cmd.getString("headCmd").equals("c2f")){
					LUANObject luan = new LUANObject();
					double temprature=aop.getC2F(cmd.getDouble("temperature"));
					luan.put("headCmd", "c2f");
					luan.put("temperature", temprature);
					com.sendLUANObject(luan);
					System.out.println("Client converted Celsius to Fahrenheit "); 	
				}
				else if(cmd.getString("headCmd").equals("f2c")){
					LUANObject luan = new LUANObject();
					double temprature=aop.getF2C(cmd.getDouble("temperature"));
					luan.put("headCmd", "f2c");
					luan.put("temperature", temprature);
					com.sendLUANObject(luan);
					System.out.println("Client converted Fahrenheit to Celsius ");
				}
				else if(cmd.getString("headCmd").equals("Date")){
					LUANObject luan = new LUANObject();
					luan.put("headCmd", "Date");
					luan.put("Date", aop.getCurrentDate());
					com.sendLUANObject(luan);
					System.out.println("Client checked the current Date ");
				}
				else if(cmd.getString("headCmd").equals("Time")){
					LUANObject luan = new LUANObject();
					luan.put("headCmd", "Time");
					luan.put("Time", aop.getCurrentTime());
					com.sendLUANObject(luan);
					System.out.println("Client checked the current Time");
				}
				else if(cmd.getString("headCmd").equals("Sum of List")){
					LUANObject luan = new LUANObject();
					luan.put("headCmd", "Sum of List");
					int[] intArr=cmd.getIntegerArray("Sum of List");
					luan.put("Sum of List", aop.getSumOfList(intArr));
					com.sendLUANObject(luan);
					System.out.println("Client calculated the Sum of List");
				}
				else if(cmd.getString("headCmd").equals("Max of List")){
					LUANObject luan = new LUANObject();
					luan.put("headCmd", "Max of List");
					int[] intArr=cmd.getIntegerArray("Max of List");
					luan.put("Max of List", aop.getMaxOfList(intArr));
					com.sendLUANObject(luan);
					System.out.println("Client calculated the Maxium of List");
				}
				else if(cmd.getString("headCmd").equals("File Download")){
					System.out.println("Client requested to download file "+cmd.getString("fileName"));
					DownloadRequest request = new DownloadRequest(cmd.getString("fileName"), idCounter++);
					fdp.addFileTransferToQueue(request);
				}else if(cmd.getString("headCmd").equals("Start-up Avalible Files")){
					com.sendLUANObject(aop.startUpGetAllFilesFromBlob());
				}
				else if(cmd.getString("headCmd").equals("Quit")){
					//logik
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
}
