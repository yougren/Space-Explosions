package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eugene Munblit on 10/16/2017.
 */

public class SpinningBadGuyPool {
    private SpinningBadGuy bg;
    public final int max;
    private final Array<SpinningBadGuyPool.PooledSpinningBadGuy> freeSpinningBadGuys;

    public SpinningBadGuyPool(SpinningBadGuy bg, int initialCapacity, int max){
        freeSpinningBadGuys = new Array<SpinningBadGuyPool.PooledSpinningBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeSpinningBadGuys.add(new SpinningBadGuyPool.PooledSpinningBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public class PooledSpinningBadGuy extends SpinningBadGuy{
        PooledSpinningBadGuy(SpinningBadGuy bg){
            super(bg);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            SpinningBadGuyPool.this.free(this);
        }
    }

    public SpinningBadGuyPool.PooledSpinningBadGuy obtain(){
        SpinningBadGuyPool.PooledSpinningBadGuy bg;
        if(freeSpinningBadGuys.size == 0){
            bg = new SpinningBadGuyPool.PooledSpinningBadGuy(this.bg);
        }
        else
            bg = freeSpinningBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(SpinningBadGuyPool.PooledSpinningBadGuy bg){
        if(bg == null){
            Gdx.app.log("DEBUG", "SpinningBadGuy IS NULL");
        }
        if(freeSpinningBadGuys.size < max){
            freeSpinningBadGuys.add(bg);
        }
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeSpinningBadGuys.size;
    }

    protected void reset(SpinningBadGuy bg){
        bg.reset();
    }
}
