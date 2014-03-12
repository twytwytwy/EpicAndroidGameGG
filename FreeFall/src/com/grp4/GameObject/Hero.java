package com.grp4.GameObject;

import com.badlogic.gdx.math.Vector2;

public class Hero {
	private Vector2 position;
	private Vector2 velocity;
	//private Vector2 acceleration;
	
	private int width;
	private int height;
	
	public Hero (float x, float y, int width, int height) {
		this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        velocity = new Vector2(80, 80); // trying velocity as gravity
        //acceleration = new Vector2(0, 460); // gravity. positive Y downwards
	}
	
	public void update(float delta) {
		
		position.add(velocity.cpy().scl(delta));

        if (position.x < 5) {
        	position.x = 5;
        } else if (position.x > 115) {
        	position.x = 115;
        }
        
        if (position.y < 5) {
        	position.y = 5;
        	velocity.y *= -1;
        } else if (position.y > 200) {
        	position.y = 200;
        	velocity.y *= -1;
        }

    }

    public void onClick() {
        velocity.x *= -1;
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

}
