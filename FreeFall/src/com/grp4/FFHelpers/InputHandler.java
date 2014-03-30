package com.grp4.FFHelpers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.grp4.GameObject.Hero;
import com.grp4.GameWorld.GameWorld;
import com.grp4.ui.SimpleButton;
import com.grp4.FFHelpers.ClientThreadSender;

/**
 * This class handles touch input during game play and will run on a separate thread
 * Scales the input co-ordinates to the game co-ordinates
 * 
 * @author Wei Yang
 *
 */
public class InputHandler implements InputProcessor {

	private GameWorld myWorld;
	private Hero hero;

	private List<SimpleButton> menuButtons;

	private SimpleButton playButton, connectButton;

	private float scaleFactorX;
	private float scaleFactorY;
	
	private ClientThreadSender clientSender;

	public InputHandler(GameWorld myWorld, ClientThreadSender clientSender, float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = myWorld;
		this.hero = myWorld.getHero();
		
		this.clientSender = clientSender;

		int midPointY = myWorld.getMidPointY();
		int midPointX = myWorld.getMidPointX();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		menuButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(midPointX
				- AssetLoader.playButtonUp.getRegionWidth() - 10,
				midPointY + 50, 29, 16, AssetLoader.playButtonUp,
				AssetLoader.playButtonDown);
		connectButton = new SimpleButton(midPointX
				+ 10,
				midPointY + 50, 29, 16, AssetLoader.playButtonUp,
				AssetLoader.playButtonDown);
		menuButtons.add(playButton);
		menuButtons.add(connectButton);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		if (myWorld.isReady()) {
//			clientSender.setTouch();
//			myWorld.start();
//			//System.err.println("input handler touched");
//		} else if (myWorld.isGameOver()) {
//			clientSender.setTouch();
//			myWorld.restart();
//		} else if (myWorld.isRunning()) {
//			clientSender.setTouch();
//			hero.onClick();
//		}
		
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		//System.out.println(screenX + " " + screenY);

		if (myWorld.isMenu()) {
			playButton.isTouchDown(screenX, screenY);
			connectButton.isTouchDown(screenX, screenY);
		} else if (myWorld.isReady()) {
			myWorld.running();
		} else if (myWorld.isRunning()) {
			hero.onClick();
		} else if (myWorld.isGameOver()) {
			myWorld.restart();
			
			
		} else if (myWorld.isConnectFail()) {
			myWorld.menu();
		} else if (myWorld.isWaiting()) {
			connectButton.isTouchDown(screenX, screenY);
		//} else if (myWorld.isReady2p()) {
			//myWorld.running2p();
		} else if (myWorld.isRunning2p()) {
			myWorld.setMessage();
		} else if (myWorld.isGameOver2p()) {
			myWorld.restart2p();
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (myWorld.isMenu()) {
			if (playButton.isTouchUp(screenX, screenY)) {											// = true
				myWorld.ready();
				return true;
			}
			if (connectButton.isTouchUp(screenX, screenY)) {
				myWorld.waiting();
				return true;
			}
		} else if (myWorld.isWaiting()) {
			if (connectButton.isTouchUp(screenX, screenY)) {
				myWorld.disconnected();
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean keyDown(int keycode) { // for desktop debugging purposes

		// Can now use Space Bar to play the game
//		if (keycode == Keys.SPACE) {
//
//			if (myWorld.isMenu()) {
//				myWorld.ready();
//			} else if (myWorld.isReady()) {
//				myWorld.start();
//			}
//
//			hero.onClick();
//
//			if (myWorld.isGameOver()) {
//				myWorld.restart();
//			}
//
//		}

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

	public List<SimpleButton> getMenuButtons() {
		return menuButtons;
	}

}
