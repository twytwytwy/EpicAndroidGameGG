package com.grp4.FFHelpers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;
import com.grp4.GameObject.Character;
import com.grp4.GameWorld.GameWorld;
import com.grp4.ui.SimpleButton;

// handles touch input from the user
// game loop will check for touch inputs before updating the game
public class InputHandler implements InputProcessor {

	private GameWorld myWorld;
	private Character hero;

	private List<SimpleButton> buttons;

	private SimpleButton singlePlayerButton, multiPlayerButton, exitButton;

	private float scaleFactorX;
	private float scaleFactorY;

	public InputHandler(GameWorld myWorld, float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = myWorld;
		this.hero = myWorld.getHero();

		int midPointY = myWorld.getMidPointY();
		int midPointX = myWorld.getMidPointX();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		buttons = new ArrayList<SimpleButton>();
		singlePlayerButton = new SimpleButton(midPointX
				- 23, midPointY + 30, 45, 15,
				AssetLoader.singlePlayerButtonUp,
				AssetLoader.singlePlayerButtonDown);
		multiPlayerButton = new SimpleButton(midPointX
				- 23, midPointY + 50, 45, 15,
				AssetLoader.multiPlayerButtonUp,
				AssetLoader.multiPlayerButtonDown);
		exitButton = new SimpleButton(midPointX
				- 23, midPointY + 70, 45, 15,
				AssetLoader.exitButtonUp,
				AssetLoader.exitButtonDown);
		buttons.add(singlePlayerButton);
		buttons.add(multiPlayerButton);
		buttons.add(exitButton);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		switch (myWorld.getCurrentState()) {
		case MENU:
			singlePlayerButton.isTouchDown(screenX, screenY);
			multiPlayerButton.isTouchDown(screenX, screenY);
			break;
		case READY:
			myWorld.running();
			break;
		case RUNNING:
			hero.onClick();
			break;
		case GAMEOVER:
			exitButton.isTouchDown(screenX, screenY);
			break;
			
		case CONNECTFAIL:
			myWorld.menu();
			break;
		case RUNNING2P:
			myWorld.setMessage();
			break;
		case GAMEOVER2P:
			exitButton.isTouchDown(screenX, screenY);
			break;
		default:
			break;
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		switch (myWorld.getCurrentState()) {
		case MENU:
			if (singlePlayerButton.isTouchUp(screenX, screenY)) {											// = true
				myWorld.ready();
				return true;
			} else if (multiPlayerButton.isTouchUp(screenX, screenY)) {
				myWorld.waiting();
				return true;
			}
			break;
		case GAMEOVER:
			if (exitButton.isTouchUp(screenX, screenY)) {
				myWorld.restart();
				return true;
			}
		case GAMEOVER2P:
			if (exitButton.isTouchUp(screenX, screenY)) {
				myWorld.restart2p();
				return true;
			}
		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
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

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

	public List<SimpleButton> getButtons() {
		return buttons;
	}

}
