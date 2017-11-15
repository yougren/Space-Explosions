package com.ugen.piano;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eugene Munblit on 11/8/2017.
 */

public class Triangle {
    private Vector2 position;
    private float[] vertices;
    private float radius;
    private Circle hitbox;
    private boolean active = true;

    public Triangle(Vector2 position, float radius){
        this.position = position;
        this.radius = radius;
        this.vertices = new float[6];
        this.hitbox = new Circle(position, radius);

        for(int i = 0; i < 6; i+=2){
            vertices[i] = (float)(position.x + radius*Math.cos(i*Math.PI/3));
            vertices[i+1] = (float)(position.y + radius*Math.sin(i*Math.PI/3));
        }
    }

    public void draw(ShapeRenderer renderer){
        renderer.triangle(vertices[0], vertices[1], vertices[2],
                vertices[3], vertices[4], vertices[5]);
    }

    public void setPosition(Vector2 pos){
        this.position = pos;
        this.hitbox.setPosition(position);

        for(int i = 0; i < 6; i+=2){
            vertices[i] = (float)(position.x + radius*Math.cos(i*Math.PI/3));
            vertices[i+1] = (float)(position.y + radius*Math.sin(i*Math.PI/3));
        }
    }

    public boolean intersects(Circle circle){
        return circle.overlaps(hitbox);
    }

    public boolean intersects(Rectangle rect){
        return rect.contains(hitbox);
    }

    public void rotate(float theta){
        for(int i = 0; i < 6; i+=2){
            vertices[i] = (float)(position.x + radius*Math.cos(i*Math.PI/3 + theta));
            vertices[i+1] = (float)(position.y + radius*Math.sin(i*Math.PI/3 + theta));
        }
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }
}
