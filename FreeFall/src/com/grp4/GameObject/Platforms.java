package com.grp4.GameObject;

import java.util.Random;

public class Platforms extends Scrollable {

	private Random r;
	
	public Platforms (float x, float y, int width, int height, float scrollSpeed) {
        super(x, y, width, height, scrollSpeed);
        r = new Random();
    }
	
	@Override
    public void reset(float newY) {        
        super.reset(newY);
        position.x = r.nextInt(81) + 13;
    }
}
