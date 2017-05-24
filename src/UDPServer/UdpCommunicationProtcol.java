package UDPServer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.luan.LUANObject;

public class UdpCommunicationProtcol{
	private DatagramSocket datagramServerSocket = null;
	private int currentPort=0;
	private InetAddress currentIP=null;
	
	public UdpCommunicationProtcol(int port){
		try {
			this.datagramServerSocket=new DatagramSocket(port);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public LUANObject reciveDatagramLUANObject(){
		LUANObject luan=null;
		byte[] inputData = new byte[60000];
		try{
			DatagramPacket datagramPackage = new DatagramPacket(inputData, inputData.length);
			datagramServerSocket.receive(datagramPackage);
			if(datagramPackage.getData()!=null){
				currentIP= datagramPackage.getAddress();
				currentPort=datagramPackage.getPort();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPackage.getData());
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
				luan=(LUANObject) objectInputStream.readObject();
			}
		}catch(Exception ex){
			if(ex.getMessage().equals("socket closed")){
				shutDownCurrentThread();
			}else{
				luan=null;
				ex.printStackTrace();
			}
		}	
		return luan;
	}
	
	public void sendDatagramLUANObject(LUANObject luan){
		if(luan!=null){
			byte[] outputData = new byte[60000];
			try{
				ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(outputData.length);
		        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(byteOutputStream));
		        objectOutputStream.writeObject(luan);
		        objectOutputStream.flush();
		        byte[] byteArray = byteOutputStream.toByteArray();
		        DatagramPacket packetOutput = new DatagramPacket(byteArray, byteArray.length,currentIP,currentPort);
		        datagramServerSocket.send(packetOutput);
			}catch(Exception ex){
				ex.printStackTrace();
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
	
	public void shutDownUdpServer(){
		try{
			this.datagramServerSocket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
