package dev.simas.ghostgame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.simas.ghostgame.Assets;
import dev.simas.ghostgame.MainLoop;
import dev.simas.ghostgame.Objects;
import dev.simas.ghostgame.map.Tile;




public class Player extends Creature {

	private Objects gameObjects;
	private int killCount = 0;

	private float speed = 5.0f;
	
	private float jumpVEL = 10.0f; // 10f 
	private float gravityVEL = 0.2f;
	
	private boolean jumping = false;
	private float jump = 10.0f;
	 
	private boolean eatingEnemy = false;
	private boolean refillCount = false;
	private double eatTimer = 50.0f;
	
	private Rectangle collBounds;
	private Rectangle healthBox;
	private Rectangle eatBox;
	
	private boolean pseudoRun = false;
	List<String> instArray = new ArrayList<String>();
	private String[] currInst;
	boolean nextInstruction = false;
	private double pseudoRunLenght;
	private boolean pseudoEat = false;
	
	private int pseudoPauseTicks = -1;
	private int pseudoTickEat = 0;
	private int pseudoUnkillableTicks = 0;
	private boolean lastAte = false;
	
	private boolean noClip = false;
	private boolean unkillable = false;
	private boolean turnWindowOff = false;
	
	private boolean collUP = false;
	private boolean collDOWN = false;
	private boolean collLEFT = false;
	private boolean collRIGHT = false;
	
	Map<String, Integer> IntVars = new HashMap<>();
	Map<String, Boolean> BoolVars = new HashMap<>();
	Map<String, Float> FloatVars = new HashMap<>();
	
	public Player(Objects gameObjects,float x, float y, int width, int height) {
		super(x, y, width, height);
		this.gameObjects = gameObjects;
		collBounds = new Rectangle((int)x,(int)y,32,56);
		healthBox = new Rectangle(440, 0, 200 ,64);
		eatBox = new Rectangle(440, 64, 200, 8);
		pseudoRunLenght = 0;
		gameObjects.setPlayer(this);
	}

	public void update() {
		if((gameObjects.getPseudoCode().getInstructions() != null) && !pseudoRun) {
			pseudoRun = true;
			readyHash();
			for(String s : gameObjects.getPseudoCode().getInstructions()) {
				instArray.add(s);
			}
			currInst = instArray.get(0).split(" ");
		}else if (!pseudoRun){
			pseudoRun = false;
			getInput();
			move();
			setBounds();
			gameObjects.getGame().getCamera().centerCamera(this);
			entityCollision();
			
			if(health <= 0) {
				health = 10;
				changeSpawn(128, 3840-64);
				gameObjects.getWorld().initLevel();
			}
		}	
	}
	
