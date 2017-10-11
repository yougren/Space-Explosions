package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Eugene Munblit on 10/10/2017.
 */

public class Hexagon {
    float[] hexVertices;
    float posX, posY;

    public Hexagon(float x, float y, float l){
        hexVertices = new float[12];

        this.posX = x;
        this.posY = y;

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
}
