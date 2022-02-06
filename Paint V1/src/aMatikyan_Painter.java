/*
 * Arden Matikyan
 * Prog 2 Pd 8 
 * Basic Paint application 
 * Use arrayList of x y points to draw images of different color/brush size
 */
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class aMatikyan_Painter extends JFrame implements ActionListener {

	private Color currentColor;
	private JColorChooser colorChooser;
	private ArrayList<Point> points;
	private int bSize;

	public aMatikyan_Painter() {

		setSize(375, 590);
		setTitle("Click Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		colorChooser = new JColorChooser();

		bSize = 10;
		currentColor = Color.black;

		DrawPanel canvas = new DrawPanel();
		canvas.setBounds(0, 0, 375, 510);

		JButton clearCanv = new JButton("Clear");
		clearCanv.setBounds(2, 480, 365, 45);
		clearCanv.addActionListener(this);

		JMenuBar menu = new JMenuBar();

		// new menu item for file =
		JMenu file = new JMenu("File");

		// add items to menu
		JMenuItem load = new JMenuItem("Load");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem impose = new JMenuItem("Impose");
		file.add(load);
		file.add(save);
		file.add(impose);

		// new menu item for options
		JMenu options = new JMenu("Options");
		// add items to the menu
		JMenuItem color = new JMenuItem("Color");
		JMenu size = new JMenu("Size");

		JMenuItem sz1 = new JMenuItem("10");
		JMenuItem sz2 = new JMenuItem("25");
		JMenuItem sz3 = new JMenuItem("50");

		size.add(sz1);
		size.add(sz2);
		size.add(sz3);

		options.add(color);
		options.add(size);

		// trigger events for each item
		sz1.addActionListener(this);
		sz2.addActionListener(this);
		sz3.addActionListener(this);
		color.addActionListener(this);
		size.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		impose.addActionListener(this);

		// add menus to bar
		menu.add(file);
		menu.add(options);

		setJMenuBar(menu);

		add(canvas);
		add(clearCanv);
		setVisible(true);
	}

	// draw image from existing file 
	public void drawImage() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.out.println("That look and feel is not found");
			System.exit(-1);
		}

		JFileChooser jfc = new JFileChooser();
		// have the box pop up in the current directory
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));

		// pop up the file menu box, "null" means we don't want to associate it with
		// component
		int result = jfc.showOpenDialog(null);

		File f = null;
		if (result == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
			
			Scanner fileIn = null; 
			try {
				fileIn = new Scanner(f);
				
				// each line in file adds new point
				while(fileIn.hasNextInt()) {
					int x = fileIn.nextInt();
					int y = fileIn.nextInt(); 
					int r = fileIn.nextInt();
					int g = fileIn.nextInt();
					int b = fileIn.nextInt();
					int sz = fileIn.nextInt();
					points.add(new Point(x, y, new Color(r, g, b), sz));
				}
				repaint();
				
			} catch (IOException e) {
				System.out.println("Error saving to file specified");
				System.exit(-1);
			}

		}
		
		
	}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("Clear")) {
			points.clear();
			repaint();
		} else if (ae.getActionCommand().equals("10")) {
			bSize = 10;
		} else if (ae.getActionCommand().equals("25")) {
			bSize = 25;
		} else if (ae.getActionCommand().equals("50")) {
			bSize = 50;
		} else if (ae.getActionCommand().equals("Color")) {
			Color newColor = colorChooser.showDialog(null, "Select a color", currentColor);
			if (newColor != null)
				currentColor = newColor;
		} else if (ae.getActionCommand().equals("Save")) {
			// save current image to a file 
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
				System.out.println("That look and feel is not found");
				System.exit(-1);
			}

			JFileChooser jfc = new JFileChooser();
			// have the box pop up in the current directory
			jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));

			// pop up the file menu box, "null" means we don't want to associate it with
			// component
			int result = jfc.showSaveDialog(null);

			File f = null;
			if (result == JFileChooser.APPROVE_OPTION) {
				f = jfc.getSelectedFile();

				try {
					FileWriter output = new FileWriter(f);
					
					// add each points components to line in file 
					for (Point nextP : points) {
						Color nextCol = nextP.ptColor;
						output.write(nextP.xLoc + " " + nextP.yLoc + " " + nextCol.getRed() + " " + nextCol.getGreen()
								+ " " + nextCol.getBlue() + " " + nextP.ptSize + "\n");
					}
					output.close();
				} catch (IOException e) {
					System.out.println("Error saving to file specified");
					System.exit(-1);
				}

			}
		} else if (ae.getActionCommand().equals("Load")) {
			points.clear();
			drawImage();

		} else if (ae.getActionCommand().equals("Impose")) {
			drawImage();
		}

	}

	public class Point {

		private int xLoc;
		private int yLoc;
		Color ptColor;
		int ptSize;

		public Point(int x, int y, Color col, int sz) {
			xLoc = x;
			yLoc = y;
			ptColor = col;
			ptSize = sz;
		}
	}

	public class DrawPanel extends JPanel implements MouseMotionListener {

		public DrawPanel() {
			// listens for a mouse to be dragged
			this.addMouseMotionListener(this);
			points = new ArrayList<Point>();

		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			setBackground(Color.white);

			// repaint every point
			for (Point nextP : points) {
				// set the color for the point
				g.setColor(nextP.ptColor);
				// draw the point
				g.fillOval(nextP.xLoc, nextP.yLoc, nextP.ptSize, nextP.ptSize);
			}

		}

		public void mouseDragged(MouseEvent me) {

			// add a new point with default or specified options
			points.add(new Point(me.getX(), me.getY(), currentColor, bSize));
			this.repaint();
		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public static void main(String[] args) {
		new aMatikyan_Painter();
	}

}
