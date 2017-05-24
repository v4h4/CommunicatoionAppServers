package GuiServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import GuiServerSelection.MainFrameServerSelection;

public class MainFrameServerGUI {
	private JFrame mainFrame = new JFrame();
	private PanelForConsolePrinter panelForConsolePrinter=null;
	private PanelForServerControl panelForServerControl=null;
	private MainFrameServerSelection serverSelectionGUI=null;
	private ValidationDialogGUI val=null;
	private String title=null;
	private ShutDownServerProtocol shutDown = null;
	public MainFrameServerGUI(MainFrameServerSelection serverSelectionGUI,ShutDownServerProtocol shutDown,String title){
		this.shutDown=shutDown;
		this.title=title;
		this.serverSelectionGUI=serverSelectionGUI;
		startServerWindowGUI(title);
		this.val= new ValidationDialogGUI(mainFrame);
		addpanelForConsolePrinterToMainFrame();
		addpanelForServerControlToMainFrame();
	
	}
	
	private void startServerWindowGUI(String title){
		try{
			this.mainFrame = new JFrame();
			mainFrame.setTitle(title);
			mainFrame.setVisible(false);
			mainFrame.setSize(425, 550);
			mainFrame.setLayout(null);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setResizable(false);
			mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			mainFrame.setLocation(10, 10);
			mainFrameCloseListener();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void addpanelForServerControlToMainFrame(){
		this.panelForServerControl = new PanelForServerControl(this,serverSelectionGUI, shutDown,"Server Control");
		mainFrame.add(panelForServerControl.getJPanel()).setBounds(5, 5, 410, 75);
	}
	
	private void addpanelForConsolePrinterToMainFrame(){
		this.panelForConsolePrinter = new PanelForConsolePrinter();
		mainFrame.add(panelForConsolePrinter.getJPanel()).setBounds(5, 85, 410, 435);
	}
	
	private void mainFrameCloseListener(){
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				boolean close=val.dynamicConfirmationDialog("Closing down "+title, "Are you sure that you want to close "+title+"?");
				if(close == true){
					mainFrame.dispose();
					serverSelectionGUI.showMainFrame();
				}	
			}
		});
	}
	
	public void clearConsolePrinter(){
		panelForConsolePrinter.clearConsolePrinter();
	}
	
	public void showMainFrameServerGUI(){
		this.mainFrame.setVisible(true);
	}
	
	public void disposeMainFrameServerGUI(){
		this.mainFrame.dispose();;
	}
}
