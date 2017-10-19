package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eugene Munblit on 10/17/2017.
 */

public class HexBadGuyPool {
    private HexagonBadGuy bg;
    public final int max;
    private final Array<HexBadGuyPool.PooledHexBadGuy> freeHexBadGuys;

    public HexBadGuyPool(HexagonBadGuy bg, int initialCapacity, int max){
        freeHexBadGuys = new Array<HexBadGuyPool.PooledHexBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeHexBadGuys.add(new HexBadGuyPool.PooledHexBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public class PooledHexBadGuy extends HexagonBadGuy{
        PooledHexBadGuy(HexagonBadGuy bg){
            super(bg);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            HexBadGuyPool.this.free(this);
        }
    }

    public HexBadGuyPool.PooledHexBadGuy obtain(){
        HexBadGuyPool.PooledHexBadGuy bg;
        if(freeHexBadGuys.size == 0){
            bg = new HexBadGuyPool.PooledHexBadGuy(this.bg);
        }
        else
            bg = freeHexBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(HexBadGuyPool.PooledHexBadGuy bg){
        if(bg == null){
            Gdx.app.log("DEBUG", "HexBadGuy IS NULL");
        }
        if(freeHexBadGuys.size < max){
            freeHexBadGuys.add(bg);
        }
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeHexBadGuys.size;
    }

    protected void reset(HexagonBadGuy bg){
        bg.reset();
    }
}


