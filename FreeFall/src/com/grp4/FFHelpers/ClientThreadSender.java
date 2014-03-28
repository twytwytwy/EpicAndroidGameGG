package com.grp4.FFHelpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameWorld.GameWorld;
import com.grp4.Screens.GameScreen;

public class ClientThreadSender extends Thread {
	private Socket hostSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String hostname = "192.168.1.217";
	private int portNumber = 4321;

	private boolean connected = false;
	private Boolean message = false;
	private GameWorld myWorld;

	private GameScreen gs;
	private CyclicBarrier cb;

	public ClientThreadSender(GameScreen gs, CyclicBarrier cb, GameWorld world) {
		this.gs = gs;
		this.cb = cb;
		myWorld = world;
	}

	public synchronized void setTouch() {
			message = true;
			//System.err.println("message to true");
			notifyAll();
	}
	
	public synchronized void sendTouch() {
		try {
			wait();
			if (message) {
				writer.println("a");
				writer.flush();
				message = false;
			}
		} catch (Exception e) {
			System.err.println("wait failed");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			hostSocket = new Socket(hostname, portNumber);
			writer = new PrintWriter(hostSocket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(
					hostSocket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Connection error!");
			e.printStackTrace();
		}

		gs.setHostSocket(hostSocket);
		gs.setHostReader(reader);
		gs.setHostWriter(writer);

		try {
			cb.await();
		} catch (Exception e) {
			System.err.println("barrier failed");
			e.printStackTrace();
		}

		//System.err.println("before ready");

		myWorld.ready();

		//System.err.println("so i never entered true loop");

		while (!isInterrupted()) {
			sendTouch();
		}
	}
}
