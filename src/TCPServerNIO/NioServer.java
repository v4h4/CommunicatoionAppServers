package TCPServerNIO;
import org.luan.LUANObject;

import GuiServer.MainFrameServerGUI;
import GuiServer.ShutDownServerProtocol;
import GuiServerSelection.MainFrameServerSelection;

public class NioServer implements Runnable {
    private CommunicationProtocolTcpNIO com = null;
    private AvalibleOperations op =null;
	private FileDownloadProtocol down=null;
	private int idCounter=12034;
	private MainFrameServerGUI gui=null;
	private MainFrameServerSelection serverSelectionGUI=null;
    public NioServer(int port,MainFrameServerSelection serverSelectionGUI) {
    	this.serverSelectionGUI=serverSelectionGUI;
    	this.com = new CommunicationProtocolTcpNIO(port);
    	this.op= new AvalibleOperations();
    	this.down=com.getFileDownloadProtocol();
    	
    }

    public void run(){
    	this.gui = new MainFrameServerGUI(serverSelectionGUI,new ShutDownServerProtocol(com),"TCP Server NEW I/O");
    	gui.showMainFrameServerGUI();
    	listen();
    }
    private void listen() {
        try { 
            while (true) {
                while(com.iteratorHasNextSelectionKey()==true){
                	if(com.getCurrentSelectionKey().isConnectable()){
                		com.establishClientConnection();
                	}
                	if(com.getCurrentSelectionKey().isAcceptable()==true){
                		com.acceptNewClient();
                	}
                	if(com.getCurrentSelectionKey().isReadable()==true){
                		operationManager(com.receiveLUANObject());//luan <-- return right task answer via operation manager  <-- recivesMesage       
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void operationManager(LUANObject cmd){
		try{
			if(cmd!=null && cmd.getString("headCmd")!=null){
				if(cmd.getString("headCmd").equals("c2f")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("temperature", op.getC2F(cmd.getDouble("temperature")));
					com.sendLUANObject(luan);
					System.out.println("Client converted Celsius to Fahrenheit ");
				}
				else if(cmd.getString("headCmd").equals("f2c")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("temperature", op.getF2C(cmd.getDouble("temperature")));
					com.sendLUANObject(luan);
					System.out.println("Client converted Fahrenheit to Celsius ");
				}
				else if(cmd.getString("headCmd").equals("Date")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("Date", op.getCurrentDate());
					com.sendLUANObject(luan);
					System.out.println("Client checked the current Date ");
				}
				else if(cmd.getString("headCmd").equals("Time")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					luan.put("Time", op.getCurrentTime());
					com.sendLUANObject(luan);
					System.out.println("Client checked the current Time");
				}
				else if(cmd.getString("headCmd").equals("Sum of List")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					int[] intArr =cmd.getIntegerArray("Sum of List");
					luan.put("Sum of List", op.getSumOfList(intArr));
					com.sendLUANObject(luan);
					System.out.println("Client calculated the Sum of List");
				}
				else if(cmd.getString("headCmd").equals("Max of List")){
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("headCmd"));
					int[] intArr =cmd.getIntegerArray("Max of List");
					luan.put("Max of List",op.getMaxOfList(intArr));
					com.sendLUANObject(luan);
					System.out.println("Client calculated the Maxium of List");
				}
				else if(cmd.getString("headCmd").equals("File Download") ){
					System.out.println("Client requested to download file "+cmd.getString("fileName"));
					DownloadRequest op = new DownloadRequest(com.getCurrentChannel(), cmd.getString("fileName"), idCounter++);
					down.addFileTransferToQueue(op);
				}
				else if(cmd.getString("headCmd").equals("Start-up Avalible Files")){
					com.sendLUANObject(op.startUpGetAllFilesFromBlob());
				}
				else{
					LUANObject luan= new LUANObject();
					luan.put("headCmd",cmd.getString("ERROR"));
					com.sendLUANObject(luan);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*public void smallGui(){
		final JFrame serverView = new JFrame();
		serverView.setTitle("TCP SERVER with NEW I/O is Running ");
		serverView.setVisible(true);
		serverView.setSize(315, 80);//300, 235
		serverView.setLayout(null);
		serverView.setAlwaysOnTop(true);
		serverView.setLocationRelativeTo(null);
		serverView.setResizable(false);
		serverView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JButton shutDownServer = new JButton("SHUTDOWN TCP Server with NEW I/O");
		serverView.add(shutDownServer).setBounds(5, 10, 300, 30);
		shutDownServer.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 selectServer.showMainFrame();
	        	 //closeConnection();
	        	 serverView.dispose();
	         }
	    });
	}*/
}