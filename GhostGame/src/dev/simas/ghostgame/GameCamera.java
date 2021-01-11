package dev.simas.ghostgame;

import dev.simas.ghostgame.entities.Entity;

public class GameCamera {

	private float yOffset;
	private MainLoop game;
	
	public GameCamera(MainLoop game, float y) {
		this.game = game;
		this.yOffset = y;
	}
	
	public void centerCamera(Entity e) {
		
		yOffset = (e.getY() - 256) - MainLoop.HEIGHT / 2;
		
	    //Keep the camera in bounds

	    if (yOffset < 0)
	    {
	    	yOffset = 0;
	    }
		
		if (e.getY() + 144 >= MainLoop.LEVEL_HEIGHT) // cia 16 pridejau
		{
			yOffset = (MainLoop.LEVEL_HEIGHT - 272) - MainLoop.HEIGHT / 2 - 128; // ir cia
			
		}
	
	//	System.out.println("x: " + e.getX());
	//	System.out.println("yOff: " + yOffset);
	//	System.out.println("y: " + e.getY());
		
		
	}
	
	
	public float getYOffset() {
		return yOffset;
	}
	
}
