package com.grp4.GameObject;

public class Fire {
	
	private float x;
	private float y;
	private int width;
	private int height;
	
	public Fire (float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
