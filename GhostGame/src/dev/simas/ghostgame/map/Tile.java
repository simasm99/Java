package dev.simas.ghostgame.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	
	public static Tile[] tiles = new Tile[256];
	public static Tile BrickLVL1 = new Brick1(1);
	public static Tile BrickLVL2 = new Brick2(2);
	public static Tile BrickLVL3 = new Brick3(3);
	
	protected BufferedImage texture;
	protected final int ID;
	public static final int TILESIZE = 64;
	protected boolean solid;
	
	public Tile(BufferedImage tex, int ID) {
		this.texture = tex;
		this.ID = ID;
		
		tiles[ID] = this;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y, TILESIZE, TILESIZE, null);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void updateTileSolid(boolean value) {
		solid = value;
	}
	
	public int getID() {
		return ID;
	}
}
