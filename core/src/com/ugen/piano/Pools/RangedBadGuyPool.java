package com.ugen.piano.Pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.BadGuys.BadGuy;
import com.ugen.piano.BadGuys.RangedBadGuy;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class RangedBadGuyPool{
    private RangedBadGuy bg;
    public final int max;
    private final Array<RangedBadGuy> freeRangedBadGuys;

    public RangedBadGuyPool(RangedBadGuy bg, int initialCapacity, int max){
        freeRangedBadGuys = new Array<RangedBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeRangedBadGuys.add(new RangedBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public RangedBadGuy obtain(){
        RangedBadGuy bg;
        if(freeRangedBadGuys.size == 0){
            bg = new RangedBadGuy(this.bg);
        }
        else
            bg = freeRangedBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(RangedBadGuy bg){
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

    protected void reset(com.ugen.piano.BadGuys.RangedBadGuy bg){
        bg.reset();
    }
}