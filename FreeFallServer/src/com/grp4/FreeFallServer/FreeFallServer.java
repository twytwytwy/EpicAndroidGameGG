package com.grp4.FreeFallServer;

import com.badlogic.gdx.Game;
import com.grp4.Helpers.AssetLoader;
import com.grp4.Screens.GameScreen;

public class FreeFallServer extends Game{

	@Override
	public void create() {		
		AssetLoader.load();
		setScreen(new GameScreen());
		
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}
