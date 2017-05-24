package UDPServer;
import org.luan.LUANObject;

import GuiServer.MainFrameServerGUI;
import GuiServer.ShutDownServerProtocol;
import GuiServerSelection.MainFrameServerSelection;


public class UdpServerIO implements Runnable {
	private AvalibleOperations op = null;
	private UdpCommunicationProtcol com=null;
	private FileDownloadProtocol fdp=null;
	private int idCounter=12034;
	private MainFrameServerGUI gui=null;
	public UdpServerIO(MainFrameServerSelection serverSelectionGUI,int port){
		try{
			this.com= new UdpCommunicationProtcol(port);
			this.op = new AvalibleOperations();
			this.fdp = new FileDownloadProtocol(com);
			new Thread(fdp,"FileDownload Protocol").start();
			this.gui = new MainFrameServerGUI(serverSelectionGUI,new ShutDownServerProtocol(com),"UDP Server I/O");
	    	gui.showMainFrameServerGUI();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void run() {
		routineProtcol();
	}
	
	private void routineProtcol(){
		while(true){
			try{
				operationManager(com.reciveDatagramLUANObject());			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private void operationManager(LUANObject cmd){
		try{
			if(cmd!=null && cmd.getString("headCmd")!=null){
				if(cmd.getString("headCmd").equals("c2f")){
					System.out.println("Client converted Celsius to Fahrenheit ");
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("temperature", op.getC2F(cmd.getDouble("temperature")));
					com.sendDatagramLUANObject(luan);
				}
				else if(cmd.getString("headCmd").equals("f2c")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("temperature", op.getF2C(cmd.getDouble("temperature")));
					com.sendDatagramLUANObject(luan);
					System.out.println("Client converted Fahrenheit to Celsius ");	
				}
				else if(cmd.getString("headCmd").equals("Date")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("Date", op.getCurrentDate());
					com.sendDatagramLUANObject(luan);
					System.out.println("Client checked the current Date ");
				}
				else if(cmd.getString("headCmd").equals("Time")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("Time", op.getCurrentTime());
					com.sendDatagramLUANObject(luan);
					System.out.println("Client checked the current Time");
				}
				else if(cmd.getString("headCmd").equals("Sum of List")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					int[] intArr =cmd.getIntegerArray("Sum of List");
					luan.put("Sum of List", op.getSumOfList(intArr));
					com.sendDatagramLUANObject(luan);
					System.out.println("Client calculated the Sum of List");
				}
				else if(cmd.getString("headCmd").equals("Max of List")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					int[] intArr =cmd.getIntegerArray("Max of List");
					luan.put("Max of List", op.getMaxOfList(intArr));
					com.sendDatagramLUANObject(luan);
					System.out.println("Client calculated the Maxium of List");
				}else if(cmd.getString("headCmd").equals("startUpGetAllFilesFromBlob")){
					com.sendDatagramLUANObject(op.startUpGetAllFilesFromBlob());	
				}
				else if(cmd.getString("headCmd").equals("File Download") ){
					System.out.println("Client requested to download file "+cmd.getString("fileName"));
					DownloadRequest request = new DownloadRequest(cmd.getString("fileName"), idCounter++);
					fdp.addFileTransferToQueue(request);
				}
				else if(cmd.getString("headCmd").equals("Start-up Avalible Files")){
					com.sendDatagramLUANObject(op.startUpGetAllFilesFromBlob());
				}
				else{
					LUANObject luan= new LUANObject();
					luan= new LUANObject();
					luan.put("headCmd","ERROR");
					com.sendDatagramLUANObject(luan);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
