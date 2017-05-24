package TCPServerNIO;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import org.luan.LUANObject;

public class CommunicationProtocolTcpNIO{
	private int port;
	private Selector selector;
	private FileDownloadProtocol down=null;
    private ServerSocketChannel server;
    private SocketChannel currentChannel;
    private SelectionKey currentKey;

	public CommunicationProtocolTcpNIO(int port){
		try{
			this.port=port;
			this.selector = Selector.open();
			this.server = ServerSocketChannel.open();
			System.out.println("server.getLocalAddress() == "+server.getLocalAddress());
			this.server.socket().bind(new InetSocketAddress(this.port));
			this.server.configureBlocking(false);
			this.server.register(this.selector, SelectionKey.OP_ACCEPT);
			this.down=new FileDownloadProtocol(this);
			new Thread(down,"FileDownloadProtocol").start();
		}catch(Exception ex){
    		ex.printStackTrace();
    	}
	}
	/****************************ConnectionManagment*************************/ 
	
	public int getSeverPort(){
		return this.port;
	}
	
	public FileDownloadProtocol getFileDownloadProtocol(){
		return this.down;
	}
	
	public boolean iteratorHasNextSelectionKey(){
		boolean hasNextIterator=false;
		try {
			selector.select();
			Iterator<SelectionKey> i = selector.selectedKeys().iterator();
			if(i.hasNext()==true){
				 currentKey= i.next();
                 i.remove();
				hasNextIterator=true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hasNextIterator;
	}
	
	
	public void establishClientConnection(){
		 try {
			((SocketChannel)currentKey.channel()).finishConnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void acceptNewClient(){
		 try{
			 currentChannel = server.accept();
			 currentChannel.configureBlocking(false);
			 currentChannel.socket().setTcpNoDelay(true);
			 currentChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );
			 System.out.println(currentChannel.getRemoteAddress() + " connected...");
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	 }
	    
	 public SelectionKey getCurrentSelectionKey(){
		 return this.currentKey;
	 } 
	 
	 public SocketChannel getCurrentChannel(){
		 return this.currentChannel;
	 }
	    
	 public void closeCurrentChannel(){
		 try{
			 currentChannel.close();
			 System.out.println("Client has been disconnected");
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	 }
	 
	 public void shutdownNioServer(){
		 try{
			 server.socket().close();
			 server.close();
			//server.notifyAll();
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	 }
	
	
	/*************************Trafic Managment*************************/
	public LUANObject receiveLUANObject() {
		byte[] receivedBytes=null;
		LUANObject luan=null;
		try{
			ByteBuffer buffer = ByteBuffer.allocateDirect(500000);//1000024
	        currentChannel.read(buffer);
	        buffer.flip();
	        receivedBytes = new byte[buffer.remaining()];
            buffer.get(receivedBytes);
	        luan = new LUANObject(receivedBytes);
		}catch(Exception ex){
			//if(ex instanceof IOException && ex.getMessage().toString().equals("An existing connection was forcibly closed by the remote host") ){
        		closeCurrentChannel();
        		luan=null;
        	/*}else{
        		ex.printStackTrace();
        		luan=null;
        	}*/
		}
		return luan;
    }
    
    public void sendLUANObject(LUANObject luan) {
    	try {
    		ByteBuffer buffer = ByteBuffer.allocateDirect(500000);//1000024
    		buffer = ByteBuffer.wrap(luan.getBytes());
            currentChannel.write(buffer);
       } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void sendFile(byte[] byteArr,SocketChannel channel) {
        try {
        	ByteBuffer buffer = ByteBuffer.allocateDirect(500000);//1000024
        	buffer = ByteBuffer.wrap(byteArr);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}