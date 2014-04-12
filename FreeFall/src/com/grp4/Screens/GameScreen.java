package com.grp4.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.grp4.FFHelpers.InputHandler;
import com.grp4.GameWorld.GameRenderer;
import com.grp4.GameWorld.GameWorld;

/**
 * this is the screen for the main gameplay
 * 
 * Methods:
 * constructor - creates the GameWorld and GameRenderer object
 * render() - called by the game at a rate of fps
 * 		takes in delta, delay time, given by the game framework
 * 		updates gameWorld and render GameRenderer
 * 		increment runTime by delta
 * 
 * @author Wei Yang
 *
 */
public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	
	private float runTime; // determines which frame in animation
	
	public GameScreen() {
		// LibGDX advises: fix the display width and set the display height
		
		float screenWidth = Gdx.graphics.getWidth();  // actual device screen size
        float screenHeight = Gdx.graphics.getHeight();
        	
		float gameWidth = 136;
		float gameHeight = 226;
        
        runTime = 0;
        
		world = new GameWorld(gameWidth, gameHeight); // initialize world
		
		Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
		renderer = new GameRenderer(world, (int) gameHeight, (int) gameWidth); // initialize renderer
	}

	@Override
	public void render(float delta) { // delta is the delay between each call to render() its built into libGDX
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