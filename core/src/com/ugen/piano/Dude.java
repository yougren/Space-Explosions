package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class Dude {

    private Sprite bullet;
    private Vector2 position, velocity, acceleration;
    private ArrayList<Particle> bullets;
    boolean shot = false;
    private Color color;
    private int health, damageTimer;
    private Circle hitbox;

    public Dude(Vector2 position){
        damageTimer = 250;
        health = 300;
        bullet = new Sprite(new Texture("particle.png"));
        bullet.setColor(new Color(1, 1, 1, 1));
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
        bullets = new ArrayList<Particle>();
        color = new Color(0.1f, 0.3f, 0.8f, 1.0f);
        hitbox = new Circle(position, 50.0f);
    }

    public void update(){
        if(UgenUtils.getMagnitude(velocity) < 10)
            velocity.add(acceleration);
        else
            velocity.sub(new Vector2(velocity.x/10, velocity.y/10));


        position.add(velocity);
        hitbox.setPosition(position);
    }

    public void draw(ShapeRenderer renderer, SpriteBatch batch, boolean update){
        if(update)
            update();

        if(!isDead()) {
            renderer.setColor(color);
            renderer.circle(position.x, position.y, 50.0f);
        }
    }

    public void shoot(Vector2 target, Particle bullet){
        double mag = Math.sqrt((target.x - position.x)*(target.x - position.x)
                + (target.y - position.y)*(target.y - position.y));

        float velocityX = 15 * (float)((target.x - position.x) / mag);
        float velocityY = 15 * (float)((target.y - position.y) / mag);

        float theta = (float)Math.atan(velocityY/ velocityX);

        //Particle bullet = new Particle(this.bullet, false);

        bullet.setPosition(position.x, position.y);
        bullet.rotate(theta * 180 / (float)Math.PI);
        bullet.setFaction("good");
        bullet.setColor(Color.WHITE);
        bullet.setVelocity(new Vector2(velocityX, velocityY));

        bullet.setAcceleration(new Vector2(0.0f, 0.0f));

        shot = true;
    }

    public boolean isDead(){
        return health <= 0;
    }

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    public Vector2 getVelocity(){return velocity;}

    public void setAcceleration(Vector2 acceleration){this.acceleration = acceleration;}

    public void applyForce(Vector2 force){this.acceleration.add(force);}

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){return health;}

    public boolean hasShot(){
        return shot;
    }

    public Vector2 getPosition(){
        return this.position;
    }

    public ArrayList<Particle> getBullets(){
        return bullets;
    }

    public int getDamageTimer(){
        return damageTimer;
    }

    public boolean intersects(Rectangle rect){
        return rect.contains(position.x, position.y) || hitbox.contains(rect.getX(), rect.getY()) ||
        hitbox.contains(rect.getX() + rect.getWidth(), rect.getY()) || hitbox.contains(rect.getX(),
                rect.getY() + rect.getHeight())
        || hitbox.contains(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
    }

    public boolean intersects(Circle circle){
        return hitbox.overlaps(circle);
    }
}