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
import RMI.RmiServer;


public class DetailPanelForRMI {
	private JPanel panelRMI = null;
	private JButton startServer = null;
	private JButton backToMenu = null;
	private ValidationDialogGUI val=null;
	private JTextField ipTextFiled=null;
	private MainFrameServerSelection gui;
	public DetailPanelForRMI(ValidationDialogGUI val,MainFrameServerSelection gui){
		this.val=val;
		this.gui=gui;
		createRmiPanel();
		addIpLabelToPanel();
		addPortLabelToPanel();
		addIpTextFieldToPanel();
		addPortTextFieldToPanel();
		addBackToMenuButtonToPanel();
		addStartServerButtonToPanel();
	}
	
	private void createRmiPanel(){
		this.panelRMI = new JPanel();
		this.panelRMI.setBorder(BorderFactory.createTitledBorder("Initilaztion of RMI Server"));
		this.panelRMI.setVisible(false);
		this.panelRMI.setLayout(null);
	}
	
	private void addIpLabelToPanel(){
		JLabel ipLabel = new JLabel();
		ipLabel.setText(" Enter IP:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		ipLabel.setFont(font);
		ipLabel.setHorizontalAlignment(JLabel.LEFT);
		ipLabel.setBorder(BorderFactory.createTitledBorder("Enter IP number"));
		panelRMI.add(ipLabel).setBounds(10, 20, 280, 60);
	}
	
	private void addPortLabelToPanel(){
		JLabel portLabel = new JLabel();
		portLabel.setText(" Enter Port:");
		Font font = new Font("Consolas", Font.BOLD, 15);
		portLabel.setFont(font);
		portLabel.setHorizontalAlignment(JLabel.LEFT);
		portLabel.setBorder(BorderFactory.createTitledBorder("Enter Port number"));
		panelRMI.add(portLabel).setBounds(10, 90, 280, 60);
	}
	
	private void addIpTextFieldToPanel(){
		this.ipTextFiled = new JTextField();
		panelRMI.add(ipTextFiled).setBounds(100, 40, 182, 30);
	}
	
	private void addPortTextFieldToPanel(){
		JTextField portTextFiled = new JTextField();
		portTextFiled.setText("RMI Default (1099)");
		portTextFiled.setEditable(false);
		portTextFiled.setHorizontalAlignment(JTextField.CENTER);
		panelRMI.add(portTextFiled).setBounds(120, 110, 162, 30);
	}
	
	private void addStartServerButtonToPanel(){
		this.startServer= new JButton("Start Server");
		startServer.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		int port=1099;
	        		if(val.textComponetIsIpNumber(ipTextFiled.getText())!=true){
	        			val.dynamicErrorDialogWindow("ERRROR - Wrong IP Structure", "Ip number is not correct, correct IP is from 0.0.0.0 to 255.255.255.255");
	        		}else{
	        			RmiServer rmi = new RmiServer(gui, ipTextFiled.getText(), port);
	        			rmi.run();
	        			hideDetailPanelForRMI();
		        		gui.showServerSelectionPanel();
		        		gui.hideMainFrame();
	        		}
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("Error Manager - "+ex.toString(), ex.getMessage());
	        	 }
	         }
	    });
		panelRMI.add(startServer).setBounds(10, 160, 135, 50);
	}
	
	private void addBackToMenuButtonToPanel(){
		this.backToMenu= new JButton("Back To Menu");
		backToMenu.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
	        		hideDetailPanelForRMI();
	        		gui.showServerSelectionPanel();
	        	}catch(Exception ex){
	        		val.dynamicErrorDialogWindow("Error Manager - "+ex.toString(), ex.getMessage());
	        	 }
	         }
	    });
		panelRMI.add(backToMenu).setBounds(155, 160, 135, 50);
	}
	
	public void showDetailPanelForRMI(){
		this.panelRMI.setVisible(true);
		ipTextFiled.setText("");
	}
	
	public void hideDetailPanelForRMI(){
		this.panelRMI.setVisible(false);
	}
	
	public JPanel getJPanel(){
		return this.panelRMI;
	}

}
