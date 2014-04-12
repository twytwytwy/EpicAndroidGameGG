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
import com.grp4.GameObject.Character;
import com.grp4.GameObject.Platforms;
import com.grp4.GameObject.ScrollHandler;
import com.grp4.TweenAccessors.Value;
import com.grp4.TweenAccessors.ValueAccessor;
import com.grp4.ui.SimpleButton;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.FFHelpers.InputHandler;

public class GameRenderer {

	// Super objects
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;

	// Game info
	private int gameHeight, gameWidth, midpointY;

	// Game Objects
	private Character hero;
	private Character villian;
	private ScrollHandler scroller;
	private Platforms[] platforms;
	private Fire flames;
	private Background bg1, bg2;

	// object data
	private int HERO_WIDTH = GameWorld.HERO_WIDTH;
	private int HERO_HEIGHT = GameWorld.HERO_HEIGHT;
	private int FIRE_WIDTH = GameWorld.FIRE_WIDTH;
	private int FIRE_HEIGHT = GameWorld.FIRE_HEIGHT;
	private int PLATFORM_WIDTH = GameWorld.PLATFORM_WIDTH;
	private int PLATFORM_HEIGHT = GameWorld.PLATFORM_HEIGHT;

	// Game Assets
	private TextureRegion bgA, bgB, platform, fire;
	private Animation heroAnimation, villianAnimation;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> buttons;

	public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {

		myWorld = world;

		this.gameHeight = gameHeight;
		this.midpointY = gameHeight / 2;
		this.gameWidth = gameWidth;

		this.buttons = ((InputHandler) Gdx.input.getInputProcessor())
				.getButtons();

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight); // this will be the game width
												// and height units

		batcher = new SpriteBatch(); // Attach batcher to camera
		batcher.setProjectionMatrix(cam.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		// initialise all game objects and stuff
		initGameObjects();
		initAssets();
		setupTweens();
	}

	// ---------- initialisation methods ---------- //
	
	private void setupTweens() {
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
				.start(manager);
	}

	private void initGameObjects() {
		hero = myWorld.getHero();
		villian = myWorld.getVillian();
		flames = myWorld.getFire();
		scroller = myWorld.getScroller();
		platforms = scroller.getPlatforms();
		bg1 = scroller.getBg1();
		bg2 = scroller.getBg2();
	}

	private void initAssets() {
		bgA = AssetLoader.bgA;
		bgB = AssetLoader.bgB;
		heroAnimation = AssetLoader.heroAnimation;
		villianAnimation = AssetLoader.villianAnimation;
		fire = AssetLoader.fire;
		platform = AssetLoader.platform;
	}

	// ---------- drawing methods ---------- //
	
	private void drawBackground() {
		batcher.draw(bgA, bg1.getX(), bg1.getY(), bg1.getWidth(), bg1.getHeight());
		batcher.draw(bgB, bg2.getX(), bg2.getY(), bg2.getWidth(), bg2.getHeight());
	}

	private void drawFire() {

		batcher.draw(fire, flames.getX(), flames.getY1(), FIRE_WIDTH,
				FIRE_HEIGHT);
		batcher.draw(fire, flames.getX(), flames.getY2(), FIRE_WIDTH,
				FIRE_HEIGHT);
	}

	private void drawPlatforms() {
		for (Platforms i : platforms) {
			batcher.draw(platform, i.getX(), i.getY(), PLATFORM_WIDTH,
					PLATFORM_HEIGHT);
		}
	}

