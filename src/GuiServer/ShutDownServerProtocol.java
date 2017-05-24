package GuiServer;

import RMI.RmiServer;
import TCPServerIO.ConnectionProtcolTcpIO;
import TCPServerNIO.CommunicationProtocolTcpNIO;
import UDPServer.UdpCommunicationProtcol;

public class ShutDownServerProtocol {
	private CommunicationProtocolTcpNIO comTcpNio=null;
	private ConnectionProtcolTcpIO comTcpIo=null;
	private UdpCommunicationProtcol comUdp=null;
	private RmiServer comRmi=null;
	public ShutDownServerProtocol(CommunicationProtocolTcpNIO comTcpNio){
		this.comTcpNio=comTcpNio;
	}
	
	public ShutDownServerProtocol(ConnectionProtcolTcpIO comTcpIo){
		this.comTcpIo=comTcpIo;
	}
	
	public ShutDownServerProtocol(UdpCommunicationProtcol comUdp){
		this.comUdp=comUdp;
	}
	
	public ShutDownServerProtocol(RmiServer comRmi) {
		this.comRmi=comRmi;
	}
	
	public void shutDownServer(){
		try{
			if(comTcpIo!=null){
				shutDownAllTcpIoActivity();
			}else if(comTcpNio!=null){
				shutDownTcpNIOServer();
			}else if(comUdp!=null){
				shutDownUdpServer();
			}else if(comRmi!=null){
				comRmi.shutDownRmiserver();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void shutDownTcpNIOServer(){
		try{
			comTcpNio.shutdownNioServer();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void shutDownUdpServer(){
		try{
			comUdp.shutDownUdpServer();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void shutDownAllTcpIoActivity(){
		comTcpIo.closeServerSocket();
		for(int i=0;i<comTcpIo.getSocketList().size();i++){
			try{
				comTcpIo.getSocketList().get(i).close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		for(int i=0;i<comTcpIo.getThreadList().size();i++){
			try{
				comTcpIo.getThreadList().get(i).stop();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
