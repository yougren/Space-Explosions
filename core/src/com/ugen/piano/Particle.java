package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by WilsonCS30 on 3/10/2017.
 */

public class Particle extends Sprite {

    private float lifetime, currentLife, v, theta;

    private Vector2 vel, accel;

    private Random rand;

    private String faction;

    public Particle(Sprite sprite, boolean toInit){
        super(sprite);

        if(toInit)
            initialize();
    }

    public void initialize(){
        rand = new Random();

        this.theta = rand.nextFloat() * 2 * (float)Math.PI;
        v = 30 * (rand.nextFloat() + .1f) + 5;

        vel = new Vector2();
        vel.x = (float)(v * Math.cos(theta));
        vel.y = (float)(v * Math.sin(theta));

        rotate(theta * 180 / (float)Math.PI);

        accel = new Vector2(-vel.x / 200, -vel.y / 200);
        lifetime = currentLife = 1000;
    }

    public void reset(){
        rotate(-theta * 180 / (float)Math.PI);
        this.theta = rand.nextFloat() * 2 * (float)Math.PI;
        v = 30 * (rand.nextFloat() + .1f) + 5;

        vel.x = (float)(v * Math.cos(theta));
        vel.y = (float)(v * Math.sin(theta));

        rotate(theta * 180 / (float)Math.PI);

        accel = new Vector2(-vel.x / 200, -vel.y / 200);
        lifetime = currentLife = 1000;
    }

    public void setVelocity(Vector2 v){
        this.vel = v;
    }

    public void setAcceleration(Vector2 a){
        this.accel = a;
    }

    public void update(){
        vel.add(accel);
        translate(vel.x, vel.y);
    }

    public void update(float delta){
        currentLife -= delta * 1000;

        vel.add(accel);
        translate(vel.x * delta * 60, vel.y * delta * 60);
        setAlpha(currentLife / lifetime);
    }

    public boolean intersects(Rectangle rect){
        return rect.contains(getX(), getY());
    }

    public Boolean isDead(){
        return currentLife < 0.0f;
    }

    public String getFaction(){
        return faction;
    }

    public void setFaction(String faction){
        this.faction = faction;
    }
}
