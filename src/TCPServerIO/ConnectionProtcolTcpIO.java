package TCPServerIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import GuiServer.MainFrameServerGUI;
import GuiServer.ShutDownServerProtocol;
import GuiServerSelection.MainFrameServerSelection;
public class ConnectionProtcolTcpIO implements Runnable{
	ArrayList <Socket> socketList = null;
	ArrayList <Thread> threadList = null;
	private ServerSocket serversocket;
	private ArrayList <ServerTcpIO> serverList = null;
	private MainFrameServerSelection serverSelectionGUI=null;
	private MainFrameServerGUI serverGUI =null;
	public ConnectionProtcolTcpIO(int port,MainFrameServerSelection serverSelectionGUI){
		try{
			this.serversocket = new ServerSocket(port);
			this.serverList = new ArrayList<ServerTcpIO>();
			this.serverSelectionGUI=serverSelectionGUI;
			this.socketList= new ArrayList<Socket>();
			this.threadList = new ArrayList<Thread>();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void run(){
		this.serverGUI = new MainFrameServerGUI(serverSelectionGUI,new ShutDownServerProtocol(this),"TCP Server I/O");
    	serverGUI.showMainFrameServerGUI();
		connectionListener();
	}
	
	private void connectionListener(){
		while(true){
			try {
				Socket socket = serversocket.accept();
				CommunicationProtocolTcpIO com = new CommunicationProtocolTcpIO(socket);
				ServerTcpIO tcpIO = new ServerTcpIO(com,serverSelectionGUI);
				serverList.add(tcpIO);
				Thread thread =new Thread(tcpIO,"Server Services For: "+socket.getRemoteSocketAddress());
				thread.start();
			} catch (IOException ex) {
				if(ex.getMessage().equals("socket closed")){
					shutDownCurrentThread();
				}else{
					ex.printStackTrace();
				}
			} 
		}
	}
	
	@SuppressWarnings("deprecation")
	private void shutDownCurrentThread(){
		try{
			Thread.currentThread().stop();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void closeServerSocket(){
		try{
			this.serversocket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public ArrayList<Socket> getSocketList(){
		return this.socketList;
	}
	
	public ArrayList<Thread> getThreadList(){
		return this.threadList;
	}
	
}
