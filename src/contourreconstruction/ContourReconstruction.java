package contourreconstruction;

import wolkvlak.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class ContourReconstruction extends Frame implements ItemListener, ActionListener, ChangeListener {
	static final long serialVersionUID = 914893476;

	static final int width = 600;
	static final int height = 400;
	
	MenuItem miOpenFile;
	MenuItem miFileExit;
	CheckboxMenuItem miDrawWolkpunten;
	CheckboxMenuItem miDrawCentroids;
	CheckboxMenuItem miDrawTangentLines;
	CheckboxMenuItem miDrawNormalVectors;
	CheckboxMenuItem miDrawSpanningTree;
	CheckboxMenuItem miDrawMarchedSquares;
	CheckboxMenuItem miUseBruteForceSquares;
	CheckboxMenuItem miUseFix1;
	CheckboxMenuItem miUseFix2;
	MenuItem miRedraw;
	JSlider range_grid, range_sample, range_planes_k, range_emst_k;
	
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
	    
		setLayout(new BorderLayout());
	    
	    wolkvlak = new Wolkvlak();
	    this.add(wolkvlak, BorderLayout.CENTER);
	    
		MenuBar menubar = new MenuBar();
		
		Menu mFile = new Menu("File");
		miOpenFile = new MenuItem("Opên filé...");
		miOpenFile.addActionListener(this);
		mFile.add(miOpenFile);
		mFile.add(new MenuItem("-"));
		miFileExit = new MenuItem("Exit");
		miFileExit.addActionListener(this);
		mFile.add(miFileExit);
		menubar.add(mFile);
		
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
		miDrawSpanningTree = new CheckboxMenuItem("Spanning tree", wolkvlak.drawspanningtree);
		miDrawSpanningTree.addItemListener(this);
		mDraw.add(miDrawSpanningTree);
		miDrawMarchedSquares = new CheckboxMenuItem("Marched squares", wolkvlak.drawmarchedsquares);
		miDrawMarchedSquares.addItemListener(this);
		mDraw.add(miDrawMarchedSquares);
		menubar.add(mDraw);
		
		Menu mTweaks = new Menu("Tweaks");
		miUseBruteForceSquares = new CheckboxMenuItem("Use brute force squares", wolkvlak.usebruteforcesquares);
		miUseBruteForceSquares.addItemListener(this);
		mTweaks.add(miUseBruteForceSquares);
		miUseFix1 = new CheckboxMenuItem("Use fix1 (prefer straight lines)", wolkvlak.usefix1);
		miUseFix1.addItemListener(this);
		mTweaks.add(miUseFix1);
		miUseFix2 = new CheckboxMenuItem("Use fix2 (better sharp corners)", wolkvlak.usefix2);
		miUseFix2.addItemListener(this);
		mTweaks.add(miUseFix2);
		miRedraw = new MenuItem("Redraw");
		miRedraw.addActionListener(this);
		mTweaks.add(miRedraw);
		menubar.add(mTweaks);
	
		setMenuBar(menubar);
		
		Container c = Box.createVerticalBox();
		c.setBackground(UIManager.getColor("control"));
		
		range_grid = new JSlider(1,200,wolkvlak.grid);
		range_sample = new JSlider(0,2000,(int)Math.round(wolkvlak.delta*100));
		range_planes_k = new JSlider(1,10,wolkvlak.k_planes);
		range_emst_k = new JSlider(0,10,wolkvlak.k_emst);
		
		range_grid.setMajorTickSpacing(199);
		range_grid.setMinorTickSpacing(10);
		range_grid.setPaintTicks(true);
		range_grid.setPaintLabels(true);
		
		range_sample.setMajorTickSpacing(500);
		range_sample.setMinorTickSpacing(100);
		range_sample.setPaintTicks(true);
		range_sample.setPaintLabels(true);
		Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 2000 ), new JLabel("20") );
		range_sample.setLabelTable( labelTable );
		
		range_planes_k.setMajorTickSpacing(9);
		range_planes_k.setMinorTickSpacing(1);
		range_planes_k.setPaintTicks(true);
		range_planes_k.setPaintLabels(true);
		range_planes_k.setSnapToTicks(true);
		
		range_emst_k.setMajorTickSpacing(10);
		range_emst_k.setMinorTickSpacing(1);
		range_emst_k.setPaintTicks(true);
		range_emst_k.setPaintLabels(true);
		range_emst_k.setSnapToTicks(true);
		
		range_grid.addChangeListener(this);
		range_sample.addChangeListener(this);
		range_planes_k.addChangeListener(this);
		range_emst_k.addChangeListener(this);
		
		c.add(new Label("Grid density:"));
		c.add(range_grid);
		c.add(new Label("Sample density:"));
		c.add(range_sample);
		c.add(new Label("Nr. of neighbours (planes):"));
		c.add(range_planes_k);
		c.add(new Label("Nr. of neighbours (MST):"));
		c.add(range_emst_k);
		
		add(c, BorderLayout.WEST);
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
		} else if (e.getSource() == miDrawSpanningTree) {
			wolkvlak.drawspanningtree = miDrawSpanningTree.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miDrawMarchedSquares) {
			wolkvlak.drawmarchedsquares = miDrawMarchedSquares.getState();
			wolkvlak.repaint();
		} else if (e.getSource() == miUseBruteForceSquares) {
			wolkvlak.usebruteforcesquares = miUseBruteForceSquares.getState();
			wolkvlak.recalcSquarePunten();
		} else if (e.getSource() == miUseFix1) {
			wolkvlak.usefix1 = miUseFix1.getState();
			wolkvlak.reloadWolk();
		} else if (e.getSource() == miUseFix2) {
			wolkvlak.usefix2 = miUseFix2.getState();
			wolkvlak.reloadWolk();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miRedraw) {
			wolkvlak.reloadWolk();
		} else if (e.getSource() == miOpenFile) {
			kiesBestand();
		} else if (e.getSource() == miFileExit) {
			dispose();
			System.exit(0);
		}
	}
	
	public void kiesBestand() {
		JFileChooser chooser = new JFileChooser(".");
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			wolkvlak.filename = chooser.getSelectedFile().getName();
			wolkvlak.reloadWolk();
		    }
	}
	
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == range_grid && !range_grid.getValueIsAdjusting()) {
			wolkvlak.grid = range_grid.getValue();
			wolkvlak.recalcSquarePunten();
		} else if (e.getSource() == range_sample && !range_sample.getValueIsAdjusting()) {
			wolkvlak.delta = range_sample.getValue() / 100.0;
			wolkvlak.recalcSquarePunten();
		} else if (e.getSource() == range_planes_k && !range_planes_k.getValueIsAdjusting()) {
			wolkvlak.k_planes = range_planes_k.getValue();
			wolkvlak.reloadWolk();
		} else if (e.getSource() == range_emst_k && !range_emst_k.getValueIsAdjusting()) {
			wolkvlak.k_emst = range_emst_k.getValue();
			wolkvlak.reloadWolk();
		} else if (e.getSource() == range_emst_k && !range_emst_k.getValueIsAdjusting()) {
			wolkvlak.k_emst = range_emst_k.getValue();
			wolkvlak.reloadWolk();
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
