package com.grp4.FFHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	
	public static Texture texture;
    public static TextureRegion bg, wallLeft, wallRight;

    public static Animation heroAnimation;
    public static TextureRegion hero, heroDown, heroUp;
    
    public static TextureRegion platform;

    //public static TextureRegion skullUp, skullDown, bar;

    public static void load() {

        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        bg = new TextureRegion(texture, 0, 0, 136, 43);
        bg.flip(false, true);

        wallLeft = new TextureRegion(texture, 0, 43, 143, 11);

        wallRight = new TextureRegion(wallLeft);
        wallRight.flip(false, true);

        heroDown = new TextureRegion(texture, 136, 0, 17, 12);
        heroDown.flip(false, true);

        hero = new TextureRegion(texture, 153, 0, 17, 12);
        hero.flip(false, true);

        heroUp = new TextureRegion(texture, 170, 0, 17, 12);
        heroUp.flip(false, true);

        TextureRegion[] heros = { heroDown, hero, heroUp };
        heroAnimation = new Animation(0.06f, heros);
        heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);

        //skullUp = new TextureRegion(texture, 192, 0, 24, 14);
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
