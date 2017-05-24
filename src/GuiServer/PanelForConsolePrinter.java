package GuiServer;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelForConsolePrinter {
	private JPanel panelConsolePrinterView=null;
	private JTextArea textArea =null;
	private JScrollPane consoleViewScroll=null;
	private boolean systemOutPrint=false;
	public PanelForConsolePrinter(){
		createDatePanel();
		addJTextPaneToPanel();
		redirectingSystemOutPrintsToJTextArea();
		consoleViewScrollbarListener();
	}
	
	private void createDatePanel(){
		this.panelConsolePrinterView = new JPanel();
		this.panelConsolePrinterView.setBorder(BorderFactory.createTitledBorder("Console System Out Printer"));
		this.panelConsolePrinterView.setVisible(true);
		this.panelConsolePrinterView.setLayout(null);
	}
	
	private void addJTextPaneToPanel(){
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		consoleViewScroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelConsolePrinterView.add(consoleViewScroll).setBounds(10, 25, 390, 400);
	}
		
	public void redirectingSystemOutPrintsToJTextArea(){
		PrintStream printStream = new PrintStream(new OutputStream() {	
			@Override
			public void write(int b) throws IOException {
				textArea./*append(textArea.getText()+String.valueOf((char) b));*/setText(textArea.getText()+String.valueOf((char) b));
				systemOutPrint=true;
			}
		});
		System.setOut(printStream);
		System.out.println("Redirection Succsessfully Completed");
	}
	
	private void consoleViewScrollbarListener(){
		this.consoleViewScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
    		public void adjustmentValueChanged(AdjustmentEvent e) {
    			if(systemOutPrint==true){
    				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        			systemOutPrint=false;
    			}
			}  
        });
	}

	public JPanel getJPanel(){
		return this.panelConsolePrinterView;
	}
	
	public void clearConsolePrinter(){
		this.textArea.setText("");
	}
}
