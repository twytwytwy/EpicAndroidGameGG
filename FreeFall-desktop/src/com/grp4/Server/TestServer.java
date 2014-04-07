package com.grp4.Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestServer {
	private int PORTNUMBER = 4321;
	private int MAX_WAIT_TIME = 10000; // milliseconds
	private ArrayList<GameSession> gameSessions;
	private int numGameSessions;
	
	public TestServer(int numGameSessions) {
		this.numGameSessions = numGameSessions;
		gameSessions = new ArrayList<GameSession>();
	}
	
	public int getPortNumber() {
		return PORTNUMBER;
	}
	public int getMaxWaitTime() {
		return MAX_WAIT_TIME;
	}

	public int getNumSessions() {
		return gameSessions.size();
	}
	public boolean isFull() {
		return numGameSessions == gameSessions.size();
	}
	public void clear() {
		for (GameSession i : gameSessions) {
			if (i.isFinished()) {
				try {
					i.join();
				} catch (Exception e) {
					System.err.println("error in joining game session thread");
				}
				gameSessions.remove(i);
			}
		}
	}
	public void add(GameSession gameSession) {
		gameSessions.add(gameSession);
	}
	
	public static void main(String[] args) {
		TestServer testServer = new TestServer(6);
		boolean serverRunning;
		ServerSocket serverSocket = null;
		
		// creating server socket
		try {
			serverSocket = new ServerSocket(testServer.getPortNumber());
			serverSocket.setSoTimeout(testServer.getMaxWaitTime());
			serverRunning = true;
		} catch (Exception e) {
			System.err.println("Server socket creation failed");
			e.printStackTrace();
			serverRunning = false;
		}
		
		
		while (serverRunning) {
			// if number of game sessions at maximum capacity
			// wait till some session has finished and clear them
			while(testServer.isFull()) {
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					System.err.println("sleep interrupted");
				}
				testServer.clear();
				System.err.println("\t\t!! clear sessions !! SIZE " + testServer.getNumSessions());
			}
			
			// accepting player 1 and 2
			int numConnected = 1;
			Socket player1socket = null;
			Socket player2socket = null;
			boolean gameSessionOn = false;

			try {
				System.out.println("awaiting player 1");
				player1socket = serverSocket.accept();
				numConnected ++; //2
				System.out.println("player 1 connected");
				
				System.out.println("awaiting player 2");
				player2socket = serverSocket.accept();
				System.out.println("player 2 connected");
				
				gameSessionOn = true;
			
			//timeout failure
			} catch (Exception e) {
				gameSessionOn = false;
				System.err.println("socket connection time out for player" + numConnected);
				if (numConnected == 2) {
					try {
						player1socket.close();
					} catch (Exception ee) {
						System.err.println("error in closing player 1");
						e.printStackTrace();
					}
				}
			}
			
			// if both player successfully accepted
			if (gameSessionOn) {
				GameSession newGameSession = new GameSession(player1socket, player2socket);
				newGameSession.start();
				testServer.add(newGameSession);
			}
			
		}
	}
}
