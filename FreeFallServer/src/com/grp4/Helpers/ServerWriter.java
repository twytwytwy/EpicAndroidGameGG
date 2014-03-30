package com.grp4.Helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameWorld.GameWorld;
import com.grp4.Screens.GameScreen;

public class ServerWriter extends Thread {

	private ServerSocket server;
	private Socket p1socket;
	private Socket p2socket;

	private PrintWriter p1writer, p2writer;
	private BufferedReader p1reader, p2reader;

	private GameWorld myWorld;

	//private String cachedMessage = "";
	private String newMessage = "";

	private CyclicBarrier cb;
	private GameScreen gs;

	private int portNumber = 4321;

	public ServerWriter(GameScreen gs, CyclicBarrier cb, GameWorld myWorld) {
		this.gs = gs;
		this.myWorld = myWorld;
		this.cb = cb;
	}

	public void run() {
		try {
			sleep(2000);
			String printOut = "Set Server";
			myWorld.setLoadDisplay(printOut);
			System.err.println(printOut);

			server = new ServerSocket(portNumber);

			sleep(2000);
			printOut = "Server Up";
			myWorld.setLoadDisplay(printOut);
			System.err.println(printOut);

			p1socket = server.accept();

			sleep(2000);
			printOut = "p1 connected";
			myWorld.setLoadDisplay(printOut);
			System.err.println(printOut);

			// p2socket = server.accept();
			// System.err.println("... player2 connected! ...");

			p1writer = new PrintWriter(p1socket.getOutputStream(), true);
			p1reader = new BufferedReader(new InputStreamReader(
					p1socket.getInputStream()));

			// p2writer = new PrintWriter(p2socket.getOutputStream(), true);
			// p2reader = new BufferedReader(new
			// InputStreamReader(p2socket.getInputStream()));

			sleep(2000);
			printOut = "all ready";
			myWorld.setLoadDisplay(printOut);
			System.err.println(printOut);

		} catch (Exception e) {
			System.err.println("!!! ERROR ERROR !!!");
			e.printStackTrace();
		}

		gs.setServer(server);
		gs.setP1socket(p1socket);
		gs.setP1reader(p1reader);
		gs.setP1writer(p1writer);
		// gs.setP2socket(p2socket);
		// gs.setP2reader(p2reader);
		// gs.setP2writer(p2writer);

		try {
			cb.await();
		} catch (Exception e) {
			System.err.println("cyclic barrier error");
			e.printStackTrace();
		}

		myWorld.ready();

		while (!isInterrupted()) {
			// System.err.println("true loop");
			if (myWorld.send()) {
				myWorld.sent();
				
				newMessage = myWorld.getMessage();
				// System.err.println("new message : " + newMessage);
				System.err.println("coordinates sending: " + newMessage);
				p1writer.println(newMessage);
				p1writer.flush();
			}
		}
	}

}
