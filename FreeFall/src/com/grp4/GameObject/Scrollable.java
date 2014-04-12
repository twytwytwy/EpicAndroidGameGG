package com.grp4.GameObject;

import com.badlogic.gdx.math.Vector2;

/*
 * Super-class for all objects in the game that scrolls
 */
public class Scrollable {
	
	protected Vector2 position, velocity;
    protected int width, height;
    protected boolean isScrolledUp;
    
    public Scrollable(float x, float y, int width, int height, float scrollSpeed) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, scrollSpeed);
        this.width = width;
        this.height = height;
        isScrolledUp = false;
    }

    // Scrolls object based on delta time step
    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        // If the Scrollable object is no longer visible, flag to reset
        if (position.y + height < 0) {
            isScrolledUp = true;
        }
    }
    
    // Stop scrolling
    public void stop() {
    	velocity.y = 0;
    }

    // Reset: Should Override in subclass for more specific behavio
    public void reset(float newX, float newY) {
        position.y = newY;
        position.x = newX;
        isScrolledUp = false;
    }

    public boolean isScrolledUp() {
        return isScrolledUp;
    }

    public float getTailY() {
        return position.y + height;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
