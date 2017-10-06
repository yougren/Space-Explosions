package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class RangedBadGuyPool {
    private RangedBadGuy bg;
    public final int max;
    private final Array<PooledRangedBadGuy> freeRangedBadGuys;

    public RangedBadGuyPool(RangedBadGuy bg, int initialCapacity, int max){
        freeRangedBadGuys = new Array<PooledRangedBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeRangedBadGuys.add(new PooledRangedBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public class PooledRangedBadGuy extends RangedBadGuy{
        PooledRangedBadGuy(RangedBadGuy bg){
            super(bg);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            RangedBadGuyPool.this.free(this);
        }
    }

    public PooledRangedBadGuy obtain(){
        PooledRangedBadGuy bg;
        if(freeRangedBadGuys.size == 0){
            bg = new PooledRangedBadGuy(this.bg);
        }
        else
            bg = freeRangedBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(PooledRangedBadGuy bg){
        if(bg == null){
            Gdx.app.log("DEBUG", "RangedBadGuy IS NULL");
        }
        if(freeRangedBadGuys.size < max){
            freeRangedBadGuys.add(bg);
        }
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeRangedBadGuys.size;
    }

    protected void reset(RangedBadGuy bg){
        bg.reset();
    }
}
