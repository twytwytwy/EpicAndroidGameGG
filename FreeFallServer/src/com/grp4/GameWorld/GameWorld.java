package com.grp4.GameWorld;

import com.badlogic.gdx.audio.Sound;
import com.grp4.Helpers.AssetLoader;
import com.grp4.Helpers.ServerThread;
import com.grp4.GameObjects.Fire;
import com.grp4.GameObjects.Hero;
import com.grp4.GameObjects.ScrollHandler;
import com.sun.xml.internal.ws.client.SenderException;

public class GameWorld {
	
	private Hero hero;
	private ScrollHandler scroller;
	private Fire fire;

	private int score = 0;
	private int finalScore = 0;

	private float runTime = 0;

	private int midPointY, midPointX;

	private GameState currentState;
	
	private String loadDisplay;
	private String message;
	private boolean touched;
	private boolean send;

	public enum GameState {
		LOADING, READY, RUNNING, GAMEOVER
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.LOADING;
		this.midPointX = (int) gameWidth / 2;
		this.midPointY = (int) gameHeight / 2;
		
		hero = new Hero(midPointX - 10, midPointY - 20, 17, 12);
		scroller = new ScrollHandler(this, midPointY);
		fire = new Fire(0, 0, gameHeight - 11, 143, 11);
		
		loadDisplay = "";
		message = "";
		touched = false;
		send = false;
	}

	// ------------------------- game update methods --------------------------//
	
	public void event(float delta) {
		if (touched) {
			touched = false;
			
			if(isReady()){
				start();
			} else if (isRunning()){
				hero.onClick();
			} else if(isGameOver()){
				restart();
			}
		}
	}
	
	public void update(float delta) {
		runTime += delta;
		
		event(delta);
		setMessage(delta);

		switch (currentState) {
		case LOADING:
			//updateLoading(delta);
		case READY:
			updateReady(delta);
			break;

		case RUNNING:
		default:
			updateRunning(delta);
			break;
		}
	}
	
	public void updateLoading(float delta) {
		
	}

	public void updateReady(float delta) {
		hero.updateReady(runTime);
	}

	public void updateRunning(float delta) {
		
		// delta correction of game data
		if (delta > .15f) {
			delta = .15f;
		}

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
	
	// ------------------------- game object methods --------------------------//
	
	public boolean send() {
		return send;
	}
	
	public void sent() {
		send = false;
	}
	
	public void touched() {
		touched = true;
	}
	
	public String getLoadDisplay() {
		return loadDisplay;
	}
	
	public void setLoadDisplay(String text) {
		loadDisplay = text;
	}
	
	public void resetLoadDisplay(String text) {
		loadDisplay = "...loading...";
	}
	
	public String getMessage() {
		String output;
		synchronized (message) {
			output = message;
		}
		return output;
	}
	
	public void setMessage(float delta) {
		synchronized (message) {
			message = "" + delta;
		}
		send = true;
	}

	public Hero getHero() {
		return hero;
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

	// ------------------------- game states methods --------------------------//

	public void ready() {
		currentState = GameState.READY;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void restart() {
		currentState = GameState.READY;
		finalScore = 0;
		score = 0;
		hero.onRestart();
		scroller.onRestart();
	}

	public boolean isLoading() {
		return currentState == GameState.LOADING;
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

}
