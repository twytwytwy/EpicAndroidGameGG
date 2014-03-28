package com.grp4.Helpers;

import java.io.BufferedReader;
import java.util.concurrent.CyclicBarrier;

import com.grp4.GameObjects.Hero;
import com.grp4.GameWorld.GameWorld;
import com.grp4.Screens.GameScreen;

/*
 * Create two threads for Reader, each client takes one BufferedReader
 */
public class ServerReader extends Thread{
	private GameWorld myWorld;
	private Hero hero;
	
	private BufferedReader reader;
	
	private CyclicBarrier cb;
	private GameScreen gs;
	private boolean first, touched, connected;
	
	public ServerReader(GameScreen gs, CyclicBarrier cb, GameWorld myworld, boolean first){
		this.myWorld = myworld;
		hero = myworld.getHero();
		this.gs = gs;
		this.cb = cb;
		this.first = first;
		touched = false;
	}
	
	public void run(){
		try {
			cb.await();
		} catch (Exception e) {
			System.err.println("cyclic barrier error");
			e.printStackTrace();
		}
		
		if (first) {
			reader = gs.getP1reader();
		} else {
			reader = gs.getP2reader();
		}
		
		connected = true;
		
		while(!isInterrupted() && connected){
			try{
				reader.readLine();
				//System.err.println("touch received");
				touched = true;
			}catch(Exception e){
				System.err.println("reader error");
				e.printStackTrace();
				connected = false;
			}
			
			if (touched) {
				if(myWorld.isReady()){
					myWorld.start();
				}
				
				hero.onClick();
				
				if(myWorld.isGameOver()){
					myWorld.restart();
				}
				
				touched = false;
			}
			
//			try {
//				sleep(1500);
//			} catch (Exception e) {
//				System.err.println("sleep error");
//			}
			
		}
	}
}

