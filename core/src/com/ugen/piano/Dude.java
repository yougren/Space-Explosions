package com.ugen.piano;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class Dude {
    Vector2 position, scale;
    Particle bullet;
    boolean shot = false;

    public Dude(Vector2 position, Vector2 scale){
        this.position = position;
        this.scale = scale;
    }

    public void update(ShapeRenderer renderer){
        bullet.run(renderer);

        if(bullet.isDead())
            shot = false;
    }

    public void draw(ShapeRenderer renderer){
        renderer.circle(position.x, position.y ,5.0f);
    }

    public void shoot(float direction){
        bullet = new Particle(position);
        bullet.setVelocity(new Vector2((float)(5 * Math.cos(direction)), (float)(5 * Math.sin(direction))));
        bullet.setAcceleration(new Vector2(0.0f, 0.0f));

        shot = true;
    }

    public boolean hasShot(){
        return shot;
    }

    public Vector2 getPosition(){
        return position;
    }

    public Particle getBullet(){
        return bullet;
    }
}
