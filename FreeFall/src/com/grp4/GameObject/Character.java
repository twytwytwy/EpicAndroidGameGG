package com.grp4.GameObject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.GameWorld.GameWorld;

public class Character {
	private Vector2 position, velocity, acceleration;

	private int width, height;
	private float originalX, originalY;
	
	private Circle boundingCircle;

	private boolean isAlive;
	
	private int GRAVITY = GameWorld.GRAVITY;
	private int MOVEMENT = GameWorld.MOVEMENT;
	private int LEFTBOUND = GameWorld.LEFTBOUND;
	private int RIGHTBOUND = GameWorld.RIGHTBOUND;
	
	private Sound smashed = AssetLoader.smashed;

	public Character(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		this.originalY = y;
		this.originalX = x;
		position = new Vector2(x, y);
		velocity = new Vector2(MOVEMENT, 0); // trying velocity as gravity
		acceleration = new Vector2(0, GRAVITY); // gravity. positive Y downwards

		boundingCircle = new Circle();

		isAlive = true;
	}

	public void update(float delta) {

		velocity.add(acceleration.cpy().scl(delta));

		if (position.x < LEFTBOUND) {
			position.x = RIGHTBOUND;
		} else if (position.x > RIGHTBOUND) {
			position.x = LEFTBOUND;
		}

		position.add(velocity.cpy().scl(delta));
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f);

	}
	
	public void updateReady(float runTime) {
        position.y = 5 * (float) Math.sin(7 * runTime) + originalY;
        position.x = originalX;
    }
	
	public void collides(Character villian) {
		float diffY = position.y - villian.getY();
		float diffX = position.x - villian.getX();
		if (diffX < 7 && diffX > -7 && diffY < 7 && diffY > -7) {
			if (Intersector.overlaps(boundingCircle, villian.getBoundingCircle())) {
				if (diffX < 0) {
					velocity.x = -80;
					villian.setVelocityX(80);
				} else {
					velocity.x = 80;
					villian.setVelocityX(-80);
				}
				smashed.play();
			}
		}
	}
	
	public void setVelocityX(float x) {
		velocity.x = x;
	}
	
	public void win() {
		position.y = originalY;
		position.x = originalX;
		velocity.x = 0;
		velocity.y = 0;
	}

	public void die() {
		isAlive = false;
		velocity.x = 0;
		velocity.y = -100;
		acceleration.y = 200;
	}
	
	public void onRestart() {
		position.y = originalY;
		position.x = originalX;
		velocity.x = MOVEMENT;
		velocity.y = 0;
		acceleration.y = GRAVITY;
		isAlive = true;
	}

	public void onClick() {
		if (isAlive) {
			velocity.x *= -1;
		}
	}

	public float getX() {
		return position.x;
	}
	public float getY() {
		return position.y;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public Circle getBoundingCircle() {
		return boundingCircle;
	}	
	public int getLeftBound() {
		return LEFTBOUND;
	}	
	public int getRightBound() {
		return RIGHTBOUND;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	public boolean isDead() {
		return !isAlive;
	}
	
	public void setParams(float y) {
		position.y = y - height;
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f);
		velocity.y = 0;
	}	
	public void setPlayer1() {
		position.x = originalX - 34;
		position.y = originalY;
		velocity.x = MOVEMENT;
	}
	public void setPlayer2() {
		position.x = originalX + 34;
		position.y = originalY;
		velocity.x = -1 * MOVEMENT;
	}

}
