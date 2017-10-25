package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eugene Munblit on 10/10/2017.
 */

public class Hexagon {
    private float[] hexVertices;
    private float posX, posY, l;

    public Hexagon(float x, float y, float l){
        hexVertices = new float[12];

        this.posX = x;
        this.posY = y;
        this.l = l;

        hexVertices[0] = x-l/2;
        hexVertices[1] = (float)(y+l*Math.sqrt(3)/2);
        hexVertices[2] = x+l/2;
        hexVertices[3] = (float)(y+l*Math.sqrt(3)/2);
        hexVertices[4] = x+l;
        hexVertices[5] = y;
        hexVertices[6] = x+l/2;
        hexVertices[7] = y - (float)(l*Math.sqrt(3)/2);
        hexVertices[8] = x-l/2;
        hexVertices[9] = y - (float)(l*Math.sqrt(3)/2);
        hexVertices[10] = x-l;
        hexVertices[11] = y;
    }

    public void draw(ShapeRenderer renderer, Color color){
        renderer.setColor(color);
        renderer.polygon(hexVertices);
    }

    public void drawFilled(ShapeRenderer renderer, Color color) {
        renderer.setColor(color);

        for (int i = 0; i < 6; i++) {
            renderer.triangle(posX, posY, hexVertices[2 * i], hexVertices[2 * i + 1],
                    hexVertices[(2 * i + 2)%12], hexVertices[(2 * i + 3)%12]);
        }
    }

    public void setPosition(Vector2 pos){
        float x = pos.x;
        float y = pos.y;
        this.posX = pos.x;
        this.posY = pos.y;

        hexVertices[0] = x-l/2;
        hexVertices[1] = (float)(y+l*Math.sqrt(3)/2);
        hexVertices[2] = x+l/2;
        hexVertices[3] = (float)(y+l*Math.sqrt(3)/2);
        hexVertices[4] = x+l;
        hexVertices[5] = y;
        hexVertices[6] = x+l/2;
        hexVertices[7] = y - (float)(l*Math.sqrt(3)/2);
        hexVertices[8] = x-l/2;
        hexVertices[9] = y - (float)(l*Math.sqrt(3)/2);
        hexVertices[10] = x-l;
        hexVertices[11] = y;
    }

    public float getX(){
        return posX;
    }

    public float getY(){
        return posY;
    }
}
