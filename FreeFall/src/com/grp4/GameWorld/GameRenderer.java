package com.grp4.GameWorld;

import java.sql.Time;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grp4.GameObject.Background;
import com.grp4.GameObject.Fire;
import com.grp4.GameObject.Character;
import com.grp4.GameObject.Platforms;
import com.grp4.GameObject.ScrollHandler;
import com.grp4.TweenAccessors.Value;
import com.grp4.TweenAccessors.ValueAccessor;
import com.grp4.ui.SimpleButton;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.FFHelpers.InputHandler;

/*
 * This helper class will draw all the game objects in game
 */
public class GameRenderer {

	//---------- Game Environment Objects and Data ---------
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	private int gameHeight, gameWidth, midpointY, midpointX;

	//---------- Game Objects ----------
	private Character hero, villain;
	private ScrollHandler scroller;
	private Platforms[] platforms;
	private Background bg1, bg2;

	//---------- Game Objects Data -----------
	private int HERO_WIDTH = GameWorld.CHARACTER_WIDTH;
	private int HERO_HEIGHT = GameWorld.CHARACTER_HEIGHT;
	private int PLATFORM_WIDTH = GameWorld.PLATFORM_WIDTH;
	private int PLATFORM_HEIGHT = GameWorld.PLATFORM_HEIGHT;

	//---------- Graphic Resources ----------
	private TextureRegion bgA, bgB, platform;
	private Animation heroAnimation, villainAnimation;
	private BitmapFont font, font2, shadow, fontSmall, font2small, shadowSmall;

	//---------- Tween Helpers -----------
	private static TweenManager manager;
	private static Value alpha = new Value();

	//---------- Buttons UI ----------
	private List<SimpleButton> buttons;

	public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {

		myWorld = world;

		this.gameHeight = gameHeight;
		this.midpointY = gameHeight / 2;
		this.gameWidth = gameWidth;
		this.midpointX = gameWidth / 2;

		this.buttons = ((InputHandler) Gdx.input.getInputProcessor())
				.getButtons();

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight); // this will be the game width
												// and height units

		batcher = new SpriteBatch(); // Attach batcher to camera
		batcher.setProjectionMatrix(cam.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		// Initialise game objects, resources and helpers
		initGameObjects();
		initAssets();
		setupTweens();
	}

	
	//--------- Initialisation Methods ---------
	
