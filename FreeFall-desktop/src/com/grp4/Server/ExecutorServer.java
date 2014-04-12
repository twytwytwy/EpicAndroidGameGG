package com.grp4.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* 
 * Networking model for "FreeFall" is the Server-Client model.
 * Each game client program will communicate with this central server program over LAN.
 * This server program handles match-making and communication of game data between clients.
 * Multiple game sessions maintained by ExecutorService.
 */
public class ExecutorServer {

	//---------- Network settings ----------
	private static int PORTNUMBER = 4321; // TCP port for this server program
	private static int MAX_WAIT_TIME = 10000; // Time Out limit for blocking socket operations
	
	//---------- Server settings ----------
	// Maximum number of concurrent game sessions allowed.
	private static int NUMBER_OF_GAMES = 3; 
	
	// @Guardedby("this")
	private int currentGameCount; // number of game sessions currently running concurrently.
	
	public ExecutorServer() {
		currentGameCount = 0;
	}
	
	public int getPortNumber() {
		return PORTNUMBER;
	}
	public int getMaxWaitTime() {
		return MAX_WAIT_TIME;
	}
	public int getMaxSize() {
		return NUMBER_OF_GAMES;
	}
	
	public synchronized int getNumSessions() {
		return currentGameCount;
	}
	public synchronized boolean isFull() {
		return currentGameCount == NUMBER_OF_GAMES;
	}
	public synchronized void addGame() {
		currentGameCount ++;
	}
	public synchronized void removeGame() {
		currentGameCount--;
	}
	
	public static void main(String[] args) {
		
		final ExecutorServer server = new ExecutorServer();
		final ExecutorService exec = new ScheduledThreadPoolExecutor(NUMBER_OF_GAMES);
		
		boolean serverRunning = false; // flag if the server is operating normally
		ServerSocket serverSocket = null;
		
		// Create ServerSocket
		try {
			serverSocket = new ServerSocket(PORTNUMBER);
			serverSocket.setSoTimeout(MAX_WAIT_TIME);
			serverRunning = true;
		} catch (Exception e) {
			System.err.println("SERVER: Fatal Connection Error: Failed to create ServerSocket.");
		}
		
		int currentGameID = 0;
		
		while (serverRunning) { // serverSocket was successfully created

			// check if number of game sessions running is at maximum capacity.
			// Wait till some game finishes.
			while(server.isFull()) {
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					System.err.println("SERVER: Warning: Queuing Sleep Interrupted.");
				}
			}
			
			// accepting player 1 and 2 sockets
			int stagesOfConnection = 1;
			Socket player1socket = null;
			Socket player2socket = null;
			boolean gameSessionOn = false;

			try {
				player1socket = serverSocket.accept();
				stagesOfConnection ++;
				
				player2socket = serverSocket.accept();
				
				gameSessionOn = true;
			
			//timeout failure
			} catch (Exception e) {
				//System.err.println("Socket Time Out: while waiting for player " + stagesOfConnection);
				if (stagesOfConnection == 2) {
					try {
						player1socket.close();
					} catch (Exception ee) {
						System.err.println("SERVER: Socket Closing Failure: Failed to close player1socket.");
					}
				}
			}
			
			
			if (gameSessionOn) { // if both player sockets successfully created
				server.addGame(); // increase current game session count
				GameSessionRunnable newGameSessionRunnable = new GameSessionRunnable(player1socket, player2socket, server, currentGameID);
				currentGameID++;
				exec.execute(newGameSessionRunnable); // delegate the running to executor
			}
			
		}
	}
}
