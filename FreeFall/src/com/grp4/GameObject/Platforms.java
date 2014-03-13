package com.grp4.GameObject;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Platforms extends Scrollable {

	private Random r;
	private Rectangle boundingBox;

	public Platforms(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		r = new Random();
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
	
	public void onRestart(float y, float scrollSpeed) {
		velocity.y = scrollSpeed;
		reset(y);
	}

	@Override
	public void reset(float newY) {
		super.reset(newY);
		position.x = r.nextInt(81) + 13;
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}
}
