package GuiServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import GuiServerSelection.MainFrameServerSelection;

public class PanelForServerControl {
	private JPanel controlPanel=null;
	private JButton quitButton = null;
	private JButton clearButton = null;
	private String title="+ ......title..... +";
	private MainFrameServerGUI gui=null;
	private MainFrameServerSelection serverSelectionGUI=null;
	private ShutDownServerProtocol shutDownServer = null;
	public PanelForServerControl(MainFrameServerGUI gui,MainFrameServerSelection serverSelectionGUI,ShutDownServerProtocol shutDownServer,String title){
		this.shutDownServer=shutDownServer;
		this.serverSelectionGUI=serverSelectionGUI;
		this.gui=gui;
		this.title=title;
		createQuitPanel();
		addQuitButtonToPanel();
		addClearConsolePrinterButtonToPanel();
	}
	
	
	private void createQuitPanel(){
		this.controlPanel = new JPanel();
		this.controlPanel.setBorder(BorderFactory.createTitledBorder(title+" Server"));
		this.controlPanel.setVisible(true);
		this.controlPanel.setLayout(null);
	}
	
	private void addQuitButtonToPanel(){
		this.quitButton= new JButton("Quit "+title+" Server");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	        	try{
	        		 serverSelectionGUI.showMainFrame();
	        		 gui.disposeMainFrameServerGUI();
	        		 shutDownServer.shutDownServer();
				}catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	    });
		controlPanel.add(quitButton).setBounds(10, 25, 185, 30);
	}
	

	
	private void addClearConsolePrinterButtonToPanel(){
		this.clearButton= new JButton("Clear Console Printer");
		clearButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
					gui.clearConsolePrinter();
	        	}catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	    });
		controlPanel.add(clearButton).setBounds(210, 25, 185, 30);
	}

	public JPanel getJPanel(){
		return this.controlPanel;
	}
}
