package com.grp4.FreeFall;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FreeFall";
		cfg.useGL20 = false;
		cfg.width = 272;
		cfg.height = 452;
		
		new LwjglApplication(new FFGame(), cfg);
	}
}
