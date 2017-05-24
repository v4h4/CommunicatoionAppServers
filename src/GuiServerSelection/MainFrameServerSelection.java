package GuiServerSelection;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import BlobManagement.BlobFrame;
import GuiServer.ValidationDialogGUI;

public class MainFrameServerSelection {
	private DetailPanelForRMI rmi =null;
	private DetailPanelForUDP udp=null;
	private DetailPanelForTcpIO tcpIO=null;
	private DetailPanelForTcpNIO tcpNIO=null;
	private PanelForServerSelection serverSelection=null;
	private JFrame mainFrame = null;
	BlobFrame blobFrame = null;
	private ValidationDialogGUI val = null;
	
	public MainFrameServerSelection(){
		createMainFrameServerSelection();
		this.val= new ValidationDialogGUI(mainFrame);
		this.rmi = new DetailPanelForRMI(val,this);
		this.udp= new DetailPanelForUDP(val,this);
		this.tcpIO= new DetailPanelForTcpIO(val,this);
		this.tcpNIO = new DetailPanelForTcpNIO(val,this);
		this.serverSelection= new PanelForServerSelection(this,rmi, udp, tcpIO, tcpNIO);
		this.blobFrame=serverSelection.getBlobFrame();
		addPanelForServerSelectionToFrame();
		addDetailPanelForUDPToFrame();
		addDetailPanelForTcpIOToFrame();
		addDetailPanelForTcpNIOToFrame();
		addDetailPanelForRMIToFrame();
		
	}
	
	private void createMainFrameServerSelection(){
		this.mainFrame= new JFrame();
		this.mainFrame.setTitle("Select Server");
		this.mainFrame.setVisible(false);
		this.mainFrame.setSize(315, 255);
		this.mainFrame.setLayout(null);
		this.mainFrame.setAlwaysOnTop(true);
		this.mainFrame.setLocation(10, 10);
		this.mainFrame.setResizable(false);
		this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrameCloseListener();
	}
	
	private void addDetailPanelForUDPToFrame(){
		this.mainFrame.add(udp.getJPanel()).setBounds(5, 5, 300, 220);
	}
	
	private void addPanelForServerSelectionToFrame(){
		this.mainFrame.add(serverSelection.getJPanel()).setBounds(5, 5, 300, 220);
	}
	
	private void addDetailPanelForTcpIOToFrame(){
		this.mainFrame.add(tcpIO.getJPanel()).setBounds(5, 5, 300, 220);
	}
	
	private void addDetailPanelForTcpNIOToFrame(){
		this.mainFrame.add(tcpNIO.getJPanel()).setBounds(5, 5, 300, 220);
	}
	
	private void addDetailPanelForRMIToFrame(){
		this.mainFrame.add(rmi.getJPanel()).setBounds(5, 5, 300, 220);
	}
	
	public void showMainFrame(){
		this.mainFrame.setVisible(true);
	}
	
	public void hideMainFrame(){
		this.mainFrame.setVisible(false);
	}
	
	public void showServerSelectionPanel(){
		this.serverSelection.getJPanel().setVisible(true);
	}
	
	public void hideServerSelectionPanel(){
		this.serverSelection.getJPanel().setVisible(false);
	}
	
	private void mainFrameCloseListener(){
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				boolean close=val.dynamicConfirmationDialog("Closing Multiple Server Application", "Are you sure that you want to close Multiple Server Application?");
				if(close == true){
					System.exit(0);
				}	
			}
		});
	}
	
	public BlobFrame getBlobManager(){
		return this.blobFrame;
	}
}