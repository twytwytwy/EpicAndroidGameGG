package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameObject.Character;
import com.grp4.GameWorld.GameWorld;

public class ConnectionThread extends Thread{
	private GameWorld world;
	private CyclicBarrier barrier;
	private Character hero, villian;
	
	private Socket hostSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean waiting, gameOn, broken;
	
	private String line;
	
	private String hostname = "192.168.1.217";
	//private String hostname = "localhost";
	private int portNumber = 4321;
	private int timeOut = 10000;
	
	public ConnectionThread(GameWorld world, CyclicBarrier barrier) {
		this.world = world;
		this.barrier = barrier;
		this.hero = world.getHero();
		this.villian = world.getVillian();
		waiting = false;
		gameOn = false;
		broken = false;
		line = "";
	}
	
	@Override
	public void run() {
		int steps = 0;
		try {
			hostSocket = new Socket(hostname, portNumber);
			steps ++; // 1
			hostSocket.setSoTimeout(timeOut);
			writer = new PrintWriter(hostSocket.getOutputStream(), true);
			steps ++; // 2
			reader = new BufferedReader(new InputStreamReader(
					hostSocket.getInputStream()));
			steps ++; // 3
			world.connected();
			waiting = true;
		} catch (Exception e) {
			System.err.println("Connection error!");
			world.disconnected();
		}
		
		if (waiting) {
			try {
				if (reader.readLine().equals("ready")) {
					String seed = reader.readLine();
					String position = reader.readLine();
					gameOn = true;
					world.setCD("Ready");
					world.p2connected(seed, position);
				} else {
					System.err.println("failed to setup game");
					//writer.println("break");
					//writer.flush();
					world.disconnected();
				}
			} catch (Exception e) {
				System.err.println("game setup timed out");
				//writer.println("break");
				//writer.flush();
				world.disconnected();
			}
		}
				
		if (gameOn) {
			writer.println("ready");
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
				System.err.println("readline error at countdown");
			}
			
			//acknowledgement to run
			writer.println("run");
			world.running2p();
			
			while (true) {
				try {
					line = reader.readLine();
					
					if (line.equals("TO")) {
						hero.onClick();
					} else if (line.equals("OT")) {
						villian.onClick();
					} else if (line.equals("TT")) {
						hero.onClick();
						villian.onClick();
					}
					
					barrier.await();
				
				} catch (Exception e) {
					System.err.println("game interrupted / connection time out");
					//writer.println("break");
					//writer.flush();

					world.restart2p();
					break;
				}
			}
		}
		
		// game ends. clear resources
		try {
			dispose(steps);
		} catch (Exception e) {
			System.err.println("failed to dispose resources");
		}
	}
	
	
	public void sendSignal(String text) {
		writer.println(text);
		writer.flush();
	}
	
	public void sendBreak() {
		if (!broken) {
			writer.println("end");
			writer.flush();
			broken = true;
		}
	}
	
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
