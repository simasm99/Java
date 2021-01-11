package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class EnemySpikes extends Creature {

	private boolean damageDone = false;
	private double animationSpeed = 0.3f;
	private Objects gameObjects;
	
	private double collisionLenght = 100.0f;
	private double enemyMovementTimer = 100.0f;
	
	private int side; // 0 center, 3 left, 6 right
	
	public EnemySpikes(Objects gameObjects, float x, float y, int width, int height, int spikeSide) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		side = spikeSide;
		damage = 1;
	}

	@Override
	public void update() {
		
		enemyMovementTimer -= animationSpeed;
		
		if(enemyMovementTimer <= 0.0f) {
			enemyMovementTimer = 100.0f;
		}
		if((enemyMovementTimer <= 75.0f) && (enemyMovementTimer >= 0.0f)) {
			if((collisionWithPlayer) && !(damageDone)) {
				gameObjects.getPlayer().receiveDamage(damage);
				damageDone = true;
			}
		}else {
			collisionWithPlayer = false;
		}
		
		if(damageDone) {
			collisionLenght -= 0.5f;
			if(collisionLenght <= 0.0f) {
				collisionLenght = 100.0f;
				damageDone = false;
				collisionWithPlayer = false;
			}
		}
			
	}

	@Override
	public void render(Graphics g) {
		if((enemyMovementTimer > 75.0f) && (enemyMovementTimer <= 100.0f)) {
			g.drawImage(Assets.spikes[0 + side], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}
		if((enemyMovementTimer > 50.0f) && (enemyMovementTimer <= 75.0f)) {
			g.drawImage(Assets.spikes[1 + side], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}
		if((enemyMovementTimer > 25.0f) && (enemyMovementTimer <= 50.0f)) {
			g.drawImage(Assets.spikes[2 + side], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}
		if(enemyMovementTimer <= 25.0f) {
			g.drawImage(Assets.spikes[1 + side], (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}		
	}

}
