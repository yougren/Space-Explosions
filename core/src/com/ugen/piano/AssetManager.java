package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by eugen_000 on 4/1/2017.
 */

public class AssetManager {

    public static Texture dudeTexture;
    public static Texture bulletTexture;
    public static Texture particleTexture;
    public static Texture joystickBackground;
    public static Texture joystickForeground;

    public static Sprite dude;
    public static Sprite bullet;
    public static Sprite particle;
    public static Sprite joystickBack;
    public static Sprite joystickFront;


    private AssetManager(){

    }

    public static void load(){
        particleTexture = new Texture(Gdx.files.internal("particle.png"));
        joystickBackground = new Texture("joystickBack.png");
       // joystickBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        joystickForeground = new Texture("joystickFront.png");
        joystickForeground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        particle = new Sprite(particleTexture, 4, 1);
        joystickBack = new Sprite(joystickBackground);
        joystickFront = new Sprite(joystickForeground);
    }

    public static void dispose(){
        particleTexture.dispose();
        joystickBackground.dispose();
        joystickForeground.dispose();
    }

    public static Texture getJoystickBackground() {
        return joystickBackground;
    }

    public static Texture getJoystickForeground() {
        return joystickForeground;
    }

    public static Sprite getNewParticle(){
        return new Sprite(particleTexture, 4, 1);
    }
    public static Sprite getParticle(){
        return particle;
    }

    public static Sprite getJoystickBack(){
        return joystickBack;
    }

    public static Sprite getJoystickFront(){
        return joystickFront;
    }




}
