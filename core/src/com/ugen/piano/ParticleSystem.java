package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eugen_000 on 3/29/2017.
 */

public class ParticleSystem {
    private Sprite sprite;
    private Vector2 position, boundary;
    private int maxParticles, activeParticles, duration;
    private ArrayList<Particle> particles;
    private boolean[] active;
    private Color color;
    private Random rand;

    public ParticleSystem(ParticleSystem ps){
        initialize(ps.getPosition(), ps.getBoundary(), ps.getMaxParticles());
    }

    public ParticleSystem(Vector2 position, Vector2 boundary, int maxParticles){
        initialize(position, boundary, maxParticles);
    }

    public void initialize(Vector2 position, Vector2 boundary, int maxParticles){
        rand = new Random();

        sprite = new Sprite(new Texture("particle.png"), 4, 1);
        sprite.setColor(new Color(.25f, .41f, .88f, 1.0f));

        this.position = position;
        this.boundary = boundary;
        this.maxParticles = maxParticles;
        this.particles = new ArrayList<Particle>();
        this.active = new boolean[maxParticles];
        this.activeParticles = 0;
        this.duration = 2000;

        for(int i = 0; i < maxParticles; i++) {
            this.particles.add(new Particle(sprite));
            active[i] = true;
            particles.get(i).scale(4.0f);
            this.particles.get(i).setPosition(position.x, position.y);
            activeParticles++;
        }
    }

    public void draw(SpriteBatch batch,  float delta){
        duration -= 10;

        for(int i = 0; i < active.length; i++){
            if(active[i]){
                if(updateParticle(particles.get(i), delta))
                    particles.get(i).draw(batch);
                else{
                    active[i] = false;
                    activeParticles--;
                }
            }
        }
    }

    public boolean updateParticle(Particle particle, float delta){
        particle.update(delta);

        if(particle.isDead() || particle.getColor().a <= 0 || particle.getX() < 0 || particle.getX() > boundary.x || particle.getY() < 0 || particle.getY() > boundary.y)
            return false;

        return true;
    }

    public boolean isComplete(){
        return activeParticles == 0 || duration <= 0;
    }

    public void reset(){
        //color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        color = (new Color(.25f, .41f, .88f, 1.0f));
        for(int i = 0; i < maxParticles; i++){
            this.particles.get(i).reset();
            this.particles.get(i).setColor(color);
            active[i] = true;
        }

        this.duration = 2000;
        this.activeParticles = maxParticles;
    }

    public void setPosition(Vector2 position){
        this.position = position;

        for(int i = 0; i < particles.size(); i++){
            //particles.get(i).initialize();
            particles.get(i).setPosition(position.x, position.y);
        }
    }
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getBoundary() {
        return boundary;
    }

    public int getMaxParticles() {
        return maxParticles;
    }
}
