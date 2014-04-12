package com.grp4.FFHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

// Contains all graphics and sounds resources
public class AssetLoader {
	
	public static Texture texture, 
		logo1Texture, logo2Texture, logo3Texture,
		heroTexture, spbUpTexture, hybridTexture;
    
	public static TextureRegion logoSUTD, logoISTD, logoG4G,
    	bgA, bgB, platform, fire,
    	singlePlayerButtonUp, singlePlayerButtonDown,
    	multiPlayerButtonUp, multiPlayerButtonDown,
    	exitButtonUp, exitButtonDown,
    	heroMid, heroDown, heroUp,
    	villianMid, villianDown, villianUp;
    
    public static Animation heroAnimation, villianAnimation;
    public static BitmapFont font, font2, shadow;
    public static TextureAtlas atlas;
    public static AtlasRegion bgRegion, spbUpRegion, hybridRegion, heroRegion;
    public static Sound dead, coin, fall, smashed, stapler;
    public static Preferences prefs;

    // Load all the resources
    public static void load() {
    	
    	//--------- SplashScreen Logos ----------
        logo1Texture = new Texture(Gdx.files.internal("data/SUTD Logo2.png"));
        logo1Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logoSUTD = new TextureRegion(logo1Texture, 0, 0, 256, 128);
        
        logo2Texture = new Texture(Gdx.files.internal("data/ISTD Logo2.png"));
        logo2Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logoISTD = new TextureRegion(logo2Texture, 0, 0, 256, 128);
        
        logo3Texture = new Texture(Gdx.files.internal("data/G4G Logo.png"));
        logo3Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logoG4G = new TextureRegion(logo3Texture, 0, 0, 256, 128);

        //--------- All other textures ----------
        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        atlas = new TextureAtlas(Gdx.files.internal("data/texture3.atlas"));
        
        bgRegion = atlas.findRegion("cloud3");
        spbUpRegion = atlas.findRegion("playbuttonblue");
        hybridRegion = atlas.findRegion("playbuttonbluedown");
        heroRegion = atlas.findRegion("twitter-bird-sprite2");
        
        spbUpTexture = spbUpRegion.getTexture();
        spbUpTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        hybridTexture = hybridRegion.getTexture();
        hybridTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        heroTexture = heroRegion.getTexture();
        heroTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        //--------- Buttons texture ----------
        singlePlayerButtonUp = new TextureRegion(spbUpTexture,
        		spbUpRegion.getRegionX(), spbUpRegion.getRegionY(),
        		spbUpRegion.getRegionWidth(), spbUpRegion.getRegionHeight());
        singlePlayerButtonDown = new TextureRegion(hybridTexture, 
        		hybridRegion.getRegionX(), hybridRegion.getRegionY(),
        		hybridRegion.getRegionWidth(), hybridRegion.getRegionHeight());
        singlePlayerButtonUp.flip(false, true);
        singlePlayerButtonDown.flip(false, true);
        
        multiPlayerButtonUp = new TextureRegion(spbUpTexture,
        		spbUpRegion.getRegionX(), spbUpRegion.getRegionY(),
        		spbUpRegion.getRegionWidth(), spbUpRegion.getRegionHeight());
        multiPlayerButtonDown = new TextureRegion(hybridTexture, 
        		hybridRegion.getRegionX(), hybridRegion.getRegionY(),
        		hybridRegion.getRegionWidth(), hybridRegion.getRegionHeight());
        multiPlayerButtonUp.flip(false, true);
        multiPlayerButtonDown.flip(false, true);
        
        exitButtonUp = new TextureRegion(spbUpTexture,
        		spbUpRegion.getRegionX(), spbUpRegion.getRegionY(),
        		spbUpRegion.getRegionWidth(), spbUpRegion.getRegionHeight());
        exitButtonDown = new TextureRegion(hybridTexture, 
        		hybridRegion.getRegionX(), hybridRegion.getRegionY(),
        		hybridRegion.getRegionWidth(), hybridRegion.getRegionHeight());
        exitButtonUp.flip(false, true);
        exitButtonDown.flip(false, true);
        
        //---------- Background and Borders ----------
        bgA = new TextureRegion(hybridTexture,
        		bgRegion.getRegionX(), bgRegion.getRegionY(),
        		bgRegion.getRegionWidth(), bgRegion.getRegionHeight());
        bgA.flip(false, true);
        bgB = new TextureRegion(hybridTexture,
        		bgRegion.getRegionX(), bgRegion.getRegionY(),
        		bgRegion.getRegionWidth(), bgRegion.getRegionHeight());
        bgB.flip(true, false);

        fire = new TextureRegion(texture, 0, 43, 143, 11);
        fire.flip(false, true);
        
        //---------- Local player sprite animation and frames ---------
        int width = heroRegion.getRegionWidth()/3;
        int height = heroRegion.getRegionHeight();
        int xPos = heroRegion.getRegionX();
        int yPos = heroRegion.getRegionY();
        heroDown = new TextureRegion(heroTexture, xPos-2, yPos, width-2, height);
        heroDown.flip(false, true);
        heroMid = new TextureRegion(heroTexture, xPos+2*width-2, yPos, width-2, height);
        heroMid.flip(false, true);
        heroUp = new TextureRegion(heroTexture, xPos+width-2, yPos, width-2, height);
        heroUp.flip(false, true);
        
        TextureRegion[] heros = { heroDown, heroMid, heroUp };
        heroAnimation = new Animation(0.1f, heros);
        heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);

