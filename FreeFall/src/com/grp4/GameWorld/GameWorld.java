package com.grp4.GameWorld;

import com.grp4.GameObject.Hero;
import com.grp4.GameObject.ScrollHandler;

public class GameWorld {
	
	private Hero hero;
	private ScrollHandler scroller;
	
	public GameWorld(int midPointX, int midPointY) {
		hero = new Hero(midPointX, midPointY, 17, 12);
		scroller = new ScrollHandler(midPointY);
	}

	public void update(float delta) {
		hero.update(delta);
		scroller.update(delta);
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public ScrollHandler getScroller() {
        return scroller;
    }

}
