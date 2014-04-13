package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameObject.Character;
import com.grp4.GameWorld.GameWorld;

/*
 * This thread handles connection and communication for game client
 */
public class ConnectionThread extends Thread{
	
	//--------- Network Settings --------
	private String hostname = "192.168.1.217";
	//private String hostname = "localhost";
	private int PORTNUMBER = 4321;
	private int TIMEOUT = 10000;
	
	//--------- Communication Resources, Objects & Flags----------
	private CyclicBarrier barrier;
	private Socket hostSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean waiting, gameOn, broken;
	private String receivedCommand;
	
	//--------- Game Objects ----------
	private GameWorld world;
	private Character hero, villian;
	
	public ConnectionThread(GameWorld world, CyclicBarrier barrier) {
		this.world = world;
		this.barrier = barrier;
		this.hero = world.getHero();
		this.villian = world.getVillian();
		waiting = false;
		gameOn = false;
		broken = false;
		receivedCommand = "";
	}
	
	@Override
	public void run() {
		
		//---------- Establishing connection with Server ----------
		int connectionStage = 0;
		try {
			
			hostSocket = new Socket(hostname, PORTNUMBER);
			connectionStage ++; // 1
			hostSocket.setSoTimeout(TIMEOUT);
			writer = new PrintWriter(hostSocket.getOutputStream(), true);
			connectionStage ++; // 2
			reader = new BufferedReader(new InputStreamReader(
					hostSocket.getInputStream()));
			connectionStage ++; // 3
			
			world.connected(); // flag connection established to game world
			waiting = true;
		
		} catch (Exception e) {
			System.err.println("CONNECTION: Fatal Initialisation Error: Failed to connect with Server.");
			world.disconnected(); // flag disconnection to game world
		}
		
		//---------- Game Set Up ----------
		if (waiting) { // if connection with Server was successful
			try {
				if (reader.readLine().equals("ready")) {
					String seed = reader.readLine();
					String position = reader.readLine();
					gameOn = true;
					world.setCD("Ready");
					world.setUp2p(seed, position);
				} else {
					System.err.println("CONNECTION: Fatal Setup Error: Server dis-synchronised.");
					world.disconnected(); // flag disconnection to game world
				}
			} catch (Exception e) {
				System.err.println("CONNECTION: Fatal Setup Error: Connection time out.");
				world.disconnected(); // flag disconnection to game world
			}
		}
		
		//--------- Maintaining Gameplay Communication ----------
		if (gameOn) { // if game was successfully set up
			writer.println("ready"); // acknowledgement to server
			writer.flush();
			try {
				reader.readLine(); //3
				world.setCD("3");
				reader.readLine(); //2
				world.setCD("2");
				reader.readLine(); //1
				world.setCD("1");
				reader.readLine(); //GO!
				world.setCD("GO!");
				reader.readLine(); //start
			} catch (Exception e) {
				System.err.println("CONNECTION: Warning: Countdown interrupted.");
			}
			
			
			writer.println("run"); // acknowledgement to server
			world.running2p(); // run the multiplayer game
			
			while (true) {
				try {
					receivedCommand = reader.readLine();
					
					if (receivedCommand.equals("TO")) {
						hero.onClick();
					} else if (receivedCommand.equals("OT")) {
						villian.onClick();
					} else if (receivedCommand.equals("TT")) {
						hero.onClick();
						villian.onClick();
					}
					
					barrier.await();
				
				} catch (Exception e) {
					System.err.println("CONNECTION: Warning: Thread interrupted / Connection time out.");
					world.restart2p(); // flag game world to restart
					break;
				}
			}
		}
		
		//--------- Clear Resources ---------
		try {
			dispose(connectionStage);
		} catch (Exception e) {
			System.err.println("CONNECTION: Warning: Failed to dispose resources.");
		}
	}
	
	// Sends the text to Server over the socket output stream
	public void sendSignal(String text) {
		writer.println(text);
		writer.flush();
		//System.err.println(text);
	}
	
	// Sends termination signal to Server over the socket output stream
	public void sendBreak() {
		if (!broken) {
			writer.println("end");
			writer.flush();
			broken = true;
		}
	}
	
	// Clear communication resources
	public void dispose(int steps) throws Exception {
		if (steps == 1) {
			hostSocket.close();
		} else if (steps == 2) {
			hostSocket.close();
			writer.close();
		} else if (steps == 3) {
			hostSocket.close();
			writer.close();
			reader.close();
		}
	}
}
