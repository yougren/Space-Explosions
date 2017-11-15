package com.ugen.piano.Pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.BadGuys.HexagonBadGuy;

/**
 * Created by Eugene Munblit on 10/17/2017.
 */

public class HexBadGuyPool {
    private com.ugen.piano.BadGuys.HexagonBadGuy bg;
    public final int max;
    private final Array<HexagonBadGuy> freeHexBadGuys;

    public HexBadGuyPool(com.ugen.piano.BadGuys.HexagonBadGuy bg, int initialCapacity, int max){
        freeHexBadGuys = new Array<HexagonBadGuy>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeHexBadGuys.add(new HexagonBadGuy(bg));

        this.max = max;
        this.bg = bg;
    }

    public HexagonBadGuy obtain(){
        HexagonBadGuy bg;
        if(freeHexBadGuys.size == 0){
            bg = new HexagonBadGuy(this.bg);
        }
        else
            bg = freeHexBadGuys.pop();

        bg.reset();
        return bg;
    }

    public void free(HexagonBadGuy bg){
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

    protected void reset(com.ugen.piano.BadGuys.HexagonBadGuy bg){
        bg.reset();
    }
}


