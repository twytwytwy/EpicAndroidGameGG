package com.grp4.GameWorld;

import java.util.concurrent.CyclicBarrier;

import com.badlogic.gdx.audio.Sound;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.FFHelpers.ConnectionThread;
import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Character;
import com.grp4.GameObject.ScrollHandler;

/*
 * This is the game world class. Keeps all game objects and game object data.
 * Keeps the game state.
 */
public class GameWorld {
	
	//---------- Game Objects ----------
	private Character hero, villain, winner, loser;
	private ScrollHandler scroller;
	private Fire fire;
	
	//--------- Game Resources ----------
	private Sound dead, fall, stapler;
	
	//--------- Game Object Data ----------
	private final float DELTA = .0175f;
	//private final float DELTA = .013f;
	//private final float DELTA = .009f;
	
	public static final int CHARACTER_WIDTH = 17; // character dimensions
	public static final int CHARACTER_HEIGHT = 12;
	public static final int FIRE_WIDTH = 143;
	public static final int FIRE_HEIGHT = 1;
	
	public static final int GRAVITY = 50;
	public static final int MOVEMENT = 80;
	public static final int LEFTBOUND = -16;
	public static final int RIGHTBOUND = 135;
	
	public static final int SCROLL_SPEED = -60;
	public static final int PLATFORM_GAP = 40;
	public static final int PLATFORM_WIDTH = 30;
	public static final int PLATFORM_HEIGHT = 4;

	//--------- Game Information / Settings ----------
	private int prevHighscore, prevHighscore2p;
	private int score = 0;
	private int finalScore = 0;
	private float runTime = 0;
	private int midPointY, midPointX;

	//--------- Multiplayer Objects / Flags ----------
	private boolean connecting = false; // flags whether ConnectionThread was started
	private boolean connected = false;
	private boolean setUp2p = false; //flags whether server is ready to start game
	private boolean disconnected = false; //flags whether ConnectionThread abandoned connection
	private String countdown = "Ready";
	private ConnectionThread connectionThread;
	private CyclicBarrier barrier;	
	private String message; // signal to Server during gameplay

	//--------- Game States ----------
	private GameState currentState;
	public enum GameState {
		MENU, 		// main menu screen
		
				// SinglePlayer:
		READY,		// pre-gameplay state
		RUNNING,	// gameplay state
		GAMEOVER,	// gameover state
		
				// MultiPLayer:
		WAITING,	// awaiting server
		READY2P,	// pre-gameplay state
		RUNNING2P,	// gameplay state
		GAMEOVER2P,	// gameover state
		EXITING,	// termination multiplayer connection
		CONNECTFAIL // notification of connection failure
	}
	
	//--------- GameOver States ----------
	private GGState ggState;
	public enum GGState {
		NONE,	// game has not ended
		WIN,	// local player won
		LOSE,	// remote player won
		DRAW	// it's a draw
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.MENU;
		ggState = GGState.NONE;
		
		this.midPointX = (int) gameWidth / 2;
		this.midPointY = (int) gameHeight / 2;
		
		hero = new Character(midPointX - 5, midPointY - 20, CHARACTER_WIDTH, CHARACTER_HEIGHT);
		villain = new Character(midPointX - 5, midPointY - 20, CHARACTER_WIDTH, CHARACTER_HEIGHT);
		scroller = new ScrollHandler(this, midPointY);
		fire = new Fire(0, 0, gameHeight - FIRE_HEIGHT, FIRE_WIDTH, FIRE_HEIGHT);

		dead = AssetLoader.dead;
		fall = AssetLoader.fall;
		stapler = AssetLoader.stapler;
		
		prevHighscore = AssetLoader.getHighScore();
		prevHighscore2p = AssetLoader.get2pHighScore();
	}
	
	
	//---------- Game Update Methods ----------

	// Main update method that perform different updates based on game state.
	// GameScreen will call this method every cycle
	public void update(float delta) {
		runTime += delta;
		
		scroller.updateClouds(DELTA); // background is always scrolling

		switch (currentState) {
		case MENU:
			animateTitle(runTime);
			break;
		
		// SinglePlayer:
		case READY:
			updateReady();
			break;
		case RUNNING:
			updateRunning();
			break;
		case GAMEOVER:
			updateGG();
			break;
			
		// MultiPlayer:
		case WAITING:
			updateWaiting();
			break;
		case EXITING:
			updateExiting();
			break;
		case RUNNING2P:
			updateRunning2p();
			break;
		case GAMEOVER2P:
			updateGG2p();
			break;
		default:
			break;
		}
	}
	
