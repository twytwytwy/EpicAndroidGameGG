package com.grp4.GameWorld;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grp4.Helpers.AssetLoader;
import com.grp4.GameObjects.Fire;
import com.grp4.GameObjects.Hero;
import com.grp4.GameObjects.Platforms;
import com.grp4.GameObjects.ScrollHandler;

public class GameRenderer {
	
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
	private int gameHeight, gameWidth;
    
    // Game Objects
    private Hero hero;
    private ScrollHandler scroller;
    private Platforms pf1, pf2, pf3, pf4, pf5, pf6;
    private Fire flames;

    // Game Assets
    private TextureRegion platform, fire;
    private Animation heroAnimation;
    private TextureRegion wall;

    public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {
        
    	myWorld = world;
    	
    	this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

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
    }

    private void initAssets() {
        heroAnimation = AssetLoader.heroAnimation;
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
        batcher.draw(wall, hero.getLeftBound()-13, 0, 13, gameHeight);
        // right wall
        batcher.draw(wall, hero.getRightBound() + 17, 0, 13, gameHeight);
    }
    
    private void drawPlatforms() {
    	
    	batcher.draw(platform, pf1.getX(), pf1.getY(), pf1.getWidth(), pf1.getHeight());
    	batcher.draw(platform, pf2.getX(), pf2.getY(), pf2.getWidth(), pf2.getHeight());
    	batcher.draw(platform, pf3.getX(), pf3.getY(), pf3.getWidth(), pf3.getHeight());
    	batcher.draw(platform, pf4.getX(), pf4.getY(), pf4.getWidth(), pf4.getHeight());
    	batcher.draw(platform, pf5.getX(), pf5.getY(), pf5.getWidth(), pf5.getHeight());
    	batcher.draw(platform, pf6.getX(), pf6.getY(), pf6.getWidth(), pf6.getHeight());
    }
    
    private void drawHero(float runTime) {
    	
    	// Draw hero at its coordinates. Retrieve the Animation object from AssetLoader
        // Pass in the runTime variable to get the current frame.
    	batcher.draw(heroAnimation.getKeyFrame(runTime),
                hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight());
    }
    
    public void render(float delta, float runTime) {
    	if (myWorld.isLoading()) {
    		renderLoad(delta);
    	} else {
    		renderGame(delta, runTime);
    	}
    }
    
    private void renderLoad(float delta) {
    	// Fill the entire screen with black, to prevent potential flickering.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        // Begin ShapeRenderer
        shapeRenderer.begin(ShapeType.Filled);
        
        // Draw Background color
        shapeRenderer.setColor(174 /255.0f, 238 /255.0f, 238 /255.0f, 1);
        shapeRenderer.rect(0, 0, 136, gameHeight);
        
        // End ShapeRenderer
        shapeRenderer.end();
        
        batcher.begin();
        batcher.disableBlending();
        AssetLoader.shadow.draw(batcher, myWorld.getLoadDisplay(), (136 / 2)- (42), 76);
        AssetLoader.font.draw(batcher, myWorld.getLoadDisplay(), (136 / 2)- (42 - 1), 75);
        batcher.end();
    }

	private void renderGame(float delta, float runTime) {

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
        
        //background. only draw when we have found an appropriate one
        //batcher.draw(bg, background.getX(), background.getY(), 136, 100);
        
        drawPlatforms();
        drawFire();
        drawWalls();
 
        // The hero needs transparency, so we enable that again.
        batcher.enableBlending();
        
        if (myWorld.isRunning()) {
        	drawHero(runTime);
        	
        } else if (myWorld.isReady()) {
            AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2)- (42), 76);
            AssetLoader.font.draw(batcher, "Touch me", (136 / 2)- (42 - 1), 75);
        	
        } else if (myWorld.isGameOver()) {
        	AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
            AssetLoader.font.draw(batcher, "Game Over", 24, 55);      
            AssetLoader.shadow.draw(batcher, "Try again?", 23, 76);
            AssetLoader.font.draw(batcher, "Try again?", 24, 75);

            drawHero(runTime);
        }
        
        // End SpriteBatch
        batcher.end();
    }
}
