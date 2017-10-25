package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 3/23/2017.
 */

public class BadGuy {
    Vector2 size;
    Vector2 position;
    Vector2 velocity;
    private Vector2 acceleration;
    Rectangle hitbox;


    public BadGuy(BadGuy bg){
        Initialize(bg.getVelocity(), bg.getAcceleration(), bg.getPosition());
    }

    private void Initialize(Vector2 v, Vector2 a, Vector2 pos){
        this.velocity = v;
        this.acceleration = a;
        this.position = pos;
        size = new Vector2(100, 100);
        hitbox = new Rectangle(position.x, position.y, size.x, size.y);
    }

    public BadGuy(Vector2 pos){
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
        this.position = pos;
        size = new Vector2(100, 100);
        hitbox = new Rectangle(position.x, position.y, size.x, size.y);
    }

    public void update(Vector2 newTarget){
        double mag = Math.sqrt((newTarget.x - position.x)*(newTarget.x - position.x) + (newTarget.y - position.y)*(newTarget.y - position.y));
        velocity.x = 5 * (float)((newTarget.x - position.x) / mag);
        velocity.y = 5 * (float)((newTarget.y - position.y) / mag);

        //velocity.add(acceleration);
        hitbox.setPosition(position);
        position.add(velocity);
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(new Color(1.0f, 0.0f, .3f, 1.0f));
        renderer.rect(position.x, position.y, size.x, size.y);
    }

    public void reset(){}

    public void setPosition(Vector2 position){
        this.position = position;
        this.hitbox.setPosition(position);
    }
    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public Vector2 getAcceleration(){
        return acceleration;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }
}
