package com.grp4.GameWorld;

import com.grp4.GameObject.Hero;

public class GameWorld {
	
	private Hero hero;
	
	public GameWorld(int midPointX, int midPointY) {
		hero = new Hero(midPointX, midPointY, 17, 12);
	}

	public void update(float delta) {
		hero.update(delta);
	}
	
	public Hero getHero() {
		return hero;
	}

}
