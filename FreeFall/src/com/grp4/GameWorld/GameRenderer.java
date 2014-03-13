package com.grp4.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grp4.GameObject.Background;
import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Hero;
import com.grp4.GameObject.Platforms;
import com.grp4.GameObject.ScrollHandler;
import com.grp4.GameObject.Sides;
import com.grp4.FFHelpers.AssetLoader;

public class GameRenderer {
	
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
	private int midPointY;
    private int gameHeight;
    
    // Game Objects
    private Hero hero;
    private ScrollHandler scroller;
    private Platforms pf1, pf2, pf3, pf4, pf5, pf6;
    private Sides s1, s2, s3;
    private Background background;
    private Fire flames;

    // Game Assets
    private TextureRegion bg, platform, fire;
    private Animation heroAnimation;
    private TextureRegion heroMid, heroDown, heroUp;
    private TextureRegion skullUp, wall;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
        
    	myWorld = world;
    	
    	this.gameHeight = gameHeight;
        this.midPointY = midPointY;
        
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, gameHeight); // this will be the game width and height units
        
        batcher = new SpriteBatch(); // Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        
        initGameObjects(); // initialise all game objects and stuff
        initAssets();
    }
    
    private void initGameObjects() {
        hero = myWorld.getHero();
        flames = myWorld.getFire();
        scroller = myWorld.getScroller();
        pf1 = scroller.getPf1();
        pf2 = scroller.getPf2();
        pf3 = scroller.getPf3();
        pf4 = scroller.getPf4();
        pf5 = scroller.getPf5();
        pf6 = scroller.getPf6();
        s1 = scroller.getS1();
        s2 = scroller.getS2();
        s3 = scroller.getS3();
        background = scroller.getBg();
    }

    private void initAssets() {
        bg = AssetLoader.bg;
        heroAnimation = AssetLoader.heroAnimation;
        heroMid = AssetLoader.heroMid;
        heroDown = AssetLoader.heroDown;
        heroUp = AssetLoader.heroUp;
        skullUp = AssetLoader.skullUp;
        wall = AssetLoader.wall;
        fire = AssetLoader.fire;
        platform = AssetLoader.platform;
    }
    
    private void drawFire() {

        batcher.draw(fire, flames.getX(), flames.getY1(),
        		flames.getWidth(), flames.getHeight());
        batcher.draw(fire, flames.getX(), flames.getY2(),
        		flames.getWidth(), flames.getHeight());
    }
    
    private void drawWalls() {
    	
    	// left wall
        batcher.draw(wall, -1, 0, 13, gameHeight);
        batcher.draw(skullUp, s1.getX(), s1.getY(), s1.getWidth(), s1.getHeight());
        batcher.draw(skullUp, s2.getX(), s2.getY(), s2.getWidth(), s2.getHeight());
        batcher.draw(skullUp, s3.getX(), s3.getY(), s3.getWidth(), s3.getHeight());
        
        // right wall
        batcher.draw(wall, 124, 0, 13, gameHeight);
        batcher.draw(skullUp, 124, s1.getY(), s1.getWidth(), s1.getHeight());
        batcher.draw(skullUp, 124, s2.getY(), s2.getWidth(), s2.getHeight());
        batcher.draw(skullUp, 124, s3.getY(), s3.getWidth(), s3.getHeight());
    }
    
    private void drawPlatforms() {
    	
    	batcher.draw(platform, pf1.getX(), pf1.getY(), pf1.getWidth(), pf1.getHeight());
    	batcher.draw(platform, pf2.getX(), pf2.getY(), pf2.getWidth(), pf2.getHeight());
    	batcher.draw(platform, pf3.getX(), pf3.getY(), pf3.getWidth(), pf3.getHeight());
    	batcher.draw(platform, pf4.getX(), pf4.getY(), pf4.getWidth(), pf4.getHeight());
    	batcher.draw(platform, pf5.getX(), pf5.getY(), pf5.getWidth(), pf5.getHeight());
    	batcher.draw(platform, pf6.getX(), pf6.getY(), pf6.getWidth(), pf6.getHeight());
    }

	public void render(float runTime) {

        // Fill the entire screen with black, to prevent potential flickering.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Begin ShapeRenderer
        shapeRenderer.begin(ShapeType.Filled);
        
        // Draw Background color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 136, gameHeight);
        
        // End ShapeRenderer
        shapeRenderer.end();

        // Begin SpriteBatch
        batcher.begin();
        // Disable transparency 
        // This is good for performance when drawing images that do not require
        // transparency.
        batcher.disableBlending();
        //batcher.draw(bg, background.getX(), background.getY(), 136, 100);
        
        drawPlatforms();
        drawWalls();
        drawFire();

        // The hero needs transparency, so we enable that again.
        batcher.enableBlending();
        
        // Draw hero at its coordinates. Retrieve the Animation object from AssetLoader
        // Pass in the runTime variable to get the current frame.
        batcher.draw(heroAnimation.getKeyFrame(runTime),
                hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight());
        
        // End SpriteBatch
        batcher.end();
        
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(hero.getBoundingCircle().x, hero.getBoundingCircle().y, hero.getBoundingCircle().radius);
        
        shapeRenderer.rect(pf1.getBoundingBox().x, pf1.getBoundingBox().y, pf1.getBoundingBox().width, pf1.getBoundingBox().height);
        shapeRenderer.rect(pf2.getBoundingBox().x, pf2.getBoundingBox().y, pf2.getBoundingBox().width, pf2.getBoundingBox().height);
        shapeRenderer.rect(pf3.getBoundingBox().x, pf3.getBoundingBox().y, pf3.getBoundingBox().width, pf3.getBoundingBox().height);
        shapeRenderer.rect(pf4.getBoundingBox().x, pf4.getBoundingBox().y, pf4.getBoundingBox().width, pf4.getBoundingBox().height);
        shapeRenderer.rect(pf5.getBoundingBox().x, pf5.getBoundingBox().y, pf5.getBoundingBox().width, pf5.getBoundingBox().height);
        shapeRenderer.rect(pf6.getBoundingBox().x, pf6.getBoundingBox().y, pf6.getBoundingBox().width, pf6.getBoundingBox().height);
        
        shapeRenderer.end();
        
    }
}