	// Animate character objects at Menu Screen
	public void animateTitle(float runTime) {
		hero.animate(runTime, DELTA);
		villain.animate2(runTime, DELTA);
		hero.collidesNoSound(villain);
	}
	
	// SinglePlayer: oscillates character at pre-gameplay preparation screen
	public void updateReady() {
		hero.updateReady(runTime);
	}
	
	// SinglePlayer: gameplay update
	public void updateRunning() {

		// update character position and platform positions
		hero.update(DELTA);
		scroller.update(DELTA);

		// collision detection for character and platforms
		if (hero.isAlive()) {
			scroller.collides(hero);
		}
		
		// collision detection character hero and fire
		// set gameover state if necessary
		if (fire.collides(hero) && hero.isAlive()) {
			scroller.stop();
			hero.die();
			gameover();
		}
	}
	
	// SinglePlayer: gameover update
	public void updateGG() {
		hero.update(DELTA);
		AssetLoader.setHighScore(prevHighscore);
	}
	
	// MultiPlayer: update method when game ends to display winner and loser or a draw
	// and to send termination signal to server via ConnectionThread
	public void updateGG2p() {
		// send gameover signal to Server via ConnectionThread
		connectionThread.sendBreak(); 

		// display winner and loser or a draw
		if (ggState == GGState.DRAW) {
			hero.update(DELTA);
			villain.update(DELTA);
		} else {
			winner.updateReady(runTime);
			loser.update(DELTA);
		}
		AssetLoader.set2pHighScore(prevHighscore2p);
	}
	
	// MultiPlayer: gameplay update
	public void updateRunning2p() {
		
		// sends touch input to server
		connectionThread.sendSignal(message);
		message = "O"; // default touch input is none (symbolised by "O")

		try {
			// wait for ConnectionThread to receive server command and 
			// modify character objects
			barrier.await();
		} catch (Exception e) {
			System.err.println("GAMEWORLD: Warning: CyclicBarrier Interrupted.");;
		}

		// update characters and scrolling platform positions
		hero.update(DELTA);
		villain.update(DELTA);
		scroller.update(DELTA);

		// collision detection for all objects
		if (hero.isAlive() && villain.isAlive()) {
			scroller.collides(hero);
			scroller.collides(villain);
			hero.collides(villain);
		}
		
		// collision detection for characters and fire
		// sets gameover state if necessary
		if (fire.collides(hero) && hero.isAlive()) {
			scroller.stop();
			hero.die();
			winner = villain;
			loser = hero;
			gameover2p();
			ggState = GGState.LOSE;
		} 
		if (fire.collides(villain) && villain.isAlive()) {
			scroller.stop();
			villain.die();
			winner = hero;
			loser = villain;
			gameover2p();
			ggState = GGState.WIN;
		}
		// check whether it is a draw
		if (villain.isDead() && hero.isDead()) {
			ggState = GGState.DRAW;
		}
	}
	
	// Multiplayer: create ConnectionThread and await connection
	private void updateWaiting() {
		
		// When method called for the first time, start a ConnectionThread
		if (!connecting) {
			connecting = true;
			
			// main thread and ConnectionThread synchronises through a CyclicBarrier
			// to maintain global order
			barrier = new CyclicBarrier(2);
			connectionThread = new ConnectionThread(this, barrier);
			connectionThread.start();
		}

		// Enters pre-gameplay state if server is ready
		if (setUp2p) {
			ready2p();
		}
		
		// ConnectionThread abandoned connection, exit to menu
		if (disconnected) {
			exiting();
		}
	}
	
	// MultiPlayer: interrupts and join ConnectionThread
	private void updateExiting() {
		
		// resets all multiplayer flags
		disconnected = false;
		setUp2p = false;
		connected = false;
		connecting = false;
		
		connectionThread.interrupt();
		
		try {
			connectionThread.join();
		} catch (Exception e) {
			System.err.println("GAMEWORLD: Warning: Unable to Join ConnectionThread.");
		}
		
		// notification of a failure in connection
		if (ggState == GGState.NONE) {
			connectFail();
		} else {
			menu();
		}
		
		ggState = GGState.NONE;
	}
	
	
	//---------- MultiPlayer Connection Methods ----------

	// Flags that ConnectionThread has connected to Server
	public void connected() {
		connected = true;
	}
	
