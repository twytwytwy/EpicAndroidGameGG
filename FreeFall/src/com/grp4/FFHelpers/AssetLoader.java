package com.grp4.FFHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	
	public static Texture texture;
    public static TextureRegion bg, wall, skullUp, platform, fire;

    public static Animation heroAnimation;
    public static TextureRegion heroMid, heroDown, heroUp;


    public static void load() {

        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        bg = new TextureRegion(texture, 0, 0, 136, 43);
        bg.flip(false, true);

        fire = new TextureRegion(texture, 0, 43, 143, 11);
        fire.flip(false, true);
        

        heroDown = new TextureRegion(texture, 136, 0, 17, 12);
        heroDown.flip(false, true);

        heroMid = new TextureRegion(texture, 153, 0, 17, 12);
        heroMid.flip(false, true);

        heroUp = new TextureRegion(texture, 170, 0, 17, 12);
        heroUp.flip(false, true);

        TextureRegion[] heros = { heroDown, heroMid, heroUp };
        heroAnimation = new Animation(0.06f, heros);
        heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);
        
        wall = new TextureRegion(texture, 136, 16, 22, 3);

        skullUp = new TextureRegion(texture, 192, 0, 24, 14);
        skullUp.flip(false, true);
        // Create by flipping existing skullUp
        //skullDown = new TextureRegion(skullUp);
        //skullDown.flip(false, true);

        platform = new TextureRegion(texture, 136, 16, 22, 3);
        platform.flip(false, true);

    }
    
    public static void dispose() {
        // We must dispose of the texture when we are finished.
        texture.dispose();
    }

}
