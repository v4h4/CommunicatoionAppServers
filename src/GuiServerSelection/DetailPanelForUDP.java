package GuiServerSelection;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import UDPServer.UdpServerIO;
import GuiServer.ValidationDialogGUI;

public class DetailPanelForUDP {

	private JPanel panelUDP = null;
	private JButton startServer = null;
	private JButton backToMenu = null;
	private ValidationDialogGUI val=null;
	private JTextField portTextFiled=null;
	private MainFrameServerSelection gui;
	public DetailPanelForUDP(ValidationDialogGUI val,MainFrameServerSelection gui){
		this.val=val;
		this.gui=gui;
		createUDPPanel();
		addIpLabelToPanel();
		addPortLabelToPanel();
		addIpTextFieldToPanel();
		addPortTextFieldToPanel();
		addBackToMenuButtonToPanel();
		addStartServerButtonToPanel();
	}
	
	private void createUDPPanel(){
		this.panelUDP = new JPanel();
		this.panelUDP.setBorder(BorderFactory.createTitledBorder("Initilaztion of UDP I/O Server"));
		this.panelUDP.setVisible(false);
		this.panelUDP.setLayout(null);
	}
	
	private void addIpLabelToPanel(){
		JLabel ipLabel = new JLabel();
		ipLabel.setText(" Enter IP:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		ipLabel.setFont(font);
		ipLabel.setHorizontalAlignment(JLabel.LEFT);
		ipLabel.setBorder(BorderFactory.createTitledBorder("Enter IP number"));
		panelUDP.add(ipLabel).setBounds(10, 20, 280, 60);
	}
	
	private void addPortLabelToPanel(){
		JLabel portLabel = new JLabel();
		portLabel.setText(" Enter Port:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		portLabel.setFont(font);
		portLabel.setHorizontalAlignment(JLabel.LEFT);
		portLabel.setBorder(BorderFactory.createTitledBorder("Enter Port number"));
		panelUDP.add(portLabel).setBounds(10, 90, 280, 60);
	}
	
	private void addIpTextFieldToPanel(){
		JTextField ipTextFiled = new JTextField();
		ipTextFiled.setEditable(false);
		ipTextFiled.setText("Only Needed with RMI");
		ipTextFiled.setHorizontalAlignment(JTextField.CENTER);
		panelUDP.add(ipTextFiled).setBounds(100, 40, 182, 30);
	}
	
	private void addPortTextFieldToPanel(){
		this.portTextFiled = new JTextField();
		panelUDP.add(portTextFiled).setBounds(120, 110, 162, 30);
	}
	
	private void addStartServerButtonToPanel(){
		this.startServer= new JButton("Start Server");
		startServer.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		int port=Integer.parseInt(portTextFiled.getText());
	        		UdpServerIO udp = new UdpServerIO(gui, port);
	        		new Thread(udp,"UDP Server I/O").start();
	        		hideDetailPanelForUDP();
	        		gui.showServerSelectionPanel();
	        		gui.hideMainFrame();
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("ERRROR - Wrong port Structure", "Port number must consit of 1 to 5 digit number");
	        	 }
	         }
	    });
		panelUDP.add(startServer).setBounds(10, 160, 135, 50);
	}
	
	private void addBackToMenuButtonToPanel(){
		this.backToMenu= new JButton("Back To Menu");
		backToMenu.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		hideDetailPanelForUDP();
	        		gui.showServerSelectionPanel();
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("Error Manager - "+ex.toString(), ex.getMessage());
	        	 }
	         }
	    });
		panelUDP.add(backToMenu).setBounds(155, 160, 135, 50);
	}
	
	public void showDetailPanelForUDP(){
		this.panelUDP.setVisible(true);
		portTextFiled.setText("");
	}
	
	public void hideDetailPanelForUDP(){
		this.panelUDP.setVisible(false);
	}
	
	public JPanel getJPanel(){
		return this.panelUDP;
	}
}
