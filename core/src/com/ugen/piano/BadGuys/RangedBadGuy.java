package com.ugen.piano.BadGuys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ugen.piano.Particle;
import com.ugen.piano.Pools.RangedBadGuyPool;

import java.util.ArrayList;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class RangedBadGuy extends BadGuy {
    private ArrayList<Particle> bullets;
    private Sprite bullet;

    private float fireRate = 0.5f;
    private long lastShot;

    public RangedBadGuy(RangedBadGuy rbg){
        super(rbg, new Sprite(new Texture("rangedbadguy.png")));
        initialize();
    }

    public RangedBadGuy(Vector2 pos){
        super(pos);
        setPosition(getX(), getY());
        initialize();
    }

    public void initialize(){
        lastShot = System.currentTimeMillis();
        bullet = new Sprite(new Texture("particle.png"));
        bullet.setColor(new Color(1, 0, 0, 1));
        bullets = new ArrayList<Particle>();
    }

    public void update(Vector2 newTarget, SpriteBatch batch){

    }

    public void shoot(Vector2 target, Particle bullet){
        double mag = Math.sqrt((target.x - getX())*(target.x - getX())
                + (target.y - getY())*(target.y - getY()));

        float velocityX = 15 * (float)((target.x - getX()) / mag);
        float velocityY = 15 * (float)((target.y - getY()) / mag);

        float theta = (float)Math.atan(velocityY/ velocityX);


        bullet.setPosition(getX(), getY());
        bullet.rotate(theta * 180 / (float)Math.PI);
        bullet.setFaction("bad");
        bullet.setColor(Color.RED);
        Gdx.app.log("DEBUG", ""+bullet.getColor());
        bullet.setVelocity(new Vector2(velocityX, velocityY));

        bullet.setAcceleration(new Vector2(0.0f, 0.0f));

        lastShot = System.currentTimeMillis();
    }

    public ArrayList<Particle> getBullets(){
        return bullets;
    }

    public long getLastShot(){
        return lastShot;
    }

    public float getFireRate(){
        return fireRate;
    }
}
