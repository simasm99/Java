package dev.simas.ghostgame.entities;

import java.awt.Graphics;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.Objects;

public class EnemyBossGhost extends Creature{

	private Objects gameObjects;
	private float speed = 1.2f;
	
	private float angle = 45.0f;
	private boolean right = true;
	private boolean doDamage = false;
	
	private double shootTimer = 25.0f;
	private double shootDir = 9.0f;
	private double speedDir = 0.5f;
	
	private double hitTimer = 20.0f;
	private double playerHitTimer = 50.0f;
	
	public EnemyBossGhost(Objects gameObjects, float x, float y, int width, int height) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		health = 1;
	}

	@Override
	public void update() {
		
		if(health <= 0) {
			gameObjects.setGameFinished(true);
			gameObjects.getGame().getKeyManager().space = false;
			gameObjects.deleteEnity(this);
		}
		
		if(right)
			angle -= 0.01f;
		
		if((angle <= -45.0f) && right) {
			right = false;
		}
		
		if(!right)
			angle += 0.01f;
		
		if((angle >= 45.0f) && !right) {
			right = true;
		}
		
	    x += speed * Math.sin(angle);
	    y += speed * Math.cos(angle);
	    
	    shootDir -= 0.1f;
		if(shootDir <= -9.0f) {
			shootDir = 9.0f;
		}
		
		shootTimer -= speedDir;
		if(shootTimer <= 0.0f) {
			shootTimer = 25.0f;
			gameObjects.addEntity(new EnemySunRay(gameObjects, x + 32, y + 32, 64, 64, shootDir, 2));
		}
		
		if((!alive) && (hitTimer == 20.0f)) {
			health -= 1;
			alive = true;
			hitTimer -= 0.1f;
		}else {
			alive = true;
		}
		if(hitTimer != 20.0f) {
			hitTimer -= 0.1f;
		}
		if(hitTimer <= 0.0f) {
			hitTimer = 20.0f;
		}
		
		if((collisionWithPlayer) && !(gameObjects.getGame().getKeyManager().space)) {
			if(playerHitTimer == 50.0f) {
				doDamage = true;
			}
			playerHitTimer -= 0.5f;
			if(playerHitTimer <= 0.0f) {
				playerHitTimer = 50.0f;
				collisionWithPlayer = false;
			}
		}else {
			collisionWithPlayer = false;
		}
		if(doDamage) {		
			doDamage = false;
			gameObjects.getPlayer().receiveDamage(1);
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		g.drawImage(Assets.bossGhost, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		
		if(health >= 1) {
			int pixelGap = 0;
			for(int i = 0; i < health; ++i) {
				g.drawImage(Assets.healthTex,  (int)x + 12 + pixelGap, (int) (y - gameObjects.getGame().getCamera().getYOffset()) - 20, 32, 32, null);
				pixelGap += 16;
			}
			
		}
	}

}
