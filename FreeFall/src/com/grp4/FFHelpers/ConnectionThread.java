package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameObject.Hero;
import com.grp4.GameWorld.GameWorld;

public class ConnectionThread extends Thread{
	private GameWorld world;
	private CyclicBarrier barrier;
	private Hero hero;
	private Hero villian;
	
	private Socket hostSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean waiting = false;
	private boolean gameOn = false;
	private boolean broken = false;
	
	private String line = "";
	
	private String hostname = "192.168.1.217";
	private int portNumber = 4321;
	
	public ConnectionThread(GameWorld world, CyclicBarrier barrier) {
		this.world = world;
		this.barrier = barrier;
		this.hero = world.getHero();
		this.villian = world.getVillian();
	}
	
	@Override
	public void run() {
		try {
			hostSocket = new Socket(hostname, portNumber);
			writer = new PrintWriter(hostSocket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(
					hostSocket.getInputStream()));
			world.connected();
			waiting = true;
		} catch (Exception e) {
			System.err.println("Connection error!");
			//e.printStackTrace();
			world.disconnected();
			waiting = false;
		}
		
		if (waiting) {
			while (true) {
				try {
					if (reader.readLine().equals("ready")) {
						String seed = reader.readLine();
						String position = reader.readLine();
						gameOn = true;
						world.setCD("Ready");
						world.p2connected(seed, position);
						break;
					}
				} catch (Exception e) {
					System.err.println("thread interrupted");
					writer.println("break");
					writer.flush();
					dispose();
					break;
				}
			}
		}
		
		writer.println("ready");
		writer.flush();
		
		if (gameOn) {
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
				System.err.println("readline error at ready");
				e.printStackTrace();
			}
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
					System.err.println("thread interrupted 2");
					writer.println("break");
					writer.flush();
					dispose();
					break;
				}
			}
		}	
	}
	
	public void sendSignal(String text) {
		writer.println(text);
		writer.flush();
	}
	
	public void sendBreak() {
		if (!broken) {
			writer.println("break");
			writer.flush();
			broken = true;
		}
	}
	
	public void dispose() {
		try {
			writer.close();
		} catch (Exception e) {
			System.err.println("writer was not created");
			//e.printStackTrace();
		}

		try {
			reader.close();
		} catch (Exception e) {
			System.err.println("reader was not created");
			//e.printStackTrace();
		}
		
		try {
			hostSocket.close();
		} catch (Exception e) {
			System.err.println("socket was not created");
			//e.printStackTrace();
		}
	}
}
