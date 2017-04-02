package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class GameScreen extends ScreenAdapter{

    InputHandler handler;

    private PianoThing game;

    private GameWorld world;
    private WorldRenderer renderer;

    public GameScreen(PianoThing game){
        this.game = game;
    }

    public void update(float delta){
        world.update(delta);
    }

    public void draw(float delta){
        renderer.render(delta);
    }

    @Override
    public void render(float delta){
        update(delta);
        draw(delta);
    }

    @Override
    public void show(){
        world = new GameWorld(game);
        renderer = new WorldRenderer(world);
        world.setRenderer(renderer);
        handler = new InputHandler(this);

        //Gdx.input.setInputProcessor(handler);
    }

    @Override
    public void resume(){
        Gdx.input.setInputProcessor(handler);
    }

    public WorldRenderer getRenderer(){
        return renderer;
    }

    public GameWorld getWorld(){
        return world;
    }

    public OrthographicCamera getCam(){
        return renderer.getCam();
    }


}
