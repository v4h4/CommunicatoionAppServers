package ApplicationStart;


import BlobManagement.BlobFrame;
import GuiServer.ValidationDialogGUI;
import GuiServerSelection.MainFrameServerSelection;

public class StartServerApplication {
	
	public static void main(String[] args) {
		MainFrameServerSelection gui = new MainFrameServerSelection();
		BlobFrame blobFrame = gui.getBlobManager();
		if(blobFrame.getBlobStatus()==true){
			gui.showMainFrame();
		}else{
			String message="The server BLOB must contain at least 6 Files that are:"
					+"\n* approximately 500 kb"
					+"\n* approximately 1 mb"
					+"\n* approximately 5 mb"
					+"\n* 10mb - 15 mb "
					+"\n* 25mb - 75mb"
					+"\n* Bigger then 100mb"
					+"\n You have to add files to the BLOB?";
			new ValidationDialogGUI(null).dynamicWarningDialogWindow("Multiple Servers does not have an approved BLOB",message);
			blobFrame.showBlobFrame();
		}
	}
}