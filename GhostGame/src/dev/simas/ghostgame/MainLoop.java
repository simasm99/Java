package dev.simas.ghostgame;
 
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.net.ssl.KeyManager;

import dev.simas.ghostgame.states.EndState;
import dev.simas.ghostgame.states.GameState;
import dev.simas.ghostgame.states.State;
import dev.simas.ghostgame.window.Window;

public class MainLoop implements Runnable{

	private Window display;
	public static int WIDTH, HEIGHT;
	public static int LEVEL_HEIGHT = 3840;
	String gameTitle;
	
	private boolean isRunning = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	//Game States
	
	private State gameState;
	
	//Input
	
	private Input keyManager;
	
	//Camera
	
	private GameCamera camera;
	
	//ObjectsHolder
	
	private Objects gameObjects;
	
	public MainLoop(String title, int width, int height) {
		gameTitle = title;
		WIDTH = width;
		HEIGHT = height;
		keyManager = new Input();
	}
	
	private void init() {
		
		display = new Window(gameTitle, WIDTH, HEIGHT);
		display.getFrame().addKeyListener(keyManager);
		Assets.init();
		
		gameObjects = new Objects(this);
		
		camera = new GameCamera(this, 0);
		
		gameState = new GameState(gameObjects);
		State.setState(gameState);
	}
	
	private void update() {	
		
		keyManager.update();
		
		if(State.getState() != null)
			State.getState().update();
		
	}
	
	private void render() {
		
		bs = display.getCanvasBox().getBufferStrategy();
		if(bs == null) {
			display.getCanvasBox().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		//Render stuff
		
		if(State.getState() != null)
			State.getState().render(g);
		
		
		//End of rendering
		
		bs.show();
		g.dispose();
		
	}
	
	public void run() {
		
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		
		while(isRunning) {	
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(gameObjects.gameFinished()) {
				State.setState(new EndState(gameObjects));
			}
			
			if(delta >= 1) {
				update();
				render();
				ticks++;
				delta = 0;
			}
			
			if(timer >= 1000000000) {
				ticks = 0;
				timer = 0;
			}
		}
		stop();
		
		
	}
	
	public Input getKeyManager() {
		return keyManager;
	}
	
	public GameCamera getCamera() {
		return camera;
	}
	
	public Window getWindow() {
		return display;
	}
	
	public synchronized void start() {
		
		if(isRunning)
			return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!isRunning)
			return;
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
