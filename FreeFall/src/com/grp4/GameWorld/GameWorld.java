package com.grp4.GameWorld;

import java.util.concurrent.CyclicBarrier;

import com.grp4.FFHelpers.ConnectionThread;
import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Character;
import com.grp4.GameObject.ScrollHandler;

/**
 * This class stores data of the game world
 * such as game state, scores, runtime.
 * 
 * Cascading updates all objects in the game
 * Invoke collision detection of various game objects
 * 
 * @author Wei Yang
 *
 */
public class GameWorld {

	// most critical game data
	private float DELTA = .013f;
	//private float DELTA = .0175f;
	//private float DELTA = .005f;
	
	// game objects
	private Character hero;
	private Character villian;
	private ScrollHandler scroller;
	private Fire fire;
	private Character winner;
	private Character loser;
	
	// object data
	private int HERO_WIDTH = 17;
	private int HERO_HEIGHT = 12;
	private int FIRE_WIDTH = 143;
	private int FIRE_HEIGHT = 11;

	// game information
	private int score = 0;
	private int finalScore = 0;
	private float runTime = 0;
	private int midPointY, midPointX;

	// multiplayer
	private boolean connecting = false;
	private boolean connected = false;
	private boolean p2connected = false;
	private boolean disconnected = false;
	private String countdown = "Ready";
	private ConnectionThread connectionThread;
	private CyclicBarrier barrier;	
	private String message;

