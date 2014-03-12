package com.grp4.FreeFall;

import com.badlogic.gdx.Game;
import com.grp4.Screens.GameScreen;

public class FFGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
	
}
