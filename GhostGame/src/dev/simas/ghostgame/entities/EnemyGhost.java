package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class EnemyGhost extends Creature {

	private boolean goingRight = true;
	private boolean doDamage = false;
	private int walkLenght;
	private int tileLenght;
	private int damage = 1;
	private float speed = 1.0f;
	private Objects gameObjects;
	
	private double collisionLenght = 50.0f;
	private double deathTimer = 100.0f;
	
	public EnemyGhost(Objects gameObjects, float x, float y, int width, int height, int walkingTile) {
		super(x, y, width, height);
		walkLenght = walkingTile * 64;
		tileLenght = walkingTile;
		this.gameObjects = gameObjects;
	}

	@Override
	public void update() {
		
		if(alive) {
			if(goingRight) {
				speed = 1.0f;
				walkLenght -= xMove;
			}else {
				speed = -1.0f;
				walkLenght += xMove;
			}
			xMove = speed;
			move();
			
			if(walkLenght <= 0) {
				walkLenght = tileLenght * 64;
				if(goingRight) {
					goingRight = false;
				}else {
					goingRight = true;
				}
			}
			
			if(collisionWithPlayer) {
				if(collisionLenght == 50.0f) {
					doDamage = true;
				}
				collisionLenght -= 0.5f;
				if(collisionLenght <= 0.0f) {
					collisionLenght = 50.0f;
					collisionWithPlayer = false;
				}
			}
			if(doDamage) {		
				doDamage = false;
				gameObjects.getPlayer().receiveDamage(damage);
			}
		}
		else {
			if(deathTimer == 100.0f) {
				gameObjects.getPlayer().updateKillCount();
			}
			deathTimer -= 0.5f;
			if(deathTimer <= 0.0f) {
				gameObjects.deleteEnity(this);
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		if(alive) {
			g.drawImage(Assets.player, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}else {
			if(deathTimer <= 100.0f) {
				g.drawImage(Assets.deathAnimation[0], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
			}
			if(deathTimer <= 80.0f) {
				g.drawImage(Assets.deathAnimation[1], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
			}
			if(deathTimer <= 60.0f) {
				g.drawImage(Assets.deathAnimation[2], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
			}
			if(deathTimer <= 40.0f) {
				g.drawImage(Assets.deathAnimation[3], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
			}
			if(deathTimer <= 20.0f) {
				g.drawImage(Assets.deathAnimation[4], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
			}
		}
	}

}
