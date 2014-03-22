package com.grp4.Helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.grp4.GameObjects.Hero;
import com.grp4.GameWorld.GameWorld;

public class ServerThread extends Thread {
	private GameWorld myWorld;
	private Hero hero;
	
	private ServerSocket serverSocket;
	private Socket player1Socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	private int portNumber = 4321;
	private boolean connected = false;
	
	public ServerThread(GameWorld world) {
		myWorld = world;
		hero = myWorld.getHero();
	}
	
	@Override
	public void run() {
		try {
			System.err.println("setting up connection!");
			serverSocket = new ServerSocket(portNumber);
			System.err.println("server set up!");
			player1Socket = serverSocket.accept();
			System.err.println("player connected!");
			writer = new PrintWriter(player1Socket.getOutputStream(), true);                   
	        reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
	        connected = true;
	        myWorld.ready();
	        System.err.println("connected!");
		} catch (Exception e) {
			System.err.println("caught error!!");
			e.printStackTrace();
		}
		
		while (connected) {
			try {
				reader.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (myWorld.isReady()) {
				myWorld.start();
			}

			hero.onClick();

			if (myWorld.isGameOver()) {
				myWorld.restart();
			}
		}
	}
}
