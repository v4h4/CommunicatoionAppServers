package BlobManagement;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import GuiServer.ValidationDialogGUI;
import GuiServerSelection.MainFrameServerSelection;
import GuiServerSelection.PanelForServerSelection;


public class BlobFrame {
	private PanelForBlobControl blobControl =null;
	private PalenForBlobTable blobTable=null;
	private PanelForServerSelection panelForServerSelection=null;
	private ValidationDialogGUI val = null;
	private JFrame mainFrame = null;
	private MainFrameServerSelection gui=null;
	
	public BlobFrame(MainFrameServerSelection gui,PanelForServerSelection panelForServerSelection){
		this.gui=gui;
		this.panelForServerSelection=panelForServerSelection;
		createBlobFrameSelection();
		this.val= new ValidationDialogGUI(mainFrame);
		addBlobTablePanelTpFrame();
		addBlobControlPanelTpFrame();
	}
	
	
	private void createBlobFrameSelection(){
		this.mainFrame= new JFrame();
		this.mainFrame.setTitle("BLOB Manager");
		this.mainFrame.setVisible(false);
		this.mainFrame.setSize(465, 475);
		this.mainFrame.setLayout(null);
		this.mainFrame.setLocation(10, 10);
		this.mainFrame.setResizable(false);
		this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void addBlobControlPanelTpFrame(){
		this.blobControl = new PanelForBlobControl(this,panelForServerSelection);
		mainFrame.add(blobControl.getJPanel()).setBounds(5, 10, 450, 120);
	}
	
	private void addBlobTablePanelTpFrame(){
		this.blobTable = new PalenForBlobTable(val,this);
		mainFrame.add(blobTable.getJPanel()).setBounds(5, 130, 450, 310);
	}
	
	public void showBlobFrame(){
		updateTable();
		getBlobStatus();
		this.mainFrame.setVisible(true);
	}
	
	public void hideBlobFrame(){
		this.mainFrame.setVisible(false);
	}
	
	public void updateTable(){
		this.blobTable.updateTable();
	}
	
	public boolean getBlobStatus(){
		return this.blobControl.getBlobStatus();
	}
	
	public MainFrameServerSelection getServerSelectionGUI(){
		return this.gui;
	}
}
