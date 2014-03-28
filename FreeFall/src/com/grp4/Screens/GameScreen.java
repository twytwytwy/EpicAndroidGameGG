package com.grp4.Screens;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.grp4.FFHelpers.ClientThreadReceiver;
import com.grp4.FFHelpers.ClientThreadSender;
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
	
	private Socket hostSocket;
	private PrintWriter hostWriter;
	private BufferedReader hostReader;
	
	private ClientThreadSender clientSender;
	private ClientThreadReceiver clientReceiver;
	
	private CyclicBarrier cb;
	
	private float runTime; // determines which frame in animation
	
	public GameScreen() {
		// fix the display width and set the display height
		float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        //System.out.println(screenWidth + "   " + screenHeight);
        // float gameWidth = 136;
        // float gameHeight = screenHeight / (screenWidth / gameWidth);
        	
		float gameWidth = 136;
		float gameHeight = 226;
        //int midPointX = (int) (gameWidth / 2);
        //int midPointY = (int) (gameHeight / 2);
        
        runTime = 0;
        
		world = new GameWorld(gameWidth, gameHeight); // initialize world
		
		cb = new CyclicBarrier(2);
		clientSender = new ClientThreadSender(this, cb, world);
		clientReceiver = new ClientThreadReceiver(this, cb, world);
		clientSender.start();
		clientReceiver.start();
		
		Gdx.input.setInputProcessor(new InputHandler(world, clientSender, screenWidth / gameWidth, screenHeight / gameHeight));
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
		hostWriter.close();
		
		clientSender.interrupt();
		clientReceiver.interrupt();
		
		try {
			hostReader.close();
			hostSocket.close();
			
			clientSender.join();
			clientReceiver.join();
		} catch (Exception e) {
			System.err.println("GameScreen dispose failed!");
			e.printStackTrace();
		}
	}
	
	// -------- getters setters for sockets, readers, writers -------- //

	public Socket getHostSocket() {
		return hostSocket;
	}

	public void setHostSocket(Socket hostSocket) {
		this.hostSocket = hostSocket;
	}

	public PrintWriter getHostWriter() {
		return hostWriter;
	}

	public void setHostWriter(PrintWriter hostWriter) {
		this.hostWriter = hostWriter;
	}

	public BufferedReader getHostReader() {
		return hostReader;
	}

	public void setHostReader(BufferedReader hostReader) {
		this.hostReader = hostReader;
	}
}
