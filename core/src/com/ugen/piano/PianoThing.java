package com.ugen.piano;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;


public class PianoThing extends Game {
	GameScreen gameScreen;


	@Override
	public void create() {
		AssetManager.load();

		gameScreen = new GameScreen(this);
		setScreen(gameScreen);


	}

	@Override
	public void render () {
		super.render();
	}



	@Override
	public void dispose () {
		super.dispose();
		AssetManager.dispose();
	}
}
