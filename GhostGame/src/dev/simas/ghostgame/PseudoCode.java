package dev.simas.ghostgame;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;



public class PseudoCode {
	
	private Objects gameObjects;
	private Rectangle TextArea;
	private TextAreaExample test;
	private boolean showThis;
	
	private String[] instructions;
	
	
	public PseudoCode(Objects gameObjects) {
		this.gameObjects = gameObjects;
		TextArea = new Rectangle(170, 256, 300 ,192); 
		showThis = false;
	}
	

	public void update() {

		if(gameObjects.getGame().getKeyManager().pButton) {
			if((test == null) && (showThis == false)) {
				showThis = true;
				gameObjects.getGame().getKeyManager().pButton = false;
				test = new TextAreaExample();
			}		
		}
		
		if((test != null) && (test.getClose())) {
			showThis = false;

			String textArea = test.getTextArea().getText();
			instructions = textArea.split("\n");
			
			test.dispose();
			test = null;
		}
			
	}
	
	public void render(Graphics g) {

				
	}
	
	public boolean getShow() {
		return showThis;
	}

	public void changeShow(boolean value) {
		showThis = value;
	}

	public String[] getInstructions() {
		return instructions;
	}
	
	public void setInstNull() {
		instructions = null;
	}
	
}
