package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eugene Munblit on 10/3/2017.
 */

public class ParticlePool {
    private Particle p;
    private Sprite particleSprite;
    public final int max;
    private final Array<PooledParticle> freeParticles;

    public ParticlePool(Particle p, int initialCapacity, int max){
        freeParticles = new Array<PooledParticle>(false, initialCapacity);

        particleSprite = new Sprite(new Texture("particle.png"));

        for(int i = 0; i < initialCapacity; i++)
            freeParticles.add(new PooledParticle(p));

        this.max = max;
        this.p = p;
    }

    public class PooledParticle extends Particle{
        PooledParticle(Particle p){
            super(p, true);
        }

        @Override
        public void reset(){
            super.reset();
        }

        public void free(){
            ParticlePool.this.free(this);
        }
    }

    public PooledParticle obtain(){
        PooledParticle p;
        if(freeParticles.size == 0){
            p = new PooledParticle(this.p);
        }
        else
            p = freeParticles.pop();

        p.reset();
        return p;
    }

    public void free(PooledParticle p){
        if(p == null){
            Gdx.app.log("DEBUG", "Particle IS NULL");
        }
        if(freeParticles.size < max){
            freeParticles.add(p);
        }
    }

    public int getMax(){
        return max;
    }

    public int getFree(){
        return freeParticles.size;
    }

    protected void reset(Particle p){
        p.reset();
    }
}
