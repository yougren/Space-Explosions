package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class BadGuyPool {
    private BadGuy bg;
    public final int max;
    private final Array<PooledBadGuy> freeBadGuys;

    public BadGuyPool(BadGuy bg, int initialCapacity, int max){
        freeBadGuys = new Array<PooledBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeBadGuys.add(new PooledBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public class PooledBadGuy extends BadGuy{
        PooledBadGuy(BadGuy bg){
            super(bg);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            BadGuyPool.this.free(this);
        }
    }

    public PooledBadGuy obtain(){
        PooledBadGuy bg;
        if(freeBadGuys.size == 0){
            bg = new PooledBadGuy(this.bg);
        }
        else
            bg = freeBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(PooledBadGuy bg){
        if(bg == null){
            Gdx.app.log("DEBUG", "BADGUY IS NULL");
        }
        if(freeBadGuys.size < max){
            freeBadGuys.add(bg);
        }
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeBadGuys.size;
    }

    protected void reset(BadGuy bg){
        bg.reset();
    }
}
