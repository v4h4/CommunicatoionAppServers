package BlobManagement;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import GuiServer.ValidationDialogGUI;

public class PalenForBlobTable {
	private JPanel blobTablePanel = null;
	private JTable blobTable =null;
	private String[][] blobData=null;
	private JScrollPane blobTableScroll=null;
	private JPopupMenu rightclickMenu = null; 
	private BlobFrame blobFrame=null;
	private ValidationDialogGUI val=null;
	public PalenForBlobTable(ValidationDialogGUI val,BlobFrame blobFrame){
		this.val=val;
		this.blobFrame=blobFrame;
		createBlobTalePanel(getFilesFromBlob());
	}
	
	private void createBlobTalePanel(File[] files){
		this.blobTablePanel = new JPanel();
		this.blobTablePanel.setBorder(BorderFactory.createTitledBorder("BLOB Viewer"));
		this.blobTablePanel.setVisible(true);
		this.blobTablePanel.setLayout(null);
		createTabele(files);
		startBlobTableRightClickListener();
		createRightClickPopupMenu();
	}
	
	private String[][] createTableData(File[] files){
		int size=15;
		if(files.length>100){
			size=files.length;
		}
		String[][] blobData= new String[size][3];
		for(int i=0;i<size;i++){
			if(i<files.length){
				blobData[i][0]=getFileName(files[i].getName());
				blobData[i][1]=getFileType(files[i].getName());
				blobData[i][2]=new DecimalFormat("#.#").format(((double)files[i].length()/(double)1000000))+" MB";
			}else{
				blobData[i][0]="";
				blobData[i][1]="";
				blobData[i][2]="";	
			}
		}
		return blobData;
	}
	
	private void createTabele(File[] files){
		String [] blobTitles={"FileName","Filetype","Size"};
		this.blobData=createTableData(files);
		this.blobTable=new JTable(blobData,blobTitles){
			private static final long serialVersionUID = 1L;
			@Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    blobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//only one row cal be selected
	    blobTable.getColumnModel().getColumn(0).setPreferredWidth(200);//sets fileName column in 200 pixels in size in the withd
		blobTable.getColumnModel().getColumn(1).setPreferredWidth(80);//sets fileType column in 80 pixels in size in the withd
		blobTable.getColumnModel().getColumn(2).setPreferredWidth(80);//sets fileSize column in 80 pixels in size in the withd
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		blobTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);//centers cellvalue fileType
		blobTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);//centers cellvalue fileSize
		blobTable.setPreferredScrollableViewportSize(new Dimension(400,400));
		blobTable.getTableHeader().setReorderingAllowed(false);
		blobTable.setFillsViewportHeight(true);
		for(int y=0;y<blobData.length;y++){
	    	for(int x=0;x<blobData[1].length;x++){
		    	blobTable.isCellEditable(y, x);
		    }
	    }
		blobTable.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent mouseEvent) {
				 if(SwingUtilities.isRightMouseButton(mouseEvent)){
					 rightclickMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	    		 }
			}
			public void mousePressed(MouseEvent mouseEvent) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
    	});
		blobTableScroll = new JScrollPane(blobTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		blobTablePanel.add(blobTableScroll).setBounds(10, 20, 430, 278);
	}
	
	private String getFileType(String fileName){
		int length=0;
		for(int i=fileName.length()-1;i>=0;i--){
			if(fileName.charAt(i)!='.'){
				length++;
			}else{
				length++;
				break;
			}
		}
		char[] string = new char[length];  
		int index=(length-1);
		for(int i=(fileName.length()-1);i>=0 && index>=0;i--){
				string[(index)]=fileName.charAt(i);
				index--;
		}
		return new String(string);
	}
	
	private String getFileName(String fileName){
		int length=0;
		for(int i=fileName.length()-1;i>=0;i--){
			if(fileName.charAt(i)!='.'){
				length++;
			}else{
				length++;
				break;
			}
		}
		char[] string = new char[length];  
		int index=(length-1);
		for(int i=(fileName.length()-1);i>=0 && index>=0;i--){
				string[(index)]=fileName.charAt(i);
				index--;
		}
		return fileName.substring(0,(fileName.length()-length));
	}
	
	private File[] getFilesFromBlob(){
		try{
			File[] files = new File("Blobs").listFiles();
			for(int i=0;i<files.length;i++){
				System.out.println("files["+i+"].getName() == "+files[i].getName());
				System.out.println("files[i].length() == "+files[i].length());
			}
			return files;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	private void createRightClickPopupMenu(){
		 rightclickMenu = new JPopupMenu("Popup");
		 JMenuItem delteBlob = new JMenuItem("Delete selected blob");
		 rightclickMenu.add(delteBlob);
		 delteBlob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int x=blobTable.getSelectedRow();
					String file=blobData[x][0]+blobData[x][1];
					if(val.textComponetIsEmpty(file)==false && val.dynamicConfirmationDialog("Delete File", "Are you sure that you want to delete this selected File:\n"+file)){
						deleteBlob((blobData[x][0]+blobData[x][1]));
						//hidePopupMenu();
					}else if(val.textComponetIsEmpty(file)==true){
						val.dynamicErrorDialogWindow("Error User Selection", "You have selected a row where no FILE is located");
					}
				}catch(Exception ex){
					val.dynamicErrorDialogWindow("Error "+ex.getCause(), ex.getMessage());
					//hidePopupMenu();
				}
			}
		 });
		 		 
		 JMenuItem delteAllBlobs = new JMenuItem("Delete All Blobs");
		 rightclickMenu.add(delteAllBlobs);
		 delteAllBlobs.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(val.dynamicConfirmationDialog("Delete All Files", "Are you sure that you want to delete all Files?")==true){
						deleteAllBlobs();
					}
				}
		 });
	}
	
	private void startBlobTableRightClickListener(){
    	blobTable.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent mouseEvent) {
				 if(SwingUtilities.isRightMouseButton(mouseEvent)){
					 rightclickMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	    		 }
			}
			
			public void mousePressed(MouseEvent mouseEvent) {}
			
			public void mouseEntered(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			public void mouseReleased(MouseEvent arg0) {}
    	});
    }
	
	private void deleteBlob(String blob){
		File[] files=getFilesFromBlob();
		for(int i=0;i<files.length;i++){
			if(files[i].getName().equals(blob)){
				files[i].delete();
			}
		}
		updateTable();
		blobFrame.getBlobStatus();
		
		
	}
	
	private void deleteAllBlobs(){
		File[] files=getFilesFromBlob();
		for(int i=0;i<files.length;i++){
			files[i].delete();
		}
		updateTable();
		blobFrame.getBlobStatus();
	}
	
	public JPanel getJPanel(){
		return this.blobTablePanel;
	}
	
	public void updateTable(){
		String [] blobTitles={"FileName","Filetype","Size"};
		this.blobData=createTableData(getFilesFromBlob());
		DefaultTableModel standard = new DefaultTableModel(blobData,blobTitles);
		standard.fireTableDataChanged();
		blobTable.setModel(standard);
	}
	 
}
