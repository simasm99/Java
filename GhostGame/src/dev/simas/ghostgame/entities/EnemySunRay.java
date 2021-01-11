package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class EnemySunRay extends Creature{

	private Objects gameObjects;
	private float shootDir;
	private double aliveTime = 100.0f;
	
	private int rayType;
	
	public EnemySunRay(Objects gameObjects, float x, float y, int width, int height, double shootDir, int type) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		this.shootDir = (float)shootDir;
		alive = true;
		rayType = type;
		damage = 1;
	}

	@Override
	public void update() {
		
		aliveTime -= 0.5f;
		if(aliveTime <= 0.0f) {
			gameObjects.deleteEnity(this);
		}
		if(alive == false) {
			gameObjects.deleteEnity(this);
		}
		if((alive == true) && collisionWithPlayer) {
			gameObjects.getPlayer().receiveDamage(damage);
			gameObjects.deleteEnity(this);
		}
		
		xMove = shootDir / 3;
		yMove = 2;
		move();
	}

	@Override
	public void render(Graphics g) {
		
		if(rayType == 1)
			g.drawImage(Assets.sunRay, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		
		if(rayType == 2)
			g.drawImage(Assets.deathAnimation[0], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
	}

}
