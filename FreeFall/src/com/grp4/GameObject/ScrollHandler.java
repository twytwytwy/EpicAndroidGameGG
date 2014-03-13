package com.grp4.GameObject;

public class ScrollHandler {
	
	private Sides s1, s2, s3;
	private Platforms pf1, pf2, pf3, pf4, pf5, pf6;
	private Background bg;
	
	// ScrollHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    // the size of the gap between platforms

    // Capital letters are used by convention when naming constants.
    public static final int SCROLL_SPEED = -30;
    public static final int PLATFORM_GAP = 40;
    public static final int SIDES_GAP = 35;
    public static final int PLATFORM_WIDTH = 30;
    public static final int PLATFORM_HEIGHT = 4;
    
    private float midPointY;
    
    // Constructor receives a float that tells us where we need to create our 
    // platforms and sides
    public ScrollHandler(float yPos) {
    	
    	midPointY = yPos;
    	
    	bg = new Background(0, yPos, 136, 100, SCROLL_SPEED*2 / 3);
    	
    	s1 = new Sides(-12, 0, 24, 65, SCROLL_SPEED);
    	s2 = new Sides(-12, s1.getTailY() + SIDES_GAP, 24, 65, SCROLL_SPEED);
    	s3 = new Sides(-12, s2.getTailY() + SIDES_GAP, 24, 65, SCROLL_SPEED);
    	
    	pf1 = new Platforms(13, yPos, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
        pf2 = new Platforms(20, pf1.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
        pf3 = new Platforms(27, pf2.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
        pf4 = new Platforms(34, pf3.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
        pf5 = new Platforms(41, pf4.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
        pf6 = new Platforms(48, pf5.getTailY() + PLATFORM_GAP, PLATFORM_WIDTH, PLATFORM_HEIGHT, SCROLL_SPEED);
    }
    
    public void update(float delta) {
    	// Update our objects;
        pf1.update(delta);
        pf2.update(delta);
        pf3.update(delta);
        pf4.update(delta);
        pf5.update(delta);
        pf6.update(delta);
        
        s1.update(delta);
        s2.update(delta);
        s3.update(delta);
        
        bg.update(delta);

        // Check if any of the platforms are scrolled up,
        // and reset accordingly
        if (pf1.isScrolledUp()) {
            pf1.reset(pf6.getTailY() + PLATFORM_GAP);
        } else if (pf2.isScrolledUp()) {
            pf2.reset(pf1.getTailY() + PLATFORM_GAP);
        } else if (pf3.isScrolledUp()) {
            pf3.reset(pf2.getTailY() + PLATFORM_GAP);
        } else if (pf4.isScrolledUp()) {
            pf4.reset(pf3.getTailY() + PLATFORM_GAP);
        } else if (pf5.isScrolledUp()) {
            pf5.reset(pf4.getTailY() + PLATFORM_GAP);
        } else if (pf6.isScrolledUp()) {
            pf6.reset(pf5.getTailY() + PLATFORM_GAP);
        }

        // Same with sides
        if (s1.isScrolledUp()) {
            s1.reset(s3.getTailY() + SIDES_GAP);
        } else if (s2.isScrolledUp()) {
            s2.reset(s1.getTailY() + SIDES_GAP);
        } else if (s3.isScrolledUp()) {
            s3.reset(s2.getTailY() + SIDES_GAP);
        }
        
        // Same with background
        if (bg.isScrolledUp) {
        	bg.reset(midPointY*2);
        }
    }

	public Sides getS1() {
		return s1;
	}

	public Sides getS2() {
		return s2;
	}

	public Sides getS3() {
		return s3;
	}

	public Platforms getPf1() {
		return pf1;
	}

	public Platforms getPf2() {
		return pf2;
	}

	public Platforms getPf3() {
		return pf3;
	}
	
	public Platforms getPf4() {
		return pf4;
	}
	
	public Platforms getPf5() {
		return pf5;
	}
	
	public Platforms getPf6() {
		return pf6;
	}

	public Background getBg() {
		return bg;
	}

    

}