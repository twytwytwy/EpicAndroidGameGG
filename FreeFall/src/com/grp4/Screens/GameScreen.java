package com.grp4.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.grp4.FFHelpers.InputHandler;
import com.grp4.GameWorld.GameRenderer;
import com.grp4.GameWorld.GameWorld;

/*
 * This is the main screen of the game.
 * It will initialise all game objects and rendering helpers.
 * 
 * render() method will be called by the framework 60x a second
 * this method will update GameWorld and render GameRenderer
 */
public class GameScreen implements Screen {
	
	//---------- Game Objects and Helpers ---------
	private GameWorld world;
	private GameRenderer renderer;
	
	// Determines the frame for all animations
	private float runTime;
	
	public GameScreen() {
		// LibGDX advises: fix the display width and set the display height
		
		// device screen size
		float screenWidth = Gdx.graphics.getWidth(); 
        float screenHeight = Gdx.graphics.getHeight();
        
        // fixed game coordinates
		float gameWidth = 136;
		float gameHeight = 226;
        
        runTime = 0;
        
        // initialise game world
		world = new GameWorld(gameWidth, gameHeight);
		// initialise touch input handler
		Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
		// initialise game renderer
		renderer = new GameRenderer(world, (int) gameWidth, (int) gameHeight);
	}

	// This method will be called 60x a second.
	// Delay between each call is given by delta, which can be used for scaling.
	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta); // GameWorld updates 
        renderer.render(delta, runTime); // GameRenderer renders
        //System.out.println(1/delta); // frame rate
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {

	}
	
}