	// Initialise Tween Helper
	private static void setupTweens() {
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, .8f).target(0).ease(TweenEquations.easeOutQuad)
				.start(manager); //.5f
	}

	// Initialise Game Objects
	private void initGameObjects() {
		hero = myWorld.getHero();
		villain = myWorld.getvillain();
		scroller = myWorld.getScroller();
		platforms = scroller.getPlatforms();
		bg1 = scroller.getBg1();
		bg2 = scroller.getBg2();
	}

	// Initialise Game Resources
	private void initAssets() {
		bgA = AssetLoader.bgA;
		bgB = AssetLoader.bgB;
		heroAnimation = AssetLoader.heroAnimation;
		villainAnimation = AssetLoader.villainAnimation;
		platform = AssetLoader.platform;
		font = AssetLoader.font;
		font2 = AssetLoader.font2;
		shadow = AssetLoader.shadow;
		fontSmall = AssetLoader.fontSmall;
		font2small = AssetLoader.font2small;
		shadowSmall = AssetLoader.shadowSmall;
	}

	
	//---------- Drawing Methods ----------
	
	// Paints Background
	private void drawBackground() {
		batcher.draw(bgA, bg1.getX(), bg1.getY(), bg1.getWidth(), bg1.getHeight());
		batcher.draw(bgB, bg2.getX(), bg2.getY(), bg2.getWidth(), bg2.getHeight());
	}

	// Paints all platforms
	private void drawPlatforms() {
		for (Platforms i : platforms) {
			batcher.draw(platform, i.getX(), i.getY(), PLATFORM_WIDTH,
					PLATFORM_HEIGHT);
		}
	}

	// Paints Hero character
	private void drawHero(float runTime) {
		batcher.draw(heroAnimation.getKeyFrame(runTime), hero.getX(),
				hero.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	// Paints both character
	private void drawBothCharacters(float runTime) {
		batcher.draw(heroAnimation.getKeyFrame(runTime), hero.getX(),
				hero.getY(), HERO_WIDTH, HERO_HEIGHT);
		batcher.draw(villainAnimation.getKeyFrame(runTime), villain.getX(),
				villain.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	// Paints Menu Screen UI
	private void drawMenuUI() {
		font2.draw(batcher, "FreeFall", midpointX - 36, 65);
		buttons.get(0).draw(batcher);
		buttons.get(1).draw(batcher);
	}

	// Paints current game score
	private void drawScore() {
		// Convert integer into String
		String score = myWorld.getScore() + "";
		// Draw shadow first
		AssetLoader.shadow.draw(batcher, score, 13, 14);
		// Draw text
		AssetLoader.font.draw(batcher, score, 14, 13);
	}

	// Paints exit button at gameover
	private void drawExitButton() {
		buttons.get(2).draw(batcher);
	}
	
	private void drawLoadingDots(float runTime) {
		float timing = runTime % 2;
		if (timing < 0.6) {
			shadow.draw(batcher, ".", midpointX - (10), 86);
			font.draw(batcher, ".", midpointX - (10 - 1), 85);
		} else if (timing < 1.3) {
			shadow.draw(batcher, " .", midpointX - (10), 86);
			font.draw(batcher, " .", midpointX - (10- 1), 85);
		} else if (timing < 2) {
			shadow.draw(batcher, "  .", midpointX - (10), 86);
			font.draw(batcher, "  .", midpointX - (10 - 1), 85);
		}
	}

	// Screen transition method
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
	
	// Main render method to paint everything
	// GameScreen will call this every cycle
	public void render(float delta, float runTime) {

		// Fill the entire screen with black, to prevent potential flickering.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Begin SpriteBatch
		batcher.begin();
		batcher.disableBlending(); // Disable transparency

		// always on display (no need transparency due to regular shapes)
		drawBackground();
		drawPlatforms();
		
		batcher.enableBlending(); // enable transparency for PNG fonts and sprites
		
		switch (myWorld.getCurrentState()) {
		
		// Menu Screen: shows 2 animations and buttons
		case MENU:
			drawBothCharacters(runTime);
			drawMenuUI();
			String scoreString;
			float timing3 = runTime % 4;
			if (timing3 < 2) {
				scoreString = "HighScore:         " + myWorld.getPrevHighscore();
				font2small.draw(batcher, scoreString, 2, gameHeight - 9);
				font2small.draw(batcher, "(1-Player)", midpointX + 20, gameHeight - 9);
			} else if (timing3 < 4) {
				scoreString = "HighScore:         " + myWorld.getPrevHighscore2p();
				font2small.draw(batcher, scoreString, 2, gameHeight - 9);
				font2small.draw(batcher, "(2-Player)", midpointX + 20, gameHeight - 9);
			}
			break;
		
		// SinglePlayer: gameplay shows 1 character and score
		case RUNNING:
			drawHero(runTime);
			drawScore();
			break;
			
		// SinglePlayer: pre-gameplay state shows instruction
		case READY:
			font2.draw(batcher, "Tap to Move", midpointX - 49, 75);
			float timing2 = runTime % 1;
			if (timing2 > 0.3) {
				font2small.draw(batcher, "-- -- -- Avoid -- -- --", midpointX - 36, 3);
				font2small.draw(batcher, "-- -- -- Avoid -- -- --", midpointX - 36, gameHeight - 9);
			}
			drawHero(runTime);
			break;
			
		// SinglePlayer: gameover shows character, messages, highscore and exit button
		case GAMEOVER:
			drawScore();
			drawHero(runTime);
			font2.draw(batcher, "Game Over", 20, 65);
			font2.draw(batcher, "Highscore", 25, 120);
			font2.draw(batcher, "" + myWorld.getPrevHighscore(), midpointX - 5, 135);
			drawExitButton();
			break;
		
		// MultiPlayer: shows status of connection
		case WAITING:
			if (!myWorld.isConnected()) {
				shadow.draw(batcher, "Connecting", midpointX - (50), 76);
				font.draw(batcher, "Connecting", midpointX - (50 - 1), 75);
			} else {
				shadow.draw(batcher, "Waiting", midpointX - (32), 56);
				font.draw(batcher, "Waiting", midpointX - (32 - 1), 55);
				shadow.draw(batcher, "For Player2", midpointX - (53), 76);
				font.draw(batcher, "For Player2", midpointX - (53 - 1), 75);
			}
			drawLoadingDots(runTime);
			break;
		
		// MultiPlayer: notify the termination of connection
		case EXITING:
			shadow.draw(batcher, "Exiting", midpointX - (30), 76);
			font.draw(batcher, "Exiting", midpointX - (30 - 1), 75);
			drawLoadingDots(runTime);
			break;
		
		// MultiPlayer: notify connection error
		case CONNECTFAIL:
			shadow.draw(batcher, "Connection", midpointX - (49), 56);
			font.draw(batcher, "Connection", midpointX - (49 - 1), 55);
			shadow.draw(batcher, "Error", midpointX - (25), 76);
			font.draw(batcher, "Error", midpointX - (25 - 1), 75);
			
			int textAlignX = midpointX - 42;
			int textAlignY = 106;
			shadowSmall.draw(batcher, "Sorry! An error", textAlignX, textAlignY);
			fontSmall.draw(batcher, "Sorry! An error", textAlignX - 1, textAlignY - 1);
			shadowSmall.draw(batcher, "occured. Please", textAlignX, textAlignY + 20);
			fontSmall.draw(batcher, "occured. Please", textAlignX - 1, textAlignY + 20 - 1);
			shadowSmall.draw(batcher, "try again Later.", textAlignX, textAlignY + 40);
			fontSmall.draw(batcher, "try again Later.", textAlignX - 1, textAlignY + 40 - 1);
			break;
		
		// MultiPlayer: pre-gameplay state shows 2 characters, countdown message and score
		case READY2P:
			String countdown = myWorld.getCD();
			font2.draw(batcher, countdown, midpointX - (countdown.length() * 5) + 1, 75);
			drawBothCharacters(runTime);
			break;
			
		// MultiPlayer: gameplay state shows 2 characters and score
		case RUNNING2P:
			drawBothCharacters(runTime);
			drawScore();
			break;
		
		// MultiPlayer: gameover state shows winner, message and highscore
		case GAMEOVER2P:
			drawScore();
			drawBothCharacters(runTime);
			if (myWorld.isWin()) {
				font2.draw(batcher, "You", midpointX - 15, 45);
				font2.draw(batcher, "WON!", midpointX - 20, 65);
				font2.draw(batcher, "2P Highscore", 10, 120);
				font2.draw(batcher, "" + myWorld.getPrevHighscore2p(), midpointX - 5, 135);
			} else if (myWorld.isLose()) {
				font2.draw(batcher, "You", midpointX - 15, 45);
				font2.draw(batcher, "LOST!", midpointX - 18, 65);
				font2.draw(batcher, "2P Highscore", 10, 120);
				font2.draw(batcher, "" + myWorld.getPrevHighscore2p(), midpointX - 5, 135);
			} else if (myWorld.isDraw()) {
				font2.draw(batcher, "it's  A", midpointX - 20, 45);
				font2.draw(batcher, "DRAW!", midpointX - 24, 65);
				font2.draw(batcher, "2P Highscore", 10, 120);
				font2.draw(batcher, "" + myWorld.getPrevHighscore2p(), midpointX - 5, 135);
			}
			drawExitButton();
			break;
		
		default:
			break;
		}

		// End SpriteBatch
		batcher.end();
		drawTransition(delta);
	}

	//---------- Alpha Modification Method ----------
	public static void setAlpha() {
		alpha.setValue(1);
		setupTweens();
	}
}