	private void drawHero(float runTime) {

		// Draw hero at its coordinates. Retrieve the Animation object from
		// AssetLoader
		// Pass in the runTime variable to get the current frame.
		batcher.draw(heroAnimation.getKeyFrame(runTime), hero.getX(),
				hero.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	private void drawVillian(float runTime) {

		// Draw hero at its coordinates. Retrieve the Animation object from
		// AssetLoader
		// Pass in the runTime variable to get the current frame.
		batcher.draw(villianAnimation.getKeyFrame(runTime), villian.getX(),
				villian.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	private void drawMenuUI() {
		//AssetLoader.shadow.draw(batcher, "FreeFall", (136 / 2) - (35), 76);
		AssetLoader.font2.draw(batcher, "FreeFall", (136 / 2) - 36, 75);
		buttons.get(0).draw(batcher);
		buttons.get(1).draw(batcher);
	}

	private void drawConnecting() {
		if (!myWorld.isConnected()) {
			AssetLoader.shadow
					.draw(batcher, "Connecting", (136 / 2) - (50), 76);
			AssetLoader.font.draw(batcher, "Connecting", (136 / 2) - (50 - 1),
					75);
		} else {
			AssetLoader.shadow.draw(batcher, "Loading", (136 / 2) - (50), 76);
			AssetLoader.font.draw(batcher, "Loading", (136 / 2) - (50 - 1),
					75);
		}
	}

	private void drawExit() {
		AssetLoader.shadow.draw(batcher, "Exit", (136 / 2) - (35), 76);
		AssetLoader.font.draw(batcher, "Exit", (136 / 2) - (35 - 1), 75);
	}

	private void drawScore() {
		// Convert integer into String
		String score = myWorld.getScore() + "";

		// Draw shadow first
		AssetLoader.shadow.draw(batcher, score, 13, 14); // (136 / 2) - (3 *
															// score.length())
		// Draw text
		AssetLoader.font.draw(batcher, score, 14, 13); // (136 / 2) - (3 *
														// score.length() - 1)
	}
	
	private void drawExitButton() {
		buttons.get(2).draw(batcher);
	}
	
	//test method
//	private void drawDot() {
//		shapeRenderer.begin(ShapeType.Filled);
//		shapeRenderer.setColor(0, 255 / 255.0f, 0, 0.1f);
//		shapeRenderer.circle(hero.getBoundingCircle().x,
//				hero.getBoundingCircle().y, hero.getBoundingCircle().radius);
//		shapeRenderer.setColor(255 / 255.0f, 0, 0, 0.1f);
//		shapeRenderer.circle(villian.getBoundingCircle().x,
//				villian.getBoundingCircle().y, villian.getBoundingCircle().radius);
//		shapeRenderer.end();
//	}

	public void render(float delta, float runTime) {

		// Fill the entire screen with black, to prevent potential flickering.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Begin ShapeRenderer
		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1); //dirty blue
		//shapeRenderer.setColor(175 / 255.0f, 238 / 255.0f, 238 / 255.0f, 1);
		shapeRenderer.rect(0, 0, 136, gameHeight);

		// End ShapeRenderer
		shapeRenderer.end();

		// Begin SpriteBatch
		batcher.begin();
		// Disable transparency
		// This is good for performance when drawing images that do not require
		// transparency.
		batcher.disableBlending();

		drawBackground();
		drawPlatforms();
		drawFire();
		
		// The hero needs transparency, so we enable that again.
		batcher.enableBlending();
		
		switch (myWorld.getCurrentState()) {
		case RUNNING:
			drawHero(runTime);
			drawScore();
			break;
		case READY:
			//AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2) - (42), 76);
			AssetLoader.font2
					.draw(batcher, "Touch me", (136 / 2) - (42 - 1), 75);
			drawHero(runTime);
			drawScore();
			break;
		case MENU:
			drawMenuUI();
			break;
		case GAMEOVER:
			drawScore();
			drawHero(runTime);
			//AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
			AssetLoader.font2.draw(batcher, "Game Over", 20, 55);
			drawExitButton();
			break;
			
		case WAITING:
			drawConnecting();
			break;
		case EXITING:
			drawExit();
			break;
		case CONNECTFAIL:
			AssetLoader.shadow.draw(batcher, "Connect", 25, 56);
			AssetLoader.font.draw(batcher, "Connect", 24, 55);
			AssetLoader.shadow.draw(batcher, "Fail", 23, 76);
			AssetLoader.font.draw(batcher, "Fail", 24, 75);
			break;
		case READY2P:
			String countdown = myWorld.getCD();
			//AssetLoader.shadow.draw(batcher, countdown, (136 / 2) - (countdown.length() * 5), 76);
			AssetLoader.font2.draw(batcher, countdown,
					(136 / 2) - (countdown.length() * 5) + 1, 75);
			drawHero(runTime);
			drawVillian(runTime);
			drawScore();
			break;
		case RUNNING2P:
			drawHero(runTime);
			drawVillian(runTime);
			drawScore();
			break;
		case GAMEOVER2P:
			drawScore();
			drawHero(runTime);
			drawVillian(runTime);
			String displayString = "";
			if (myWorld.isWin()) {
				displayString = "WIN";
			} else if (myWorld.isLose()) {
				displayString = "LOSE";
			} else if (myWorld.isDraw()) {
				displayString = "DRAW";
			}
			//AssetLoader.shadow.draw(batcher, displayString, (136 / 2) - (displayString.length()*5), 56);
			AssetLoader.font2.draw(batcher, displayString, (136 / 2) - (displayString.length()*5) + 1, 55);
			drawExitButton();
			break;
		
		default:
			break;
		}

		// End SpriteBatch
		batcher.end();
//		if (myWorld.getCurrentState() == GameState.RUNNING2P) {
//			drawDot();
//		}
		drawTransition(delta);
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