        //---------- Remote player sprite animation and frames ---------
        villianDown = new TextureRegion(texture, 136, 0, 17, 12);
        villianDown.flip(false, true);
        villianMid = new TextureRegion(texture, 153, 0, 17, 12);
        villianMid.flip(false, true);
        villianUp = new TextureRegion(texture, 170, 0, 17, 12);
        villianUp.flip(false, true);

        TextureRegion[] villians = { villianDown, villianMid, villianUp };
        villianAnimation = new Animation(0.1f, villians);
        villianAnimation.setPlayMode(Animation.LOOP_PINGPONG);

        //--------- Scrolling Platforms ----------
        platform = new TextureRegion(texture, 136, 16, 22, 3);
        platform.flip(false, true);
        
        //--------- Audio ---------
        dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));
        smashed = Gdx.audio.newSound(Gdx.files.internal("data/smashed.wav"));
        stapler = Gdx.audio.newSound(Gdx.files.internal("data/stapler.wav"));
        
        //--------- Fonts ---------
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        font2 = new BitmapFont(Gdx.files.internal("data/text2.fnt"));
        font2.setScale(.40f, -.40f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);
        
        //--------- Local Preferences ----------
        prefs = Gdx.app.getPreferences("FreeFall");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        if (!prefs.contains("highScore2p")) {
            prefs.putInteger("highScore2p", 0);
        }
    }
    
    //Receives an integer and maps it to the String highScore in prefs
    public static void setHighScore(int val) {
    	int recordScore = getHighScore();
    	if (val > recordScore) {
    		prefs.putInteger("highScore", val);
    		prefs.flush();
    	}
    }
    
    //Receives an integer and maps it to the String highScore2p in prefs
    public static void set2pHighScore(int val) {
    	int recordScore = get2pHighScore();
    	if (val > recordScore) {
    		prefs.putInteger("highScore2p", val);
    		prefs.flush();
    	}
    }
    
    // Retrieves the highScore
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }
    
    // Retrieves highScore2p
    public static int get2pHighScore() {
        return prefs.getInteger("highScore2p");
    }
    
    // Destroy loaded resources
    public static void dispose() {
    	logo1Texture.dispose();
    	logo2Texture.dispose();
    	logo3Texture.dispose();
        texture.dispose();
        heroTexture.dispose();
        spbUpTexture.dispose();
        hybridTexture.dispose();
        
        dead.dispose();
        coin.dispose();
        fall.dispose();
        stapler.dispose();
        smashed.dispose();
        
        font.dispose();
        font2.dispose();
        shadow.dispose();
    }

}
