package com.grp4.FFHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Needs to clean up once we implement our own sprites
 * 
 * @author Wei Yang
 *
 */
public class AssetLoader {
	
	public static Texture texture, logoTexture;
    public static TextureRegion logo, bg, platform, fire, playButtonUp, playButtonDown;

    public static Animation heroAnimationR, heroAnimationL, villianAnimationR, villianAnimationL;
    public static TextureRegion heroMidR, heroDownR, heroUpR, heroMidL, heroDownL, heroUpL;
    public static TextureRegion villianMidR, villianDownR, villianUpR, villianMidL, villianDownL, villianUpL;
    
    public static BitmapFont font, shadow;
    
    // public static Sound dead, coin;

    public static void load() {
    	
    	// loading splash screen logo
    	logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, 512, 512);

        // loading all other textures for the game
        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        // "play" button
        playButtonUp = new TextureRegion(texture, 0, 83, 29, 16);
        playButtonDown = new TextureRegion(texture, 29, 83, 29, 16);
        playButtonUp.flip(false, true);
        playButtonDown.flip(false, true);
        
        // background
        bg = new TextureRegion(texture, 0, 0, 136, 43);
        bg.flip(false, true);
        
        // ceiling and floor
        fire = new TextureRegion(texture, 0, 43, 143, 11);
        fire.flip(false, true);
        
        // local player sprite animation frames (facing right)
        heroDownR = new TextureRegion(texture, 136, 0, 17, 12);
        heroDownR.flip(false, true);
        heroMidR = new TextureRegion(texture, 153, 0, 17, 12);
        heroMidR.flip(false, true);
        heroUpR = new TextureRegion(texture, 170, 0, 17, 12);
        heroUpR.flip(false, true);
        // (facing left)
        heroDownL = new TextureRegion(texture, 136, 0, 17, 12);
        heroDownL.flip(true, true);
        heroMidL = new TextureRegion(texture, 153, 0, 17, 12);
        heroMidL.flip(true, true);
        heroUpL = new TextureRegion(texture, 170, 0, 17, 12);
        heroUpL.flip(true, true);
        
        // remote player sprite animation frames
        villianDownR = new TextureRegion(texture, 136, 0, 17, 12);
        villianDownR.flip(false, true);
        villianMidR = new TextureRegion(texture, 153, 0, 17, 12);
        villianMidR.flip(false, true);
        villianUpR = new TextureRegion(texture, 170, 0, 17, 12);
        villianUpR.flip(false, true);
        // (facing left)
        villianDownL = new TextureRegion(texture, 136, 0, 17, 12);
        villianDownL.flip(true, true);
        villianMidL = new TextureRegion(texture, 153, 0, 17, 12);
        villianMidL.flip(true, true);
        villianUpL = new TextureRegion(texture, 170, 0, 17, 12);
        villianUpL.flip(true, true);
        
        // local player sprite animation object
        TextureRegion[] herosR = { heroDownR, heroMidR, heroUpR };
        heroAnimationR = new Animation(0.06f, herosR);
        heroAnimationR.setPlayMode(Animation.LOOP_PINGPONG);
        
        TextureRegion[] herosL = { heroDownL, heroMidL, heroUpL };
        heroAnimationL = new Animation(0.06f, herosL);
        heroAnimationL.setPlayMode(Animation.LOOP_PINGPONG);
        
        // remote player sprite animation object
        TextureRegion[] villiansR = { villianDownR, villianMidR, villianUpR };
        villianAnimationR = new Animation(0.06f, villiansR);
        villianAnimationR.setPlayMode(Animation.LOOP_PINGPONG);
        
        TextureRegion[] villiansL = { villianDownL, villianMidL, villianUpL };
        villianAnimationL = new Animation(0.06f, villiansL);
        villianAnimationL.setPlayMode(Animation.LOOP_PINGPONG);

        // scrolling platforms
        platform = new TextureRegion(texture, 136, 16, 22, 3);
        platform.flip(false, true);
        
        // sounds
        // dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
        // coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        
        // fonts and their shadows
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

    }
    
    public static void dispose() {
        // We must dispose off the texture when we are finished.
    	logoTexture.dispose();
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }

}
