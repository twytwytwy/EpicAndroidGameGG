package com.grp4.GameWorld;

import com.badlogic.gdx.audio.Sound;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Hero;
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

	private Hero hero;
	private ScrollHandler scroller;
	private Fire fire;

	private Sound coin = AssetLoader.coin;

	private int score = 0;
	private int finalScore = 0;

	private float runTime = 0;

	private int midPointY, midPointX;

	private GameState currentState;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.MENU;
		this.midPointX = (int) gameWidth / 2;
		this.midPointY = (int) gameHeight / 2;
		
		hero = new Hero(midPointX - 10, midPointY - 20, 17, 12);
		scroller = new ScrollHandler(this, midPointY);
		fire = new Fire(0, 0, gameHeight - 11, 143, 11);
	}

	public void update(float delta) {
		runTime += delta;

		switch (currentState) {
		case READY:
		case MENU:
			updateReady(delta);
			break;

		case RUNNING:
		default:
			updateRunning(delta);
			break;
		}
	}

	public void updateReady(float delta) {
		hero.updateReady(runTime);
		scroller.updateReady(delta);
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
			AssetLoader.dead.play();
			currentState = GameState.GAMEOVER;
		}

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

	public int getScore() {
		if (score / 6 > finalScore) {
			finalScore = score / 6;
			coin.play();
		}
		return finalScore;
	}

	public void addScore(int increment) {
		score += increment;
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

}
