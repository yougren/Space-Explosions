package com.ugen.piano;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class GameWorld {
    final PianoThing game;
    WorldRenderer renderer;

    public GameWorld(PianoThing game){
        this.game = game;


    }

    public void update(float delta){

    }

    public void setRenderer(WorldRenderer renderer){
        this.renderer = renderer;
    }
}