	public void updatePlayerPseudo() {

		if((instArray.isEmpty()) && nextInstruction){
			gameObjects.getPseudoCode().setInstNull();
			pseudoRun = false;
			return;
		}
		if((nextInstruction) && (instArray.size() > 1)) {
			//System.out.println("Kitas");
			instArray.remove(0);
			currInst = instArray.get(0).split(" ");			
		}else if((nextInstruction) && (instArray.size() == 1)) {
			System.out.println("Baigias instrukcijos, exit pseudo");
			instArray.clear();
			gameObjects.getPseudoCode().setInstNull();
			pseudoRun = false;
			setDefault();
			deleteHash();
			return;
		}
		//System.out.println(collLEFT);
		//System.out.println(instArray.size());
		System.out.println(currInst[0]);
		
		// Pseudo Controls
		
		if((currInst[0].equals("jump")) && !jumping) { 				//if
			  gameObjects.getGame().getKeyManager().up = true; 
			  getInput();
			  gameObjects.getGame().getKeyManager().up = false; 
			  nextInstruction = true; 
		}
		else if((currInst[0].equals("jump")) && jumping) {
			  getInput();
			  nextInstruction = false; 
		}
		else if(currInst[0].equals("right")) { 						//if
			nextInstruction = false;
			gameObjects.getGame().getKeyManager().right = true;
			if(pseudoRunLenght == 0)
				pseudoRunLenght = Double.parseDouble(currInst[1]);
			
			getInput();
			if(pseudoRunLenght <= 0) {
				nextInstruction = true;
				pseudoRunLenght = 0;
				gameObjects.getGame().getKeyManager().right = false;
			}
		}
		else if(currInst[0].equals("left")) {		 				//if
			nextInstruction = false;
			gameObjects.getGame().getKeyManager().left = true;
			if(pseudoRunLenght == 0)
				pseudoRunLenght = Double.parseDouble(currInst[1]);
			
			getInput();
			
			if(pseudoRunLenght <= 0) {
				nextInstruction = true;
				pseudoRunLenght = 0;
				gameObjects.getGame().getKeyManager().left = false;
			}
		}
		else if(currInst[0].equals("speed")){		 				//if
			speed = Float.parseFloat(currInst[1]);
			getInput();
			nextInstruction = true;
		}
		else if(currInst[0].equals("gravity")){		 				//if
			gravityVEL = Float.parseFloat(currInst[1]);
			getInput();
			nextInstruction = true;
		}
		else if(currInst[0].equals("for")){								
			int commandTimes = Integer.parseInt(currInst[1]);
			String[] commands = instArray.get(1).split(" ");	
			int size = commands.length;
			
			ArrayList<String> loopCommands = new ArrayList<String>();
			for(int i = 0; i < size; ) {
				loopCommands.add((commands[i] + " " + commands[i+1]));
				++i;
				++i;
			}	
			instArray.remove(0);
			
			int counter = loopCommands.size();
			for(int i = 0; i < commandTimes; ++i) {
				for(int z = counter; 0 < z; --z) {
					instArray.add(0, loopCommands.get(z - 1));
				}
			}
			currInst[0] = instArray.get(0).split(" ")[0];
			currInst[1] = instArray.get(0).split(" ")[1];
			
			nextInstruction = false;
			instArray.remove(counter * commandTimes);
			
			return;
		}
		else if(currInst[0].equals("spaceHold")){		 				//if
			pseudoEat = true;
			nextInstruction = true;
		}
		else if(currInst[0].equals("spaceTick")){	 					//if
			pseudoTickEat = Integer.parseInt(currInst[1]);
			pseudoEat = true;
			nextInstruction = true;
		}
		else if(currInst[0].equals("teleport")){
			int xx = Integer.parseInt(currInst[1]);
			int yy = Integer.parseInt(currInst[2]);
			changeSpawn(xx, yy);
			nextInstruction = true;
		}
		else if(currInst[0].equals("noclipOn")){ 						//if
			noClip = true;
			nextInstruction = true;
		}
		else if(currInst[0].equals("noclipOff")){		 				//if
			noClip = false;
			nextInstruction = true;
		}
		else if(currInst[0].equals("pause")){		 					//if
			if(pseudoPauseTicks == -1) {
				pseudoPauseTicks = Integer.parseInt(currInst[1]);
			}
			nextInstruction = false;
			--pseudoPauseTicks;
			if(pseudoPauseTicks < 0) {
				nextInstruction = true;
				pseudoPauseTicks = -1;
			}
			
		}
		else if(currInst[0].equals("killEnemy")) { 						//if
			EnemyGhost closest = null;
			double distance = 0;

			for(Entity e : gameObjects.getEntities()) { 
					if(e instanceof EnemyGhost) {
						if(e.getAlive()){
							double xd = Math.abs((double)e.getX() - (double)x);
							double yd = Math.abs((double)e.getY() - (double)y);
							
							if(distance == 0) {
								distance = Math.sqrt(xd * xd + yd * yd);
								closest = (EnemyGhost) e;
							}
							else {
								
							double temp = Math.sqrt(xd * xd + yd * yd);
							
								if(temp < distance) {
									distance = temp;
									closest = (EnemyGhost) e;
								}
							}
						}
				}
			}
			if(closest != null)
				closest.setAlive(false);
			
			nextInstruction = true;
			
		}
		else if(currInst[0].equals("unkillable")){		 				//if
			unkillable = true;
			pseudoUnkillableTicks = Integer.parseInt(currInst[1]);
			nextInstruction = true;
		}
		else if(currInst[0].equals("jumpVEL")){			 				//if
			jump = Float.parseFloat(currInst[1]);
			jumpVEL = jump;
			nextInstruction = true;
		}
		else if(currInst[0].equals("turnSpikesOff")){
			for(Entity e : gameObjects.getEntities()) { 
				if(e instanceof EnemySpikes) {
					if(e.getAlive()){
						((EnemySpikes) e).changeDamage(0);
					}
				}
			}
			nextInstruction = true;
		}
		else if(currInst[0].equals("turnWindowOff")){  					//if
			turnWindowOff = true;
			nextInstruction = true;
		}
		else if(currInst[0].equals("turnSpikesOn")){		 			//if
			for(Entity e : gameObjects.getEntities()) { 
				if(e instanceof EnemyWindow) {
					if(e.getAlive()){
						((EnemyWindow) e).changeDamage(1);
					}
				}
			}
			nextInstruction = true;
		}
		else if(currInst[0].equals("turnWindowOn")){	 				//if
			turnWindowOff = false;
			nextInstruction = true;
		}
		else {
			nextInstruction = false;
		}
		
		if((currInst[0].equals("if")) || currInst.length == 4) {
			ArrayList<String> ifConditions = new ArrayList<String>();  
			ArrayList<String> ifCommand = new ArrayList<String>();
			nextInstruction = true;
			
			parseCondition(currInst[1], ifConditions, ifCommand);
			if(ifConditions.size() == 1) {
				String condition = ifConditions.get(0);
				if(condition.charAt(0) != '!') {
					if(BoolVars.get(condition)) {
						instArray.remove(0);
						currInst[0] = ifCommand.get(0);
						currInst[1] = ifCommand.get(1);
						instArray.add(0, currInst[0] + " " + currInst[1]);
						nextInstruction = false;
					}
				}else if(condition.charAt(0) == '!') {
					String newString = condition.substring(1, condition.length());
					if(BoolVars.get(newString) == false) {
						instArray.remove(0);
						currInst[0] = ifCommand.get(0);
						currInst[1] = ifCommand.get(1);
						instArray.add(0, currInst[0] + " " + currInst[1]);
						nextInstruction = false;
					}
				}
			}
			else if(ifConditions.size() == 3) {
				String condition1 = ifConditions.get(0);
				String condition2 = ifConditions.get(2);
				
				if((FloatVars.get(condition1) != null) || (FloatVars.get(condition2) != null)) {		
					if(condition1.matches("-?\\d+(\\.\\d+)?")) {
						switch(ifConditions.get(1)) {
						  case "<":
						    if(Float.parseFloat(condition1) < FloatVars.get(condition2)) {
						    	instArray.remove(0);
								currInst[0] = ifCommand.get(0);
								currInst[1] = ifCommand.get(1);
								instArray.add(0, currInst[0] + " " + currInst[1]);
								nextInstruction = false;
						    }
						    break;
						  case "<=":
							if(Float.parseFloat(condition1) <= FloatVars.get(condition2)) {
								instArray.remove(0);
								currInst[0] = ifCommand.get(0);
								currInst[1] = ifCommand.get(1);
								instArray.add(0, currInst[0] + " " + currInst[1]);
								nextInstruction = false;
							}
						    break;
						  case ">":
							  if(Float.parseFloat(condition1) > FloatVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							break;
						  case ">=":
							  if(Float.parseFloat(condition1) >= FloatVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							    break;
						  case "==":
							  if(Float.parseFloat(condition1) == FloatVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							    break;
						  default:
						    break;
						}
					}
					else if(condition2.matches("-?\\d+(\\.\\d+)?")) {
						switch(ifConditions.get(1)) {
						  case "<":
						     if(FloatVars.get(condition1) < Float.parseFloat(condition2)) {
						    	 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
						     }
						     break;
						  case "<=":
							 if(FloatVars.get(condition1) <= Float.parseFloat(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
						     break;
						  case ">":
							 if(FloatVars.get(condition1) > Float.parseFloat(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case ">=":
							 if(FloatVars.get(condition1) >= Float.parseFloat(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case "==":
							 if(FloatVars.get(condition1) == Float.parseFloat(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  default:
						    break;
						}
					}
					else if ((FloatVars.get(condition1) != null) && (FloatVars.get(condition2) != null)) {
						switch(ifConditions.get(1)) {
						  case "<":
						     if(FloatVars.get(condition1) < FloatVars.get(condition2)) {
						    	 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
						     }
						     break;
						  case "<=":
							 if(FloatVars.get(condition1) <= FloatVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
						     break;
						  case ">":
							 if(FloatVars.get(condition1) > FloatVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case ">=":
							 if(FloatVars.get(condition1) >= FloatVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case "==":
							 if(FloatVars.get(condition1) == FloatVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  default:
						    break;
						}
					}
				}
				else if((IntVars.get(condition1) != null) || (IntVars.get(condition2) != null)) {
					if(condition1.matches("-?\\d+(\\.\\d+)?")) {
						switch(ifConditions.get(1)) {
						  case "<":
						    if(Integer.parseInt(condition1) < IntVars.get(condition2)) {
						    	instArray.remove(0);
								currInst[0] = ifCommand.get(0);
								currInst[1] = ifCommand.get(1);
								instArray.add(0, currInst[0] + " " + currInst[1]);
								nextInstruction = false;
						    }
						    break;
						  case "<=":
							if(Integer.parseInt(condition1) <= IntVars.get(condition2)) {
								instArray.remove(0);
								currInst[0] = ifCommand.get(0);
								currInst[1] = ifCommand.get(1);
								instArray.add(0, currInst[0] + " " + currInst[1]);
								nextInstruction = false;
							}
						    break;
						  case ">":
							  if(Integer.parseInt(condition1) > IntVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							break;
						  case ">=":
							  if(Integer.parseInt(condition1) >= IntVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							    break;
						  case "==":
							  if(Integer.parseInt(condition1) == IntVars.get(condition2)) {
								  instArray.remove(0);
								  currInst[0] = ifCommand.get(0);
								  currInst[1] = ifCommand.get(1);
								  instArray.add(0, currInst[0] + " " + currInst[1]);
								  nextInstruction = false;
							  }
							    break;
						  default:
						    break;
						}
					}
					else if(condition2.matches("-?\\d+(\\.\\d+)?")) {
						switch(ifConditions.get(1)) {
						  case "<":
						     if(IntVars.get(condition1) < Integer.parseInt(condition2)) {
						    	 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
						     }
						     break;
						  case "<=":
							 if(IntVars.get(condition1) <= Integer.parseInt(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
						     break;
						  case ">":
							 if(IntVars.get(condition1) > Integer.parseInt(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case ">=":
							 if(IntVars.get(condition1) >= Integer.parseInt(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case "==":
							 if(IntVars.get(condition1) == Integer.parseInt(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  default:
						    break;
						}
					}
					else if ((IntVars.get(condition1) != null) && (IntVars.get(condition2) != null)) {
						switch(ifConditions.get(1)) {
						  case "<":
						     if(IntVars.get(condition1) < IntVars.get(condition2)) {
						    	 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
						     }
						     break;
						  case "<=":
							 if(IntVars.get(condition1) <= IntVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
						     break;
						  case ">":
							 if(IntVars.get(condition1) > IntVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case ">=":
							 if(IntVars.get(condition1) >= IntVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  case "==":
							 if(IntVars.get(condition1) == IntVars.get(condition2)) {
								 instArray.remove(0);
								 currInst[0] = ifCommand.get(0);
								 currInst[1] = ifCommand.get(1);
								 instArray.add(0, currInst[0] + " " + currInst[1]);
								 nextInstruction = false;
							 }
							 break;
						  default:
						    break;
						}
					}
					
				}
			}
		}
		
		//Eating enemy check
		if((pseudoTickEat != 0) && pseudoEat) {
			if(lastAte == true) {
				eatTimer += 0.5;
				--pseudoTickEat;
				lastAte = false;
				eatingEnemy = false;
			}else {
				lastAte = true;
				--pseudoTickEat;
				eatTimer -= 0.5;
				eatingEnemy = true;
			}
			if(eatTimer <= 0.0f) {
				pseudoTickEat = 0;
				eatingEnemy = false;
				refillCount = true;
				lastAte = false;
			}
		}
		else if((pseudoTickEat <= 0) && pseudoEat) {
			lastAte = false;
			pseudoTickEat = 0;
		}
		else if(pseudoEat) {
			
			if((eatTimer <= 50.0f) && !refillCount) {
				eatTimer -= 0.5f;
				eatingEnemy = true;
			}
			if((eatTimer <= 0.0f) || (refillCount)) {
				refillCount = true;
				eatingEnemy = false;
				eatTimer += 0.5f;
				if(eatTimer >= 50.0f) {
					refillCount = false;
					eatTimer = 50.0f;
					pseudoEat = false;
				}
			}
			
		}
		
		//Unkillable check
		
		if(unkillable) {
			--pseudoUnkillableTicks;
			if(pseudoUnkillableTicks <= 0) {
				unkillable = false;
				pseudoUnkillableTicks = 0;
			}
		}
		
		updateHash();
		move();
		setBounds();
		gameObjects.getGame().getCamera().centerCamera(this);
		entityCollision();
		if(health <= 0) {
			health = 10;
			changeSpawn(128, 3840-64);
			gameObjects.getWorld().initLevel();
		}
			
	}
	
	public void setBounds() {		
			
		collBounds.setBounds((int)x + 14, (int)y + 8, 32, 56); 

	}
	
	public void entityCollision() {
		
		for(int i = 0; i < gameObjects.getEntities().size(); i++) {
			Entity e = gameObjects.getEntities().get(i);
			Rectangle entityBounds = new Rectangle((int)e.x, (int)e.y, e.getWidth(), e.getHeight());
			if(collBounds.intersects(entityBounds)) {
				e.setCollision(true);
				if(eatingEnemy) {
					e.setAlive(false);
				}
				
			}
		}
	}
	
	public void receiveDamage(int damage) {
		if(!unkillable)
			health -= damage;
	}
	
	public void updateKillCount() {
		++killCount;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public boolean getPseudoRun() {
		return pseudoRun;
	}
	
	public void changeSpawn(int xPos, int yPos) {
		x = (float)xPos;
		y = (float)yPos;
		
		collBounds.setBounds((int)x + 14, (int)y + 8, 32, 56); 
		
		xMove = 0;
		yMove = 0;
	}
	
	@Override
	public void move() {
		
		if(pseudoRun) {
			if(x + CREATURE_WIDTH < MainLoop.WIDTH) 
				collRIGHT = false;
			if(x >= 0) 
				collLEFT = false;
		}
		
		if(xMove > 0){//Moving right
			int tx = (int) (xMove + collBounds.x + collBounds.width) / 64;
							
			if(!collisionWithTile(tx, (int) (collBounds.y) / 64) &&
					!collisionWithTile(tx, (int) (collBounds.y + collBounds.height - 1) / 64)){
				x += xMove;
			}else {
				if(noClip) {
					x += xMove;
				}else {
					if(pseudoRun) {
						collRIGHT = true;
					}
					x = tx * 64 - CREATURE_WIDTH + 16;
				}
			}
		}else if(xMove < 0){//Moving left
			int tx = (int) (xMove + collBounds.x) / 64;
			
			if(!collisionWithTile(tx, (int) (collBounds.y) / 64) &&
					!collisionWithTile(tx, (int) (collBounds.y + collBounds.height - 1 ) / 64)){
				x += xMove;
			}else {
				if(noClip) {
					x += xMove;
				}else {
					if(pseudoRun)
						collLEFT = true;
					x = tx * 64 + CREATURE_WIDTH - 14;
				}
			}
		}
		
		if(yMove < 0){//Up
			int ty = (int) (yMove + collBounds.y) / 64;

			if(!collisionWithTile((int) (collBounds.x) / 64, ty) &&
					!collisionWithTile((int) (collBounds.x + collBounds.width) / 64, ty) &&
					(y > 0)){
				y += yMove;
				if(pseudoRun)
					collUP = false;
			}else {
				if(noClip) {
					y += yMove;
				}else {
					if(pseudoRun)
						collUP = true;
					yMove = 0;
				}
			}
		}else if(yMove >= 0){//Down
			int ty = (int) (yMove + collBounds.y + collBounds.height) / 64;

			if(!collisionWithTile((int) (collBounds.x) / 64, ty) &&
					!collisionWithTile((int) (collBounds.x + collBounds.width) / 64, ty) &&
					(y + CREATURE_WIDTH < MainLoop.LEVEL_HEIGHT)){
				yMove += gravityVEL;
				y += yMove;
				if(pseudoRun)
					collDOWN = false;
			}else {
				y = ty * 64 - CREATURE_HEIGHT;
				yMove = 0;
				if(pseudoRun)
					collDOWN = true;
				if(jumping)
					jumping = false;
			}
		}
	}
	
	public boolean collisionWithTile(int x, int y) {
				
		return gameObjects.getWorld().getTile(x, y).isSolid();
	}
	
	private void getInput() {
		
		if(pseudoRun) {
			xMove = 0;
			if(yMove >= jumpVEL) {	
				yMove = jumpVEL;
			}
			
			if((gameObjects.getGame().getKeyManager().up) || (jumping)) {
				if(!jumping) {
					jumping = true;
					yMove = 0;
					yMove -= jump;
				}
				if(jumping) {
					yMove += gravityVEL;
				}
			}
			if(gameObjects.getGame().getKeyManager().left) {
				pseudoRunLenght -= (double)speed;
				if(x > 0) {
					xMove -= speed;
				}else {
					collLEFT = true;
				}
			}
			if(gameObjects.getGame().getKeyManager().right) {
				pseudoRunLenght -= (double)speed;
				if(x + CREATURE_WIDTH < MainLoop.WIDTH) {
					xMove += speed;
				}else {
					collRIGHT = true;
				}
			}
		}else {		
			xMove = 0;
			if(yMove >= jumpVEL) {	
				yMove = jumpVEL;
			}
			
			if((gameObjects.getGame().getKeyManager().up) || (jumping)) {
				if(!jumping) {
					jumping = true;
					yMove = 0;
					yMove -= jump;
				}
				if(jumping) {
					yMove += gravityVEL;
				}
			}
			if(gameObjects.getGame().getKeyManager().left)
				if(x > 0)
					xMove -= speed;
			if(gameObjects.getGame().getKeyManager().right)
				if(x + CREATURE_WIDTH < MainLoop.WIDTH)
					xMove += speed;
			if(gameObjects.getGame().getKeyManager().space) {
				spacePressed();
			}else {
				spaceReleased();
			}
		}
		
	}
	
	public void render(Graphics g) {

		g.setColor(Color.orange);
		g.fillRect(healthBox.x, healthBox.y, healthBox.width, healthBox.height);

		if(health >= 1) {
			int pixelGap = 0;
			int pixelGapY = 0;
			for(int i = 0; i < health; ++i) {
				g.drawImage(Assets.healthTex,  healthBox.x + pixelGap, healthBox.y + pixelGapY, 32, 32, null);
				pixelGap += 32;
				if(pixelGap >= 192) {
					pixelGap = 0;
					pixelGapY = 32;
				}
			}
			
		}

		g.fillRect(eatBox.x, eatBox.y, eatBox.width, eatBox.height);
		
		g.setColor(Color.MAGENTA);
		g.fillRect(eatBox.x, eatBox.y, eatBox.width / 100 * (int)eatTimer * 2, eatBox.height);
		
		g.setColor(Color.BLACK);
		g.drawString("Ability timer: ", eatBox.x + 128, eatBox.y - 4);
		
		if(!eatingEnemy) {
			g.drawImage(Assets.player, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}else {
			g.drawImage(Assets.playerEating, (int)x, (int) (y - gameObjects.getGame().getCamera().getYOffset()), CREATURE_WIDTH, CREATURE_HEIGHT, null);
		}
		
	}

	private void spacePressed() {
		if((eatTimer <= 50.0f) && !refillCount) {
			eatTimer -= 0.5f;
			eatingEnemy = true;
		}
		if(eatTimer <= 0.0f) {
			refillCount = true;
			spaceReleased();
		}
	}
	
	private void spaceReleased() {		
		eatingEnemy = false;
		eatTimer += 0.5f;
		if(eatTimer >= 50.0f) {
			refillCount = false;
			eatTimer = 50.0f;
		}
	}
	
	private void setDefault() {
		pseudoRunLenght = 0;
		nextInstruction = false;
		speed = 5.0f;
		jumpVEL = 10.0f;
		gravityVEL = 0.2f;
		jump = 10.0f;
		eatTimer = 50.0f;
		
		pseudoEat = false;
		pseudoTickEat = 0;
		pseudoUnkillableTicks = 0;
		pseudoPauseTicks = -1;
		lastAte = false;
		noClip = false;
		unkillable = false;
		turnWindowOff = false;
		
		collUP = false;
		collDOWN = false;
		collLEFT = false;
		collRIGHT = false;
		
		for(Entity e : gameObjects.getEntities()) { 
			if(e instanceof EnemyWindow) {
				if(e.getAlive()){
					((EnemyWindow) e).changeDamage(1);
				}
		}
	}
	}
	
	public boolean getWindowCom() {
		return turnWindowOff;
	}
	
	public void parseCondition(String condition, ArrayList<String> ifConditions, ArrayList<String> ifCommand) {
		StringBuilder sb = new StringBuilder();
		String str;// = sb.toString()
		
		for(int i = 0; i < condition.length(); ++i) {
			if(condition.charAt(i) == '(') {
				continue;
			}
			if(condition.charAt(i) == '<'){
				if(condition.charAt(i + 1) == '=') {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					sb.append(condition.charAt(i + 1));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					i++;
					continue;
				}else {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					continue;
				}
				
			}
			if(condition.charAt(i) == '>'){
				if(condition.charAt(i + 1) == '=') {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					sb.append(condition.charAt(i + 1));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					i++;
					continue;
				}else {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					continue;
				}
				
			}
			if(condition.charAt(i) == '='){
				if(condition.charAt(i + 1) == '=') {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					sb.append(condition.charAt(i + 1));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					i++;
					continue;
				}else {
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					sb.append(condition.charAt(i));
					str = sb.toString();
					ifConditions.add(str);
					sb.delete(0, sb.length());
					
					continue;
				}
				
			}
			if(condition.charAt(i) == ')') {
				str = sb.toString();
				ifConditions.add(str);
				sb.delete(0, sb.length());
				++i;
				
				for(int z = i; z <= condition.length(); ++z){
					if(condition.charAt(z) == '(') {
						ifCommand.add(sb.toString());
						sb.delete(0, sb.length());
						++z;
						
						if(condition.charAt(z) == ')') {
							sb.append("0");
							ifCommand.add(sb.toString());
							break;
						}
						
						for(int k = z; k <= condition.length(); ++k) {
							if(condition.charAt(k) == ')') {
								ifCommand.add(sb.toString());
								break;
							}
							sb.append(condition.charAt(k));
						}
						
						break;
						
					}
					
					sb.append(condition.charAt(z));
				}
				
				break;
			}
			sb.append(condition.charAt(i));
		}
	}
	
	public void readyHash() {
		
		IntVars.put("killCount", killCount);
		IntVars.put("health", health);
		FloatVars.put("speed", speed);
		FloatVars.put("jump", jump);
		FloatVars.put("gravity", gravityVEL);
		FloatVars.put("x", x);
		FloatVars.put("y", y);
		BoolVars.put("collisionWithPlayer", collisionWithPlayer);
		BoolVars.put("jumping", jumping);
		BoolVars.put("eatingEnemy", eatingEnemy);
		BoolVars.put("noClip", noClip);
		BoolVars.put("unkillable", unkillable);
		BoolVars.put("turnWindowOff", turnWindowOff);
		BoolVars.put("collUP", collUP);
		BoolVars.put("collDOWN", collDOWN);
		BoolVars.put("collLEFT", collLEFT);
		BoolVars.put("collRIGHT", collRIGHT);

	}
	
	public void updateHash() {
		
		IntVars.replace("killCount", killCount);
		IntVars.replace("health", health);
		FloatVars.replace("speed", speed);
		FloatVars.replace("jump", jump);
		FloatVars.replace("gravity", gravityVEL);
		FloatVars.replace("x", x);
		FloatVars.replace("y", y);
		BoolVars.replace("collisionWithPlayer", collisionWithPlayer);
		BoolVars.replace("jumping", jumping);
		BoolVars.replace("eatingEnemy", eatingEnemy);
		BoolVars.replace("noClip", noClip);
		BoolVars.replace("unkillable", unkillable);
		BoolVars.replace("turnWindowOff", turnWindowOff);
		BoolVars.replace("collUP", collUP);
		BoolVars.replace("collDOWN", collDOWN);
		BoolVars.replace("collLEFT", collLEFT);
		BoolVars.replace("collRIGHT", collRIGHT);
	}
	
	public void deleteHash() {
		IntVars.clear();
		FloatVars.clear();
		BoolVars.clear();
	}
	
}
