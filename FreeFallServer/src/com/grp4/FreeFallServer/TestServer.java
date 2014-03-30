package com.grp4.FreeFallServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import org.w3c.dom.css.Counter;

public class TestServer {

	public static void main(String[] args) throws Exception{
		int portNumber = 4321;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		String lastLine = "";
		String lastLine2 = "";
		Random r = new Random();
		
		while (true) {
			System.err.println("awaiting new client");
			
			Socket clientSocket = serverSocket.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
			
			writer.println("p2wait");
			writer.flush();
			System.err.println("client 1 connected");
			
			Socket clientSocket2 = serverSocket.accept();
			PrintWriter writer2 = new PrintWriter(clientSocket2.getOutputStream(), true);
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
				clientSocket2.getInputStream()));
			
			writer2.println("p2wait");
			writer2.flush();
			System.err.println("client 2 connected");
			
			boolean clientdc = false;
			
//			while (!clientdc) {
//				
//				if (reader.readLine().equals("break")) {
//					clientdc = true;
//				} else {
//					System.err.println("client still alive");
//					writer.println("p2wait");
//					writer.flush();
//				}
//			}
			
			System.err.println("sending seed info");
			if (!clientdc) {
				writer.println("ready");
				writer.flush();
				writer2.println("ready");
				writer2.flush();
				
				int newSeed = r.nextInt(100);
				
				writer.println(newSeed);
				writer.flush();
				writer2.println(newSeed);
				writer2.flush();
				
				writer.println(1);
				writer.flush();
				writer2.println(2);
				writer2.flush();
			}
			System.err.println("seed info sent");
			
			// check if both are ready for countdown
			try {
				reader.readLine();
				reader2.readLine();
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println("countdown sleep interrupted");
			}
			
			lastLine = "ready";
			lastLine2 = "ready";
			
			int count = 0;
			System.err.println("countdown");
			while (count < 4) {
				System.err.println(count);
				writer.println(lastLine);
				writer.flush();
				writer2.println(lastLine);
				writer2.flush();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.err.println("countdown sleep interrupted");
				}
				count++;
			}
			
			System.err.println("start!!");
			writer.println(lastLine);
			writer.flush();
			writer2.println(lastLine);
			writer2.flush();
			
			while (!clientdc) {
				String fromClient = reader.readLine(); // current p1 input
				String fromClient2 = reader2.readLine(); // current p2 input
				
				writer.println(lastLine); // send previous compiled input to all
				writer.flush();
				writer2.println(lastLine2);
				writer2.flush();
				
				if (fromClient.equals("break")) {
					reader.readLine();
					reader2.readLine();
					
					writer.println("break");
					writer.flush();
					writer2.println("break");
					writer2.flush();
					
					clientdc = true;
				} else {
					System.err.println(lastLine);
					lastLine = fromClient + fromClient2;
					lastLine2 = fromClient2 + fromClient;
				}
			}
			
			System.err.println("client dc");
			clientSocket.close();
			reader.close();
			writer.close();
			
			clientSocket2.close();
			reader2.close();
			writer2.close();
			
			System.err.println("client disposed");
		}
	}
}
