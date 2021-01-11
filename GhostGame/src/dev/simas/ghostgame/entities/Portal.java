package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class Portal extends Creature{

	private Objects gameObjects;
	private int requiredKills;
	private boolean open = false;
	
	public Portal(Objects gameObjects, float x, float y, int width, int height, int killsToOpen) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		requiredKills = killsToOpen;
	}

	@Override
	public void update() {
		
		if(requiredKills == gameObjects.getPlayer().getKillCount()) {
			open = true;
		}
		
		if((collisionWithPlayer) && (!open)) {
			collisionWithPlayer = false;
		}
		
		if((collisionWithPlayer) && (open)) {
			gameObjects.getWorld().updateLvl();
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		if(!open) {
			g.drawImage(Assets.portal[0], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}else {
			g.drawImage(Assets.portal[1], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}
		
	}

}
