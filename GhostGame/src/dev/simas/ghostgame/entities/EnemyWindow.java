package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class EnemyWindow extends Creature {

	private Objects gameObjects;
	private double shootTimer = 25.0f;
	private double shootDir = 9.0f;
	private double speedDir = 0.5f;
	
	public EnemyWindow(Objects gameObjects, float x, float y, int width, int height, int damage) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		this.damage = damage;
	}
	
	@Override
	public void update() {
		
		shootDir -= 0.1f;
		if(shootDir <= -9.0f) {
			shootDir = 9.0f;
		}
		shootTimer -= speedDir;
		if(shootTimer <= 0.0f) {
			shootTimer = 25.0f;
			if((damage != 0) && !gameObjects.getPlayer().getWindowCom())
				gameObjects.addEntity(new EnemySunRay(gameObjects, x + 32, y + 32, 64, 64, shootDir, 1));
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.window, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		
	}
	
}
