package com.grp4.FFHelpers;

import com.badlogic.gdx.InputProcessor;
import com.grp4.GameObject.Hero;
import com.grp4.GameWorld.GameWorld;

public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
	private Hero hero;
	
	public InputHandler(GameWorld myWorld) {
		this.myWorld = myWorld;
		this.hero = myWorld.getHero();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (myWorld.isReady()) {
            myWorld.start();
        }

        hero.onClick();

        if (myWorld.isGameOver()) {
            myWorld.restart();
        }

        return true;
	}


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
