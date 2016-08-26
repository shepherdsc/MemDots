package com.shepapps.memdots;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shepapps.memdots.Screens.GameScreen;

public class MemDots extends Game {

	//create public batch
	public static SpriteBatch batch;

	//create static viewport variables
	public static final int V_WIDTH = 300;
	public static final int V_HEIGHT = 500;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();

	}
}
