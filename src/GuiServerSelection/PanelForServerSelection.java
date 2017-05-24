package GuiServerSelection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import BlobManagement.BlobFrame;

public class PanelForServerSelection {
	private JPanel panelForServerSelection = null;
	private DetailPanelForRMI rmi =null;
	private DetailPanelForUDP udp=null;
	private DetailPanelForTcpIO tcpIO=null;
	private DetailPanelForTcpNIO tcpNIO=null;
	private BlobFrame blobFrame=null;
	private MainFrameServerSelection gui=null;
	public PanelForServerSelection(MainFrameServerSelection gui,DetailPanelForRMI rmi,DetailPanelForUDP udp,DetailPanelForTcpIO tcpIO,DetailPanelForTcpNIO tcpNIO){
		this.gui=gui;
		this.rmi=rmi;
		this.udp=udp;
		this.tcpIO=tcpIO;
		this.tcpNIO=tcpNIO;
		createServerSelectionPanel();
		this.blobFrame=new BlobFrame(gui,this);
		udpIOServerButton();
		tcpIOServerButton();
		tcpNIOServerButton();
		rmiServerButton();
		blobFrameButton();
	}
	private void createServerSelectionPanel(){
		this.panelForServerSelection = new JPanel();
		this.panelForServerSelection.setBorder(BorderFactory.createTitledBorder("Server Selection"));
		this.panelForServerSelection.setVisible(true);
		this.panelForServerSelection.setLayout(null);
	}
	
	private void udpIOServerButton() {
		JButton udpServerIO = new JButton("Start UDP Server with I/O");
		udpServerIO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelForServerSelection.setVisible(false);
				udp.showDetailPanelForUDP();
				//selectIpAndPort.ipAndPortGUI("Start UDP Server with I/O");
			}
		});
		panelForServerSelection.add(udpServerIO).setBounds(10, 20, 280, 30);
	}

	private void tcpNIOServerButton() {
		JButton tcpServerIO = new JButton("Start TCP Server with I/O");
		tcpServerIO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelForServerSelection.setVisible(false);
				tcpIO.showDetailPanelForTcpIO();
			}
		});
		panelForServerSelection.add(tcpServerIO).setBounds(10, 60, 280, 30);
	}

	private void tcpIOServerButton() {
		JButton tcpServerNIO = new JButton("Start TCP Server with NEW I/O");
		tcpServerNIO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelForServerSelection.setVisible(false);
				tcpNIO.showDetailPanelForTcpNIO();
				//selectIpAndPort.ipAndPortGUI("Start TCP Server with NEW I/O");
			}
		});
		panelForServerSelection.add(tcpServerNIO).setBounds(10, 100, 280, 30);
	}

	private void rmiServerButton() {
		JButton rmiServer = new JButton("Start RMI Server");
		rmiServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideServerSelection();
				rmi.showDetailPanelForRMI();
				//selectIpAndPort.ipAndPortGUI("Start RMI Server");
			}
		});	
		panelForServerSelection.add(rmiServer).setBounds(10, 140, 280, 30);
	}
	
	private void blobFrameButton() {
		JButton blobFrameButton = new JButton("Open Blob Manager");
		blobFrameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideServerSelection();
				gui.hideMainFrame();
				blobFrame.showBlobFrame();
				//selectIpAndPort.ipAndPortGUI("Start RMI Server");
			}
		});	
		panelForServerSelection.add(blobFrameButton).setBounds(10, 180, 280, 30);
	}
	
	public JPanel getJPanel(){
		return this.panelForServerSelection;
	}
	
	public void showServerSelection(){
		this.panelForServerSelection.setVisible(true);
	}
	
	public void hideServerSelection(){
		this.panelForServerSelection.setVisible(false);
	}
	
	public BlobFrame getBlobFrame(){
		return this.blobFrame;
	}
}
