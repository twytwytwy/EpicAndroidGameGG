package com.grp4.GameObject;

public class Background extends Scrollable{

	public Background(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed/3);
	}
	
	public void reset(float newX, float newY) {
		super.reset(newX, newY);
	}

}

