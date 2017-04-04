package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class Dude {

    private Sprite bullet;
    private Vector2 position, velocity, scale;
    private ArrayList<Particle> bullets;
    boolean shot = false;
    private Color color;
    private int health, damageTimer;

    public Dude(Vector2 position, Vector2 scale){
        damageTimer = 250;
        health = 300;
        bullet = new Sprite(new Texture("particle.png"));
        bullet.setColor(new Color(1, 1, 1, 1));
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.scale = scale;
        bullets = new ArrayList<Particle>();
        color = new Color(0.1f, 0.3f, 0.8f, 1.0f);
    }

    public void update(){
        position.add(velocity);
    }

    public void draw(ShapeRenderer renderer, SpriteBatch batch){
        update();

        renderer.setColor(color);
        renderer.circle(position.x, position.y ,50.0f);


        for(Particle p : bullets){
            p.update();
            p.draw(batch);
        }
    }

    public void shoot(Vector2 target){
        double mag = Math.sqrt((target.x - position.x)*(target.x - position.x)
                + (target.y - position.y)*(target.y - position.y));

        float velocityX = 10 * (float)((target.x - position.x) / mag);
        float velocityY = 10 * (float)((target.y - position.y) / mag);

        float theta = (float)Math.atan((target.y - position.y) / (target.x - position.x));

        Particle bullet = new Particle(this.bullet);
       // bullet.set(this.bullet);
        bullet.setPosition(position.x, position.y);
        bullet.rotate(theta * 180 / (float)Math.PI + 90);


        bullet.setVelocity(new Vector2(velocityX, velocityY));


        bullet.setAcceleration(new Vector2(0.0f, 0.0f));

        bullets.add(bullet);

        shot = true;
    }

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){
        return health;
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

    public int getDamageTimer(){
        return damageTimer;
    }

    public boolean intersects(Rectangle rect){
        return rect.getPosition(new Vector2(0, 0)).add(-position.x, -position.y).len() < 50.0f;
    }
}
