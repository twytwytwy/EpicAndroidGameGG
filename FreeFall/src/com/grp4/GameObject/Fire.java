package com.grp4.GameObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/*
 * boundary objects in game
 * Hitboxes are "death-zones" causing players to die
 */
public class Fire {
	
	private float x, y1, y2;
	private int width, height;
	
	// Hitboxes
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
	
	// Check if character has collided with this object
	public boolean collides(Character character) {
		if (character.getY() < y1 + height) {
			return Intersector.overlaps(character.getBoundingCircle(), boundingBox1);
		} else if (character.getY() + character.getHeight() > y2) {
			return Intersector.overlaps(character.getBoundingCircle(), boundingBox2);
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
