package com.grp4.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.grp4.FFHelpers.InputHandler;
import com.grp4.GameWorld.GameRenderer;
import com.grp4.GameWorld.GameWorld;

/**
 * GameScreen class takes care of the display that users will see
 * 
 * render() method will be called at the fps rate therefore it
 * is the Game Loop. Use it to update game data and render image
 * 
 * @author Wei Yang
 *
 */
public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	
	private float runTime; // determines which frame in animation
	
	public GameScreen() {
		float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();      
        float gameWidth = 136;
        float gameHeight = screenHeight / (screenWidth / gameWidth);
        
        int midPointX = (int) (gameWidth / 2);
        int midPointY = (int) (gameHeight / 2);
        
        runTime = 0;
        
		world = new GameWorld(midPointY, gameHeight, midPointX); // initialize world
		renderer = new GameRenderer(world, (int) gameHeight, midPointY); // initialize renderer
		
		Gdx.input.setInputProcessor(new InputHandler(world));
	}

	@Override
	public void render(float delta) { // delta is the delay between each call to render() its built into libGDX
		runTime += delta;
		world.update(delta); // GameWorld updates 
        renderer.render(runTime); // GameRenderer renders
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
		// TODO Auto-generated method stub
		
	}
	
	
}
