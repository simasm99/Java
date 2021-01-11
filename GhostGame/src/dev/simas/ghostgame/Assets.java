package dev.simas.ghostgame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Assets {

	public static BufferedImage firstTile, secondTile, thirdTile, fourthTile, fifthTile;
	
	public static BufferedImage healthTex;
	
	public static BufferedImage[] deathAnimation = new BufferedImage[5];
	
	public static BufferedImage[] spikes = new BufferedImage[9];	// spikes 0-2 center, 3-5 left, 6-8 right
	
	public static BufferedImage portal[] = new BufferedImage[2];
	
	public static BufferedImage window, sunRay;
	
	public static BufferedImage bossGhost;
	
	public static BufferedImage player, playerEating;
	
	public static void init() {
		Sprites sheet = new Sprites(TextureLoader.loadImage("/textures/tiles.png"));
		
		firstTile = sheet.crop(0, 0, 32, 32);
		secondTile = sheet.crop(32, 0, 32, 32);
		thirdTile = sheet.crop(64, 0, 32, 32);
		fourthTile = sheet.crop(96, 0, 32, 32);
		fifthTile = sheet.crop(128, 0, 32, 32);
		
		deathAnimation[0] = sheet.crop(32, 32, 32, 32);
		deathAnimation[1] = sheet.crop(64, 32, 32, 32);
		deathAnimation[2] = sheet.crop(96, 32, 32, 32);
		deathAnimation[3] = sheet.crop(128, 32, 32, 32);
		deathAnimation[4] = sheet.crop(160, 32, 32, 32);
		
		spikes[0] = sheet.crop(0, 64, 32, 32);
		spikes[1] = sheet.crop(32, 64, 32, 32);
		spikes[2] = sheet.crop(64, 64, 32, 32);
		
		spikes[3] = sheet.crop(160, 64, 32, 32);
		spikes[4] = sheet.crop(0, 96, 32, 32);
		spikes[5] = sheet.crop(32, 96, 32, 32);
		
		spikes[6] = sheet.crop(64, 96, 32, 32);
		spikes[7] = sheet.crop(96, 96, 32, 32);
		spikes[8] = sheet.crop(128, 96, 32, 32);
		
		healthTex = sheet.crop(96, 64, 32, 32);
		
		portal[0] = sheet.crop(128, 64, 32, 32);
		portal[1] = sheet.crop(160, 96, 32, 32);
		
		window = sheet.crop(0, 128, 64, 64);
		sunRay = sheet.crop(64, 128, 32, 32);
		
		bossGhost = sheet.crop(96, 128, 64, 64);
		
		player = sheet.crop(160, 0, 32, 32);
		playerEating = sheet.crop(0, 32, 32, 32);
	}
	
}
