package com.grp4.FreeFall;

import com.badlogic.gdx.Game;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.Screens.SplashScreen;

/**
 * FFGame is the main class initialise the entire game
 * extends the Game class, which is a default class from
 * libGDX game engine.
 * 
 * Methods:
 * create() - initialises game assets and game screen
 * dispose() - disposes game assets on exit
 * 
 * @author Wei Yang
 *
 */
public class FFGame extends Game {
	
	/**
	 * Called by Main Activity
	 * Loads all resources in the AssetLoader class
	 * Sets the current screen to SplashScreen
	 */
	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}
	
	/**
	 * Called when game terminates
	 * Disposes the game and all resources
	 */
	@Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
