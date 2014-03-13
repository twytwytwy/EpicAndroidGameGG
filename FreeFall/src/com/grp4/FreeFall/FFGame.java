package com.grp4.FreeFall;

import com.badlogic.gdx.Game;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.Screens.SplashScreen;

/**
 * FFGame is the main class initialise the entire game
 * extends the Game class, which is a default class from
 * libGDX game engine.
 * 
 * 2 methods 
 * create() initialises game assets and game screen
 * dispose() disposes game assets on exit
 * 
 * @author Wei Yang
 *
 */
public class FFGame extends Game {

	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}
	
	@Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
