package com.grp4.GameObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Fire {
	
	private float x;
	private float y1;
	private float y2;
	private int width;
	private int height;
	
	private Rectangle boundingBox1 = new Rectangle();
	private Rectangle boundingBox2 = new Rectangle();
	
	public Fire (float x, float y1, float y2, int width, int height) {
		this.x = x;
		this.y1 = y1;
		this.y2 = y2;
		this.width = width;
		this.height = height;
		boundingBox1.set(x, y1, width, height);
		boundingBox2.set(x, y2, width, height);
	}
	
	public boolean collides(Hero hero) {
		if (hero.getY() < y1 + height) {
			return Intersector.overlaps(hero.getBoundingCircle(), boundingBox1);
		} else if (hero.getY() + hero.getHeight() > y2) {
			return Intersector.overlaps(hero.getBoundingCircle(), boundingBox2);
		}
		return false;
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
