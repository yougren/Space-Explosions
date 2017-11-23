package com.ugen.piano.BadGuys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ugen.piano.Pools.SpinningBadGuyPool;

/**
 * Created by Eugene Munblit on 10/16/2017.
 */

public class SpinningBadGuy extends BadGuy {
    private double theta, omega, alpha;
    private float velMag, length;
    private Circle hitBox;

    public SpinningBadGuy(BadGuy bg){
        super(bg, new Sprite(new Texture("spinningbadguy.png")));
        init();
    }

    public SpinningBadGuy(Vector2 pos){
        super(pos);
        init();
    }

    public void init(){
        theta = 0;
        omega = Math.PI/360;
        alpha = Math.PI/2160;
        velMag = 5;
        length = 100;
        hitBox = new Circle(getX() + getWidth()/2, getY() + getHeight()/2, getWidth()/2);
    }

    @Override
    public void update(Vector2 newTarget){
        double mag = Math.sqrt((newTarget.x - getX())*(newTarget.x - getX()) + (newTarget.y - getY())*(newTarget.y - getY()));
        velocity.x = velMag * (float)((newTarget.x - getX()) / mag);
        velocity.y = velMag * (float)((newTarget.y - getY()) / mag);

        velMag += .05;
        omega += alpha;
        theta += omega;

        rotate((float) (omega * 180 / Math.PI));
        translate(velocity.x, velocity.y);
        hitBox.setPosition(new Vector2(getX(), getY()));
    }

    @Override
    public void reset(){
        omega = Math.PI/360;
        theta = 0;
        velMag = 5;
        setRotation(0);
    }

    @Override
    public Circle getHitbox(){
        return hitBox;
    }
}