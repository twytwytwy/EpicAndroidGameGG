package com.grp4.GameWorld;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

	// object data
	private int HERO_WIDTH = 17;
	private int HERO_HEIGHT = 12;
	private int FIRE_WIDTH = 143;
	private int FIRE_HEIGHT = 11;
	public int PLATFORM_WIDTH = 30;
	public int PLATFORM_HEIGHT = 4;

	// Game Assets
	private TextureRegion bg, platform, fire;
	private Animation heroAnimationR, heroAnimationL, villianAnimationR, villianAnimationL;
	private TextureRegion heroMidR, heroMidL, villianMidR, villianMidL;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;

	public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {

		myWorld = world;

		this.gameHeight = gameHeight;
		this.midpointY = gameHeight / 2;
		this.gameWidth = gameWidth;

		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getMenuButtons();

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
	}

	private void initAssets() {
		bg = AssetLoader.bg;
		heroAnimationL = AssetLoader.heroAnimationL;
		heroAnimationR = AssetLoader.heroAnimationR;
		villianAnimationL = AssetLoader.villianAnimationL;
		villianAnimationR = AssetLoader.villianAnimationR;
		heroMidL = AssetLoader.heroMidL;
		heroMidR = AssetLoader.heroMidR;
		villianMidL = AssetLoader.villianMidL;
		villianMidR = AssetLoader.villianDownR;
		fire = AssetLoader.fire;
		platform = AssetLoader.platform;
	}

	// ---------- drawing methods ---------- //
	
	private void drawBackground() {
		batcher.draw(bg, 0, midpointY + 23, 136, 100);
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
		batcher.draw(heroAnimationR.getKeyFrame(runTime), hero.getX(),
				hero.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	private void drawVillian(float runTime) {

		// Draw hero at its coordinates. Retrieve the Animation object from
		// AssetLoader
		// Pass in the runTime variable to get the current frame.
		batcher.draw(villianAnimationR.getKeyFrame(runTime), villian.getX(),
				villian.getY(), HERO_WIDTH, HERO_HEIGHT);
	}

	//test method
	private void drawDot() {
		if (myWorld.isReady2p() || myWorld.isRunning2p()
				|| myWorld.isRunning2p() || myWorld.isGameOver2p()) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(villian.getX() + 9, villian.getY() + 6,
					6.5f);
			shapeRenderer.end();
		}
	}

	private void drawMenuUI() {
		AssetLoader.shadow.draw(batcher, "FreeFall", (136 / 2) - (35), 76);
		AssetLoader.font.draw(batcher, "FreeFall", (136 / 2) - (35 - 1), 75);

		for (SimpleButton button : menuButtons) {
			button.draw(batcher);
		}
	}

	private void drawConnectingUI() {
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

		// drawBackground();
		drawPlatforms();
		drawFire();
		
		// The hero needs transparency, so we enable that again.
		batcher.enableBlending();

		if (myWorld.isRunning()) {
			drawHero(runTime);
			drawScore();
		} else if (myWorld.isReady()) {
			AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2) - (42), 76);
			AssetLoader.font
					.draw(batcher, "Touch me", (136 / 2) - (42 - 1), 75);
			drawHero(runTime);
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

		} else if (myWorld.isWaiting()) {
			drawConnectingUI();
		} else if (myWorld.isExiting()) {
			drawExit();
		} else if (myWorld.isConnectFail()) {
			AssetLoader.shadow.draw(batcher, "Connect", 25, 56);
			AssetLoader.font.draw(batcher, "Connect", 24, 55);
			AssetLoader.shadow.draw(batcher, "Fail", 23, 76);
			AssetLoader.font.draw(batcher, "Fail", 24, 75);
		} else if (myWorld.isReady2p()) {
			String countdown = myWorld.getCD();
			AssetLoader.shadow.draw(batcher, countdown,
					(136 / 2) - (countdown.length() * 5), 76);
			AssetLoader.font.draw(batcher, countdown,
					(136 / 2) - (countdown.length() * 5), 75);
			drawHero(runTime);
			drawVillian(runTime);
			drawScore();

		} else if (myWorld.isRunning2p()) {
			drawHero(runTime);
			drawVillian(runTime);
			drawScore();
		} else if (myWorld.isGameOver2p()) {
			AssetLoader.shadow.draw(batcher, "Game Over2", 25, 56);
			AssetLoader.font.draw(batcher, "Game Over2", 24, 55);
			AssetLoader.shadow.draw(batcher, "Try again?2", 23, 76);
			AssetLoader.font.draw(batcher, "Try again?2", 24, 75);
			drawScore();
			drawHero(runTime);
			drawVillian(runTime);
		}

		// End SpriteBatch
		batcher.end();
		drawDot();
		drawTransition(delta);

		// --------------------------- collision render for debug
		// --------------------------------------//

		// shapeRenderer.begin(ShapeType.Filled);
		// shapeRenderer.setColor(Color.RED);
		// shapeRenderer.circle(villian.getX(), villian.getY(),
		// hero.getBoundingCircle().radius);
		//
		// shapeRenderer.rect(pf1.getBoundingBox().x, pf1.getBoundingBox().y,
		// pf1.getBoundingBox().width, pf1.getBoundingBox().height);
		// shapeRenderer.rect(pf2.getBoundingBox().x, pf2.getBoundingBox().y,
		// pf2.getBoundingBox().width, pf2.getBoundingBox().height);
		// shapeRenderer.rect(pf3.getBoundingBox().x, pf3.getBoundingBox().y,
		// pf3.getBoundingBox().width, pf3.getBoundingBox().height);
		// shapeRenderer.rect(pf4.getBoundingBox().x, pf4.getBoundingBox().y,
		// pf4.getBoundingBox().width, pf4.getBoundingBox().height);
		// shapeRenderer.rect(pf5.getBoundingBox().x, pf5.getBoundingBox().y,
		// pf5.getBoundingBox().width, pf5.getBoundingBox().height);
		// shapeRenderer.rect(pf6.getBoundingBox().x, pf6.getBoundingBox().y,
		// pf6.getBoundingBox().width, pf6.getBoundingBox().height);
		//
		// shapeRenderer.end();

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
