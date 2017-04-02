package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class InputHandler implements InputProcessor {
    GameScreen screen;

    public InputHandler(GameScreen screen){
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float trueX = screen.getCam().viewportWidth * screenX / Gdx.graphics.getWidth();
        float trueY = screen.getCam().viewportHeight - screen.getCam().viewportHeight * screenY / Gdx.graphics.getHeight();
        Dude dude = screen.getRenderer().getDude();
        //screen.getRenderer().explosion(new Vector2(trueX, trueY), screen.getRenderer().getParticles(), 2000);

        dude.shoot(new Vector2(screenX, screenY));

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
