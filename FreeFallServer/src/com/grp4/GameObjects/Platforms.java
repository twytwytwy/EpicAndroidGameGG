package com.grp4.GameObjects;

import java.util.Random;

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

	public void collides(Hero hero) {
		if (Intersector.overlaps(hero.getBoundingCircle(), boundingBox)) {
//			if ((hero.getX() + hero.getWidth()) / 2 > position.x
//					&& (hero.getX() + hero.getWidth()) / 2 < position.x + width) {
				//System.out.println("collision\n!!!");
				hero.setParams(position.y);
//			}
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
}
