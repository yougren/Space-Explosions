package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 3/23/2017.
 */

public class BadGuy {
    private Vector2 position, velocity, acceleration, size;
    private float health;
    private Rectangle hitbox;

    public BadGuy(Vector2 pos){
        this.position = pos;
        size = new Vector2(10, 10);
        hitbox = new Rectangle(position.x, position.y, size.x, size.y);
    }

    public void update(){

    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
        renderer.rect(position.x, position.y, size.x, size.y);
    }

    public Vector2 getPosition(){
        return position;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }
}
