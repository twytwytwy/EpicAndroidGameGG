package com.grp4.FreeFall;

import com.badlogic.gdx.Game;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.Screens.GameScreen;
import com.grp4.Screens.SplashScreen;

// Main Method of "FreeFall"
// Running on top of the MainActivity in Android
public class FFGame extends Game {
	
	// Initialisation Method
	@Override
	public void create() {
		AssetLoader.load(); // Loads graphic and sound resources
		setScreen(new SplashScreen(this)); // Generation of splash screen
	}
	
	// Disposal Method to destroy all resources when game terminates
	@Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
