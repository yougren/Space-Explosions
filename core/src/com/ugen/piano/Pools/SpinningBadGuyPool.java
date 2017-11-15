package com.ugen.piano.Pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.BadGuys.SpinningBadGuy;

/**
 * Created by Eugene Munblit on 10/16/2017.
 */

public class SpinningBadGuyPool {
    private SpinningBadGuy bg;
    public final int max;
    private final Array<SpinningBadGuy> freeSpinningBadGuys;

    public SpinningBadGuyPool(SpinningBadGuy bg, int initialCapacity, int max){
        freeSpinningBadGuys = new Array<SpinningBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeSpinningBadGuys.add(new SpinningBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public SpinningBadGuy obtain(){
        SpinningBadGuy bg;
        if(freeSpinningBadGuys.size == 0){
            bg = new SpinningBadGuy(this.bg);
        }
        else
            bg = freeSpinningBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(SpinningBadGuy bg){
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

    protected void reset(com.ugen.piano.BadGuys.SpinningBadGuy bg){
        bg.reset();
    }
}