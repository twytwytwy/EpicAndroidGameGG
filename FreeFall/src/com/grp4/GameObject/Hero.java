package com.grp4.GameObject;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Hero {
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	
	private int width;
	private int height;
	
	private Circle boundingCircle;
	
	public Hero (float x, float y, int width, int height) {
		this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        velocity = new Vector2(30, 0); // trying velocity as gravity
        acceleration = new Vector2(0, 40); // gravity. positive Y downwards
        
        boundingCircle = new Circle();
	}
	
	public void update(float delta) {
		
		velocity.add(acceleration.cpy().scl(delta));
		

        if (position.x < 12) {
        	position.x = 12;
        	velocity.x *= -1;
        } else if (position.x > 107) {
        	position.x = 107;
        	velocity.x *= -1;
        }
        
        if (position.y < 11) {
        	position.y = 11;
        } else if (position.y > 216) {
        	position.y = 216;
        	velocity.y = 0;
        }
        
        position.add(velocity.cpy().scl(delta));
        boundingCircle.set(position.x + 9, position.y + 6, 6.5f);

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
    
    public Circle getBoundingCircle() {
        return boundingCircle;
    }
    
    public void setParams(float y) {
    	position.y = y - height;
    	boundingCircle.set(position.x + 9, position.y + 6, 6.5f);
    	velocity.y = 0;
    }

}
