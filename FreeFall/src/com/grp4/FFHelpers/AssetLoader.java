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
		heroTexture, villainTexture, bgTexture, 
		player1UpTexture, player1DownTexture, 
		player2UpTexture, player2DownTexture,
		exitUpTexture, exitDownTexture;
    
	public static TextureRegion logoSUTD, logoISTD, logoG4G,
    	bgA, bgB, platform,
    	singlePlayerButtonUp, singlePlayerButtonDown,
    	multiPlayerButtonUp, multiPlayerButtonDown,
    	exitButtonUp, exitButtonDown,
    	heroMid, heroDown, heroUp,
    	villainMid, villainDown, villainUp;
    
    public static Animation heroAnimation, villainAnimation;
    public static BitmapFont fontSmall, shadowSmall, font, shadow, font2small, font2;
    public static TextureAtlas atlas;
    public static AtlasRegion bgRegion, player1UpRegion, player1DownRegion, 
    	player2UpRegion, player2DownRegion, exitUpRegion, exitDownRegion,
    	heroRegion, villainRegion;
    public static Sound dead, fall, smashed, stapler;
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
        player1UpRegion = atlas.findRegion("1playerup");
        player1DownRegion = atlas.findRegion("1playerdown");
        player2UpRegion = atlas.findRegion("2playerup");
        player2DownRegion = atlas.findRegion("2playerdown");
        exitUpRegion = atlas.findRegion("exitup");
        exitDownRegion = atlas.findRegion("exitdown");
        heroRegion = atlas.findRegion("birdself");
        villainRegion = atlas.findRegion("birdother");
        
        bgTexture = bgRegion.getTexture();
        bgTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        player1UpTexture = player1UpRegion.getTexture();
        player1UpTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        player1DownTexture = player1DownRegion.getTexture();
        player1DownTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        player2UpTexture = player2UpRegion.getTexture();
        player2UpTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        player2DownTexture = player2DownRegion.getTexture();
        player2DownTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        exitUpTexture = exitUpRegion.getTexture();
        exitUpTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        exitDownTexture = exitDownRegion.getTexture();
        exitDownTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        heroTexture = heroRegion.getTexture();
        heroTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        villainTexture = villainRegion.getTexture();
        villainTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        //--------- Buttons texture ----------
        singlePlayerButtonUp = new TextureRegion(player1UpTexture,
        		player1UpRegion.getRegionX(), player1UpRegion.getRegionY(),
        		player1UpRegion.getRegionWidth(), player1UpRegion.getRegionHeight());
        singlePlayerButtonDown = new TextureRegion(player1DownTexture, 
        		player1DownRegion.getRegionX(), player1DownRegion.getRegionY(),
        		player1DownRegion.getRegionWidth(), player1DownRegion.getRegionHeight());
        singlePlayerButtonUp.flip(false, true);
        singlePlayerButtonDown.flip(false, true);
        
        multiPlayerButtonUp = new TextureRegion(player2UpTexture,
        		player2UpRegion.getRegionX(), player2UpRegion.getRegionY(),
        		player2UpRegion.getRegionWidth(), player2UpRegion.getRegionHeight());
        multiPlayerButtonDown = new TextureRegion(player2DownTexture, 
        		player2DownRegion.getRegionX(), player2DownRegion.getRegionY(),
        		player2DownRegion.getRegionWidth(), player2DownRegion.getRegionHeight());
        multiPlayerButtonUp.flip(false, true);
        multiPlayerButtonDown.flip(false, true);
        
        exitButtonUp = new TextureRegion(exitUpTexture,
        		exitUpRegion.getRegionX(), exitUpRegion.getRegionY(),
        		exitUpRegion.getRegionWidth(), exitUpRegion.getRegionHeight());
        exitButtonDown = new TextureRegion(exitDownTexture, 
        		exitDownRegion.getRegionX(), exitDownRegion.getRegionY(),
        		exitDownRegion.getRegionWidth(), exitDownRegion.getRegionHeight());
        exitButtonUp.flip(false, true);
        exitButtonDown.flip(false, true);
        
        //---------- Background and Borders ----------
        bgA = new TextureRegion(bgTexture,
        		bgRegion.getRegionX(), bgRegion.getRegionY(),
        		bgRegion.getRegionWidth(), bgRegion.getRegionHeight());
        bgA.flip(false, true);
        bgB = new TextureRegion(bgTexture,
        		bgRegion.getRegionX(), bgRegion.getRegionY(),
        		bgRegion.getRegionWidth(), bgRegion.getRegionHeight());
        bgB.flip(true, false);

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
        int vil_width = villainRegion.getRegionWidth()/3;
        int vil_height = villainRegion.getRegionHeight();
        int vil_xPos = villainRegion.getRegionX();
        int vil_yPos = villainRegion.getRegionY();
        villainDown = new TextureRegion(villainTexture, vil_xPos-2, vil_yPos, vil_width-2, vil_height);
        villainDown.flip(false, true);
        villainMid = new TextureRegion(villainTexture, vil_xPos+2*width-2, vil_yPos, vil_width-2, vil_height);
        villainMid.flip(false, true);
        villainUp = new TextureRegion(villainTexture, vil_xPos+width-2, vil_yPos, vil_width-2, vil_height);
        villainUp.flip(false, true);
        
        TextureRegion[] villains = { villainDown, villainMid, villainUp };
        villainAnimation = new Animation(0.1f, villains);
        villainAnimation.setPlayMode(Animation.LOOP_PINGPONG);

        //--------- Scrolling Platforms ----------
        platform = new TextureRegion(texture, 136, 16, 22, 3);
        platform.flip(false, true);
        
        //--------- Audio ---------
        dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
        fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));
        smashed = Gdx.audio.newSound(Gdx.files.internal("data/smashed.wav"));
        stapler = Gdx.audio.newSound(Gdx.files.internal("data/stapler.wav"));
        
        //--------- Fonts ---------
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);
        
        fontSmall = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        fontSmall.setScale(.15f, -.15f);
        shadowSmall = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadowSmall.setScale(.15f, -.15f);
        
        font2 = new BitmapFont(Gdx.files.internal("data/text2.fnt"));
        font2.setScale(.4f, -.4f);
        font2small = new BitmapFont(Gdx.files.internal("data/text2.fnt"));
        font2small.setScale(.2f, -.2f);
        
        
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
        villainTexture.dispose();
        bgTexture.dispose();
		player1UpTexture.dispose();
		player1DownTexture.dispose();
		player2UpTexture.dispose();
		player2DownTexture.dispose();
		exitUpTexture.dispose();
		exitDownTexture.dispose();
        
        dead.dispose();
        fall.dispose();
        stapler.dispose();
        smashed.dispose();
        
        font.dispose();
        font2.dispose();
        shadow.dispose();
        fontSmall.dispose();
        font2small.dispose();
        shadowSmall.dispose();
    }

}
