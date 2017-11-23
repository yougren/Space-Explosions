package com.ugen.piano.BadGuys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.Hexagon;
import com.ugen.piano.Pools.HexBadGuyPool;
import com.ugen.piano.Pools.SpinningBadGuyPool;

/**
 * Created by Eugene Munblit on 10/17/2017.
 */

public class HexagonBadGuy extends BadGuy {
    public HexagonBadGuy(BadGuy bg){
        super(bg, new Sprite(new Texture("hexagonbadguy.png")));
        init();
    }

    public HexagonBadGuy(Vector2 pos){
        super(pos);
        init();
    }

    public void init(){
        hitbox = new Circle(getX() + getWidth()/2, getY() + getHeight()/2, getWidth()/2);
    }

    @Override
    public void update(Vector2 targetPos){

    }

    public Array<SpinningBadGuy> explode(SpinningBadGuyPool pool){
        Array<SpinningBadGuy> s = new Array<SpinningBadGuy>();
        float radius = getWidth()/2;

        for(int i = 0; i < 6; i++){
            s.add(pool.obtain());
            s.get(i).setPosition((float)(radius/Math.sqrt(3)*Math.cos(i*Math.PI/3) + getX()),
                    (float)(radius/Math.sqrt(3)*Math.sin(i*Math.PI/3)) + getY());
            s.get(i).rotate((float)((i+1) * Math.PI/3 * 180 / Math.PI));
        }

        return s;
    }
}