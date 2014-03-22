package com.grp4.Screens;

import com.badlogic.gdx.Screen;
import com.grp4.GameWorld.GameRenderer;
import com.grp4.GameWorld.GameWorld;
import com.grp4.Helpers.ServerThread;

public class GameScreen implements Screen{
	
	private GameWorld world;
	private GameRenderer renderer;
	private ServerThread server;
	
	private float runTime;
	
	public GameScreen() {
		float gameWidth = 136;
		float gameHeight = 226;
		
		runTime = 0;
        
		world = new GameWorld(gameWidth, gameHeight);
		renderer = new GameRenderer(world, (int) gameHeight, (int) gameWidth);
		server = new ServerThread(world);
		server.start();
	}

	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
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
