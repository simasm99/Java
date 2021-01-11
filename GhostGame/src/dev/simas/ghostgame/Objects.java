package dev.simas.ghostgame;

import java.awt.Graphics;
import java.util.ArrayList;

import dev.simas.ghostgame.entities.Entity;
import dev.simas.ghostgame.entities.Player;
import dev.simas.ghostgame.map.World;

public class Objects {

	private PseudoCode pseudocode;
	private MainLoop game;
	private World world;
	private Player player;
	private ArrayList<Entity> entities;
	
	private boolean gameCompleted = false;
	
	public Objects(MainLoop game) {
		this.game = game;
		entities = new ArrayList<Entity>();
	}
	
	public MainLoop getGame() {
		return game;
	}
	
	public boolean gameFinished() {
		return gameCompleted;
	}
	
	public void setGameFinished(boolean value) {
		gameCompleted = value;
	}
	
	//World
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	//Player
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	//PseudoCode
	
	public void setPseudoCode(PseudoCode pseudocode) {
		this.pseudocode = pseudocode;
	}
	
	public PseudoCode getPseudoCode() {
		return pseudocode;
	}
	
	//Entity Manager
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void updateEntity() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
		}
	}
	
	public void deleteEnity(Entity e) {
		for(int i = 0; i < entities.size(); i++) {
			if(e == entities.get(i)) {
				entities.remove(i);
			}
		}
	}
	
	public void clearEntities() {
		entities.clear();
	}
	
	public void renderEntity(Graphics g) {
		for(Entity e : entities) {
			e.render(g);
		}
	}
	
	public ArrayList<Entity> getEntities(){
		return entities;
	}
	
}
