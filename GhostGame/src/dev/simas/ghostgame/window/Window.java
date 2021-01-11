package dev.simas.ghostgame.window;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window {

	private JFrame frame;
	private Canvas canvasBox;
	
	public Window(String title, int width, int height) {
		
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvasBox = new Canvas();
		canvasBox.setPreferredSize(new Dimension(width, height));
		canvasBox.setMaximumSize(new Dimension(width, height));
		canvasBox.setMinimumSize(new Dimension(width, height));
		canvasBox.setFocusable(false);
		
		frame.add(canvasBox);
		frame.pack();
	}
	
	public Canvas getCanvasBox() {
		return canvasBox;
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
