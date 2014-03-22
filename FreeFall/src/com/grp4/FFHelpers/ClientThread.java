package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.grp4.GameWorld.GameWorld;

public class ClientThread extends Thread {
	private Socket hostSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String hostname = "192.168.1.217";
	private int portNumber = 4321;
	
	private boolean connected = false;
	private Boolean message = false;
	private GameWorld myWorld;
	
	public ClientThread(GameWorld world) {
		myWorld = world;
	}
	
	public void sendMessage() {
		synchronized (message) {
			message = true;
		}
	}
	
	@Override
	public void run() {
		try {
			hostSocket = new Socket(hostname, portNumber);
			writer = new PrintWriter(hostSocket.getOutputStream(), true);
	        reader = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
	        connected = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (connected) {
			myWorld.ready();
		}
		while (connected) {
			if (message) {
				synchronized (message) {
					writer.println("touch");
					writer.flush();
					message = false;
				}
			}
		}
	}
}
