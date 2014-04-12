package com.grp4.GameObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/*
 * Scrolling platform objects in game
 * Hitboxes are zones which allow characters to "stand" on them
 */
public class Platforms extends Scrollable {

	// Hitbox
	private Rectangle boundingBox;

	public Platforms(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		boundingBox = new Rectangle();
	}

	// update the position of platform for delta time step
	@Override
	public void update(float delta) {
		super.update(delta);

		// update hitbox base on new positions
		boundingBox.set(position.x + 5, position.y, width - 10, height);
	}

	// check if a character has collided with the platform
	public void collides(Character character) {
		if (Intersector.overlaps(character.getBoundingCircle(), boundingBox)) {
			character.setParams(position.y); // flush character to "stand" on the platform
		}
	}
	
	// reset the platform position and scrolling speed
	public void onRestart(float x, float y, float scrollSpeed) {
		velocity.y = scrollSpeed;
		reset(x, y);
	}

	@Override
	public void reset(float newX, float newY) {
		super.reset(newX, newY);
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	public void setX(int newPosX) {
		position.x = newPosX;
	}
}
