package com.ugen.piano.Pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ugen.piano.ParticleSystem;

/**
 * Created by eugen_000 on 3/30/2017.
 */

public class ParticleSystemPool {
    private ParticleSystem ps;
    public final int max;
    private final Array<PooledSystem> freeSystems;

    public ParticleSystemPool(ParticleSystem ps, int initialCapacity, int max){
        freeSystems = new Array<PooledSystem>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeSystems.add(new PooledSystem(ps));

        this.max = max;
        this.ps = ps;
    }

    public class PooledSystem extends ParticleSystem{
        PooledSystem(ParticleSystem system){
            super(system);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            ParticleSystemPool.this.free(this);
        }
    }

    public PooledSystem obtain(){
        PooledSystem ps;
        if(freeSystems.size == 0){
            ps = new PooledSystem(this.ps);
        }
        else
            ps = freeSystems.pop();

        ps.reset();
        return ps;
    }

    public void free(PooledSystem ps){
        if(ps == null){
            Gdx.app.log("DEBUG", "PARTICLESYSTEM IS NULL");
        }
        if(freeSystems.size < max){
            freeSystems.add(ps);
        }

        //reset(ps);
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeSystems.size;
    }

    protected void reset(ParticleSystem ps){
        ps.reset();
    }
}
