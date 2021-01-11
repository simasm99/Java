package dev.simas.ghostgame.states;

import java.awt.Graphics;

import dev.simas.ghostgame.MainLoop;
import dev.simas.ghostgame.Objects;

public abstract class State {
	
	private static State currState = null;
	
	public static void setState(State state) {
		currState = state;
	}
	
	public static State getState() {
		return currState;
	}
	
	// Abstractus state metodai
	
	protected Objects gameObjects;
	
	public State(Objects gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
}
