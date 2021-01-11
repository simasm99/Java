package dev.simas.ghostgame.states;

import java.awt.Graphics;

import dev.simas.ghostgame.MainLoop;
import dev.simas.ghostgame.Objects;
import dev.simas.ghostgame.PseudoCode;
import dev.simas.ghostgame.entities.EnemyGhost;
import dev.simas.ghostgame.entities.EnemySpikes;
import dev.simas.ghostgame.entities.Player;
import dev.simas.ghostgame.entities.Portal;
import dev.simas.ghostgame.map.World;

public class GameState extends State {
	
	private Player player;
	private World world;
	private PseudoCode pseudocode;
	
	public GameState(Objects gameObjects) {
		super(gameObjects);
		world = new World("res/map/lvl1.txt", gameObjects);
		player = new Player(gameObjects, 128, 3840-64, 64, 64);
		pseudocode = new PseudoCode(gameObjects);
		gameObjects.setPseudoCode(pseudocode);
		world.initLevel();
	}

	public void update() {

		if(!player.getPseudoRun()) {
			world.update();
			gameObjects.updateEntity();
			player.update();
			pseudocode.update();
		}else {
			world.update();
			gameObjects.updateEntity();
			player.updatePlayerPseudo();
			pseudocode.update();
		}
		
	}
	
	public void render(Graphics g) {
		
		//pseudocode.render(g);
		world.render(g);
		gameObjects.renderEntity(g);
		player.render(g);
		
	}
}
