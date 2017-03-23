package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by WilsonCS30 on 3/10/2017.
 */

public class Particle {

    private float decayRate = 2.0f;
    private float lifetime, v, theta;
    private int radius;
    private Vector2 pos, vel, accel;
    private Color defaultColor;
    Random rand;

    public Particle(){
        defaultColor = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        pos = new Vector2(0.0f, 0.0f);
        vel = new Vector2(0.0f ,0.0f);
        accel = new Vector2(0.0f, 0.0f);
    }

    public Particle(Vector2 position){
        rand = new Random();

        theta = rand.nextFloat() * 2 * (float)Math.PI;
        v = 5 * (rand.nextFloat() + .1f);

        this.vel = new Vector2();
        vel.x = (float)(v * Math.cos(theta));
        vel.y = (float)(v * Math.sin(theta));

        this.accel = new Vector2(-vel.x / 500, -vel.y / 500);
        this.pos = position;
        lifetime = 255;
        radius = 2;
        defaultColor = new Color(0, 0, 1.0f, 1.0f);
    }

    public void setVelocity(Vector2 v){
        this.vel = v;
    }

    public void setAcceleration(Vector2 a){
        this.accel = a;
    }

    public void run(ShapeRenderer renderer, Color color){
        update();
        draw(renderer, color);
    }

    public void update(){
        lifetime -= decayRate;
        vel.add(accel);
        pos.add(vel);

        defaultColor.a -= decayRate/255.0f;

       // Gdx.app.log("DEBUG", color.a + "");
    }

    public void draw(ShapeRenderer renderer, Color color){
        //renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(color);
        //renderer.circle(pos.x, pos.y, radius);
        renderer.rect(pos.x, pos.y, 0.25f, 2.0f, 0.5f, 4.0f, 1.0f, 1.0f,  (float)(Math.atan(vel.y / vel.x) * 180 / Math.PI) + 90.0f);
        //renderer.end();
    }

    public Vector2 getPosition(){
        return pos;
    }

    public boolean intersects(Rectangle rect){
        return rect.contains(pos);
    }

    public Boolean isDead(){
        return lifetime < 0.0f;
    }

}
