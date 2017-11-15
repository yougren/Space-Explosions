package com.ugen.piano.Pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.BadGuys.BadGuy;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class BadGuyPool {
    private com.ugen.piano.BadGuys.BadGuy bg;
    public final int max;
    private final Array<BadGuy> freeBadGuys;

    public BadGuyPool(com.ugen.piano.BadGuys.BadGuy bg, int initialCapacity, int max){
        freeBadGuys = new Array<BadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeBadGuys.add(new BadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public BadGuy obtain(){
        BadGuy bg;
        if(freeBadGuys.size == 0){
            bg = new BadGuy(this.bg);
        }
        else
            bg = freeBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(BadGuy bg){
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

    protected void reset(com.ugen.piano.BadGuys.BadGuy bg){
        bg.reset();
    }
}
