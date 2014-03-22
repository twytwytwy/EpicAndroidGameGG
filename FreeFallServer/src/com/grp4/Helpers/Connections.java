package com.grp4.Helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Connections implements Runnable{
	private ServerSocket serverSocket;
	private Socket player1Socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private int portNumber;
	
	public Connections(int portNumber) {
		this.portNumber = portNumber;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			player1Socket = serverSocket.accept();
			writer = new PrintWriter(player1Socket.getOutputStream(), true);                   
	        reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public Socket getPlayer1Socket() {
		return player1Socket;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
	}

}
