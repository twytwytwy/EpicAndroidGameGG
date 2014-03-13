package com.grp4.GameObject;

public class Sides extends Scrollable {

	public Sides(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
	}

	@Override
	public void reset(float newY) {
		super.reset(newY);
	}
	
	public void onRestart(float y, float scrollSpeed) {
		position.y = y;
		velocity.y = scrollSpeed;
	}

}
