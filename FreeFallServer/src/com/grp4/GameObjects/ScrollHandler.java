package com.grp4.GameObjects;

import com.grp4.GameWorld.GameWorld;

public class ScrollHandler {
	
	private GameWorld myWorld;
	private Platforms pf1, pf2, pf3, pf4, pf5, pf6;
	private Platforms[] platforms;

	// ScrollHandler will use the constants below to determine
	// how fast we need to scroll and also determine
	// the size of the gap between platforms

	// Capital letters are used by convention when naming constants.
	public static final int SCROLL_SPEED = -60;
	public static final int PLATFORM_GAP = 40;
	public static final int PLATFORM_WIDTH = 30;
	public static final int PLATFORM_HEIGHT = 4;

	private float midPointY;

	// Constructor receives a float that tells us where we need to create our
	// platforms and sides
	public ScrollHandler(GameWorld myWorld, float yPos) {
		
		this.myWorld = myWorld;
		
		midPointY = yPos;

		pf1 = new Platforms(13, yPos*2, PLATFORM_WIDTH, PLATFORM_HEIGHT,
				SCROLL_SPEED);
		pf2 = new Platforms(100, pf1.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf3 = new Platforms(40, pf2.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf4 = new Platforms(80, pf3.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf5 = new Platforms(25, pf4.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);
		pf6 = new Platforms(95, pf5.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH,
				PLATFORM_HEIGHT, SCROLL_SPEED);

		platforms = new Platforms[] { pf1, pf2, pf3, pf4, pf5, pf6 };
	}

	public void update(float delta) {
		// Update our objects;
		for (Platforms i : platforms) {
			i.update(delta);
		}

		// Check if any of the platforms are scrolled up,
		// and reset accordingly
		if (pf1.isScrolledUp()) {
			pf1.reset(pf6.getTailY() + PLATFORM_GAP);
		} else if (pf2.isScrolledUp()) {
			pf2.reset(pf1.getTailY() + PLATFORM_GAP);
		} else if (pf3.isScrolledUp()) {
			pf3.reset(pf2.getTailY() + PLATFORM_GAP);
		} else if (pf4.isScrolledUp()) {
			pf4.reset(pf3.getTailY() + PLATFORM_GAP);
		} else if (pf5.isScrolledUp()) {
			pf5.reset(pf4.getTailY() + PLATFORM_GAP);
		} else if (pf6.isScrolledUp()) {
			pf6.reset(pf5.getTailY() + PLATFORM_GAP);
		}
	}

	public void stop() {
		for (Platforms i : platforms) {
			i.stop();
		}
	}

	public void collides(Hero hero) {
		float current = hero.getY() + hero.getHeight();
		for (Platforms i : platforms) {
			if (current - i.getY() < 8 && current - i.getY() > 0) {
				i.collides(hero);
			}
		}
	}
	
	public void onRestart() {	
		pf1.onRestart(midPointY*2, SCROLL_SPEED);
		pf2.onRestart(pf1.getTailY() + PLATFORM_GAP, SCROLL_SPEED);
		pf3.onRestart(pf2.getTailY() + PLATFORM_GAP, SCROLL_SPEED);
		pf4.onRestart(pf3.getTailY() + PLATFORM_GAP, SCROLL_SPEED);
		pf5.onRestart(pf4.getTailY() + PLATFORM_GAP, SCROLL_SPEED);
		pf6.onRestart(pf5.getTailY() + PLATFORM_GAP, SCROLL_SPEED);
	}

	public Platforms getPf1() {
		return pf1;
	}

	public Platforms getPf2() {
		return pf2;
	}

	public Platforms getPf3() {
		return pf3;
	}

	public Platforms getPf4() {
		return pf4;
	}

	public Platforms getPf5() {
		return pf5;
	}

	public Platforms getPf6() {
		return pf6;
	}
}
