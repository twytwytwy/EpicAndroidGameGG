package com.grp4.GameObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Platforms extends Scrollable {

	private Rectangle boundingBox;

	public Platforms(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		boundingBox = new Rectangle();
	}

	@Override
	public void update(float delta) {
		// Call the update method in the superclass (Scrollable)
		super.update(delta);

		// The set() method allows you to set the top left corner's x, y
		// coordinates,
		// along with the width and height of the rectangle
		boundingBox.set(position.x + 5, position.y, width - 10, height);
	}

	public void collides(Character character) {
		if (Intersector.overlaps(character.getBoundingCircle(), boundingBox)) {
			character.setParams(position.y);
			//character.stand();
		}
	}
	
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
