package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class Dude {

    private Vector2 position, scale;
    private ArrayList<Particle> bullets;
    boolean shot = false;
    private Color color;

    public Dude(Vector2 position, Vector2 scale){
        this.position = position;
        this.scale = scale;
        bullets = new ArrayList<Particle>();
        color = new Color(0.1f, 0.3f, 0.8f, 1.0f);
    }

    public void update(ShapeRenderer renderer){
        for(Particle p : bullets)
            p.run(renderer, color);

    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(color);
        renderer.circle(position.x, position.y ,5.0f);
    }

    public void shoot(Vector2 target){
        float theta = (float)Math.atan((target.y - position.y) / (target.x - position.x));


        Particle bullet = new Particle(new Vector2(position.x, position.y));

        if(target.x - position.x < 0)
            bullet.setVelocity(new Vector2(-5 * (float)Math.cos(theta), -5 * (float)Math.sin(theta)));
        else
            bullet.setVelocity(new Vector2(5 * (float)Math.cos(theta), 5 * (float)Math.sin(theta)));


        bullet.setAcceleration(new Vector2(0.0f, 0.0f));

        bullets.add(bullet);

        shot = true;
    }

    public boolean hasShot(){
        return shot;
    }

    public Vector2 getPosition(){
        return position;
    }

    public ArrayList<Particle> getBullets(){

        return bullets;
    }
}
