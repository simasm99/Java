package dev.simas.ghostgame.entities;

public abstract class Creature extends Entity {

	protected int health;
	protected int damage;
	protected int CREATURE_WIDTH, CREATURE_HEIGHT;
	
	protected float xMove, yMove;
	
	
	public Creature(float x, float y, int width, int height ) {
		super(x, y);
		health = 10;
		CREATURE_WIDTH = width;
		CREATURE_HEIGHT = height;
		xMove = 0;
		yMove = 0;
	}

	
	public void move() {
		x += xMove;
		y += yMove;
	}
	
	//getters and setters
	
	public float getxMove() {
		return xMove;
	}

	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	public float getyMove() {
		return yMove;
	}

	public void setyMove(float yMove) {
		this.yMove = yMove;
	}
	
	public int getWidth() {
		return CREATURE_WIDTH;
	}
	
	public int getHeight() {
		return CREATURE_HEIGHT;
	}
	
	public void changeDamage(int damage) {
		this.damage = damage;
	}
	
}
