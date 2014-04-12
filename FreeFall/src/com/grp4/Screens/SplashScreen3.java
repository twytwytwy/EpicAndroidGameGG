package com.grp4.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grp4.FFHelpers.AssetLoader;
import com.grp4.FreeFall.FFGame;
import com.grp4.TweenAccessors.SpriteAccessor;

public class SplashScreen3 implements Screen{
	
	private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite;
    private FFGame game;

    public SplashScreen3(FFGame game) {
        this.game = game;
    }

    
    @Override
    public void show() {
        sprite = new Sprite(AssetLoader.logoG4G);
        sprite.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = width * .85f;
        float scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        setupTween();
        batcher = new SpriteBatch();
    }

    private void setupTween() {
    	// register SpriteAccessor to do tweening for Sprite.class
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        
        // manager will run the actual tweening based on delta
        manager = new TweenManager();
        
        // TweenCallback objects will have their method called when tweening is done
        TweenCallback cb = new TweenCallback() {
        	
        	// will set the current game screen to GameScreen object
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new GameScreen());
            }
        };

        // line 1	tween the sprite object using the SpriteAccessor's ALPHA tweenType.
        // 			Take .8 seconds. modify alpha value to desired target value of 1.
        // line 2	Use quadratic interpolation and repeat once as a Yoyo (with .4 seconds between the repetition).
        // 			bringing our opacity back down to zero once it hits one.
        // line 3	notify callback object cb once tweening is completed
        // line 4	specify which manager to execute the tweening
        Tween.to(sprite, SpriteAccessor.ALPHA, 1f).target(1) 
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .4f)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
