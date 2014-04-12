package com.grp4.GameObject;

import java.util.Random;

import com.grp4.GameWorld.GameWorld;

public class ScrollHandler {
	
	// Game World
	private GameWorld myWorld;
	
	//---------- Scrolling Game Objects
	private Background bg1, bg2;
	private Platforms pf1, pf2, pf3, pf4, pf5, pf6;
	private Platforms[] platforms;
	
	// Random element
	private Random r, cloudRandom;

	//---------- Game Data (Scrolling-Related) ----------
	private int SCROLL_SPEED = GameWorld.SCROLL_SPEED;
	private int PLATFORM_GAP = GameWorld.PLATFORM_GAP;
	private int PLATFORM_WIDTH = GameWorld.PLATFORM_WIDTH;
	private int PLATFORM_HEIGHT = GameWorld.PLATFORM_HEIGHT;

	private float midPointY;

	
	public ScrollHandler(GameWorld myWorld, float yPos) {
		
		this.myWorld = myWorld;
		midPointY = yPos;

		r = new Random(System.currentTimeMillis()); // this is to determine platform positions
		cloudRandom = new Random(); // this is for the clouds in the background

		pf1 = new Platforms(nextRandom(), yPos*2, PLATFORM_WIDTH, PLATFORM_HEIGHT,
				SCROLL_SPEED);
		pf2 = new Platforms(nextRandom(), pf1.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf3 = new Platforms(nextRandom(), pf2.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf4 = new Platforms(nextRandom(), pf3.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf5 = new Platforms(nextRandom(), pf4.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf6 = new Platforms(nextRandom(), pf5.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);

		platforms = new Platforms[] { pf1, pf2, pf3, pf4, pf5, pf6 };
		
		bg1 = new Background(-50, 0, 250, (int) (midPointY*3), SCROLL_SPEED/4);
		bg2 = new Background(-50, bg1.getY() + bg1.getHeight() - 1, 250, (int) (midPointY*3), SCROLL_SPEED/4);
		
	}
	
	// scrolls background and reset when necessary
	public void updateClouds(float delta) {
		bg1.update(delta);
		bg2.update(delta);
		
		if (bg1.isScrolledUp) {
			bg1.reset(cloudRandom.nextInt(100) - 100, bg2.getY() + bg2.getHeight() - 1);
		}
		if (bg2.isScrolledUp) {
			bg2.reset(cloudRandom.nextInt(100) - 100, bg1.getY() + bg1.getHeight() - 1);
		}
	}

	// scrolls all platforms and reset them when necessary
	public void update(float delta) {
		// Update our objects;
		for (Platforms i : platforms) {
			i.update(delta);
		}

		// Check if any of the platforms are scrolled up, and reset
		for (int i = 0; i < 6; i ++) {
			Platforms j = platforms[i];
			if (j.isScrolledUp) {
				if (i == 0) {
					j.reset(nextRandom(), platforms[5].getTailY() + PLATFORM_GAP);
					addScore(1);
				} else {
					j.reset(nextRandom(), platforms[i-1].getTailY() + PLATFORM_GAP);
					addScore(1);
				}
			}
		}
	}

	// stop scrolling all platforms and hide them from view
	public void stop() {
		for (Platforms i : platforms) {
			i.stop();
			i.position.y = midPointY * 2 + 5;
		}
	}

	// check if character collided with any platforms
	public void collides(Character character) {
		float current = character.getY() + character.getHeight();
		for (Platforms i : platforms) {
			if (current - i.getY() < 8 && current - i.getY() > 0) {
				i.collides(character);
			}
		}
	}
	
	// reset all platform positions
	public void onRestart() {
		for (int i = 0; i < 6; i ++) {
			Platforms j = platforms[i];
			if (i == 0) {
				j.onRestart(nextRandom(), midPointY*2, SCROLL_SPEED);
			} else {
				j.onRestart(nextRandom(), platforms[i-1].getTailY() + PLATFORM_GAP, SCROLL_SPEED);
			}
		}
	}
	
	public Background getBg1() {
		return bg1;
	}
	
	public Background getBg2() {
		return bg2;
	}
	
	// flag game world to increment score
	private void addScore(int increment) {
	    myWorld.addScore(increment);
	}
	
	// generate the next random number
	private int nextRandom() {
		return r.nextInt(107);
	}
	
	// return array of all platform objects
	public Platforms[] getPlatforms() {
		return platforms;
	}
	
	// change the Random seed and re-position all platforms based on new seed
	public void setPlatforms(int seed) {
		r = new Random(seed);
		for (Platforms i : platforms) {
			i.setX(nextRandom());
		}
	}

}
