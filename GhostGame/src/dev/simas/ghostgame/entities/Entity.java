package dev.simas.ghostgame.entities;

import java.awt.Graphics;

public abstract class Entity {

	protected float x, y;
	protected boolean collisionWithPlayer;
	protected boolean alive;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		collisionWithPlayer = false;
		alive = true;
	}
	
	public abstract void update();
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract void render(Graphics g);

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public void setCollision(boolean value) {
		collisionWithPlayer = value;
	}
	
	public boolean getCollision() {
		return collisionWithPlayer;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public void setAlive(boolean value) {
		alive = value;
	}
	
}
