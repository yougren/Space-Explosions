package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * Created by Eugene Munblit on 11/6/2017.
 */

public class Powerup extends Sprite {
    private String types[];
    private String type;
    private boolean active = true;
    private long initTime;
    private HashMap<String, Color> colorMap;

    public Powerup(Vector2 position, Sprite sprite, int typeNum){
        super(sprite);
        this.setPosition(position.x, position.y);

        types = new String[]{"piercing", "barrier", "nova"};
        this.type = types[typeNum];
        this.initTime = System.currentTimeMillis();

        colorMap = new HashMap<String, Color>();
        colorMap.put("piercing", Color.BLUE);
        colorMap.put("barrier", Color.WHITE);
        colorMap.put("nova", Color.GOLD);
    }

    @Override
    public void draw(Batch batch){
        long currTime = System.currentTimeMillis();

        Color tempColor = colorMap.get(type);

        setColor(new Color(tempColor.r, tempColor.g, tempColor.b, getColor().a));

        if(currTime - initTime < 3000){
            this.setAlpha(((float)(currTime-initTime)/3000.0f));
        }
        else if(currTime - initTime < 6000){
            this.setAlpha(1.0f);
        }
        else if(currTime - initTime < 9000){
            this.setAlpha((1.0f + (float)(6000+initTime-currTime)/3000));
        }
        else if(currTime - initTime > 9000){
            active = false;
        }

        super.draw(batch);
    }

    public HashMap getColorMap(){
        return colorMap;
    }

    public String getType(){
        return type;
    }

    public Circle getHitbox(){
        return new Circle(this.getX(), this.getY(), 50);
    }

    public boolean isActive(){
        return active;
    }
}