package com.grp4.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This is the Runnable logic of each game session.
 * This will be executed by ExecutorService therefore saving the hassle of thread management.
 */
public class GameSessionRunnable implements Runnable{
	
	//--------- Game Session Resources and Settings ---------
	private Socket player1socket, player2socket;
	private PrintWriter player1writer, player2writer;
	private BufferedReader player1reader, player2reader;
	private int timeOut = 10000; // Time out for blocking socket operations
	private int gameID;
	
	//--------- Game Data ---------
	private String response1, response2, lastLine1, lastLine2;
	
	//--------- Game Server ----------
	// Server guards the current count of game sessions
	private ExecutorServer server;
	
	//--------- Game Session Stage Flags ---------
	private boolean gameSetUp, gameOn, gameRunning;

	public GameSessionRunnable(Socket player1socket, Socket player2socket, ExecutorServer server, int gameID) {
		this.player1socket = player1socket;
		this.player2socket = player2socket;
		this.server = server;
		this.gameID = gameID;
		gameSetUp = false;
		gameOn = false;
		gameRunning = false;
		response1 = "";
		response2 = "";
		lastLine1 = "cd";
		lastLine2 = "cd";
	}
	
	// Initialise all Game Resources
	private void initIO() throws Exception {
		player1writer = new PrintWriter(player1socket.getOutputStream(), true);
		player2writer = new PrintWriter(player2socket.getOutputStream(), true);
		player1reader = new BufferedReader(new InputStreamReader(player1socket.getInputStream()));
		player2reader = new BufferedReader(new InputStreamReader(player2socket.getInputStream()));
		player1socket.setSoTimeout(timeOut);
		player2socket.setSoTimeout(timeOut);
	}
	
	@Override
	public void run() {
		System.out.println("GAMESESSION: " + gameID + " started.");
		
		//--------- Initialisation ---------
		try {
			initIO();
			gameSetUp = true;
		} catch (Exception e) {
			System.err.println("GAMESESSION: Fatal Initialisation Error.");
		}
		
		//--------- Preparing Clients for Game Session ---------
		if (gameSetUp) { // If initialisation was successful
			
			// Pre-empt clients the sending of seed and position
			player1writer.println("ready");
			player1writer.flush();
			player2writer.println("ready");
			player2writer.flush();
			
			// generate seed based on last 7 digits of current system time
			long currentTime = System.currentTimeMillis();
			long newSeed = currentTime - ((currentTime / 1000000) * 1000000);
			
			// Send seed to clients
			player1writer.println(newSeed);
			player1writer.flush();
			player2writer.println(newSeed);
			player2writer.flush();
			
			// Send positions to clients to determine who is player 1/2
			player1writer.println(1);
			player1writer.flush();
			player2writer.println(2);
			player2writer.flush();
			
			// Acknowledgment that clients set seed and positions
			try {
				response1 = player1reader.readLine();
				response2 = player2reader.readLine();
				if (response1.equals("ready") && response2.equals("ready")) {
					gameOn = true;
				} else {
					System.err.println("GAMESESSION: Fatal Preparation Error: Clients Dis-synchronised.");
				}
			} catch (Exception e) {
				System.err.println("GAMESESSION: Fatal Preparation Error: Timed Out in Waiting for Client Acknowledgment.");
			}
		}
		
		//--------- Running Game Session --------
		if (gameOn) { // clients prepared to run game
			int count = 0;
			while (count < 4) { // Synchronised countdown between clients
				player1writer.println(lastLine1);
				player1writer.flush();
				player2writer.println(lastLine2);
				player2writer.flush();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println("GAMESESSION: Warning: Countdown Sleep Interrupted.");
				}
				count++;
				
			}
			player1writer.println(lastLine1);
			player1writer.flush();
			player2writer.println(lastLine2);
			player2writer.flush();
				
			// Acknowledge that clients finished countdown
			try {
				response1 = player1reader.readLine();
				response2 = player2reader.readLine();
				if (response1.equals("run") && response2.equals("run")) {
					gameRunning = true;
				} else {
					System.err.println("GAMESESSION: Fatal Countdown Error: Clients Dis-synchronised.");
				}
			} catch (Exception e) {
				System.err.println("GAMESESSION: Fatal Countdown Error: Timed Out in Waiting for Client Acknowledgment.");
			}	
		}

		while (gameRunning) { // countdown Successful
			try {
				response1 = player1reader.readLine(); // current cycle player1 input
				response2 = player2reader.readLine(); // current cycle player2 input
				
				// send previous cycle compiled input to clients
				player1writer.println(lastLine1); 
				player1writer.flush();
				player2writer.println(lastLine2);
				player2writer.flush();
				
				// compile current cycle inputs for future cycle
				lastLine1 = response1 + response2;
				lastLine2 = response2 + response1;
				
				// if current cycle inputs from either client is termination flag
				if (response1.equals("end") || response2.equals("end")) {
					break; // terminate running loop
				}
				
			} catch (Exception e) {
				System.err.println("GAMESESSION: Message Relay Time Out: Timed Out during Gameplay.");
				break;
			}
		}
		
		// dispose all resources
		player1writer.close();
		player2writer.close();
		try {
			player1socket.close();
			player2socket.close();
			player1reader.close();
			player2reader.close();
		} catch (Exception e) {
			System.err.println("GAMESESSION: Resource Disposal Failure.");
		}
		
		server.removeGame();
		System.out.println("GAMESESSION: " + gameID + " Closed.");
	}
}
