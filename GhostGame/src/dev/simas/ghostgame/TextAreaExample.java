package dev.simas.ghostgame;

import java.awt.*;  
import java.awt.event.*;  
public class TextAreaExample extends Frame implements ActionListener{ 
	
	private static final long serialVersionUID = 1L;
	Label l1,l2;  
	TextArea area;  
	Button b;  
	private boolean close = false;
	
	TextAreaExample(){  
	    l1=new Label();  
	    l1.setBounds(50,50,100,30);  
	    l2=new Label();  
	    l2.setBounds(160,50,100,30);  
	    area=new TextArea();  
	    area.setBounds(20,100,300,300);  
	    b=new Button("Blow computer up");  
	    b.setBounds(100,400,100,30);  
	    b.addActionListener(this);
	    add(l1);add(l2);add(area);add(b);  
	    setSize(400,450);  
	    setLayout(null);  
	    setVisible(true);
	}  
	
	public TextArea getTextArea() {
		return area;
	}
	
	public void actionPerformed(ActionEvent e){  
	    
		close = true;
	    
	}  
	
	public boolean getClose() {
		return close;
	}
	
}  