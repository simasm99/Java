package dev.simas.ghostgame.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dev.simas.ghostgame.MainLoop;
import dev.simas.ghostgame.Objects;
import dev.simas.ghostgame.entities.EnemyBossGhost;
import dev.simas.ghostgame.entities.EnemyGhost;
import dev.simas.ghostgame.entities.EnemySpikes;
import dev.simas.ghostgame.entities.EnemyWindow;
import dev.simas.ghostgame.entities.Portal;

public class World {

	private Objects gameObjects;
	private int width, height;
	private int [][] tiles;
	
	private int yStart;
	private int yEnd;
	
	private int currentLvl;
	private int prevLvl;
	
	public World(String path, Objects gameObjects) {
		width = 10;
		height = 60;
		tiles = new int[width][height];
		
		loadMap(path);
		this.gameObjects = gameObjects;
		gameObjects.setWorld(this);
		currentLvl = 1;
		prevLvl = currentLvl;
	}
	
	public void update() {

		if(currentLvl > prevLvl) {
			initLevel();
			prevLvl = currentLvl;
			gameObjects.getPlayer().changeSpawn(128, 3840 - 64);
		}
		if((currentLvl == 2) && (gameObjects.getPlayer().getY() <= 1152)) {
			++currentLvl;
			prevLvl = currentLvl;
			tiles[8][19] = 3;
			initLevel();
		}
	}
	
	public void render(Graphics g) {
			
		yStart = (int) Math.max(0, gameObjects.getGame().getCamera().getYOffset() / 64) ;
		yEnd = yStart + 14;
		
		if(yEnd >= height)
			yEnd = height;
		
		for(int y = yStart; y < yEnd; y++) {
			for(int x = 0; x < width; x++) {		
				getTile(x, y).render(g, x * Tile.TILESIZE, (int)(y * Tile.TILESIZE - gameObjects.getGame().getCamera().getYOffset()));
			}
		}
	}
	
	public int getyStart() {
		return yStart;
	}

	public int getyEnd() {
		return yEnd;
	}

	public Tile getTile(int x, int y) {
		
		int tempx = x;
		int tempy = y;
		
		if(tempx < 0) {
			tempx = 0;
		}
		if(tempx >= 10) {
			tempx = 9;
		}
		if(tempy < 0) {
			tempy = 0;
		}
		if(tempy >= height) {
			tempy = height - 1;
		}
		
		Tile t = Tile.tiles[tiles[tempx][tempy]];
		if(t == null)
			return Tile.BrickLVL1;
		return t;
	}
	
	public void loadMap(String path) {
		
		String file = null;
		
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null)
				builder.append(line + "\n");
			br.close();	
			
			file = builder.toString();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] map = file.split("\\s+");
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = Integer.parseInt(map[(x + y * width)]);
			}
		}
		
	}
	
	public void initLevel() {
		if(currentLvl == 1) {
			gameObjects.clearEntities();
			gameObjects.addEntity(new EnemyGhost(gameObjects, 448, 3584, 64, 64, 2));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 0, 3392, 64, 64, 2));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 0, 3008, 64, 64, 3));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 128, 2432, 64, 64, 1));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 256, 2368, 64, 64, 2));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 256, 2112, 64, 64, 3));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 256, 1984, 64, 64, 3));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 384, 1408, 64, 64, 3));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 0, 1216, 64, 64, 4));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 128, 512, 64, 64, 3));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 384, 384, 64, 64, 2));
			gameObjects.addEntity(new EnemyGhost(gameObjects, 512, 256, 64, 64, 1));
			
			gameObjects.addEntity(new Portal(gameObjects, 128, 192, 64, 64, 12)); //12
			
			gameObjects.addEntity(new EnemySpikes(gameObjects, 384, 3648, 64, 64, 3));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 512, 3264, 64, 64, 0));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 384, 2624, 64, 64, 0));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 64, 2880, 64, 64, 6));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 64, 2688, 64, 64, 6));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 64, 2496, 64, 64, 6));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 512, 2176, 64, 64, 6));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 384, 1728, 64, 64, 0));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 320, 1472, 64, 64, 3));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 256, 1088, 64, 64, 3));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 192, 896, 64, 64, 0));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 128, 832, 64, 64, 3));
			gameObjects.addEntity(new EnemySpikes(gameObjects, 256, 832, 64, 64, 6));
			
		}
		if(currentLvl == 2) {
			gameObjects.clearEntities();
			Tile.BrickLVL2.updateTileSolid(false);
			loadMap("res/map/lvl2.txt");
			if(gameObjects.getPlayer().getPseudoRun() && gameObjects.getPlayer().getWindowCom()) {
				gameObjects.addEntity(new EnemyWindow(gameObjects, 384, 3328, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 192, 3008, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 512, 2816, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 512, 2304, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 0, 2304, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 256, 1920, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 256, 1664, 128, 128, 0));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 0, 1280, 128, 128, 0));
			}else {
				gameObjects.addEntity(new EnemyWindow(gameObjects, 384, 3328, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 192, 3008, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 512, 2816, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 512, 2304, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 0, 2304, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 256, 1920, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 256, 1664, 128, 128, 1));
				gameObjects.addEntity(new EnemyWindow(gameObjects, 0, 1280, 128, 128, 1));
			}

		}
		
		if(currentLvl == 3) {
			gameObjects.clearEntities();
			
			gameObjects.addEntity(new EnemyBossGhost(gameObjects, 340, 640, 128, 128));
			//Tile.BrickLVL2.updateTileSolid(false);
			
		}
	}
	
	public void updateLvl() {
		++currentLvl;
	}
	
	public int getLvl() {
		return currentLvl;
	}
}
