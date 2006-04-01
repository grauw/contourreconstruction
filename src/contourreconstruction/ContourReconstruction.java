package contourreconstruction;

import wolkvlak.*;

import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ContourReconstruction extends Frame implements ItemListener{
	static final long serialVersionUID = 914893476;

	static final int width = 500;
	static final int height = 400;
	
	CheckboxMenuItem miDrawWolkpunten;
	CheckboxMenuItem miDrawCentroids;
	CheckboxMenuItem miDrawTangentLines;
	CheckboxMenuItem miDrawNormalVectors;
	CheckboxMenuItem miSignedDistance;
	
	Wolkvlak wolkvlak;
	
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

	    wolkvlak = new Wolkvlak("prac3-1.txt");
	    this.add(wolkvlak);
	    
		MenuBar menubar = new MenuBar();
		Menu mDraw = new Menu("Draw");
		miDrawWolkpunten = new CheckboxMenuItem("Wolkpunten", wolkvlak.drawwolkpunten);
		miDrawWolkpunten.addItemListener(this);
		mDraw.add(miDrawWolkpunten);
		miDrawCentroids = new CheckboxMenuItem("Centroids", wolkvlak.drawcentroids);
		miDrawCentroids.addItemListener(this);
		mDraw.add(miDrawCentroids);
		miDrawTangentLines = new CheckboxMenuItem("Tangent lines", wolkvlak.drawtangentlines);
		miDrawTangentLines.addItemListener(this);
		mDraw.add(miDrawTangentLines);
		miDrawNormalVectors = new CheckboxMenuItem("Normal vectors", wolkvlak.drawnormalvectors);
		miDrawNormalVectors.addItemListener(this);
		mDraw.add(miDrawNormalVectors);
		miSignedDistance = new CheckboxMenuItem("Normal vectors", wolkvlak.drawsigneddistance);
		miSignedDistance.addItemListener(this);
		mDraw.add(miSignedDistance);
		menubar.add(mDraw);
		setMenuBar(menubar);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == miDrawWolkpunten) {
			wolkvlak.drawwolkpunten = miDrawWolkpunten.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miDrawCentroids) {
			wolkvlak.drawcentroids = miDrawCentroids.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miDrawNormalVectors) {
			wolkvlak.drawnormalvectors = miDrawNormalVectors.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miDrawTangentLines) {
			wolkvlak.drawtangentlines = miDrawTangentLines.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miSignedDistance) {
			wolkvlak.drawsigneddistance = miSignedDistance.getState();
			wolkvlak.repaint();
		}
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