	// game states
	private GameState currentState;
	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, WAITING, READY2P, RUNNING2P, GAMEOVER2P, EXITING, CONNECTFAIL
	}
	
	// game over states
	private GGState ggState;
	public enum GGState {
		NONE, WIN, LOSE, DRAW
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.MENU;
		ggState = GGState.NONE;
		this.midPointX = (int) gameWidth / 2;
		this.midPointY = (int) gameHeight / 2;
		
		hero = new Character(midPointX - 10, midPointY - 20, HERO_WIDTH, HERO_HEIGHT);
		villian = new Character(midPointX - 10, midPointY - 20, HERO_WIDTH, HERO_HEIGHT);
		scroller = new ScrollHandler(this, midPointY);
		fire = new Fire(0, 0, gameHeight - FIRE_HEIGHT, FIRE_WIDTH, FIRE_HEIGHT);
	}
	
	// ------------------------- update methods --------------------------//

	public void update(float delta) {
		runTime += delta;

		switch (currentState) {
		case MENU:
		case READY:
			updateReady();
			break;
		case RUNNING:
		case GAMEOVER:
			updateRunning(delta);
			break;
			
			// game modes for multiplayer
		case WAITING:
			updateWaiting();
			break;
		case EXITING:
			updateExiting();
			break;
		case RUNNING2P:
			updateRunning2p(delta);
			break;
		case GAMEOVER2P:
			updateGG2p(delta);
			break;
		default:
			break;
		}
	}

	public void updateRunning(float delta) {
		
		delta = DELTA;

		hero.update(delta);
		scroller.update(delta);

		// collision detection for hero and platforms
		if (hero.isAlive()) {
			scroller.collides(hero);
		}
		
		// collision detection for hero and fire
		if (fire.collides(hero) && hero.isAlive()) {
			scroller.stop();
			hero.die();
			currentState = GameState.GAMEOVER;
		}
	}
	public void updateReady() {
		hero.updateReady(runTime);
	}
	
	public void updateGG2p(float delta) {
		connectionThread.sendBreak();

		delta = DELTA;

		if (ggState == GGState.DRAW) {
			hero.update(delta);
			villian.update(delta);
		} else {
			winner.updateReady(runTime);
			loser.update(delta);
		}
	}
	public void updateRunning2p(float delta) {
		//System.err.println("before send signal");
		connectionThread.sendSignal(message);
		message = "O";
		//System.err.println("before barrier");
		try {
			barrier.await();
		} catch (Exception e) {
			System.err.println("main thread barrier interrupted");;
		}
		//System.err.println("after barrier");
		
		delta = DELTA;

		hero.update(delta);
		villian.update(delta);
		scroller.update(delta);

		// collision detection for hero and platforms
		if (hero.isAlive() && villian.isAlive()) {
			scroller.collides(hero);
			scroller.collides(villian);
		}
		
		// collision detection for hero and fire
		if (fire.collides(hero) && hero.isAlive()) {
			scroller.stop();
			hero.die();
			//villian.win();
			winner = villian;
			loser = hero;
			currentState = GameState.GAMEOVER2P;
			ggState = GGState.LOSE;
		} 
		if (fire.collides(villian) && villian.isAlive()) {
			scroller.stop();
			villian.die();
			//hero.win();
			winner = hero;
			loser = villian;
			currentState = GameState.GAMEOVER2P;
			ggState = GGState.WIN;
		}
		// check whether it is a draw
		if (villian.isDead() && hero.isDead()) {
			ggState = GGState.DRAW;
		}
	}
	private void updateWaiting() {
		if (!connecting) {
			connecting = true;
			barrier = new CyclicBarrier(2);
			connectionThread = new ConnectionThread(this, barrier);
			connectionThread.start();
			System.out.println("thread started");
		}

		if (p2connected) {
			p2connected = false;
			connected = false;
			connecting = false;
			ready2p();
		}
		
		if (disconnected) {
			exiting();
		}
	}
	
	private void updateExiting() {
		disconnected = false;
		p2connected = false;
		connected = false;
		connecting = false;
		
		System.err.println("exit interrupting thread");
		connectionThread.interrupt();
		try {
			connectionThread.join();
			System.err.println("thread joined");
		} catch (Exception e) {
			System.err.println("unable to join connection thread");
			//e.printStackTrace();
		}
		connectFail();
	}
	
	// ------------------------- connection methods --------------------------//

	public void connected() {
		connected = true;
	}
	public void p2connected(String seed, String pos) {
		p2connected = true;
		scroller.setPlatforms(Integer.parseInt(seed));
		int position = Integer.parseInt(pos);
		if (position == 1) {
			hero.setPlayer1();
			villian.setPlayer2();
		} else {
			hero.setPlayer2();
			villian.setPlayer1();
		}
	}
	public void disconnected() {
		disconnected = true;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setMessage() {
		message = "T";
	}
	public synchronized void setCD(String text) {
		countdown = text;
	}
	public synchronized String getCD() {
		return countdown;
	}

	// ------------------------- game objects methods --------------------------//

	public Character getHero() {
		return hero;
	}
	public Character getVillian() {
		return villian;
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
	public int getScore() {
		if (score / 6 > finalScore) {
			finalScore = score / 6;
		}
		return finalScore;
	}
	public void addScore(int increment) {
		score += increment;
	}
	
	// ------------------------- game states methods --------------------------//

	public void connectFail() {
		currentState = GameState.CONNECTFAIL;
	}
	public void menu() {
		currentState = GameState.MENU;
	}
	public void exiting() {
		currentState = GameState.EXITING;
	}
	public void waiting() {
		currentState = GameState.WAITING;
	}
	public void ready2p() {
		currentState = GameState.READY2P;
	}
	public void running2p() {
		currentState = GameState.RUNNING2P;
	}
	public void restart2p() {
		exiting();
		finalScore = 0;
		score = 0;
		hero.onRestart();
		villian.onRestart();
		scroller.onRestart();
		ggState = GGState.NONE;
	}
	public void ready() {
		currentState = GameState.READY;
	}
	public void running() {
		currentState = GameState.RUNNING;
	}
	public void restart() {
		currentState = GameState.MENU;
		finalScore = 0;
		score = 0;
		hero.onRestart();
		scroller.onRestart();
	}
	
	
	public boolean isConnectFail() {
		return currentState == GameState.CONNECTFAIL;
	}
	public boolean isExiting() {
		return currentState == GameState.EXITING;
	}
	public boolean isWaiting() {
		return currentState == GameState.WAITING;
	}
	public boolean isReady2p() {
		return currentState == GameState.READY2P;
	}
	public boolean isRunning2p() {
		return currentState == GameState.RUNNING2P;
	}
	public boolean isGameOver2p() {
		return currentState == GameState.GAMEOVER2P;
	}
	public boolean isMenu() {
		return currentState == GameState.MENU;
	}
	public boolean isReady() {
		return currentState == GameState.READY;
	}
	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}
	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}
	
	public GameState getCurrentState() {
		return currentState;
	}
	
	
	public boolean isDraw() {
		return ggState == GGState.DRAW;
	}
	public boolean isWin() {
		return ggState == GGState.WIN;
	}
	public boolean isLose() {
		return ggState == GGState.LOSE;
	}
	
	public GGState getGGState() {
		return ggState;
	}
}
