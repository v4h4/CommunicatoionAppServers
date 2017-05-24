package RMI;
import java.rmi.Naming;  
import java.rmi.registry.LocateRegistry;  
import java.rmi.registry.Registry;

import GuiServer.MainFrameServerGUI;
import GuiServer.ShutDownServerProtocol;
import GuiServerSelection.MainFrameServerSelection;
public class RmiServer{  
    private RmiImplementation rmiImplementation;
	private int port=0;
	private String ipServer="";
	private MainFrameServerGUI gui;
	private ShutDownServerProtocol shutDown=null;
	private Registry reg=null;
	public RmiServer(MainFrameServerSelection selectServer,String ipServer, int port){
    	this.ipServer=ipServer;
    	this.port=port;
    	this.shutDown= new ShutDownServerProtocol(this);
    	this.gui = new MainFrameServerGUI(selectServer,shutDown,"RMI Server");
    	gui.showMainFrameServerGUI();
    }
	
	public void run(){  
        try {  
        	rmiImplementation = new RmiImplementation();  
        	reg=LocateRegistry.createRegistry(port);  
            Naming.bind("//"+ipServer+"/RMILab1_Services", rmiImplementation);  
            System.out.println("Server working.");  
        } catch (Exception e) {  
            System.out.println("ERROR:\n1.Check if server allready runs\n2.Check if ip numbers is correct  ");  
        }  
    }  
	
	@SuppressWarnings("static-access")
	public void shutDownRmiserver(){
		try {
			rmiImplementation.unexportObject(reg, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}  