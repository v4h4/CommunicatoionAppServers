package GuiServer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ValidationDialogGUI{
	private JFrame mainFrame=null;
	
	public ValidationDialogGUI(JFrame mainFrame){
		this.mainFrame=mainFrame;
	}

	@SuppressWarnings("static-access")
	public boolean dynamicWarningDialogWindow(String title,String message){
		disableFrame();
		JOptionPane errorDialog= new JOptionPane();
		errorDialog.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		while(true){
			if(errorDialog.isShowing()==false){
				enableFrame();
				return true;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean dynamicErrorDialogWindow(String title,String message){
		disableFrame();
		JOptionPane errorDialog= new JOptionPane();
		errorDialog.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
		while(true){
			if(errorDialog.isShowing()==false){
				enableFrame();
				return true;
			}
		}	
	}
	
	public boolean dynamicConfirmationDialog(String title,String message){
		disableFrame();
		JFrame dialog = new JFrame(); 
		int dialogResult = JOptionPane.showConfirmDialog(dialog, message, title,JOptionPane.YES_NO_OPTION);
		
		
		while(true){
			if (dialogResult == JOptionPane.YES_OPTION) {
				enableFrame();
				return true;
			}else{
				enableFrame();
				return false;
			}
		}
	}	
	
	public boolean textComponetIsEmpty(String textComponent){
		char empty=' ';
		int counter=0;
		for(int i=0;i<textComponent.length();i++ ){
			if(textComponent.charAt(i)==empty){
				counter++;
			}
		}
		if(counter==textComponent.length()){
			return true;
		}
		return false;
	}
	
	private boolean textComponentIsIpNumeric(String textComponent){
		try{
			Integer.parseInt(textComponent);
			if(textComponent.length()>3){
				return false;
			}
		}catch(Exception ex){
			return false;
		}
		return true;
	}
	
	private boolean toManyIpDots(String ip){
		if(!ip.contains("..") && !ip.contains("...") && ip.length()<16 && ip.length()>6){
			int counter=0;
			for(int i=0;i<ip.length();i++){
				if(ip.charAt(i)=='.'){
					counter++;
				}
			}
			if(counter>0 && counter<4){
				return false;
			}
		}
		return true;
	}
	
	public boolean textComponetIsIpNumber(String ip){
		if(toManyIpDots(ip)==true){
			return false;
		}
		String lastSub="";
		int beginIndex=0;
		for(int i=1;i<=ip.length();i++ ){
			String txt=ip.substring(beginIndex, i);
			if(textComponentIsIpNumeric(txt)==true){
				lastSub=txt;
			}
			if(textComponentIsIpNumeric(txt)==false && ip.charAt(i-1)=='.'){
				
				if(lastSub.length()>0 && lastSub.length()<=4){
					beginIndex=i;
				}
			}
			else if(textComponentIsIpNumeric(txt)==false){
				return false;
			}
		}
		return true;
	}
	
	private void disableFrame(){
		if(this.mainFrame!=null){
			this.mainFrame.setEnabled(false);
		}
	}
	
	private void enableFrame(){
		if(this.mainFrame!=null){
			this.mainFrame.setEnabled(true);
		}
	}
}
