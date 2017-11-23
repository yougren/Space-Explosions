package com.ugen.piano.BadGuys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 3/23/2017.
 */

public class BadGuy extends Sprite {
    private Vector2 size;
    Vector2 velocity;
    private Vector2 acceleration;
    Circle hitbox;


    public BadGuy(BadGuy bg, Sprite sprite){
        super(sprite);
        Initialize(bg.getVelocity(), bg.getAcceleration(), bg.getX(), bg.getY());
    }

    public BadGuy(BadGuy bg){
        super(new Texture("badguy.png"));
        Initialize(bg.getVelocity(), bg.getAcceleration(), bg.getX(), bg.getY());
    }

    public BadGuy(Vector2 pos){
        Initialize(new Vector2(0, 0), new Vector2(0,0), pos.x, pos.y);
    }

    private void Initialize(Vector2 v, Vector2 a, float x, float y){
        this.setX(x);
        this.setY(y);
        this.velocity = v;
        this.acceleration = a;
        size = new Vector2(100, 100);
        hitbox = new Circle(x + getWidth()/2, y + getHeight()/2, size.x);
    }

    public void update(Vector2 newTarget){
        double mag = Math.sqrt((newTarget.x - this.getX())*(newTarget.x - this.getX()) + (newTarget.y - this.getY())*(newTarget.y - this.getY()));
        velocity.x = 5 * (float)((newTarget.x - this.getX()) / mag);
        velocity.y = 5 * (float)((newTarget.y - this.getY()) / mag);

        translate(velocity.x, velocity.y);

        hitbox.setPosition(new Vector2(getX(), getY()));
    }

    public void reset(){}

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public Vector2 getAcceleration(){
        return acceleration;
    }

    public Vector2 getSize(){return size;}

    public Circle getHitbox(){
        return hitbox;
    }
}
