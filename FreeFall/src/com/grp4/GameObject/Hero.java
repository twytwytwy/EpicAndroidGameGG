package com.grp4.GameObject;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Hero {
	private Vector2 position, velocity, acceleration;

	private int width, height;
	private float originalX, originalY;
	
	private Circle boundingCircle;

	private boolean isAlive;
	
	private static final int GRAVITY = 10;
	private static final int MOVEMENT = 50;

	public Hero(float x, float y, int width, int height) {
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

		if (position.x < 12) {
			position.x = 12;
		} else if (position.x > 107) {
			position.x = 107;
		}

		position.add(velocity.cpy().scl(delta));
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f);

	}
	
	public void updateReady(float runTime) {
        position.y = 2 * (float) Math.sin(7 * runTime) + originalY;
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

	public boolean isAlive() {
		return isAlive;
	}

	public void setParams(float y) {
		position.y = y - height;
		boundingCircle.set(position.x + 9, position.y + 6, 6.5f);
		velocity.y = 0;
	}

}
