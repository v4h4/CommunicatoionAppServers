package TCPServerIO;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.luan.LUANObject;

public class CommunicationProtocolTcpIO {
	private Socket socket=null;
	public CommunicationProtocolTcpIO(Socket socket){
		this.socket=socket;
	}
	
	public void sendLUANObject(LUANObject luan){
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(luan);
			objectOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public LUANObject reciveLUANObject(){
    	LUANObject luan=null;;
    	try{
    		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
    		luan=(LUANObject) objectInput.readObject();
    	}catch(Exception ex){
    		if(ex instanceof EOFException){
    			clientHassBeenDisconnectedPrintOut();
    			stopCurrentThread();
    		}else{
    			ex.printStackTrace();
        		return null;
    		}
    		
    	}
    	return luan;
    }
	
	private void clientHassBeenDisconnectedPrintOut(){
		try{
			System.out.println("\n\nClient "+socket.getRemoteSocketAddress()+" has been Disconnected\n\n");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void stopCurrentThread(){
		try{
			Thread.currentThread().stop();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
    public void closeConnection(){
		try{
			this.socket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
