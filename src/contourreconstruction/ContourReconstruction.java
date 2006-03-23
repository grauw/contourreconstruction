package contourreconstruction;

import wolkvlak.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ContourReconstruction extends Frame {
	static final long serialVersionUID = 914893476;

	static final int width = 500;
	static final int height = 400;
	
	public ContourReconstruction() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (InstantiationException e) {
	    } catch (ClassNotFoundException e) {
	    } catch (UnsupportedLookAndFeelException e) {
	    } catch (IllegalAccessException e) {
	    }
		
	    Wolkvlak wf = new Wolkvlak("prac3-1.txt");
	    
	    this.add(wf);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ContourReconstruction mainFrame = new ContourReconstruction();
		mainFrame.setTitle("ContourReconstruction");
		mainFrame.setSize(ContourReconstruction.width+10, ContourReconstruction.height+50);
		mainFrame.setVisible(true);
	}

}
