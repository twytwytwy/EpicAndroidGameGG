package com.grp4.GameWorld;

import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Hero;
import com.grp4.GameObject.ScrollHandler;

public class GameWorld {
	
	private Hero hero;
	private ScrollHandler scroller;
	private Fire fire;
	
	public GameWorld(int midPointX, int midPointY, float gameHeight) {
		hero = new Hero(midPointX, midPointY, 17, 12);
		scroller = new ScrollHandler(midPointY);
		fire = new Fire(0, 0, gameHeight - 11, 143, 11);
	}

	public void update(float delta) {
		hero.update(delta);
		scroller.update(delta);
		scroller.collides(hero);
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

}
