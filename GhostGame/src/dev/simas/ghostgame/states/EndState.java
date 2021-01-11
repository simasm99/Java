package dev.simas.ghostgame.states;

import java.awt.Color;
import java.awt.Graphics;

import dev.simas.ghostgame.Objects;

public class EndState extends State{
	
	public EndState(Objects gameObjects) {
		super(gameObjects);
	}

	@Override
	public void update() {
			
		 if(gameObjects.getGame().getKeyManager().space) {
			 gameObjects.getGame().stop();
		 }
		 
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 640, 800);
		
		g.setColor(Color.BLACK);
		g.drawString("Game finished, good job: ", 256, 320);
		
		g.drawString("Press space to exit ", 256, 440);

	}

}
