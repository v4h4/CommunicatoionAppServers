package BlobManagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import GuiServer.ValidationDialogGUI;
import GuiServerSelection.MainFrameServerSelection;
import GuiServerSelection.PanelForServerSelection;

public class PanelForBlobControl {
	private BlobFrame blobFrame=null;
	private PanelForServerSelection serverSelectionPanel=null;
	private JPanel controlPanel=null;
	private JButton quitButton = null;
	private JButton startButton = null;
	private JButton saveToBlobButton = null;
	private JLabel approvedBlobLabel=null;
	private ValidationDialogGUI val=null;
	private MainFrameServerSelection gui=null;
	public PanelForBlobControl(BlobFrame blobFrame,PanelForServerSelection panelForServerSelection){
		this.blobFrame=blobFrame;
		this.gui=blobFrame.getServerSelectionGUI();
		this.serverSelectionPanel=panelForServerSelection;
		createQuitPanel();
		addQuitButtonToPanel();
		addStartServerButtonToPanel();		
		addSaveFilesToBlobButtonToPanel();
		addApprovedBlobLabelToPanel();
		this.val= new ValidationDialogGUI(null);
	}
	
	
	private void createQuitPanel(){
		this.controlPanel = new JPanel();
		this.controlPanel.setBorder(BorderFactory.createTitledBorder("BLOB Control"));
		this.controlPanel.setVisible(true);
		this.controlPanel.setLayout(null);
	}
	
	private void addQuitButtonToPanel(){
		this.quitButton= new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	        	try{
	        		System.exit(0);
				}catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	    });
		controlPanel.add(quitButton).setBounds(10, 20, 100, 30);
	}
	
	private void addStartServerButtonToPanel(){
		this.startButton= new JButton("Start Server");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	        	try{
	        		if(blobIsApproved(getFilesFromBlob())==true){
	        			blobFrame.hideBlobFrame();
	        			gui.showMainFrame();
	        			serverSelectionPanel.showServerSelection();
	        		}else if(blobIsApproved(getFilesFromBlob())==false){
	        			String message="The server BLOB must contain at least 6 Files that are:"
	        					+"\n* approximately 500 kb"
	        					+"\n* approximately 1 mb"
	        					+"\n* approximately 5 mb"
	        					+"\n* 10mb - 15 mb "
	        					+"\n* 25mb - 75mb"
	        					+"\n* Bigger then 100mb"
	        					+"\n You have to add files to the BLOB?";
	        			val.dynamicWarningDialogWindow("Multiple Servers does not have an approved BLOB",message);
	        		}
				}catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	    });
		controlPanel.add(startButton).setBounds(115, 20, 120, 30);
	}
	
	private void addSaveFilesToBlobButtonToPanel(){
		this.saveToBlobButton= new JButton("Save Files to BLOB");
		saveToBlobButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	try{
					selectAndSaveDialog();
					blobFrame.updateTable();
					getBlobStatus();
	        	}catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	    });
		controlPanel.add(saveToBlobButton).setBounds(240, 20, 195, 30);
	}
	
	private void addApprovedBlobLabelToPanel(){
		this.approvedBlobLabel = new JLabel();
		Font font = new Font("Consolas", Font.BOLD, 15);
		approvedBlobLabel.setFont(font);
		getBlobStatus();
		approvedBlobLabel.setHorizontalAlignment(JLabel.CENTER);
		approvedBlobLabel.setBorder(BorderFactory.createTitledBorder("BLOB Status"));
		controlPanel.add(approvedBlobLabel).setBounds(10, 55, 427, 50);
	}
	
	private boolean blobIsApproved(File[] files){
		Boolean[] boolArr = new Boolean[6];
		for(int i=0;i<boolArr.length;i++){
			boolArr[i]=false;
		}
		for(int i=0;i<files.length;i++){
			if(files[i].length()>=450000 && files[i].length()<716800){//checks if file is approximately 500kb 
				boolArr[0]=true;
			}
			else if(files[i].length()>=800000 && files[i].length()<2097152){//checks if file is approximately 1mb-2mb 
				boolArr[1]=true;
			}
			else if(files[i].length()>=5242880 && files[i].length()<7340032){//checks if file is approximately 5mb - 7mb
				boolArr[2]=true;
			}
			else if(files[i].length()>=10485760 && files[i].length()<15728640){//checks if file is approximately 10mb -15mb
				boolArr[3]=true;
			}
			else if(files[i].length()>=26214400 && files[i].length()<78643200){//checks if file is approximately 25-75mb 
				boolArr[4]=true;
			}
			else if(files[i].length()>=104857600){//checks if file is bigger 100mb 
				boolArr[5]=true;
			}
		}
		for(int i=0;i<boolArr.length;i++){
			//System.out.println("boolarArr["+i+"] == "+boolArr[i]);
			if(boolArr[i]==false){
				return false;
			}		
		}
		return true;
	}
	
	private void selectAndSaveDialog(){
		try{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save the file to BLOB");
			//fileChooser.getSelectedFile()
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			    File file = fileChooser.getSelectedFile();
			    FileOutputStream fileOutputStream = new FileOutputStream("Blobs\\"+file.getName());
			    byte[] byteFile = convertFileToByteArray_IO(file);
			    fileOutputStream.write(byteFile);
			    fileOutputStream.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private byte[] convertFileToByteArray_IO(File file){
		FileInputStream fileInputStream=null;
        byte[] byteFile = new byte[(int) file.length()];
		try {
			fileInputStream = new FileInputStream(file);
		    fileInputStream.read(byteFile);
		    fileInputStream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
		return byteFile;
	}
	
	private File[] getFilesFromBlob(){
		try{
			File[] files = new File("Blobs").listFiles();
			return files;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public JPanel getJPanel(){
		return this.controlPanel;
	}
	
	public boolean getBlobStatus(){
		if(blobIsApproved(getFilesFromBlob())==true){
			approvedBlobLabel.setForeground(new Color(0,150,0));
			approvedBlobLabel.setText("THE BLOB IS APPROVED TO BE ABLE TO START SERVER");
			return true;
		}else{
			approvedBlobLabel.setForeground(Color.RED);
			approvedBlobLabel.setText("THE BLOB IS NOT APPROVED TO BE ABLE TO START SERVER");
			return false;
		}
	}	
}
