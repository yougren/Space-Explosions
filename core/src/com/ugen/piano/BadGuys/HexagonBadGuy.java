package com.ugen.piano.BadGuys;

import com.badlogic.gdx.graphics.Color;
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
    private Hexagon hex;
    private Circle hitbox;
    private float radius;

    public HexagonBadGuy(BadGuy bg){
        super(bg);
        init();
    }

    public HexagonBadGuy(Vector2 pos){
        super(pos);
        init();
    }

    public void init(){
        radius = 50*(float)Math.sqrt(3);

        hex = new Hexagon(position.x, position.y, radius);
        hitbox = new Circle(position.x, position.y, radius);
    }

    @Override
    public void update(Vector2 targetPos){

    }

    @Override
    public void draw(ShapeRenderer renderer){
        hex.drawFilled(renderer, Color.GOLD);
    }

    public Array<SpinningBadGuy> explode(SpinningBadGuyPool pool){
        Array<SpinningBadGuy> s = new Array<SpinningBadGuy>();

        for(int i = 0; i < 6; i++){
            s.add(pool.obtain());
            s.get(i).setPosition(new Vector2((float)(radius/Math.sqrt(3)*Math.cos(i*Math.PI/3) + this.position.x),
                    (float)(radius/Math.sqrt(3)*Math.sin(i*Math.PI/3)) + this.position.y));
            s.get(i).rotate((float)((i+1) * Math.PI/3));
        }

        return s;
    }

    @Override
    public Circle getHitbox(){
        return hitbox;
    }

    @Override
    public void setPosition(Vector2 newPos){
        this.position = newPos;
        this.hitbox.setPosition(newPos);
        this.hex.setPosition(this.position);
    }
}