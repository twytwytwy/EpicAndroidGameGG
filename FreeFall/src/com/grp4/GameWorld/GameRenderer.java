package com.grp4.GameWorld;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
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
import com.grp4.TweenAccessors.Value;
import com.grp4.TweenAccessors.ValueAccessor;
import com.grp4.ui.SimpleButton;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.FFHelpers.InputHandler;

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
    private Sides s1, s2, s3;
    //private Background background;
    private Fire flames;

    // Game Assets
    private TextureRegion bg, platform, fire;
    private Animation heroAnimation;
    //private TextureRegion heroMid, heroDown, heroUp;
    private TextureRegion skullUp, wall;
    
    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();

    // Buttons
    private List<SimpleButton> menuButtons;

    public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {
        
    	myWorld = world;
    	
    	this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        
        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
        
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, gameHeight); // this will be the game width and height units
        
        batcher = new SpriteBatch(); // Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        
        initGameObjects(); // initialise all game objects and stuff
        initAssets();
        setupTweens();
    }
    
    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
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
//        background = scroller.getBg();
    }

    private void initAssets() {
        bg = AssetLoader.bg;
        heroAnimation = AssetLoader.heroAnimation;
//        heroMid = AssetLoader.heroMid;
//        heroDown = AssetLoader.heroDown;
//        heroUp = AssetLoader.heroUp;
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
        batcher.draw(wall, hero.getLeftBound()-13, 0, 13, gameHeight);
        batcher.draw(skullUp, s1.getX(), s1.getY(), s1.getWidth(), s1.getHeight());
        batcher.draw(skullUp, s2.getX(), s2.getY(), s2.getWidth(), s2.getHeight());
        batcher.draw(skullUp, s3.getX(), s3.getY(), s3.getWidth(), s3.getHeight());
        
        // right wall
        batcher.draw(wall, hero.getRightBound() + 17, 0, 13, gameHeight);
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
    
    private void drawHero(float runTime) {
    	
    	// Draw hero at its coordinates. Retrieve the Animation object from AssetLoader
        // Pass in the runTime variable to get the current frame.
    	batcher.draw(heroAnimation.getKeyFrame(runTime),
                hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight());
    }
    
    private void drawMenuUI() {
    	AssetLoader.shadow.draw(batcher, "FreeFall", (136 / 2) - (35), 76);
    	AssetLoader.font.draw(batcher, "FreeFall", (136 / 2) - (35 - 1), 75);

        for (SimpleButton button : menuButtons) {
            button.draw(batcher);
        }
    }
    
    private void drawScore() {
    	 // Convert integer into String
        String score = myWorld.getScore() + "";

        // Draw shadow first
        AssetLoader.shadow.draw(batcher, score, 13, 14); //(136 / 2) - (3 * score.length())
        // Draw text
        AssetLoader.font.draw(batcher, score, 14, 13); // (136 / 2) - (3 * score.length() - 1)
    }

	public void render(float delta, float runTime) {

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
        	drawScore();
        	
        } else if (myWorld.isReady()) {
            AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2)- (42), 76);
            AssetLoader.font.draw(batcher, "Touch me", (136 / 2)- (42 - 1), 75);
            drawScore();
            
        } else if (myWorld.isMenu()) {
        	drawMenuUI();
        	
        } else if (myWorld.isGameOver()) {
        	AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
            AssetLoader.font.draw(batcher, "Game Over", 24, 55);      
            AssetLoader.shadow.draw(batcher, "Try again?", 23, 76);
            AssetLoader.font.draw(batcher, "Try again?", 24, 75);
            
            drawScore();
            drawHero(runTime);
        }
        
        // End SpriteBatch
        batcher.end();
        drawTransition(delta);
        
        
        
        // --------------------------- collision render for debug --------------------------------------//
        
//        shapeRenderer.begin(ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(hero.getBoundingCircle().x, hero.getBoundingCircle().y, hero.getBoundingCircle().radius);
//        
//        shapeRenderer.rect(pf1.getBoundingBox().x, pf1.getBoundingBox().y, pf1.getBoundingBox().width, pf1.getBoundingBox().height);
//        shapeRenderer.rect(pf2.getBoundingBox().x, pf2.getBoundingBox().y, pf2.getBoundingBox().width, pf2.getBoundingBox().height);
//        shapeRenderer.rect(pf3.getBoundingBox().x, pf3.getBoundingBox().y, pf3.getBoundingBox().width, pf3.getBoundingBox().height);
//        shapeRenderer.rect(pf4.getBoundingBox().x, pf4.getBoundingBox().y, pf4.getBoundingBox().width, pf4.getBoundingBox().height);
//        shapeRenderer.rect(pf5.getBoundingBox().x, pf5.getBoundingBox().y, pf5.getBoundingBox().width, pf5.getBoundingBox().height);
//        shapeRenderer.rect(pf6.getBoundingBox().x, pf6.getBoundingBox().y, pf6.getBoundingBox().width, pf6.getBoundingBox().height);
//        
//        shapeRenderer.end();
        
    }
	
	private void drawTransition(float delta) {
        if (alpha.getValue() > 0) {
            manager.update(delta);
            Gdx.gl.glEnable(GL10.GL_BLEND);
            Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, alpha.getValue());
            shapeRenderer.rect(0, 0, 136, 300);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL10.GL_BLEND);

        }
    }
}