	// Flags that Server is ready for game and set up platform seed and character position
	public void setUp2p(String seed, String pos) {
		setUp2p = true;
		
		scroller.setPlatforms(Integer.parseInt(seed)); // set platform seed
		
		int position = Integer.parseInt(pos);
		// set character position
		if (position == 1) {
			hero.setPlayer1();
			villain.setPlayer2();
		} else {
			hero.setPlayer2();
			villain.setPlayer1();
		}
	}
	
	// Flags that ConnectionThread has abandoned connection
	public void disconnected() {
		disconnected = true;
	}
	
	// Checks if connection with Server is active
	public boolean isConnected() {
		return connected;
	}
	
	// Sets the signal to be set to Server to "T" which symbolises a touch input
	public void setMessage() {
		message = "T";
	}
	
	// Sets the countdown display for pre-gameplay state
	public synchronized void setCD(String text) {
		countdown = text;
	}
	
	// Get the countdown display for pre-gameplay state
	public synchronized String getCD() {
		return countdown;
	}

	
	//---------- Game Objects and Data Methods ----------

	public Character getHero() {
		return hero;
	}
	public Character getvillain() {
		return villain;
	}
	public ScrollHandler getScroller() {
		return scroller;
	}
	public Fire getFire() {
		return fire;
	}
	public int getMidPointX() {
		return midPointX;
	}
	public int getMidPointY() {
		return midPointY;
	}
	
	// Returns current finalscore
	public int getScore() {
		// final score only increments every 6 points of game score
		if (score / 6 > finalScore) {
			finalScore = score / 6;
			stapler.play();
		}
		return finalScore;
	}
	
	// increment current game score
	public void addScore(int increment) {
		score += increment;
	}

	// Returns cached multiplayer highscore
	public int getPrevHighscore2p() {
		return prevHighscore2p;
	}
	
	// Returns cached singleplayer highscore
	public int getPrevHighscore() {
		return prevHighscore;
	}
	
	//---------- Game States Methods ----------

	// Change game to menu state
	public void menu() {
		currentState = GameState.MENU;
		GameRenderer.setAlpha();
	}
	
	// Multiplayer: Change to gameover state, play sound effect and set 2player highscore
	public void gameover2p() {
		if (currentState != GameState.GAMEOVER2P) {
			currentState = GameState.GAMEOVER2P;
			dead.play();
			fall.play();
			if (finalScore > prevHighscore2p) {
				prevHighscore2p = finalScore;
			}
		}
	}
	
	// Multiplayer: Flags that connection has failed
	public void connectFail() {
		currentState = GameState.CONNECTFAIL;
	}
	
	// Multiplayer: Flags that game is terminating ConnectionThread
	public void exiting() {
		currentState = GameState.EXITING;
	}
	
	// Multiplayer: Flags that game is establishing connection
	public void waiting() {
		currentState = GameState.WAITING;
		GameRenderer.setAlpha();
	}
	
	// Multiplayer: Change to pre-gameplay state
	public void ready2p() {
		currentState = GameState.READY2P;
		GameRenderer.setAlpha();
	}
	
	// Multiplayer: Change to gameplay state
	public void running2p() {
		currentState = GameState.RUNNING2P;
	}
	
	// Multiplayer: Resets game objects and data
	public void restart2p() {
		exiting();
		finalScore = 0;
		score = 0;
		hero.onRestart();
		villain.onRestart();
		scroller.onRestart();
	}
	
	// SinglePlayer: Change to pre-gameplay state
	public void ready() {
		currentState = GameState.READY;
		GameRenderer.setAlpha();
	}
	
	// SinglePlayer: Change to gameplay state
	public void running() {
		currentState = GameState.RUNNING;
	}
	
	// SinglePlayer: Change to gameover state, play sound effects and set highscore
	public void gameover() {
		if (currentState != GameState.GAMEOVER) {
			currentState = GameState.GAMEOVER;
			dead.play();
			fall.play();
			if (finalScore > prevHighscore) {
				prevHighscore = finalScore;
			}
		}
	}
	
	// SinglePlayer: Resets game objects and data
	public void restart() {
		menu();
		finalScore = 0;
		score = 0;
		hero.onRestart();
		scroller.onRestart();
	}
	
	// Return current GameState
	public GameState getCurrentState() {
		return currentState;
	}
	
	
	// Check if GameOver condition is a draw
	public boolean isDraw() {
		return ggState == GGState.DRAW;
	}
	
	// Check if GameOver is a draw
	public boolean isWin() {
		return ggState == GGState.WIN;
	}
	
	// Check if GameOver is a lost
	public boolean isLose() {
		return ggState == GGState.LOSE;
	}
	
	// Check if GameOver is a win
	public GGState getGGState() {
		return ggState;
	}
}
