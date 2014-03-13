package com.grp4.GameObject;

public class Fire {
	
	private float x;
	private float y1;
	private float y2;
	private int width;
	private int height;
	
	public Fire (float x, float y1, float y2, int width, int height) {
		this.x = x;
		this.y1 = y1;
		this.y2 = y2;
		this.width = width;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public float getY1() {
		return y1;
	}
	
	public float getY2() {
		return y2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
