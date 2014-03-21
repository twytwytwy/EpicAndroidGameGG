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
    public static TextureRegion logo, bg, wall, skullUp, platform, fire, playButtonUp, playButtonDown;

    public static Animation heroAnimation;
    public static TextureRegion heroMid, heroDown, heroUp;
    
    public static BitmapFont font, shadow;
    
    public static Sound dead, coin;

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
        
        // character sprite animation frames
        heroDown = new TextureRegion(texture, 136, 0, 17, 12);
        heroDown.flip(false, true);
        heroMid = new TextureRegion(texture, 153, 0, 17, 12);
        heroMid.flip(false, true);
        heroUp = new TextureRegion(texture, 170, 0, 17, 12);
        heroUp.flip(false, true);
        
        // character sprite animation object
        TextureRegion[] heros = { heroDown, heroMid, heroUp };
        heroAnimation = new Animation(0.06f, heros);
        heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);
        
        // walls by the left and right of the game screen
        wall = new TextureRegion(texture, 136, 16, 22, 3);
        skullUp = new TextureRegion(texture, 192, 0, 24, 14);
        skullUp.flip(false, true);
        // Create by flipping existing skullUp
        //skullDown = new TextureRegion(skullUp);
        //skullDown.flip(false, true);

        // scrolling platforms
        platform = new TextureRegion(texture, 136, 16, 22, 3);
        platform.flip(false, true);
        
        // sounds
        dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        
        // fonts and their shadows
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

    }
    
    public static void dispose() {
        // We must dispose of the texture when we are finished.
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }

}
