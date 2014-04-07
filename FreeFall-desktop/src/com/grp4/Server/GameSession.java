package com.grp4.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.lwjgl.Sys;

public class GameSession extends Thread {
	private Socket player1socket, player2socket;
	private PrintWriter player1writer, player2writer;
	private BufferedReader player1reader, player2reader;
	private String response1, response2, lastLine1, lastLine2;
	
	private boolean finished, gameSetUp, gameOn, gameRunning;
	
	private int timeOut = 10000;

	public GameSession(Socket player1socket, Socket player2socket) {
		this.player1socket = player1socket;
		this.player2socket = player2socket;
		finished = false;
		gameSetUp = false;
		gameOn = false;
		gameRunning = false;
		response1 = "";
		response2 = "";
		lastLine1 = "cd";
		lastLine2 = "cd";
	}
	
	private void initIO() throws Exception {
		player1writer = new PrintWriter(player1socket.getOutputStream(), true);
		player2writer = new PrintWriter(player2socket.getOutputStream(), true);
		player1reader = new BufferedReader(new InputStreamReader(player1socket.getInputStream()));
		player2reader = new BufferedReader(new InputStreamReader(player2socket.getInputStream()));
		player1socket.setSoTimeout(timeOut);
		player2socket.setSoTimeout(timeOut);
	}
	
	private synchronized void finished() {
		finished = true;
	}
	public synchronized boolean isFinished() {
		return finished;
	}
	
	@Override
	public void run() {
		System.err.println("\t\t!! session started !!");
		
		try {
			initIO();
			gameSetUp = true;
		} catch (Exception e) {
			System.err.println("error in getting I/O streams. abort!");
		}
		
		// setting up seed and position for clients
		if (gameSetUp) {
			System.out.println("sending seed/position info");
			
			player1writer.println("ready");
			player1writer.flush();
			player2writer.println("ready");
			player2writer.flush();
			
			long currentTime = System.currentTimeMillis();
			long newSeed = currentTime - ((currentTime / 1000000) * 1000000);
			
			player1writer.println(newSeed);
			player1writer.flush();
			player2writer.println(newSeed);
			player2writer.flush();
			
			player1writer.println(1);
			player1writer.flush();
			player2writer.println(2);
			player2writer.flush();
			
			System.out.println("seed/position info sent");
			
			try {
				response1 = player1reader.readLine();
				response2 = player2reader.readLine();
				if (response1.equals("ready") && response2.equals("ready")) {
					gameOn = true;
				} else {
					// do something to break out
				}
			} catch (Exception e) {
				System.err.println("game setup time out. abort!");
			}
		}
		
		if (gameOn) {
			int count = 0;
			// 3,2,1,start
			System.out.println("countdown");
			while (count < 4) {
				player1writer.println(lastLine1);
				player1writer.flush();
				player2writer.println(lastLine2);
				player2writer.flush();
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					System.err.println("countdown sleep interrupted");
				}
				count++;
				
			}
			
			// signal to run game on client side
			player1writer.println(lastLine1);
			player1writer.flush();
			player2writer.println(lastLine2);
			player2writer.flush();
				
			// acknowledgement that game is running on client side
			try {
				response1 = player1reader.readLine();
				response2 = player2reader.readLine();
				if (response1.equals("run") && response2.equals("run")) {
					gameRunning = true;
				}
			} catch (Exception e) {
				System.err.println("no acknowledgement to run from clients. abort!");
			}	
		}

		while (gameRunning) {
			try {
				response1 = player1reader.readLine(); // current p1 input
				response2 = player2reader.readLine(); // current p2 input
				
				// send previous compiled input to all
				player1writer.println(lastLine1); 
				player1writer.flush();
				player2writer.println(lastLine2);
				player2writer.flush();
				
				// compile current input
				lastLine1 = response1 + response2;
				lastLine2 = response2 + response1;
				
				// if lastLine is game ended then terminate
				if (lastLine1.equals("endend")) {
					break;
				}
				
			} catch (Exception e) {
				System.err.println("gameplay response time out!");
				break;
			}
		}
		
		System.out.println("game ended or client dc");
		
		// dispose all resources
		player1writer.close();
		player2writer.close();
		try {
			player1socket.close();
			player2socket.close();
			player1reader.close();
			player2reader.close();
		} catch (Exception e) {
			System.err.println("error in closing socket/ buffered reader");
		}
		
		finished();
		System.err.println("\t\t!! session ended !!");
	}
}
