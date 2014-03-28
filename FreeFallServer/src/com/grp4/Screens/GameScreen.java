package com.grp4.Screens;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.badlogic.gdx.Screen;
import com.grp4.GameWorld.GameRenderer;
import com.grp4.GameWorld.GameWorld;
import com.grp4.Helpers.ServerReader;
import com.grp4.Helpers.ServerThread;
import com.grp4.Helpers.ServerWriter;

public class GameScreen implements Screen {

	private GameWorld world;
	private GameRenderer renderer;
	
	//private ServerThread serverThread;
	private ServerReader p1threadReader, p2threadReader;
	private ServerWriter serverThreadWriter;
	
	private ServerSocket server;
	private Socket p1socket, p2socket;
	private PrintWriter p1writer, p2writer;
	private BufferedReader p1reader, p2reader;
	
	private CyclicBarrier cb;

	private float runTime;

	public GameScreen() {
		float gameWidth = 136;
		float gameHeight = 226;

		runTime = 0;

		world = new GameWorld(gameWidth, gameHeight);
		renderer = new GameRenderer(world, (int) gameHeight, (int) gameWidth);
		
		cb = new CyclicBarrier(2);
		
		//serverThread = new ServerThread(this, cb, world);
		serverThreadWriter = new ServerWriter(this, cb, world);
		p1threadReader = new ServerReader(this, cb, world, true);
		//p2threadReader = new ServerReader(this, cb, world, false);
		
		serverThreadWriter.start();
		//serverThread.start();
		p1threadReader.start();
		//p2threadReader.start();
		
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
		p1writer.close();
		//p2writer.close();
		
		//serverThread.interrupt();
		p1threadReader.interrupt();
		//p2threadReader.interrupt();
		serverThreadWriter.interrupt();
		
		try {
			p1reader.close();
			//p2reader.close();
			
			p1socket.close();
			p2socket.close();
			server.close();
			
			//serverThread.join();
			p1threadReader.join();
			//p2threadReader.join();
			serverThreadWriter.join();
			
		} catch (Exception e) {
			System.err.println("GameScreen dispose failed!");
			e.printStackTrace();
		}
	}
	
	// -------- getters setters for sockets, readers, writers -------- //
	
	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public Socket getP1socket() {
		return p1socket;
	}

	public void setP1socket(Socket p1socket) {
		this.p1socket = p1socket;
	}

	public Socket getP2socket() {
		return p2socket;
	}

	public void setP2socket(Socket p2socket) {
		this.p2socket = p2socket;
	}

	public PrintWriter getP1writer() {
		return p1writer;
	}

	public void setP1writer(PrintWriter p1writer) {
		this.p1writer = p1writer;
	}

	public PrintWriter getP2writer() {
		return p2writer;
	}

	public void setP2writer(PrintWriter p2writer) {
		this.p2writer = p2writer;
	}

	public BufferedReader getP1reader() {
		return p1reader;
	}

	public void setP1reader(BufferedReader p1reader) {
		this.p1reader = p1reader;
	}

	public BufferedReader getP2reader() {
		return p2reader;
	}

	public void setP2reader(BufferedReader p2reader) {
		this.p2reader = p2reader;
	}

}
