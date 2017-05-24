package GuiServerSelection;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import GuiServer.ValidationDialogGUI;
import TCPServerNIO.NioServer;

public class DetailPanelForTcpNIO {

	private JPanel panelTcpNIO = null;
	private JButton startServer = null;
	private JButton backToMenu = null;
	private ValidationDialogGUI val=null;
	private JTextField portTextFiled=null;
	private MainFrameServerSelection gui;
	public DetailPanelForTcpNIO(ValidationDialogGUI val,MainFrameServerSelection gui){
		this.val=val;
		this.gui=gui;
		createTcpNIOPanel();
		addIpLabelToPanel();
		addPortLabelToPanel();
		addIpTextFieldToPanel();
		addPortTextFieldToPanel();
		addBackToMenuButtonToPanel();
		addStartServerButtonToPanel();
	}
	
	private void createTcpNIOPanel(){
		this.panelTcpNIO = new JPanel();
		this.panelTcpNIO.setBorder(BorderFactory.createTitledBorder("Initilaztion of TCP NEW I/O Server"));
		this.panelTcpNIO.setVisible(false);
		this.panelTcpNIO.setLayout(null);
	}
	
	private void addIpLabelToPanel(){
		JLabel ipLabel = new JLabel();
		ipLabel.setText(" Enter IP:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		ipLabel.setFont(font);
		ipLabel.setHorizontalAlignment(JLabel.LEFT);
		ipLabel.setBorder(BorderFactory.createTitledBorder("Enter IP number"));
		panelTcpNIO.add(ipLabel).setBounds(10, 20, 280, 60);
	}
	
	private void addPortLabelToPanel(){
		JLabel portLabel = new JLabel();
		portLabel.setText(" Enter Port:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		portLabel.setFont(font);
		portLabel.setHorizontalAlignment(JLabel.LEFT);
		portLabel.setBorder(BorderFactory.createTitledBorder("Enter Port number"));
		panelTcpNIO.add(portLabel).setBounds(10, 90, 280, 60);
	}
	
	private void addIpTextFieldToPanel(){
		JTextField ipTextFiled = new JTextField();
		ipTextFiled.setEditable(false);
		ipTextFiled.setText("Only Needed with RMI");
		ipTextFiled.setHorizontalAlignment(JTextField.CENTER);
		panelTcpNIO.add(ipTextFiled).setBounds(100, 40, 182, 30);
	}
	
	private void addPortTextFieldToPanel(){
		this.portTextFiled = new JTextField();
		panelTcpNIO.add(portTextFiled).setBounds(120, 110, 162, 30);
	}
	
	private void addStartServerButtonToPanel(){
		this.startServer= new JButton("Start Server");
		startServer.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		int port=Integer.parseInt(portTextFiled.getText());
	        		NioServer tcpNIO = new NioServer(port, gui);
	        		new Thread(tcpNIO,"TCP New I/O Server").start();
	        		hideDetailPanelForTcpNIO();
	        		gui.showServerSelectionPanel();
	        		gui.hideMainFrame();
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("ERRROR - Wrong port Structure", "Port number must consit of 1 to 5 digit number");
	        	 }
	         }
	    });
		panelTcpNIO.add(startServer).setBounds(10, 160, 135, 50);
	}
	
	private void addBackToMenuButtonToPanel(){
		this.backToMenu= new JButton("Back To Menu");
		backToMenu.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		hideDetailPanelForTcpNIO();
	        		gui.showServerSelectionPanel();
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("Error Manager - "+ex.toString(), ex.getMessage());
	        	 }
	         }
	    });
		panelTcpNIO.add(backToMenu).setBounds(155, 160, 135, 50);
	}
	
	public void showDetailPanelForTcpNIO(){
		this.panelTcpNIO.setVisible(true);
		portTextFiled.setText("");
	}
	
	public void hideDetailPanelForTcpNIO(){
		this.panelTcpNIO.setVisible(false);
	}
	
	public JPanel getJPanel(){
		return this.panelTcpNIO;
	}
}
