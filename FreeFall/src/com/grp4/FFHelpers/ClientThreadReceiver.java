package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameWorld.GameWorld;
import com.grp4.Screens.GameScreen;

public class ClientThreadReceiver extends Thread{
	private GameScreen gs;
	private CyclicBarrier cb;
	private GameWorld myWorld;
	private BufferedReader reader;
	private String inString;
	private boolean connected;
	
	public ClientThreadReceiver(GameScreen gs, CyclicBarrier cb, GameWorld world) {
		this.gs = gs;
		this.cb = cb;
		myWorld = world;
	}
	
	@Override
	public void run() {
		try {
			cb.await();
		} catch (Exception e) {
			System.err.println("barrier failed");
			e.printStackTrace();
		}
		
		reader = gs.getHostReader();
		
		connected = true;
		
		while (!isInterrupted() && connected) {
			try {
				inString = reader.readLine();
				//myWorld.setMessage(inString);
				System.err.println("message set : " + inString);
			} catch (Exception e) {
				System.err.println("read line failed");
				e.printStackTrace();
				connected = false;
			}
		}
	}
}
